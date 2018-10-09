package com.sun.poi.event2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class UserModelEventMain {

    /**
     * 解析Excel2003的事件解析格式
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        UserModelEventListener xlsEventListener = new UserModelEventListener();

        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(
                xlsEventListener);
        FormatTrackingHSSFListener formatListener = new FormatTrackingHSSFListener(listener);


        //创建一个excel输入流  
        FileInputStream fin = new FileInputStream("D:\\B.xls");
        //创建一个 org.apache.poi.poifs.filesystem.Filesystem  
        POIFSFileSystem poifs = new POIFSFileSystem(fin);
        //将excel 2003格式POI文档输入流  
        InputStream din = poifs.createDocumentInputStream("Workbook");
        //这儿为所有类型的Record都注册了监听器，如果需求明确的话，可以用addListener方法，并指定所需的Record类型     
        HSSFRequest req = new HSSFRequest();
        //添加监听记录的事件   
        req.addListenerForAllRecords(xlsEventListener);

        boolean outputFormulaValues = true;
        if (outputFormulaValues) {
            req.addListenerForAllRecords(formatListener);
        } else {
            EventWorkbookBuilder.SheetRecordCollectingListener workbookBuildingListener = new EventWorkbookBuilder.SheetRecordCollectingListener(
                    formatListener);
            req.addListenerForAllRecords(workbookBuildingListener);
        }

        //创建时间工厂  
        HSSFEventFactory factory = new HSSFEventFactory();
        //处理基于时间文档流  
        factory.processEvents(req, din);
        //关闭文件流  
        fin.close();
        //关闭基于POI文档流  
        din.close();

        List<Map<String, Object>> sheetDataList = xlsEventListener.getCurrentSheetDataMap();

        System.out.println("Excel Sheet记录数" + sheetDataList.size() + " 内容" + sheetDataList.toString());
    }

}  
