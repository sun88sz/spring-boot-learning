package com.sun.graph;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author : Sun
 * @date : 2018/11/9 16:34
 */
@Data
public class Line {

    /**
     * FF FS SF SS
     * 完成对开始(FS)：后续活动的开始要等到先行活动的完成。
     * 完成对完成(FF)：后续活动的完成要等到先行活动的完成。
     * 开始对开始(SS)：后续活动的开始要等到先行活动的开始。
     * 开始对完成(SF)：后续活动的完成要等到先行活动的开始。
     */
    private String type;

    private Line() {
    }

    private Line(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static Line build(String quantity) {
        if (StringUtils.isBlank(quantity))
            return build();
        return new Line(quantity);
    }

    public static Line build() {
        return new Line("FS");
    }

}
