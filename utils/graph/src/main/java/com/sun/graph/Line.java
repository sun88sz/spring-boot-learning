package com.sun.graph;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author : Sun
 * @date : 2018/11/9 16:34
 */
@Data
public class Line {

    /**
     * 前置节点id
     */
    private Long prevNodeId;

    /**
     * 关系
     */
    private List<RelationType> relations;


    private Line() {
    }

    @Override
    public String toString() {
        return "";
    }

    /**
     * @param relations 节点间关系
     * @return 关系line
     */
    public static Line build(Long prevNodeId, RelationType... relations) {
        Line line = new Line();
        line.setPrevNodeId(prevNodeId);
        if (relations == null || relations.length == 0) {
            line.setRelations(Lists.newArrayList(relation()));
        } else {
            line.setRelations(Lists.newArrayList(relations));
        }
        return line;
    }


    public static Line build(RelationType... relations) {
        Line line = new Line();
        if (relations == null || relations.length == 0) {
            line.setRelations(Lists.newArrayList(relation()));
        } else {
            line.setRelations(Lists.newArrayList(relations));
        }
        return line;
    }

    /**
     * 创建节点之间的关系, type=FS, delay=0
     *
     * @return
     */
    public static RelationType relation() {
        return new RelationType();
    }

    /**
     * 创建节点之间的关系, delay=0
     *
     * @param type
     * @return
     */
    public static RelationType relation(String type) {
        return new RelationType(type);
    }

    /**
     * 创建节点之间的关系
     *
     * @param type
     * @param delay
     * @return
     */
    public static RelationType relation(String type, Integer delay) {
        return new RelationType(type, delay);
    }

    @Data
    public static class RelationType {
        /**
         * FF FS SF SS
         * 完成对开始(FS)：后续活动的开始要等到先行活动的完成。
         * 完成对完成(FF)：后续活动的完成要等到先行活动的完成。
         * 开始对开始(SS)：后续活动的开始要等到先行活动的开始。
         * 开始对完成(SF)：后续活动的完成要等到先行活动的开始。
         */
        private String type;

        private Integer delay;

        public RelationType(String type, Integer delay) {
            if (StringUtils.isBlank(type)) {
                this.type = "FS";
            } else {
                this.type = type.toUpperCase();
            }
            if (delay == null) {
                this.delay = 0;
            } else {
                this.delay = delay;
            }
        }

        public RelationType(String type) {
            this(type, 0);
        }

        public RelationType() {
            this("FS");
        }
    }
}
