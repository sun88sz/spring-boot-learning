package com.sun.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;


/**
 * Excel 工具类
 *
 * @author Sunchenjie
 */
public class ExcelUtil {

    private static ThreadLocal<Map<String, Integer>> cellStyleThreadLocal = new ThreadLocal<>();

    /**
     * 添加边框
     *
     * @param sheet
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol
     */
    public static void addCellBorder(HSSFSheet sheet, int firstRow, int lastRow, int firstCol, int lastCol, int borderWeight) {
        if (borderWeight == 0) {
            borderWeight = 1;
        }
        CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);

        // 左边框
        RegionUtil.setBorderLeft(borderWeight, region, sheet);
        // 上边框
        RegionUtil.setBorderTop(borderWeight, region, sheet);
        // 右边框
        RegionUtil.setBorderRight(borderWeight, region, sheet);
        // 下边框
        RegionUtil.setBorderBottom(borderWeight, region, sheet);
    }

    /**
     * 输出到File
     *
     * @param wb       book
     * @param filePath 文件夹路径
     * @param fileName 文件名（带后缀名）
     */
    public static void writeLocalFile(HSSFWorkbook wb, String filePath, String fileName) {
        // 设置默认文件名为当前时间：年月日时分秒
        if (StringUtils.isBlank(fileName)) {
            fileName = defaultFileName();
        }
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            wb.write(file);

            // 清空ThreadLocal
            cellStyleThreadLocal.remove();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLocalFile(HSSFWorkbook wb, String filePath) {
        writeLocalFile(wb, filePath, null);
    }

    /**
     * 输出到 http response
     *
     * @param wb
     * @param response
     * @param fileName
     */
    public static void writeHttpResponse(HSSFWorkbook wb, HttpServletResponse response, String fileName) {
        // 设置默认文件名为当前时间：年月日时分秒
        if (StringUtils.isBlank(fileName)) {
            fileName = defaultFileName();
        }
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            // 设置response头信息
            // 改成输出excel文件
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename="
                    + fileName + ".xls");
            //将文件输出
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();

            // 清空ThreadLocal
            cellStyleThreadLocal.remove();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置默认文件名为当前时间：年月日时分秒
     *
     * @return
     */
    private static String defaultFileName() {
        // 设置默认文件名为当前时间：年月日时分秒
        return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddhhmmss") + ".xls";
    }


    /**
     * 创建 workbook
     *
     * @param
     * @return
     */
    public static HSSFWorkbook createWorkBook() {

        // 清空ThreadLocal
        cellStyleThreadLocal.remove();

        //创建一个WorkBook,对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        return wb;
    }


    /**
     * 创建一个列表标签
     *
     * @param sheetName 标签名称
     * @param list      内容列表
     * @param mappings  映射列表
     * @return
     */
    public static <T> HSSFSheet createSheet(HSSFWorkbook book, String sheetName, List<T> list, List<ExcelMapping> mappings) {
        //在Workbook中，创建一个sheet，对应Excel中的工作薄（sheet）
        HSSFSheet sheet = book.createSheet(sheetName);

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (CollectionUtils.isEmpty(mappings)) {
            return null;
        }

        // 填充工作表
        try {
            Optional<ExcelMapping> childrenMapping = mappings.stream().filter(e -> CollectionUtils.isNotEmpty(e.getChildren())).findAny();
            // 是否有子mapping
            List<ExcelMapping> titleMapping = mappings;
            if (childrenMapping.isPresent()) {
                //  添加表头
                titleMapping = fillHeader(book, sheet, mappings);
            }
            //  添加表头
            drawTitle(book, sheet, titleMapping);
            // 添加内容
            drawContent(book, sheet, titleMapping, list);
            // 设置列宽
            setColumnWidth(sheet, titleMapping);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sheet;
    }

    /**
     * @param book
     * @param sheet
     * @param mappings
     * @return
     */
    private static List<ExcelMapping> fillHeader(HSSFWorkbook book, HSSFSheet sheet, List<ExcelMapping> mappings) {
        List<ExcelMapping> columns = Lists.newArrayList();
        for (int i = 0; i < mappings.size(); i++) {

            ExcelMapping currentMapping = mappings.get(i);
            ExcelMapping lastMapping = null;
            if (i > 0) {
                lastMapping = mappings.get(i - 1);
            }
            ExcelMapping parentMapping = currentMapping.getParent();
            if (parentMapping != null) {
                currentMapping.setFirstRow(parentMapping.getLastRow() + 1);
            } else {
                currentMapping.setFirstRow(0);
            }

            // 根据上一个mappings 和 parent 以及 列宽列高 来确定单元格大小位置
            drawHeader(book, sheet, currentMapping, parentMapping, lastMapping);

            // 是否是header
            List<ExcelMapping> children = currentMapping.getChildren();
            // 如果有children表示不是
            if (CollectionUtils.isNotEmpty(children)) {
                List<ExcelMapping> excelMappings = fillHeader(book, sheet, children);
                if (CollectionUtils.isNotEmpty(excelMappings)) {
                    columns.addAll(excelMappings);
                }
            }
            // 如果是mapping
            else if (StringUtils.isNotBlank(currentMapping.getField())) {
                if (currentMapping.getParent() == null) {
                    currentMapping.setFirstRow(0);
                } else {
                    currentMapping.setFirstRow(parentMapping.getLastRow() + 1);
                }
                columns.add(currentMapping);
            }
        }
        return columns;
    }

    /**
     * @param book
     * @param sheet
     * @param current
     * @param parent
     * @param last
     */
    private static void drawHeader(HSSFWorkbook book, HSSFSheet sheet, ExcelMapping current, ExcelMapping parent, ExcelMapping last) {

        int rows = current.getRows();
        int cols = current.getCols();

        if (last == null) {
            current.setFirstCol(0);
            current.setLastCol(cols - 1);
        } else {
            current.setFirstCol(last.getLastCol() + 1);
            current.setLastCol(last.getLastCol() + cols);
        }
        if (parent == null) {
            current.setFirstRow(0);
            current.setLastRow(rows - 1);
        } else {
            current.setFirstRow(parent.getLastRow() + 1);
            current.setLastRow(parent.getLastRow() + rows);
        }

        // 合并单元格
        if (rows > 1 || cols > 1) {
            // 起始行, 终止行, 起始列, 终止列
            CellRangeAddress cra = new CellRangeAddress(current.getFirstRow(), current.getLastRow(), current.getFirstCol(), current.getLastCol());
            sheet.addMergedRegion(cra);

            HSSFRow row = getOrCreateRow(sheet, current.getFirstRow());

            HSSFCell cell = row.createCell(current.getFirstCol());
            // 设置style
            if (current.getTitleStyle() != null) {
                HSSFCellStyle style = createStyle(book, current.getTitleStyle());
                cell.setCellStyle(style);
            }
            // 设置内容
            if (current.getContent() != null) {
                defaultConvertSet(cell, current.getContent());
            }
        }

    }

    /**
     * @param headers
     * @return
     */
//    public static Integer getNextLine(List<ExcelHeader> headers) {
//        int maxLine = 0;
//        if (CollectionUtils.isNotEmpty(headers)) {
//            for (ExcelHeader head : headers) {
//                if (head.getLastRow() > maxLine) {
//                    maxLine = head.getLastRow();
//                }
//            }
//            return maxLine + 1;
//        }
//        return 0;
//    }


//    public static <T> HSSFSheet createSheetByHeader(HSSFWorkbook book, String sheetName, List<T> list, List<ExcelHeader> headers) {
//        //在Workbook中，创建一个sheet，对应Excel中的工作薄（sheet）
//        HSSFSheet sheet = book.createSheet(sheetName);
//        // 填充工作表
//        try {
//            List<ExcelMapping> mappings = setHeader(book, sheet, headers,0);
//
//            //  添加表头
//            drawTitle(book, sheet, mappings, 0);
//            // 添加内容
//            drawContent(book, sheet, mappings, list, 0 + 1);
//            // 设置列宽
//            setColumnWidth(sheet, mappings);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sheet;
//    }

//    /**
//     * 填充自定义表头
//     *
//     * @param sheet
//     * @param headers
//     */
//    private static void fillHeader(HSSFWorkbook book, HSSFSheet sheet, List<ExcelMapping> headers) {
//        Map<Integer, HSSFRow> rows = new HashMap<>();
//        headers.stream().forEach(
//                h -> {
//                    sheet.addMergedRegion(new CellRangeAddress(h.getFirstRow(), h.getLastRow(), h.getFirstCol(), h.getLastCol()));
//                    HSSFRow row = rows.get(h.getFirstRow());
//                    if (row == null) {
//                        row = getOrCreateRow(sheet, h.getFirstRow());
//                        rows.put(h.getFirstRow(), row);
//                    }
//                    HSSFCell cell = row.getCell(h.getFirstCol());
//                    if (cell == null) {
//                        cell = row.createCell(h.getFirstCol());
//                    }
//
//                    ExcelColumnCellStyle style = h.getTitleStyle();
//                    if (style != null) {
//                        HSSFCellStyle cellStyle = createStyle(book, style);
//                        cell.setCellStyle(cellStyle);
//                    }
//                    // 是否设置值
//                    if (h.getContent() != null) {
//                        defaultConvertSet(cell, h.getContent());
//                    }
//                }
//        );
//    }


    /**
     * 根据字段名获取字段对象
     *
     * @param fieldName 字段名
     * @param clazz     包含该字段的类
     * @return 字段
     */
    public static Field getFieldByName(String fieldName, Class<?> clazz) {
        // 根据字段名获取字段对象:getFieldByName()
        // 拿到本类的所有字段
        Field[] selfFields = clazz.getDeclaredFields();

        // 如果本类中存在该字段，则返回
        for (Field field : selfFields) {
            //如果本类中存在该字段，则返回
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        // 否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            //递归
            return getFieldByName(fieldName, superClazz);
        }

        // 如果本类和父类都没有，则返回空
        return null;
    }

    /**
     * 根据字段名获取字段值
     *
     * @param fieldName 字段名
     * @param o         对象
     * @return 字段值
     * @throws Exception 异常
     */
    public static Object getFieldValueByName(String fieldName, Object o)
            throws Exception {

        // 是否是默认排序列
        if (ExcelMapping.AUTO_INDEX.equals(fieldName)) {
            return null;
        }

        // 根据字段名获取字段值:getFieldValueByName()");
        Object value = null;
        //根据字段名得到字段对象
        Field field = getFieldByName(fieldName, o.getClass());

        //如果该字段存在，则取出该字段的值
        if (field != null) {
            // 类中的成员变量为private,在类外边使用属性值，故必须进行此操作
            field.setAccessible(true);
            // 获取当前对象中当前Field的value
            value = field.get(o);
        } else {
            throw new Exception(o.getClass().getSimpleName() + "类不存在字段名 "
                    + fieldName);
        }

        return value;
    }

    /**
     * 根据带路径或不带路径的属性名获取属性值,即接受简单属性名，
     * 如userName等，又接受带路径的属性名，如student.department.name等
     *
     * @param fieldNameSequence 带路径的属性名或简单属性名
     * @param o                 对象
     * @return 属性值
     * @throws Exception 异常
     */
    public static Object getFieldValueByNameSequence(String fieldNameSequence, Object o) throws Exception {
        // 根据带路径或不带路径的属性名获取属性值,即接受简单属性名:getFieldValueByNameSequence
        Object value = null;

        // 将fieldNameSequence进行拆分
        String[] attributes = fieldNameSequence.split("\\.");
        if (attributes.length == 1) {
            value = getFieldValueByName(fieldNameSequence, o);
        } else {
            // 根据数组中第一个连接属性名获取连接属性对象，如student.department.name
            Object fieldObj = getFieldValueByName(attributes[0], o);
            if (fieldObj != null) {
                //截取除第一个属性名之后的路径
                String subFieldNameSequence = fieldNameSequence.substring(fieldNameSequence.indexOf(".") + 1);
                //递归得到最终的属性对象的值
                value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
            }
        }
        return value;

    }

    /**
     * 创建行
     *
     * @param sheet
     * @param rowIndex
     * @return
     */
    private static HSSFRow getOrCreateRow(HSSFSheet sheet, int rowIndex) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        return row;
    }

    /**
     * 添加表头
     *
     * @param sheet
     * @param mappings
     */
    private static void drawTitle(HSSFWorkbook book, HSSFSheet sheet, List<ExcelMapping> mappings) {
        // 填充表头
        for (int i = 0; i < mappings.size(); i++) {
            ExcelMapping excelMapping = mappings.get(i);
            // 在sheet中添加表头
            HSSFRow row = getOrCreateRow(sheet, excelMapping.getFirstRow());

            HSSFCell cell = row.getCell(i);
            if (cell == null) {
                cell = row.createCell(i);
            }

            ExcelColumnCellStyle titleStyle = excelMapping.getTitleStyle();

            if (titleStyle != null) {
                HSSFCellStyle style = createStyle(book, titleStyle);
                cell.setCellStyle(style);
            }
            cell.setCellValue(excelMapping.getTitle());
        }
    }

    /**
     * 样式
     *
     * @param book
     * @param style
     * @return
     */
    private static HSSFCellStyle createStyle(HSSFWorkbook book, ExcelColumnCellStyle style) {

        Map<String, Integer> cellStyleMap = cellStyleThreadLocal.get();
        if (cellStyleMap == null) {
            cellStyleMap = new HashMap<>();
            cellStyleThreadLocal.set(cellStyleMap);
        }

        Integer cellIndex = cellStyleMap.get(style.getCharacteristic());
        if (cellIndex != null) {
            HSSFCellStyle cellStyle = book.getCellStyleAt(cellIndex);
            return cellStyle;
        } else {
            HSSFCellStyle cellStyle = book.createCellStyle();
            int index = cellStyle.getIndex();

            HSSFFont font = book.createFont();
            font.setBold(style.getBold());
            if (style.getFontSize() != null && style.getFontSize() != 0) {
                font.setFontHeightInPoints((short) style.getFontSize().intValue());
            }
            if (StringUtils.isNotBlank(style.getFontName())) {
                font.setFontName(style.getFontName());
            }
            // 垂直居中
            cellStyle.setFont(font);
            if (style.getHorizontalAlign() == 1) {
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
            } else if (style.getHorizontalAlign() == 2) {
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
            } else if (style.getHorizontalAlign() == 3) {
                cellStyle.setAlignment(HorizontalAlignment.RIGHT);
            }

            // 水平居中
            if (style.getHorizontalAlign() == 1) {
                cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
            } else if (style.getHorizontalAlign() == 2) {
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            } else if (style.getHorizontalAlign() == 3) {
                cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
            }

            cellStyleMap.put(style.getCharacteristic(), index);
            return cellStyle;
        }
    }

    /**
     * 设置内容
     *
     * @param sheet
     * @param mappings
     * @param list
     * @throws Exception
     */
    private static <T> void drawContent(HSSFWorkbook book, HSSFSheet sheet, List<ExcelMapping> mappings, List<T> list) throws Exception {
        // 填充内容
        for (int n = 0; n < list.size(); n++) {

            // 获取单个对象
            T item = list.get(n);
            for (int i = 0; i < mappings.size(); i++) {

                ExcelMapping mapping = mappings.get(i);
                // 计算行数
                HSSFRow row = getOrCreateRow(sheet, mapping.getLastRow() + n + 1);

                HSSFCell cell = row.createCell(i);

                // 设置style
                if (mapping.getColumnStyle() != null) {
                    HSSFCellStyle columnStyle = createStyle(book, mapping.getColumnStyle());
                    cell.setCellStyle(columnStyle);
                }
                // 设置内容
                if (ExcelMapping.AUTO_INDEX.equals(mapping.getField())) {
                    cell.setCellValue(n + 1);
                } else {
                    Object objValue = getFieldValueByNameSequence(mapping.getField(), item);
                    if (mapping.getFunction() != null && objValue != null) {
                        objValue = mapping.getFunction().apply(objValue);
                    }
                    defaultConvertSet(cell, objValue);
                }
            }
        }

    }

    /**
     * 设置列宽
     *
     * @param sheet
     * @param mappings
     */
    private static void setColumnWidth(HSSFSheet sheet, List<ExcelMapping> mappings) {
        for (int i = 0; i < mappings.size(); i++) {
            // 设置默认列宽，width为字符个数
            // 设置第 columnIndex+1 列的列宽，单位为字符宽度的1/256 , 以中文为准, *512
            ExcelMapping mapping = mappings.get(i);
            ExcelColumnCellStyle columnStyle = mapping.getColumnStyle();
            ExcelColumnCellStyle titleStyle = mapping.getTitleStyle();
            if (titleStyle != null && titleStyle.getColumnWidth() != null && titleStyle.getColumnWidth() != 0) {
                sheet.setColumnWidth(i, titleStyle.getColumnWidth() * 512);
            } else if (columnStyle != null && columnStyle.getColumnWidth() != null && columnStyle.getColumnWidth() != 0) {
                sheet.setColumnWidth(i, columnStyle.getColumnWidth() * 512);
            } else {
                sheet.autoSizeColumn(i);
            }
        }
    }

    /**
     * 默认转换一些非String类型
     *
     * @param cell
     * @param objValue
     * @return
     */
    private static Object defaultConvertSet(HSSFCell cell, Object objValue) {
        String fieldValue;

        // 数字型
        if (objValue instanceof Number) {
            if (objValue instanceof Integer) {
                cell.setCellValue((Integer) objValue);
            } else if (objValue instanceof Long) {
                cell.setCellValue((Long) objValue);
            } else {
                cell.setCellValue((Double) objValue);
            }
            return objValue;
        }
        // 默认的时间转换 可在Function中自己转换
        else if (objValue instanceof Date) {
            fieldValue = DateFormatUtils.format((Date) objValue, "yyyy-MM-dd HH:mm:ss");
        }
        // 集合
        else if (objValue instanceof Collection) {
            fieldValue = Joiner.on(",").join((Collection) objValue);
        } else {
            fieldValue = objValue == null ? "" : objValue.toString();
        }
        cell.setCellValue(fieldValue);
        return fieldValue;
    }

}