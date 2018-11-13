package com.sun.graph;

import com.google.common.collect.Lists;
import com.google.common.graph.MutableValueGraph;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * 计算 单代网络图
     *
     * @param graph
     */
    public static void calAON(MutableValueGraph<Node, Line> graph) {
        Set<Node> nodes = graph.nodes();
        if (CollectionUtils.isEmpty(nodes)) {
            throw new IllegalArgumentException("网络图为空");
        }

        Node head = new Node(0, 0);
        Node tail = new Node(Integer.MAX_VALUE, 0);

        // 给没有前继节点的节点加一个头

        // 给没有后继节点的节点加一个尾

        ArrayList<Node> nodeList = Lists.newArrayList(nodes);
        Node node = nodeList.get(0);
        next(graph, node);
        prev(graph, node);
    }


    public static List<Node> getFullNode(MutableValueGraph<Node, Line> graph) {
        calAON(graph);
        List<Node> collect = graph.nodes().stream().filter(n -> n.getTf() != null && n.getTf() == 0).collect(Collectors.toList());
        return collect;
    }

    public static void printFullNode(MutableValueGraph<Node, Line> graph) {
        List<Node> fullNode = getFullNode(graph);
        if (CollectionUtils.isNotEmpty(fullNode)) {
            fullNode.stream().forEach(System.out::println);
        }
    }

    /**
     * 计算后序
     *
     * @param graph
     * @param node
     */
    public static void next(MutableValueGraph<Node, Line> graph, Node node) {

        // 前序
        Set<Node> prevs = graph.predecessors(node);
        // 没有前序
        if (CollectionUtils.isEmpty(prevs)) {
            node.setEs(0);
            node.setEf(node.getQuantity());
            node.setEsMax(node.getEs());
            node.setEfMax(node.getEf());
            node.setPrev(true);
        }
        // 有前序 则计算当前
        else {
            // 如果前序 还没有计算
            prevs.stream().filter(p -> !p.getPrev()).forEach(p -> next(graph, p));

            // 根据前序计算当前
            prevs.stream().forEach(
                    prev -> {
                        Line line = graph.edgeValue(prev, node).get();
                        // 只计算开始
                        List<Line.RelationType> relations = line.getRelations();
                        relations.stream().forEach(
                                r -> {
                                    String type = r.getType();
                                    Integer delay = r.getDelay();

                                    // 完成对开始(FS)：后续活动的开始要等到先行活动的完成。
                                    if ("FS".equals(type)) {
                                        if (node.getEs() < prev.getEf() + delay) {
                                            node.setEs(prev.getEf() + delay);
                                            node.setEf(node.getEs() + node.getQuantity());
                                        }
                                        if (node.getEs() < prev.getEfMax()) {
                                            node.setEs(prev.getEfMax());
                                            node.setEf(node.getEs() + node.getQuantity());
                                        }
                                    }
                                    // 完成对完成(FF)：后续活动的完成要等到先行活动的完成。
                                    else if ("FF".equals(type)) {
                                        if (node.getEf() < prev.getEf() + delay) {
                                            node.setEf(prev.getEf() + delay);
                                            node.setEs(node.getEf() - node.getQuantity());
                                        }
                                        if (node.getEf() < prev.getEfMax()) {
                                            node.setEf(prev.getEfMax());
                                            node.setEs(node.getEf() - node.getQuantity());
                                        }
                                    }
                                    // 开始对开始(SS)：后续活动的开始要等到先行活动的开始。
                                    else if ("SS".equals(type)) {
                                        if (node.getEs() < prev.getEs() + delay) {
                                            node.setEs(prev.getEs() + delay);
                                            node.setEf(node.getEs() + node.getQuantity());
                                        }
                                    }
                                    // 开始对完成(SF)：后续活动的完成要等到先行活动的开始。
                                    else if ("SF".equals(type)) {
                                        if (node.getEf() < prev.getEs() + delay) {
                                            node.setEf(prev.getEs() + delay);
                                            node.setEs(node.getEf() - node.getQuantity());
                                        }
                                    }
                                    // 修正为 负值的情况
                                    if (node.getEs() < 0) {
                                        node.setEs(0);
                                        node.setEf(node.getQuantity());
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
            );
        }

        // 计算后序
        Set<Node> nexts = graph.successors(node);
        if (CollectionUtils.isNotEmpty(nexts)) {
            nexts.stream().forEach(p -> next(graph, p));
        }
    }

    /**
     * 前序
     *
     * @param graph
     * @param node
     */
    public static void prev(MutableValueGraph<Node, Line> graph, Node node) {
        // 后序
        Set<Node> nexts = graph.successors(node);
        // 没有后序
        if (CollectionUtils.isEmpty(nexts)) {
            node.setLs(node.getEs());
            node.setLf(node.getEf());
            node.setTf(0);
            node.setNext(true);
        }
        // 有后序 则计算当前
        else {
            // 如果后序 还没有计算
            nexts.stream().filter(p -> !p.getNext()).forEach(p -> prev(graph, p));

            // 根据后续计算当前
            nexts.stream().forEach(
                    next -> {
                        Line line = graph.edgeValue(node, next).get();

                        List<Line.RelationType> relations = line.getRelations();
                        relations.stream().forEach(
                                r -> {
                                    String type = r.getType();
                                    Integer delay = r.getDelay();

                                    // 完成对开始(FS)：后续活动的开始要等到先行活动的完成。
                                    if ("FS".equals(type)) {
                                        if (node.getLf() > next.getLs() - delay) {
                                            node.setLf(next.getLs() - delay);
                                            node.setLs(node.getLf() - node.getQuantity());
                                        }
                                    }
                                    // 完成对完成(FF)：后续活动的完成要等到先行活动的完成。
                                    else if ("FF".equals(type)) {
                                        if (node.getLf() > next.getLf() - delay) {
                                            node.setLf(next.getLf() - delay);
                                            node.setLs(node.getLf() - node.getQuantity());
                                        }
                                    }
                                    // 开始对开始(SS)：后续活动的开始要等到先行活动的开始。
                                    else if ("SS".equals(type)) {
                                        if (node.getLs() > next.getLs() - delay) {
                                            node.setLs(next.getLs() - delay);
                                            node.setLf(node.getLs() + node.getQuantity());
                                        }
                                    }
                                    // 开始对完成(SF)：后续活动的完成要等到先行活动的开始。
                                    else if ("SF".equals(type)) {
                                        if (node.getLs() > next.getLf() - delay) {
                                            node.setLs(next.getLf() - delay);
                                            node.setLf(node.getLs() + node.getQuantity());
                                        }
                                    }

                                    // TODO
                                    if (node.getLf() > next.getLf()) {
                                        node.setLf(next.getLf());
                                        node.setLs(node.getLf() - node.getQuantity());
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
            predecessors.stream().forEach(p -> prev(graph, p));
        }
    }
}
