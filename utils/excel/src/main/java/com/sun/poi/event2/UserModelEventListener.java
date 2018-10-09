package com.sun.poi.event2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class UserModelEventListener implements HSSFListener {
    
    private static Logger logger = LoggerFactory.getLogger(UserModelEventListener.class);
    private SSTRecord sstrec;
    /**
     * Should we output the formula, or the value it has?
     */
    private boolean outputFormulaValues = true;
    /**
     * For parsing Formulas
     */
    private EventWorkbookBuilder.SheetRecordCollectingListener workbookBuildingListener;

    /**
     * 当前Sheet的内容
     */
    private List<Map<String, Object>> currentSheetDataMap = new ArrayList<Map<String, Object>>();
    /**
     * 列对应的字段
     */
    private static String[] trianListheadTitle = new String[]{"trainCode", "firstStation", "lastStation", "startStation", "arriveStation", "startTime", "arriveTime", "fistLevelPrice", "secondLevelPrice", "km", "useDate"};

    //一行记录  
    private Map<String, Object> currentSheetRowDataMap = new HashMap<String, Object>();
    private int curRowNum = 0;
    private int ignoreRowNum = 1;
    private int sheetNo = 0;

    @Override
    public void processRecord(org.apache.poi.hssf.record.Record record) {
        switch (record.getSid()) {

            case BOFRecord.sid:
                BOFRecord bof = (BOFRecord) record;
                //顺序进入新的Workbook    
                if (bof.getType() == BOFRecord.TYPE_WORKBOOK) {
                    logger.debug("开始解析excel 文档.....");
                    //顺序进入新的Worksheet，因为Event API不会把Excel文件里的所有数据结构都关联起来，  
                    //所以这儿一定要记录现在进入第几个sheet了。  
                } else if (bof.getType() == BOFRecord.TYPE_WORKSHEET) {
                    //读取新的一个Sheet页  
                    logger.debug("开始解析sheet页面内容...");
                    System.out.println("sheetNo=" + sheetNo);
                    sheetNo++;
                    currentSheetDataMap = new ArrayList<Map<String, Object>>();
                }
                break;
            //开始解析Sheet的信息，记录sheet，这儿会把所有的sheet都顺序打印出来，如果有多个sheet的话，可以顺序记入到一个List里     
            case BoundSheetRecord.sid:
                BoundSheetRecord bsr = (BoundSheetRecord) record;
                System.out.println("sheetName=" + bsr.getSheetname());
                logger.debug("New sheet named: " + bsr.getSheetname());
                break;
            //执行行记录事件  
            case RowRecord.sid:
                RowRecord rowrec = (RowRecord) record;
                logger.debug("记录开始, first column at "
                        + rowrec.getFirstCol() + " last column at "
                        + rowrec.getLastCol());
                break;
            // SSTRecords store a array of unique strings used in Excel.  
            case SSTRecord.sid:
                sstrec = (SSTRecord) record;
                for (int k = 0; k < sstrec.getNumUniqueStrings(); k++) {
                    logger.debug("String table value " + k + " = "
                            + sstrec.getString(k));
                }
                break;

            //发现数字类型的cell，因为数字和日期都是用这个格式，所以下面一定要判断是不是日期格式，另外默认的数字也会被视为日期格式，所以如果是数字的话，一定要明确指定格式！！！！！！！     
            case NumberRecord.sid:
                NumberRecord nr = (NumberRecord) record;
                //HSSFDateUtil.isInternalDateFormat(nr.getXFIndex())  判断是否为时间列  
                int column = nr.getColumn();
                if (column == 5 || column == 6) {
                    addDataAndrChangeRow(nr.getRow(), nr.getColumn(), getTime(nr.getValue()));
                } else {
                    addDataAndrChangeRow(nr.getRow(), nr.getColumn(), (int) nr.getValue());
                }
                break;
            //发现字符串类型，这儿要取字符串的值的话，跟据其index去字符串表里读取     
            case LabelSSTRecord.sid:
                LabelSSTRecord lsr = (LabelSSTRecord) record;
                addDataAndrChangeRow(lsr.getRow(), lsr.getColumn(), sstrec.getString(lsr.getSSTIndex()));
                logger.debug("文字列:" + sstrec.getString(lsr.getSSTIndex()) + ",　行：" + lsr.getRow() + ", 列：" + lsr.getColumn());
                break;
            //解析boolean错误信息  
            case BoolErrRecord.sid: 
                BoolErrRecord ber = (BoolErrRecord) record;
                if (ber.isBoolean()) {
                    addDataAndrChangeRow(ber.getRow(), ber.getColumn(), ber.getBooleanValue());
                    logger.debug("Boolean:" + ber.getBooleanValue() + ", 行：" + ber.getRow() + ", 列：" + ber.getColumn());
                }
                if (ber.isError()) {
                    logger.debug("Error:" + ber.getErrorValue() + ", 行：" + ber.getRow() + ", 列：" + ber.getColumn());
                }
                break;
            //空白记录的信息  
            case BlankRecord.sid:
                BlankRecord br = (BlankRecord) record;
                addDataAndrChangeRow(br.getRow(), br.getColumn(), "");
                logger.debug("空。　行：" + br.getRow() + ", 列：" + br.getColumn());
                break;
            //数式  
            case FormulaRecord.sid:    
                FormulaRecord fr = (FormulaRecord) record;
                addDataAndrChangeRow(fr.getRow(), fr.getColumn(), fr.getValue());
                logger.debug("数字 。　行：" + fr.getRow() + ", 列：" + fr.getColumn());
                break;
        }
    }

    /**
     * HH:MM格式时间的数字转换方法</li>
     *
     * @param
     * @return
     */
    private static String getTime(double daynum) {
        double totalSeconds = daynum * 86400.0D;
        //总的分钟数  
        int seconds = (int) totalSeconds / 60;
        //实际小时数  
        int hours = seconds / 60;
        int minutes = seconds - hours * 60;
        //剩余的实际分钟数  
        StringBuffer sb = new StringBuffer();
        if (String.valueOf(hours).length() == 1) {
            sb.append("0" + hours);
        } else {
            sb.append(hours);
        }
        sb.append(":");
        if (String.valueOf(minutes).length() == 1) {
            sb.append("0" + minutes);
        } else {
            sb.append(minutes);
        }
        return sb.toString();
    }

    /**
     * 添加数据记录并检查是否换行
     *
     * @param row   实际当前行号
     * @param col   实际记录当前列
     * @param value 当前cell的值
     */
    public void addDataAndrChangeRow(int row, int col, Object value) {
        //当前行如果大于实际行表示改行忽略，不记录  
        if (curRowNum != row) {
            if (CollectionUtils.isEmpty(currentSheetDataMap)) {
                currentSheetDataMap = new ArrayList<>();
            }
            currentSheetDataMap.add(currentSheetRowDataMap);
            logger.debug("行号:" + curRowNum + " 行内容：" + currentSheetRowDataMap.toString());
            logger.debug("\n");
            currentSheetRowDataMap = new HashMap<>();
            currentSheetRowDataMap.put(trianListheadTitle[col], value);
            logger.debug(row + ":" + col + "  " + value + "\r");
            curRowNum = row;
        } else {
            currentSheetRowDataMap.put(trianListheadTitle[col], value);
            logger.debug(row + ":" + col + "  " + value + "\r");
        }
    }

    public List<Map<String, Object>> getCurrentSheetDataMap() {
        return currentSheetDataMap;
    }

    public void setCurrentSheetDataMap(List<Map<String, Object>> currentSheetDataMap) {
        this.currentSheetDataMap = currentSheetDataMap;
    }

    public Map<String, Object> getCurrentSheetRowDataMap() {
        return currentSheetRowDataMap;
    }

    public void setCurrentSheetRowDataMap(Map<String, Object> currentSheetRowDataMap) {
        this.currentSheetRowDataMap = currentSheetRowDataMap;
    }

    public int getCurRowNum() {
        return curRowNum;
    }

    public void setCurRowNum(int curRowNum) {
        this.curRowNum = curRowNum;
    }

    public int getIgnoreRowNum() {
        return ignoreRowNum;
    }

    public void setIgnoreRowNum(int ignoreRowNum) {
        this.ignoreRowNum = ignoreRowNum;
    }

}  
