package com.sun.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Sun
 * @date : 2018/11/9 16:34
 */
@Data
@AllArgsConstructor
public class Line {

    /**
     * FF FS SF SS
     */
    private Integer type;
    private Integer quantity;

    public Line(Integer quantity) {
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return String.valueOf(quantity);
    }

    public static Line build(Integer quantity) {
        return new Line(quantity);
    }

}
