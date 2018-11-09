package com.sun.graph;

import lombok.Data;

/**
 * @author : Sun
 * @date : 2018/11/9 17:22
 */
@Data
public class Node {

    Integer name;

    // 最早开始时间
    Integer es;
    // 最晚完成时间
    Integer ef;
    // 最迟开始时间
    Integer ls;
    // 最迟完成时间
    Integer lf;
    // 总时差
    Integer tf;
    // 
    Boolean cal = false;

    public Node(Integer name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
