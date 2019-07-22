package com.sun;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Excel2Html {

	private static final String htmlTemp = "<html>" + "<head>"
			+ "<META http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" 
			+ "<style type=\"text/css\">"
			+ "*:focus {outline: none;}" + "body{width:100%;height:100%;background-color:#fff;overflow:hidden;overflow:auto;}"
			+ "td{height:20px;}" + "</style>"
			+ "</head>" + "<body>" + "</body>" + "</html>";
	
	
	private static final String htmlTempSheel = "<html>" + "<head>"
			+ "<META http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" 
			+ "<style type=\"text/css\">"
			+ "*:focus {outline: none;}" + "body{width:100%;height:100%;background-color:#fff;overflow:hidden;overflow:auto;}"
			+ "td{height:20px;}" + "</style>"
			+ "</head>" + "<body>";

	/**
	 *
	 * @param input
	 * @param outPath
	 * @param htmlFileName
	 * @param isWithStyle
	 * @return
	 */
	public static synchronized boolean readExcelToHtml(InputStream input, String outPath, String htmlFileName, boolean isWithStyle) {

		String htmlExcel = null;
		boolean flag = false;
		try {		
			Workbook wb = WorkbookFactory.create(input);
			if (wb instanceof XSSFWorkbook) { // 03版excel处理方法
				XSSFWorkbook xWb = (XSSFWorkbook) wb;
				htmlExcel = getExcelInfo(xWb, isWithStyle);
			} else if (wb instanceof HSSFWorkbook) { // 07及10版以后的excel处理方法
				HSSFWorkbook hWb = (HSSFWorkbook) wb;
				htmlExcel = getExcelInfo(hWb, isWithStyle);
			}
			flag = writeFile(htmlExcel, outPath, htmlFileName);
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(input != null) {
					input.close();					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	private static String getExcelInfo(Workbook wb, boolean isWithStyle) {
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		StringBuffer sbinput = new StringBuffer();
		StringBuffer body = new StringBuffer();
		sbinput.append("<div >");
		for(int i= 0;i<wb.getNumberOfSheets();i++) {
			StringBuffer sb = new StringBuffer();
			Sheet sheet = wb.getSheetAt(i);// 获取第一个Sheet的内容
			
			int lastRowNum = sheet.getLastRowNum();
			String sheetName = sheet.getSheetName();
			sbinput.append("<input type=\"button\" onclick=\"copyText(input"+i+")\" value=\""+sheetName+"\" />");
			Map<String, String> map[] = getRowSpanColSpanMap(sheet);
			Row row = null; // 兼容
			Cell cell = null; // 兼容
			
			for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
				row = sheet.getRow(rowNum);
				if (row == null) {
					sb.append("<tr><td ><nobr> </nobr></td></tr>");
					continue;
				}
				sb.append("<tr>");
				int lastColNum = row.getLastCellNum();
				for (int colNum = 0; colNum < lastColNum; colNum++) {
					cell = row.getCell(colNum);
					if (cell == null) { // 特殊情况 空白的单元格会返回null
						sb.append("<td> </td>");
						continue;
					}
					
					String stringValue = getCellValue(cell, evaluator);
					if (map[0].containsKey(rowNum + "," + colNum)) {
						String pointString = map[0].get(rowNum + "," + colNum);
						map[0].remove(rowNum + "," + colNum);
						int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
						int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
						int rowSpan = bottomeRow - rowNum + 1;
						int colSpan = bottomeCol - colNum + 1;
						sb.append("<td rowspan= '" + rowSpan + "' colspan= '" + colSpan + "' ");
					} else if (map[1].containsKey(rowNum + "," + colNum)) {
						map[1].remove(rowNum + "," + colNum);
						continue;
					} else {
						sb.append("<td ");
					}
					// 判断是否需要样式
					if (isWithStyle) {
						dealExcelStyle(wb, sheet, cell, sb);// 处理单元格样式
					}
					sb.append("><nobr>");
					if (stringValue == null || "".equals(stringValue.trim())) {
						sb.append("   ");
					} else {
						// 将ascii码为160的空格转换为html下的空格（ ）
						//String replace = stringValue.replace(String.valueOf((char) 160), " ");
						String replaceAll = stringValue.replace(String.valueOf((char) 160), " ").replaceAll("\r|\n", "");
						sb.append(replaceAll);
					}
					sb.append("</nobr></td>");
				}
				sb.append("</tr>");
			}

			if(i==0) {
				body.append("<table  id=\"table\" style='border-collapse:collapse;' width='80%'>")
				.append(sb)
				.append("</table>").append("</body><br/>").append("<script type=\"text/javascript\">")
				.append("var  input"+i+" = \"").append(sb).append("\"").append(";");
			}else {
				body.append("var  input"+i+" = \"").append(sb).append("\"").append(";");
			}
			
			
		}
		body.append("function copyText(obj){var table = document.getElementById(\"table\");table.innerHTML =obj;}</script></html>" );
		sbinput.append("</div >");
		sbinput.append(body.toString());
		return sbinput.toString();
	}

	private static Map<String, String>[] getRowSpanColSpanMap(Sheet sheet) {

		Map<String, String> map0 = new HashMap<String, String>();
		Map<String, String> map1 = new HashMap<String, String>();
		int mergedNum = sheet.getNumMergedRegions();
		CellRangeAddress range = null;
		for (int i = 0; i < mergedNum; i++) {
			range = sheet.getMergedRegion(i);
			int topRow = range.getFirstRow();
			int topCol = range.getFirstColumn();
			int bottomRow = range.getLastRow();
			int bottomCol = range.getLastColumn();
			map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
			// System.out.println(topRow + "," + topCol + "," + bottomRow + "," +
			// bottomCol);
			int tempRow = topRow;
			while (tempRow <= bottomRow) {
				int tempCol = topCol;
				while (tempCol <= bottomCol) {
					map1.put(tempRow + "," + tempCol, "");
					tempCol++;
				}
				tempRow++;
			}
			map1.remove(topRow + "," + topCol);
		}
		Map[] map = { map0, map1 };
		return map;
	}

	/**
	 * 获取表格单元格Cell内容
	 * 
	 * @param cell
	 * @return
	 */
	private static String getCellValue(Cell cell, FormulaEvaluator evaluator) {

		String result = new String();
		switch (cell.getCellTypeEnum()) {
			// 数字类型
			case CellType.NUMERIC:
				// 处理日期格式、时间格式
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat sdf = null;
					if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
						sdf = new SimpleDateFormat("HH:mm");
					} else {// 日期
						sdf = new SimpleDateFormat("yyyy-MM-dd");
					}
					Date date = cell.getDateCellValue();
					result = sdf.format(date);
				} else if (cell.getCellStyle().getDataFormat() == 58) {
					// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					double value = cell.getNumericCellValue();
					Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
					result = sdf.format(date);
				} else {
					double value = cell.getNumericCellValue();
					result = getCellString(cell, value);
				}
				break;
			// String类型
			case CellType.STRING:
				result = cell.getRichStringCellValue().toString();
				break;
			//函数类型，如SUM(A1:A5)
			case CellType.FORMULA:
				CellValue cellValue = evaluator.evaluate(cell);
				double value = cellValue.getNumberValue();
				result = getCellString(cell, value);
				break;
			case CellType.BLANK:
			default:
				result = "";
				break;
		}
		return result;
	}

	/**
	 * 获取cell值的字符串
	 * @param cell
	 * @param value
	 * @return
	 */
	private static String getCellString(Cell cell, double value) {
		String result;
		DecimalFormat format = new DecimalFormat();
		CellStyle style = cell.getCellStyle();
		String temp = style.getDataFormatString();
		// 单元格设置成常规
		if (temp != null && temp.equals("General")) {
			format.applyPattern("#");
		}
		result = format.format(value);
		return result;
	}

	/**
	 * 处理表格样式
	 * 
	 * @param wb
	 * @param sheet
	 * @param sb
	 */
	private static void dealExcelStyle(Workbook wb, Sheet sheet, Cell cell, StringBuffer sb) {

		CellStyle cellStyle = cell.getCellStyle();
		if (cellStyle != null) {
			short alignment = cellStyle.getAlignment();
			// sb.append("align='" + convertAlignToHtml(alignment) + "' ");//单元格内容的水平对齐方式
			short verticalAlignment = cellStyle.getVerticalAlignment();
			sb.append("valign='" + convertVerticalAlignToHtml(verticalAlignment) + "' ");// 单元格中内容的垂直排列方式

			if (wb instanceof XSSFWorkbook) {

				XSSFFont xf = ((XSSFCellStyle) cellStyle).getFont();
				short boldWeight = xf.getFontHeightInPoints();
				String align = convertAlignToHtml(alignment);
				sb.append("style='");
				sb.append("font-weight:").append(boldWeight).append(";"); // 字体加粗
				sb.append("font-size: ").append(xf.getFontHeight() / 2).append("%;"); // 字体大小
				int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());
				sb.append("width:").append(columnWidth).append("px;");
				sb.append("text-align:").append(align).append(";");// 表头排版样式
				XSSFColor xc = xf.getXSSFColor();
				if (xc != null && !"".equals(xc)) {
					sb.append("color:#" + xc.getARGBHex().substring(2) + ";"); // 字体颜色
				}

				XSSFColor bgColor = (XSSFColor) cellStyle.getFillForegroundColorColor();
				if (bgColor != null && !"".equals(bgColor)) {
					sb.append("background-color:#" + bgColor.getARGBHex().substring(2) + ";"); // 背景颜色
				}
				sb.append(getBorderStyle(0, cellStyle.getBorderTop(), ((XSSFCellStyle) cellStyle).getTopBorderXSSFColor()));
				sb.append(getBorderStyle(1, cellStyle.getBorderRight(), ((XSSFCellStyle) cellStyle).getRightBorderXSSFColor()));
				sb.append(getBorderStyle(2, cellStyle.getBorderBottom(), ((XSSFCellStyle) cellStyle).getBottomBorderXSSFColor()));
				sb.append(getBorderStyle(3, cellStyle.getBorderLeft(), ((XSSFCellStyle) cellStyle).getLeftBorderXSSFColor()));

			} else if (wb instanceof HSSFWorkbook) {

				HSSFFont hf = ((HSSFCellStyle) cellStyle).getFont(wb);
				short boldWeight = hf.getFontHeightInPoints();
				short fontColor = hf.getColor();
				sb.append("style='");
				HSSFPalette palette = ((HSSFWorkbook) wb).getCustomPalette(); // 类HSSFPalette用于求的颜色的国际标准形式
				HSSFColor hc = palette.getColor(fontColor);
				sb.append("font-weight:" + boldWeight + ";"); // 字体加粗
				sb.append("font-size: " + hf.getFontHeight() / 2 + "%;"); // 字体大小
				String align = convertAlignToHtml(alignment);
				sb.append("text-align:" + align + ";");// 表头排版样式
				String fontColorStr = convertToStardColor(hc);
				if (fontColorStr != null && !"".equals(fontColorStr.trim())) {
					sb.append("color:" + fontColorStr + ";"); // 字体颜色
				}
				int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());
				sb.append("width:" + columnWidth + "px;");
				short bgColor = cellStyle.getFillForegroundColor();
				hc = palette.getColor(bgColor);
				String bgColorStr = convertToStardColor(hc);
				if (bgColorStr != null && !"".equals(bgColorStr.trim())) {
					sb.append("background-color:" + bgColorStr + ";"); // 背景颜色
				}
				sb.append(getBorderStyle(palette, 0, cellStyle.getBorderTop(), cellStyle.getTopBorderColor()));
				sb.append(getBorderStyle(palette, 1, cellStyle.getBorderRight(), cellStyle.getRightBorderColor()));
				sb.append(getBorderStyle(palette, 3, cellStyle.getBorderLeft(), cellStyle.getLeftBorderColor()));
				sb.append(getBorderStyle(palette, 2, cellStyle.getBorderBottom(), cellStyle.getBottomBorderColor()));
			}

			sb.append("' ");
		}
	}

	/**
	 * 单元格内容的水平对齐方式
	 * 
	 * @param alignment
	 * @return
	 */
	private static String convertAlignToHtml(short alignment) {

		String align = "center";
		switch (alignment) {
		case 1:
			align = "left";
			break;
		case 2:
			align = "center";
			break;
		case 3:
			align = "right";
			break;
		default:
			break;
		}
		return align;
	}

	/**
	 * 单元格中内容的垂直排列方式
	 * 
	 * @param verticalAlignment
	 * @return
	 */
	private static String convertVerticalAlignToHtml(short verticalAlignment) {

		String valign = "middle";
		switch (verticalAlignment) {
		case 2:
			valign = "bottom";
			break;
		case 1:
			valign = "center";
			break;
		case 0:
			valign = "top";
			break;
		default:
			break;
		}
		return valign;
	}

	private static String convertToStardColor(HSSFColor hc) {

		StringBuffer sb = new StringBuffer("");
		if (hc != null) {
			if (HSSFColor.AUTOMATIC.index == hc.getIndex()) {
				return null;
			}
			sb.append("#");
			for (int i = 0; i < hc.getTriplet().length; i++) {
				sb.append(fillWithZero(Integer.toHexString(hc.getTriplet()[i])));
			}
		}

		return sb.toString();
	}

	private static String fillWithZero(String str) {
		if (str != null && str.length() < 2) {
			return "0" + str;
		}
		return str;
	}

	static String[] bordesr = { "border-top:", "border-right:", "border-bottom:", "border-left:" };
	static String[] borderStyles = { "solid ", "solid ", "solid ", "solid ", "solid ", "solid ", "solid ", "solid ",
			"solid ", "solid", "solid", "solid", "solid", "solid" };

	private static String getBorderStyle(HSSFPalette palette, int b, short s, short t) {
		if (s == 0) {
			return bordesr[b] + borderStyles[s] + "#d0d7e5 1px;";
		}
		String borderColorStr = convertToStardColor(palette.getColor(t));
		borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000" : borderColorStr;
		return bordesr[b] + borderStyles[s] + borderColorStr + " 1px;";
	}

	private static String getBorderStyle(int b, short s, XSSFColor xc) {
		if (s == 0) {
			return bordesr[b] + borderStyles[s] + "#d0d7e5 1px;";
		}
		if (xc != null && !"".equals(xc)) {
			String borderColorStr = xc.getARGBHex();// t.getARGBHex();
			borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000"
					: borderColorStr.substring(2);
			return bordesr[b] + borderStyles[s] + borderColorStr + " 1px;";
		}
		return "";
	}

	/*
	 * @param content 生成的excel表格标签
	 * 
	 * @param htmlPath 生成的html文件地址
	 */
	private static boolean writeFile(String content, String outPath, String htmlFileName) throws Exception {
		File htmlDir = new File(outPath);
		if (!htmlDir.isDirectory()) {
			htmlDir.mkdirs();
		}
		File file = new File(outPath + "/" + htmlFileName);
		if(file.exists()) {
			return true;
		}
		boolean flag = false;
		StringBuilder sb = new StringBuilder();
		OutputStreamWriter write = null;
		try {
			file.createNewFile();// 创建文件

			sb.append(htmlTempSheel);
			sb.append(content);
			/*if (sb.toString().indexOf("<body>") > 0) {
				sb.insert(sb.toString().indexOf("<body>")
						+ "<body>".length(), content);
			}*/
			write = new OutputStreamWriter(new FileOutputStream(file), "utf-8");

			write.write(sb.toString());// 将字符串写入文件

			write.close();
			flag = true;
		} catch (IOException e) {

			e.printStackTrace();
		}finally {
			if(write != null) {
				write.close();
			}
		}
		return flag;
	}
}
