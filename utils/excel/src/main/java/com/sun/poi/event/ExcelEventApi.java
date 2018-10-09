package com.sun.poi.event;

import java.util.ArrayList;
import java.util.List;

import com.sun.poi.event.ExcelEventParser;

/**
 * @author : Sun
 * @date : 2018/10/9 15:23
 */
public class ExcelEventApi {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        final List<List<String>> table = new ArrayList<>();
        new ExcelEventParser("D:/B.xlsx").setHandler(new ExcelEventParser.SimpleSheetContentsHandler(){

            private List<String> fields;

            @Override
            public void endRow(int rowNum) {
//                if(rowNum == 0){
//                    // 第一行中文描述忽略  
//                }else if(rowNum == 1){
//                    // 第二行字段名  
//                    fields = row;
//                }else {
//                    // 数据  
//                    table.add(row);
//                    System.out.println(row);
//                }
            }
        }).parse();

        long end = System.currentTimeMillis();

        System.err.println(table.size());
        System.err.println(end - start);
    }

}
