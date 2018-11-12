package com.sun.graph;

import java.util.Objects;

import lombok.Data;

/**
 * @author : Sun
 * @date : 2018/11/9 17:22
 */
@Data
public class Node {

    Integer name;

    /**
     * 最早开始时间
     */
    Integer es = Integer.MIN_VALUE;
    /**
     * 最早完成时间
     */
    Integer ef = Integer.MIN_VALUE;
    /**
     * 最迟开始时间
     */
    Integer ls = Integer.MAX_VALUE;
    /**
     * 最迟完成时间
     */
    Integer lf = Integer.MAX_VALUE;
    /**
     * 总时差
     */
    Integer tf = null;
    /**
     *
     */
    Boolean prev = false;
    Boolean next = false;

    public Node(Integer name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toString() + " : " + es + " " + ef + " " + ls + " " + lf + " " + String.valueOf(tf);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
