package com.sun.graph;


import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

/**
 * @author : Sun
 * @date : 2018/11/9 16:01
 */
public class GraphTest {

    public static void main(String[] args) {
        GraphTest xx = new GraphTest();
        MutableValueGraph<Node, Line> graph = xx.test2();

        System.out.println(graph);

        GraphAON.printFullNode(graph);

        System.out.println("-----------------------------------------------------------------");

        graph.nodes().stream().forEach(System.out::println);
    }


    public MutableValueGraph<Node, Line> test3() {

//        ValueGraph<Integer, Line >  from = new ConfigurableMutableValueGraph();
        MutableValueGraph<Node, Line> graph = ValueGraphBuilder.directed().build();

        Node n1 = new Node(1, 6);
        Node n2 = new Node(2, 8);
        Node n3 = new Node(3, 12);
        Node n4 = new Node(4, 5);
        Node n5 = new Node(5, 4);
        Node n6 = new Node(6, 0);

        graph.putEdgeValue(n1, n2, Line.build(Line.createRelation("SS", 4)));
        graph.putEdgeValue(n1, n3, Line.build(Line.createRelation("FF", 10)));
        graph.putEdgeValue(n2, n4, Line.build(Line.createRelation("SF", 10)));
        graph.putEdgeValue(n3, n4, Line.build(Line.createRelation("FF", 8)));
        graph.putEdgeValue(n3, n5, Line.build(Line.createRelation("FS", 4)));
        graph.putEdgeValue(n4, n6, Line.build(Line.createRelation("FS", 0)));
        graph.putEdgeValue(n5, n6, Line.build(Line.createRelation("FS", 0)));

        return graph;
    }

    public MutableValueGraph<Node, Line> test2() {

//        ValueGraph<Integer, Line >  from = new ConfigurableMutableValueGraph();
        MutableValueGraph<Node, Line> graph = ValueGraphBuilder.directed().build();

        Node n1 = new Node(1, 6);
        Node n2 = new Node(2, 8);
        Node n3 = new Node(3, 14);
        Node n4 = new Node(4, 10);
        Node n5 = new Node(5, 10);
        Node n6 = new Node(6, 14);
        Node n7 = new Node(7, 6);
        Node n8 = new Node(8, 4);
        Node n9 = new Node(9, 0);

        graph.putEdgeValue(n1, n2, Line.build(Line.createRelation("SS", 2)));
        graph.putEdgeValue(n1, n3, Line.build(Line.createRelation("FF", 4)));
        graph.putEdgeValue(n1, n4, Line.build(Line.createRelation("FF", 2)));
        graph.putEdgeValue(n2, n5, Line.build(Line.createRelation("FS", 2)));
        graph.putEdgeValue(n3, n5, Line.build(Line.createRelation("SS", 6)));
        graph.putEdgeValue(n3, n6, Line.build(Line.createRelation("SS", 3), Line.createRelation("FF", 6)));
        graph.putEdgeValue(n4, n6, Line.build(Line.createRelation("FF", 14)));
        graph.putEdgeValue(n4, n7, Line.build(Line.createRelation("FS", 0)));
        graph.putEdgeValue(n5, n8, Line.build(Line.createRelation("SS", 4)));
        graph.putEdgeValue(n6, n8, Line.build(Line.createRelation("SS", 2)));
        graph.putEdgeValue(n7, n9, Line.build(Line.createRelation("FF")));
        graph.putEdgeValue(n8, n9, Line.build(Line.createRelation("FF")));

        return graph;
    }

    public MutableValueGraph<Node, Line> test1() {

//        ValueGraph<Integer, Line >  from = new ConfigurableMutableValueGraph();
        MutableValueGraph<Node, Line> graph = ValueGraphBuilder.directed().build();

        Node n1 = new Node(1, 6);
        Node n2 = new Node(2, 8);
        Node n3 = new Node(3, 15);
        Node n4 = new Node(4, 5);
        Node n5 = new Node(5, 10);
        Node n6 = new Node(6, 15);
        Node n7 = new Node(7, 5);
        Node n8 = new Node(8, 10);
        Node n9 = new Node(9, 10);
        Node n10 = new Node(10, 10);
        Node n11 = new Node(11, 16);
        Node n12 = new Node(12, 15);

        graph.putEdgeValue(n1, n2, Line.build());
        graph.putEdgeValue(n2, n3, Line.build());
        graph.putEdgeValue(n3, n4, Line.build());
        graph.putEdgeValue(n4, n5, Line.build());
        graph.putEdgeValue(n3, n6, Line.build());
        graph.putEdgeValue(n6, n7, Line.build());
        graph.putEdgeValue(n4, n7, Line.build());
        graph.putEdgeValue(n7, n8, Line.build());
        graph.putEdgeValue(n5, n8, Line.build());
        graph.putEdgeValue(n5, n9, Line.build());
        graph.putEdgeValue(n9, n10, Line.build());
        graph.putEdgeValue(n8, n11, Line.build());
        graph.putEdgeValue(n9, n11, Line.build());
        graph.putEdgeValue(n11, n12, Line.build());
        graph.putEdgeValue(n10, n12, Line.build());

        return graph;
    }

}