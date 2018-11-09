package com.sun.graph;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.sun.graph.Line;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author : Sun
 * @date : 2018/11/9 16:01
 */
public class GraphTest {


    public static void main(String[] args) {


    }


    public void xx() {

//        ValueGraph<Integer, Line >  from = new ConfigurableMutableValueGraph();
        MutableValueGraph<Node, Line> graph = ValueGraphBuilder.directed().build();

        Line ln1 = Line.build(1, 1);
        Line ln2 = Line.build(1, 2);
        Line ln3 = Line.build(1, 3);
        Line ln4 = Line.build(1, 4);
        Line ln5 = Line.build(1, 5);

        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);

        graph.putEdgeValue(n1, n2, ln1);
        graph.putEdgeValue(n1, n3, ln2);
        graph.putEdgeValue(n2, n4, ln3);
        graph.putEdgeValue(n3, n5, ln4);
        graph.putEdgeValue(n4, n5, ln5);

        System.out.println(graph);

        // 前置节点
        Set<Node> predecessors = graph.predecessors(n5);
        System.out.println(predecessors);

        // 后置
        Set<Node> successors = graph.successors(n2);
        System.out.println(successors);


        next(graph, n1);

    }


    public void next(MutableValueGraph<Node, Line> graph, Node node) {
        Set<Node> children = graph.successors(node);
        if (CollectionUtils.isNotEmpty(children)) {

            children.stream().forEach(
                    c -> {
                        // 前置
                        Set<Node> predecessors = graph.predecessors(c);

                        if (CollectionUtils.isNotEmpty(predecessors)) {
                            predecessors.stream().forEach(
                                    pc -> {
                                        if (!pc.getCal()) {
                                            next(graph, pc);
                                        }
                                    }
                            );
                        }
                        c.setCal(true);

                        // 后置
                        Set<Node> successors = graph.successors(c);
                        if (CollectionUtils.isNotEmpty(successors)) {
                            successors.stream().forEach(
                                    s -> next(graph, s)
                            );
                        }
                    }
            );
        }
    }


    public void max(List<Node> nodes) {
        nodes.stream().filter(n -> n.getTf() == 0).forEach(System.out::println);
    }
}