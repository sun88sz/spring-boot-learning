package com.sun.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Sun
 * @date : 2018/11/9 16:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    /**
     * FF FS SF SS
     */
    private Integer type;
    private Integer quantity;

    
    public static Line build(Integer type, Integer quantity) {
        return new Line(type, quantity);
    }

}
