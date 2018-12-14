package com.sun;

import com.sun.graph.GraphAON;
import com.sun.graph.Line;
import com.sun.graph.Node;

import static com.sun.graph.Line.relation;

/**
 * @author : Sun
 * @date : 2018/11/9 16:01
 */
public class GraphTest {

    public static void main(String[] args) {
        GraphTest xx = new GraphTest();
        GraphAON graphAON = xx.test7();
        graphAON.printAllNode();

        graphAON.calcAON();

        System.out.println("--------------------------------------------------------");
//        graphAON.printFullNode();

        graphAON.printAllNode();
        
        
        System.out.println("--------------------------------------------------------");
        graphAON.printFullNode();
    }


    public GraphAON test7() {

        GraphAON graphAON = new GraphAON(true,false);

        graphAON.addNode(new Node(1L, "2018-11-27", 0));
        graphAON.addNode(new Node(2L, "2018-12-01", 0.5 ), Line.build(1L, relation()));
        graphAON.addNode(new Node(3L, "2018-11-28", 3 ), Line.build(1L, relation()));
        graphAON.addNode(new Node(4L, "2018-12-03 12:00:00", 4 ), Line.build(2L, relation()), Line.build(3L, relation()));
        graphAON.addNode(new Node(5L, "2018-12-07", 1.5), Line.build(4L, relation()));

        return graphAON;
    }

    public GraphAON test6() {

        GraphAON graphAON = new GraphAON();

        graphAON.addNode(new Node(1L, 0));
        graphAON.addNode(new Node(2L, 0.5 ), Line.build(1L, relation()));
        graphAON.addNode(new Node(3L, 2 ), Line.build(2L, relation()));
        graphAON.addNode(new Node(4L, 1.5), Line.build(3L, relation()));
        graphAON.addNode(new Node(2L, 1.5), Line.build(4L, relation()));

        return graphAON;
    }



    public GraphAON test5() {

        GraphAON graphAON = new GraphAON();

        graphAON.addNode(new Node(1L, "2018-01-01", 0));
        graphAON.addNode(new Node(2L, "2018-02-01", 0.5 ), Line.build(1L, relation()));
        graphAON.addNode(new Node(3L, "2018-03-01", 3 ), Line.build(1L, relation()));
        graphAON.addNode(new Node(4L, "2018-04-01", 4 ), Line.build(2L, relation()), Line.build(3L, relation()));
        graphAON.addNode(new Node(5L, "2018-05-01", 1.5), Line.build(3L, relation()));

        return graphAON;
    }
    
    
    public GraphAON test4() {

        GraphAON graphAON = new GraphAON();

        graphAON.addNode(new Node(1L, "2018-01-01", 8d ));
        graphAON.addNode(new Node(2L, "2018-02-01", 2d ), Line.build(1L, relation("FS", 0)));
        graphAON.addNode(new Node(3L, "2018-03-01", 5d ), Line.build(2L, relation("FS", 0)));
        graphAON.addNode(new Node(4L, "2018-04-01", 4d ), Line.build(2L, relation("FS", 0)), Line.build(3L, relation("FS", 0)));
        graphAON.addNode(new Node(5L, "2018-05-01", 10d), Line.build(3L, relation("FS", 0)));
        graphAON.addNode(new Node(6L, "2018-06-01", 6d ), Line.build(4L, relation("FS", 0)), Line.build(5L, relation("FS", 0)));

        return graphAON;
    }
//
//    public MutableValueGraph<Node, Line> test3() {
//
////        ValueGraph<Integer, Line >  from = new ConfigurableMutableValueGraph();
//        MutableValueGraph<Node, Line> graph = ValueGraphBuilder.directed().build();
//
//        Node n1 = new Node(1, 6);
//        Node n2 = new Node(2, 8);
//        Node n3 = new Node(3, 12);
//        Node n4 = new Node(4, 5);
//        Node n5 = new Node(5, 4);
//        Node n6 = new Node(6, 0);
//
//        graph.putEdgeValue(n1, n2, Line.build(Line.relation("SS", 4)));
//        graph.putEdgeValue(n1, n3, Line.build(Line.relation("FF", 10)));
//        graph.putEdgeValue(n2, n4, Line.build(Line.relation("SF", 10)));
//        graph.putEdgeValue(n3, n4, Line.build(Line.relation("FF", 8)));
//        graph.putEdgeValue(n3, n5, Line.build(Line.relation("FS", 4)));
//        graph.putEdgeValue(n4, n6, Line.build(Line.relation("FS", 0)));
//        graph.putEdgeValue(n5, n6, Line.build(Line.relation("FS", 0)));
//
//        return graph;
//    }
//
//    public MutableValueGraph<Node, Line> test2() {
//
////        ValueGraph<Integer, Line >  from = new ConfigurableMutableValueGraph();
//        MutableValueGraph<Node, Line> graph = ValueGraphBuilder.directed().build();
//
//        Node n1 = new Node(1, 6);
//        Node n2 = new Node(2, 8);
//        Node n3 = new Node(3, 14);
//        Node n4 = new Node(4, 10);
//        Node n5 = new Node(5, 10);
//        Node n6 = new Node(6, 14);
//        Node n7 = new Node(7, 6);
//        Node n8 = new Node(8, 4);
//        Node n9 = new Node(9, 0);
//        
//        graph.putEdgeValue(n1, n2, Line.build(Line.relation("SS", 2)));
//        graph.putEdgeValue(n1, n3, Line.build(Line.relation("FF", 4)));
//        graph.putEdgeValue(n1, n4, Line.build(Line.relation("FF", 2)));
//        graph.putEdgeValue(n2, n5, Line.build(Line.relation("FS", 2)));
//        graph.putEdgeValue(n3, n5, Line.build(Line.relation("SS", 6)));
//        graph.putEdgeValue(n3, n6, Line.build(Line.relation("SS", 3), Line.relation("FF", 6)));
//        graph.putEdgeValue(n4, n6, Line.build(Line.relation("FF", 14)));
//        graph.putEdgeValue(n4, n7, Line.build(Line.relation("FS", 0)));
//        graph.putEdgeValue(n5, n8, Line.build(Line.relation("SS", 4)));
//        graph.putEdgeValue(n6, n8, Line.build(Line.relation("SS", 2)));
//        graph.putEdgeValue(n7, n9, Line.build(Line.relation("FF")));
//        graph.putEdgeValue(n8, n9, Line.build(Line.relation("FF")));
//
//        return graph;
//    }
//
//    public MutableValueGraph<Node, Line> test1() {
//
////        ValueGraph<Integer, Line >  from = new ConfigurableMutableValueGraph();
//        MutableValueGraph<Node, Line> graph = ValueGraphBuilder.directed().build();
//
//        Node n1 = new Node(1, 6);
//        Node n2 = new Node(2, 8);
//        Node n3 = new Node(3, 15);
//        Node n4 = new Node(4, 5);
//        Node n5 = new Node(5, 10);
//        Node n6 = new Node(6, 15);
//        Node n7 = new Node(7, 5);
//        Node n8 = new Node(8, 10);
//        Node n9 = new Node(9, 10);
//        Node n10 = new Node(10, 10);
//        Node n11 = new Node(11, 16);
//        Node n12 = new Node(12, 15);
//
//        graph.putEdgeValue(n1, n2, Line.build());
//        graph.putEdgeValue(n2, n3, Line.build());
//        graph.putEdgeValue(n3, n4, Line.build());
//        graph.putEdgeValue(n4, n5, Line.build());
//        graph.putEdgeValue(n3, n6, Line.build());
//        graph.putEdgeValue(n6, n7, Line.build());
//        graph.putEdgeValue(n4, n7, Line.build());
//        graph.putEdgeValue(n7, n8, Line.build());
//        graph.putEdgeValue(n5, n8, Line.build());
//        graph.putEdgeValue(n5, n9, Line.build());
//        graph.putEdgeValue(n9, n10, Line.build());
//        graph.putEdgeValue(n8, n11, Line.build());
//        graph.putEdgeValue(n9, n11, Line.build());
//        graph.putEdgeValue(n11, n12, Line.build());
//        graph.putEdgeValue(n10, n12, Line.build());
//
//        return graph;
//    }

}