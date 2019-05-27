package com.sun;

import com.sun.domain.Employee;
import com.sun.domain.Project;
import com.sun.domain.QSDomain;
import com.sun.util.ExcelColumnCellStyle;
import com.sun.util.ExcelMapping;
import com.sun.util.ExcelUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author : Sun
 * @date : 2018/10/25 14:28
 */
public class ExcelExportTest {

    @Test
    public void test() {
        String sheetName = "安全计划报表";

        ExcelColumnCellStyle headerStyle = new ExcelColumnCellStyle().fontSize(18).horizontalAlign(1).verticalAlign(2);
        ExcelColumnCellStyle titleStyle = new ExcelColumnCellStyle().fontSize(14).horizontalAlign(2).verticalAlign(2);
        ExcelColumnCellStyle columnStyle = new ExcelColumnCellStyle().bold(false).fontSize(11).horizontalAlign(1).verticalAlign(2);

        HSSFWorkbook book = ExcelUtil.createWorkBook();

        Date now = new Date();

        // 添加header
        ExcelMapping zbr = new ExcelMapping("制表人：" + "xxxxxx", 1, 5, headerStyle);
        ExcelMapping zbsj = new ExcelMapping("制表时间：" + DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss"), 1, 6, headerStyle);

        StringBuffer dateStr = new StringBuffer("日期：【2000-01-01】到【" + DateFormatUtils.format(now, "yyyy-MM-dd") + "】");

        ExcelMapping rq = new ExcelMapping(dateStr.toString(), 1, 11, headerStyle);
        rq.addChildren(zbr, zbsj);

        ExcelMapping head = new ExcelMapping(sheetName, 1, 11, new ExcelColumnCellStyle(null, 20, 2, false));
        head.addChildren(rq);

        // 自动的序号列
        ExcelMapping xhRow = new ExcelMapping().title("序号").field(ExcelMapping.AUTO_INDEX).titleStyle(titleStyle).columnStyle(columnStyle).columnWidth(4).rows(2).cols(1);
        zbr.addChildren(xhRow);
        zbr.addChildren(new ExcelMapping("项目", "project.projectName", titleStyle, columnStyle, 16).rows(2));
//        zbr.addChildren(new ExcelMapping("项目时间", "project.createTime", titleStyle, columnStyle, 16).rows(2).function(e->DateFormatUtils.format((Date) e,"yyyy-MM-dd")));

        zbr.addChildren(new ExcelMapping("总计划", "plan.allQty", titleStyle, columnStyle, 8).rows(2));

        ExcelMapping jc = new ExcelMapping("检查").titleStyle(titleStyle).cols(2);
        jc.addChildren(new ExcelMapping("检查次数", "check.totalQty", titleStyle, columnStyle, 8));
        jc.addChildren(new ExcelMapping("一次检查通过", "check.oncePassQty", titleStyle, columnStyle, 8));

        ExcelMapping jcjg = new ExcelMapping("检查结果").titleStyle(titleStyle).cols(3);
        jcjg.addChildren(new ExcelMapping("通过", "checkResult.passQty", titleStyle, columnStyle, 8));
        jcjg.addChildren(new ExcelMapping("口头警告", "checkResult.warningQty", titleStyle, columnStyle, 8));
        jcjg.addChildren(new ExcelMapping("书面整改", "checkResult.changeQty", titleStyle, columnStyle, 8));

        ExcelMapping zg = new ExcelMapping("整改").titleStyle(titleStyle).cols(3);
        zg.addChildren(new ExcelMapping("一次整改通过", "change.oncePassQty", titleStyle, columnStyle, 8));
        zg.addChildren(new ExcelMapping("多次整改", "change.manyTimesQty", titleStyle, columnStyle, 8));
        zg.addChildren(new ExcelMapping("逾期", "change.overdueQty", titleStyle, columnStyle, 8));

        zbr.addChildren(jc, jcjg, zg);


        new ExcelMapping().title("备注").titleStyle(headerStyle).cols(6).parent(xhRow);

        List<QSDomain> records = createRecords();
        HSSFSheet sheet = ExcelUtil.createSheet(book, sheetName, records, Collections.singletonList(head));

        ExcelUtil.addCellBorder(sheet, 3, 3 + records.size() + 1, 0, 10, 2);

        ExcelUtil.writeLocalFile(book, "D:/");
    }

    private List<QSDomain> createRecords() {
        QSDomain qsDomain = new QSDomain();
        qsDomain.setProject(new Project(1l, "项目1", new Date()));
        qsDomain.setEmployee(new Employee(1l, "张三"));

        qsDomain.setCheck(new QSDomain.QualitySafety(1, 2, 6, 9));
        qsDomain.setChange(new QSDomain.QualitySafetyChange(1, 2, 6));
        qsDomain.setCheckResult(new QSDomain.QualitySafetyCheckResult(5, 23, 2, 1));
        qsDomain.setPlan(new QSDomain.QualitySafetyPlan(32));

        return Lists.newArrayList(qsDomain);
    }
}
