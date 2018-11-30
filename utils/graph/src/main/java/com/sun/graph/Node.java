package com.sun.graph;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import lombok.Data;

/**
 * @author : Sun
 * @date : 2018/11/9 17:22
 */
@Data
public class Node {

    /**
     *
     */
    private Long id;
    /**
     * 开始日期
     */
    private Date beginDate;

    /**
     * 最小开始时间
     */
    private Double minBetweenDays;
    /**
     * 工期
     */
    private Double duration;

    /**
     * 最早开始时间
     */
    private Double es = -1d;
    /**
     * 最早完成时间
     */
    private Double ef = -1d;
    /**
     * 最迟开始时间
     */
    private Double ls = 999999d;
    /**
     * 最迟完成时间
     */
    private Double lf = 999999d;

    /**
     * 前继至今最大的时间
     */
    private Double esMax;
    private Double efMax;

    /**
     * 总时差
     */
    private Double tf = null;
    /**
     *
     */
    private Boolean prev = false;
    private Boolean next = false;

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Node(Long id, Double duration) {
        this.id = id;
        this.duration = duration;
    }

    public Node(Long id, Integer duration) {
        this(id, Double.valueOf(duration));
    }

    public Node(Long id, String beginDate, Integer duration) {
        this(id, beginDate, (double) duration);
    }

    public Node(Long id, String beginDate, Double duration) {
        this.id = id;
        try {
            this.beginDate = sdf.parse(beginDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.duration = duration;
    }

    public Node(Long id, Date beginDate, Double duration) {
        this.id = id;
        this.beginDate = beginDate;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return id.toString() + " : " + es + " " + ef + " " + ls + " " + lf + " " + String.valueOf(tf);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Node node = (Node) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
