package com.sun.util;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Excel简单样式类
 *
 * @author Sunchenjie
 */
@NoArgsConstructor
@Data
public class ExcelColumnCellStyle implements Cloneable {

    /**
     * 列宽
     */
    private Integer columnWidth;

    /**
     * 行高
     */
    private Integer rowHeight;

    /**
     * 字体大小
     */
    private Integer fontSize;

    /**
     * 字体
     */
    private String fontName;

    /**
     * 是否加粗
     */
    private Boolean bold = false;

    /**
     * 水平方向对齐方式（1左对齐 2居中 3右对齐）
     */
    private Integer horizontalAlign = 1;

    /**
     * 垂直方向对齐方式（1上对齐 2居中 3下对齐）
     */
    private Integer verticalAlign = 2;

    public ExcelColumnCellStyle(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    public ExcelColumnCellStyle(Integer columnWidth, Integer fontSize, Boolean bold) {
        this.columnWidth = columnWidth;
        this.fontSize = fontSize;
        this.bold = bold;
    }

    public ExcelColumnCellStyle(Integer fontSize, String fontName, Integer horizontalAlign) {
        this.fontSize = fontSize;
        this.fontName = fontName;
        this.horizontalAlign = horizontalAlign;
    }


    public ExcelColumnCellStyle(Integer columnWidth, Integer fontSize, Integer horizontalAlign, Boolean bold) {
        this(columnWidth, fontSize, bold);
        this.horizontalAlign = horizontalAlign;
    }

    public ExcelColumnCellStyle(Integer columnWidth, Integer rowHeight, Integer fontSize, String fontName, Boolean bold) {
        this.columnWidth = columnWidth;
        this.rowHeight = rowHeight;
        this.fontSize = fontSize;
        this.fontName = fontName;
        this.bold = bold;
    }

    public ExcelColumnCellStyle(Integer columnWidth, Integer rowHeight, Integer fontSize, String fontName, Boolean bold, Integer horizontalAlign, Integer verticalAlign) {
        this.columnWidth = columnWidth;
        this.rowHeight = rowHeight;
        this.fontSize = fontSize;
        this.fontName = fontName;
        this.bold = bold;
        this.horizontalAlign = horizontalAlign;
        this.verticalAlign = verticalAlign;
    }

    @Override
    protected ExcelColumnCellStyle clone() {
        try {
            return (ExcelColumnCellStyle) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 列宽
     *
     * @param columnWidth
     * @return
     */
    public ExcelColumnCellStyle columnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
        return this;
    }

    /**
     * 行高
     *
     * @param rowHeight
     * @return
     */
    public ExcelColumnCellStyle rowHeight(Integer rowHeight) {
        this.rowHeight = rowHeight;
        return this;
    }

    /**
     * 字体大小
     */
    public ExcelColumnCellStyle fontSize(Integer fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 字体
     */
    public ExcelColumnCellStyle fontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    /**
     * 是否加粗
     */
    public ExcelColumnCellStyle bold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    /**
     * 水平方向对齐方式（1左对齐 2居中 3右对齐）
     */
    public ExcelColumnCellStyle horizontalAlign(Integer horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
        return this;
    }

    /**
     * 垂直方向对齐方式（1上对齐 2居中 3下对齐）
     */
    public ExcelColumnCellStyle verticalAlign(Integer verticalAlign) {
        this.verticalAlign = verticalAlign;
        return this;
    }

    /**
     * 特征值
     *
     * @return
     */
    public String getCharacteristic() {
        StringBuffer b = new StringBuffer();
        b.append(columnWidth).append(rowHeight).append(fontSize).append(fontName).append(bold).append(horizontalAlign).append(verticalAlign);
        return b.toString();
    }
}
