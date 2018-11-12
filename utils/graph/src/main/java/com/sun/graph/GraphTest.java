package com.sun.graph;


import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

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

        Node n1 = new Node(1, 1);
        Node n2 = new Node(2, 2);
        Node n3 = new Node(3, 5);
        Node n4 = new Node(4, 4);
        Node n5 = new Node(5, 5);

        graph.putEdgeValue(n1, n2, Line.build());
        graph.putEdgeValue(n1, n3, Line.build());
        graph.putEdgeValue(n2, n4, Line.build());
        graph.putEdgeValue(n3, n5, Line.build());
        graph.putEdgeValue(n4, n5, Line.build());

        System.out.println(graph);

        GraphAON.printFullNode(graph);

        System.out.println("-----------------------------------------------------------------");

        graph.nodes().stream().forEach(System.out::println);

    }

}