package com.sun.graph;

import com.google.common.collect.Maps;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.sun.WorkDayUtil;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: <br/>
 * Date: 2018-11-12
 *
 * @author Sun
 */
public class GraphAON {

    public static final String FF = "FF";
    public static final String FS = "FS";
    public static final String SS = "SS";
    public static final String SF = "SF";

    public GraphAON() {
        this(true, true);
    }

    public GraphAON(boolean includeDate, boolean includeWeekend) {
        this.includeDate = includeDate;
        this.includeWeekend = includeWeekend;
        graph = ValueGraphBuilder.directed().allowsSelfLoops(false).build();
        nodeMap = Maps.newHashMap();
    }

    /**
     * 计算单代图时 是否忽略开始时间
     * 默认不忽略
     */
    private boolean includeDate;

    /**
     * 计算天数时 是否包含周末
     */
    private boolean includeWeekend;

    /**
     * 单代号网络图
     */
    private MutableValueGraph<Node, Line> graph;

    /**
     * 图中所有的node节点
     * 按照 id -> Node 存放
     */
    private Map<Long, Node> nodeMap;

    /**
     * 添加节点 以及节点之间的关系
     *
     * @param node  添加节点
     * @param lines 与前置节点的逻辑关系
     */
    public void addNode(Node node, Line... lines) {
        if (includeDate && node.getBeginDate() == null) {
            throw new IllegalArgumentException("node [" + node.getId() + "] 节点开始时间必填");
        }
        // 检查开始时间点
        if (includeDate && !includeWeekend) {
            node.setBeginDate(WorkDayUtil.getNextWorkDay(node.getBeginDate()));
        }

        Node exist = nodeMap.get(node.getId());
        if (exist == null) {
            graph.addNode(node);
            nodeMap.put(node.getId(), node);
        }
        if (lines != null && lines.length > 0) {
            Arrays.stream(lines).forEach(
                    l -> {
                        Node prevNode = nodeMap.get(l.getPrevNodeId());
                        if (prevNode == null) {
                            throw new IllegalArgumentException("不存在id为 [" + l.getPrevNodeId() + "] 的节点");
                        }
                        graph.putEdgeValue(prevNode, node, l);
                    }
            );
        }

        // 检查成环
        boolean hasLoop = checkHasLoop(node, node);
        if (hasLoop) {
            throw new IllegalArgumentException("node [" + node.getId() + "] 前置节点成环");
        }
    }


    /**
     * 计算 单代网络图
     */
    public void calcAON() {

        Set<Node> nodes = graph.nodes();
        if (CollectionUtils.isEmpty(nodes)) {
            throw new IllegalArgumentException("网络图为空");
        }

        // 是否有多个头节点
        Node headNode = checkHeadNode();

        // 是否有多个尾节点
        checkTailNode();

        // 每个节点的最早开始时间
        if (includeDate) {
            calcStartDateNodes();
        }

        // 向后计算最早节点
        next(headNode);

        // 向前计算最晚节点
        prev(headNode);
    }


    /**
     * 检查成环
     *
     * @return true 有环  false 无环
     */
    private boolean checkHasLoop(Node checkNode, Node currentNode) {
        Set<Node> prevNodes = graph.predecessors(currentNode);
        if (CollectionUtils.isEmpty(prevNodes)) {
            return false;
        }
        // 前置节点 = 添加的节点 表示有重复
        Optional<Node> any = prevNodes.stream().filter(n -> n.getId().equals(checkNode.getId())).findAny();
        if (any.isPresent()) {
            return true;
        }
        Optional<Boolean> nextCheck = prevNodes.stream().map(n -> checkHasLoop(checkNode, n)).filter(n -> n).findAny();
        return nextCheck.isPresent();
    }

    /**
     * 给多个头节点加一个虚拟的共同头节点
     *
     * @return 头结点
     */
    private Node checkHeadNode() {
        Node headNode;
        Set<Node> headNodes = headNodes();
        if (headNodes.size() == 1) {
            headNode = headNodes.stream().findFirst().get();
        } else {
            // 前置节点=-2 表示起始节点
            headNode = new Node(-2L, 0);
            headNodes.forEach(
                    n -> graph.putEdgeValue(headNode, n, Line.build(Line.relation(SS, 0)))
            );
            if (includeDate) {
                Node node = headNodes.stream().min(Comparator.comparingLong(n -> n.getBeginDate().getTime())).get();
                headNode.setBeginDate(node.getBeginDate());
            }
        }
        return headNode;
    }

    /**
     * 给多个尾节点的节点加一个虚拟的共同尾节点
     */
    private void checkTailNode() {
        Set<Node> tailNodes = tailNodes();
        if (tailNodes.size() > 1) {
            // 前置节点=-1 表示起始节点
            Node tailNod = new Node(-1L, 0);
            tailNodes.forEach(
                    n -> graph.putEdgeValue(n, tailNod, Line.build(Line.relation(FF, 0)))
            );
            if (includeDate) {
                Node node = tailNodes.stream().max(Comparator.comparingLong(n -> (long) (n.getDuration() * 86400000) + n.getBeginDate().getTime())).get();
                tailNod.setBeginDate(new Date((long) (node.getDuration() * 86400000) + node.getBeginDate().getTime()));
            }
        }
    }


    /**
     * 每个节点的最早开始时间
     * 与第一个开始节点的开始时间的天数差
     */
    private void calcStartDateNodes() {
        Set<Node> nodes = graph.nodes();
        if (CollectionUtils.isEmpty(nodes)) {
            throw new IllegalArgumentException("网络图为空");
        }
        Optional<Node> min = nodes.stream().filter(e -> e.getBeginDate() != null).min(Comparator.comparingLong(e -> e.getBeginDate().getTime()));
        if (min.isPresent()) {
            Date startDate = min.get().getBeginDate();
            nodes.forEach(
                    n -> {
                        double v;
                        if (includeWeekend) {
                            v = WorkDayUtil.betweenDaysOfDigit(startDate, n.getBeginDate(), 1);
                        } else {
                            v = WorkDayUtil.betweenDaysOfDigitExcludeWeekend(startDate, n.getBeginDate(), 1);
                        }
                        n.setMinBetweenDays(v);
                    }
            );
        }
    }


    /**
     * 没有前序节点的节点
     *
     * @return 没有前序节点的节点
     */
    private Set<Node> headNodes() {
        Set<Node> nodes = graph.nodes();
        Set<Node> headNodes = nodes.stream().filter(n -> CollectionUtils.isEmpty(graph.predecessors(n))).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(headNodes)) {
            throw new IllegalArgumentException("没有头节点");
        }
        return headNodes;
    }

    /**
     * 没有后序节点的节点
     *
     * @return 没有后序节点的节点
     */
    private Set<Node> tailNodes() {
        Set<Node> nodes = graph.nodes();
        Set<Node> tailNodes = nodes.stream().filter(n -> CollectionUtils.isEmpty(graph.successors(n))).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(tailNodes)) {
            throw new IllegalArgumentException("没有尾节点");
        }
        return tailNodes;
    }


    /**
     * 获取关键工序
     *
     * @return tf=0 的节点,且不是虚拟的头和尾节点
     */
    public List<Node> getFullNode() {
        calcAON();
        List<Node> collect = graph.nodes().stream().filter(n -> n.getId() != -1 && n.getId() != -2).filter(n -> n.getTf() != null && n.getTf() == 0).collect(Collectors.toList());
        return collect;
    }

    /**
     * 打印关键工序Node
     */
    public void printFullNode() {
        List<Node> fullNode = getFullNode();
        if (CollectionUtils.isNotEmpty(fullNode)) {
            fullNode.forEach(System.out::println);
        }
    }


    /**
     * 打印所有Node
     */
    public void printAllNode() {
        Set<Node> fullNode = graph.nodes();
        if (CollectionUtils.isNotEmpty(fullNode)) {
            fullNode.forEach(System.out::println);
        }
    }


    /**
     * 计算后序
     *
     * @param node 当前节点
     */
    private void next(Node node) {

        // 前序
        Set<Node> prevs = graph.predecessors(node);
        // 没有前序
        if (CollectionUtils.isEmpty(prevs)) {
            node.setEs(0d);
            node.setEf(node.getDuration());
            node.setEsMax(node.getEs());
            node.setEfMax(node.getEf());
            node.setPrev(true);
        }
        // 有前序 则计算当前
        else {
            // 如果前序 还没有计算
            prevs.stream().filter(p -> !p.getPrev()).forEach(this::next);

            // 根据前序计算当前
            prevs.forEach(
                    prev -> {
                        Optional<Line> lineOptional = graph.edgeValue(prev, node);
                        if (lineOptional.isPresent()) {
                            Line line = lineOptional.get();
                            // 只计算开始
                            List<Line.RelationType> relations = line.getRelations();
                            relations.forEach(
                                    r -> {
                                        String type = r.getType();
                                        Integer delay = r.getDelay();

                                        // 完成对开始(FS)：后续活动的开始要等到先行活动的完成。
                                        if (FS.equals(type)) {
                                            if (node.getEs() < prev.getEf() + delay) {
                                                node.setEs(prev.getEf() + delay);
                                                node.setEf(node.getEs() + node.getDuration());
                                            }
                                            if (node.getEs() < prev.getEfMax()) {
                                                node.setEs(prev.getEfMax());
                                                node.setEf(node.getEs() + node.getDuration());
                                            }
                                        }
                                        // 完成对完成(FF)：后续活动的完成要等到先行活动的完成。
                                        else if (FF.equals(type)) {
                                            if (node.getEf() < prev.getEf() + delay) {
                                                node.setEf(prev.getEf() + delay);
                                                node.setEs(node.getEf() - node.getDuration());
                                            }
                                            if (node.getEf() < prev.getEfMax()) {
                                                node.setEf(prev.getEfMax());
                                                node.setEs(node.getEf() - node.getDuration());
                                            }
                                        }
                                        // 开始对开始(SS)：后续活动的开始要等到先行活动的开始。
                                        else if (SS.equals(type)) {
                                            if (node.getEs() < prev.getEs() + delay) {
                                                node.setEs(prev.getEs() + delay);
                                                node.setEf(node.getEs() + node.getDuration());
                                            }
                                        }
                                        // 开始对完成(SF)：后续活动的完成要等到先行活动的开始。
                                        else if (SF.equals(type)) {
                                            if (node.getEf() < prev.getEs() + delay) {
                                                node.setEf(prev.getEs() + delay);
                                                node.setEs(node.getEf() - node.getDuration());
                                            }
                                        }
                                        // 修正为 负值的情况
                                        if (node.getEs() < 0) {
                                            node.setEs(0d);
                                            node.setEf(node.getDuration());
                                        }

                                        // 小于指定的开始时间
                                        if (includeDate) {
                                            if (node.getEs() < node.getMinBetweenDays()) {
                                                node.setEs(node.getMinBetweenDays());
                                                node.setEf(node.getEs() + node.getDuration());
                                            }
                                        }

                                        // 至今最大的节点
                                        if (node.getEs() > prev.getEsMax()) {
                                            node.setEsMax(node.getEs());
                                        } else {
                                            node.setEsMax(prev.getEsMax());
                                        }
                                        if (node.getEf() > prev.getEfMax()) {
                                            node.setEfMax(node.getEf());
                                        } else {
                                            node.setEfMax(prev.getEfMax());
                                        }
                                        node.setPrev(true);
                                    }
                            );

                        }
                    }
            );
        }

        // 计算后序
        Set<Node> nexts = graph.successors(node);
        if (CollectionUtils.isNotEmpty(nexts)) {
            nexts.forEach(this::next);
        }
    }

    /**
     * 前序
     *
     * @param node 当前节点
     */
    private void prev(Node node) {
        // 后序
        Set<Node> nexts = graph.successors(node);
        // 没有后序
        if (CollectionUtils.isEmpty(nexts)) {
            node.setLs(node.getEs());
            node.setLf(node.getEf());
            node.setTf(0d);
            node.setNext(true);
        }
        // 有后序 则计算当前
        else {
            // 如果后序 还没有计算
            nexts.stream().filter(p -> !p.getNext()).forEach(this::prev);

            // 根据后续计算当前
            nexts.forEach(
                    next -> {
                        Line line = graph.edgeValue(node, next).get();

                        List<Line.RelationType> relations = line.getRelations();
                        relations.forEach(
                                r -> {
                                    String type = r.getType();
                                    Integer delay = r.getDelay();

                                    // 完成对开始(FS)：后续活动的开始要等到先行活动的完成。
                                    if (FS.equals(type)) {
                                        if (node.getLf() > next.getLs() - delay) {
                                            node.setLf(next.getLs() - delay);
                                            node.setLs(node.getLf() - node.getDuration());
                                        }
                                    }
                                    // 完成对完成(FF)：后续活动的完成要等到先行活动的完成。
                                    else if (FF.equals(type)) {
                                        if (node.getLf() > next.getLf() - delay) {
                                            node.setLf(next.getLf() - delay);
                                            node.setLs(node.getLf() - node.getDuration());
                                        }
                                    }
                                    // 开始对开始(SS)：后续活动的开始要等到先行活动的开始。
                                    else if (SS.equals(type)) {
                                        if (node.getLs() > next.getLs() - delay) {
                                            node.setLs(next.getLs() - delay);
                                            node.setLf(node.getLs() + node.getDuration());
                                        }
                                    }
                                    // 开始对完成(SF)：后续活动的完成要等到先行活动的开始。
                                    else if (SF.equals(type)) {
                                        if (node.getLs() > next.getLf() - delay) {
                                            node.setLs(next.getLf() - delay);
                                            node.setLf(node.getLs() + node.getDuration());
                                        }
                                    }

                                    // TODO
                                    if (node.getLf() > next.getLf()) {
                                        node.setLf(next.getLf());
                                        node.setLs(node.getLf() - node.getDuration());
                                    }

                                    node.setTf(node.getLs() - node.getEs());
                                    node.setNext(true);
                                }
                        );
                    }
            );
        }

        // 计算前序
        Set<Node> predecessors = graph.predecessors(node);
        if (CollectionUtils.isNotEmpty(predecessors)) {
            predecessors.forEach(this::prev);
        }
    }
}
