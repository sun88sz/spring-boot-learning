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
            node.setPrev(true);
        }
        // 有前序 则计算当前
        else {
            // 如果前序 还没有计算
            prevs.stream().filter(p -> !p.getPrev()).forEach(p -> next(graph, p));

            // 根据前序计算当前
            prevs.stream().forEach(
                    p -> {
                        Line line = graph.edgeValue(p, node).get();
                        // 完成对开始(FS)：后续活动的开始要等到先行活动的完成。
                        if ("FS".equals(line.getType())) {
                            if (node.getEs() < p.getEf()) {
                                node.setEs(p.getEf());
                                node.setEf(node.getEs() + node.getQuantity());
                                node.setPrev(true);
                            }
                        }
                        // 开始对开始(SS)：后续活动的开始要等到先行活动的开始。
                        else if ("SS".equals(line.getType())) {
                            if (node.getEs() < p.getEs()) {
                                node.setEs(p.getEs());
                                node.setEf(node.getEs() + node.getQuantity());
                                node.setPrev(true);
                            }
                        }
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
                    p -> {
                        Line line = graph.edgeValue(node, p).get();
                        // 完成对开始(FS)：后续活动的开始要等到先行活动的完成。
                        if ("FS".equals(line.getType())) {
                            if (p.getLs() < node.getLf()) {
                                node.setLf(p.getLs());
                                node.setLs(node.getLf() - node.getQuantity());
                                node.setTf(node.getLs() - node.getEs());
                                node.setNext(true);
                            }
                        }
                        // 完成对完成(FF)：后续活动的完成要等到先行活动的完成。
                        else if ("FF".equals(line.getType())) {

                        }
                        // 开始对开始(SS)：后续活动的开始要等到先行活动的开始。
                        else if ("SS".equals(line.getType())) {

                        }
                        // 开始对完成(SF)：后续活动的完成要等到先行活动的开始。
                        else if ("SF".equals(line.getType())) {

                        }
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
