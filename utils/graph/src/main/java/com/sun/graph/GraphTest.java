package com.sun.graph;

import java.util.Optional;
import java.util.Set;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author : Sun
 * @date : 2018/11/9 16:01
 */
public class GraphTest {


    public static void main(String[] args) {

        new GraphTest().xx();
    }


    public void xx() {

//        ValueGraph<Integer, Line >  from = new ConfigurableMutableValueGraph();
        MutableValueGraph<Node, Line> graph = ValueGraphBuilder.directed().build();

        Node n1 = new Node(1);
        n1.setEf(0);
        n1.setEs(0);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);

        graph.putEdgeValue(n1, n2, Line.build(1));
        graph.putEdgeValue(n1, n3, Line.build(2));
        graph.putEdgeValue(n2, n4, Line.build(4));
        graph.putEdgeValue(n3, n5, Line.build(3));
        graph.putEdgeValue(n4, n5, Line.build(5));

        System.out.println(graph);

        // 前置节点
        Set<Node> predecessors = graph.predecessors(n5);
        System.out.println(predecessors);

        // 后置
        Set<Node> successors = graph.successors(n2);
        System.out.println(successors);

        next(graph, n1);
        prev(graph, n5);

        System.out.println(graph);

        graph.nodes().stream().filter(n -> n.getTf() != null && n.getTf() == 0).forEach(System.out::println);
    }

    /**
     * 计算后序
     *
     * @param graph
     * @param node
     */
    public void next(MutableValueGraph<Node, Line> graph, Node node) {

        // 前序
        Set<Node> prevs = graph.predecessors(node);
        // 没有前序
        if (CollectionUtils.isEmpty(prevs)) {
            node.setEs(0);
            node.setEf(0);
            node.setPrev(true);
        }
        // 有前序 则计算当前
        else {
            // 如果前序 还没有计算
            prevs.stream().filter(p -> !p.getPrev()).forEach(p -> next(graph, p));

            prevs.stream().forEach(
                    p -> {
                        //
                        Optional<Line> lineOptional = graph.edgeValue(p, node);
                        if (lineOptional.isPresent()) {
                            Line line = lineOptional.get();
                            int i = p.getEs() + line.getQuantity();
                            if (i > node.getEf()) {
                                node.setEs(p.getEf());
                                node.setEf(i);
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
    public void prev(MutableValueGraph<Node, Line> graph, Node node) {
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

            nexts.stream().forEach(
                    p -> {
                        //
                        Optional<Line> lineOptional = graph.edgeValue(node, p);
                        if (lineOptional.isPresent()) {
                            Line line = lineOptional.get();
                            int i = p.getLf() - line.getQuantity();
                            if (i < node.getLs()) {
                                node.setLf(p.getLs());
                                node.setLs(i);
                                node.setTf(node.getLs() - node.getEs());
                                node.setNext(true);
                            }
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