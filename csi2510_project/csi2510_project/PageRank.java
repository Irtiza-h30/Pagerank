/*
 * Computes the PageRank (PR) of each node A in a directed graph using a recursive definition:
 * PR(A) = (1-d) + d (PR(T1)/C(T1) + ... + PR(Tn)/C(Tn))
 * Here d is a damping factor that we will set to 0.85. Nodes T1, T2, ..., Tn are the nodes that
 * connect to A (i.e. having a link going from Ti to A). The term C(Ti) is the number of links outgoing from Ti.
 *
 * @author Md Atiqur Rahman (mrahm021@uottawa.ca)
 */

package csi2510_project;

import java.util.*;

public class PageRank {
    public static final double DAMPING_FACTOR = 0.85;    // damping factor
    private double tolerance;                            // tolerance to stop
    private long maxIter;                                // max iterations to stop
    private HashMap<Integer, Double[]> prList;

    PageRank() {
        // default tolerance=0.000001, default maxIter=100
        this(0.000001, 100);
    }

    PageRank(double tolerance, long maxIter) {
        this.tolerance = tolerance;
        this.maxIter = maxIter;
    }

    /**
     * Computes the PageRank (PR) of each node in a graph in an iterative way.
     * Iteration stops as soon as this.maxIter or this.tolerance whichever is reached first.
     *
     * @param graph the Graph to compute PR for
     * @return returns a Map<Integer, Double> mapping each node to its PR
     */

    public Map<Integer, Double> computePageRank(Graph graph) {
        // replace this with your code
        Map<Integer, Double> prValue = initializeMap(graph);
        Map<Integer, List<Integer>> node = edgeMap(graph);
        prList = new HashMap<>();
        List<Integer> nodes = graph.getGraphNodes();

        for (int i = 0; i < nodes.size(); i++){
            prList.put(nodes.get(i), new Double[]{0.0,1.0} );
        }

        int count = 0;
        double difference = 0.0;
        double total;

        while (count++ < maxIter){
            if (difference  < tolerance && count != 1){
                break;
            }
            total = 0.0;
            prValue = pageRank(graph, prValue, node);
            for (int i = 0; i < nodes.size(); i++){
                total += Math.abs(prList.get(nodes.get(i))[0] - prList.get(nodes.get(i))[1]);
            }
            difference = total/nodes.size();
        };
        return prValue;
    }

    /**
     *
     * Creates the map by putting all the nodes into the map and initializing each node with a value of 1.0
     * @param graph the Graph to compute PR for
     * @return returns a Map<Integer, Double> mapping each node with an initial node value of 1.0
     */
    private static Map<Integer, Double> initializeMap(Graph graph) {

        List<Integer> nodes = graph.getGraphNodes();
        Map<Integer, Double> map = new HashMap<>();

        for (int i = 0; i < nodes.size(); i++) {
            map.put(nodes.get(i), 1.0);
        }
        return map;
    }

    /**
     * @param graph the Graph
     * @return returns a Map<Integer, Double> with nodes and its links to the outgoing edges
     */
    private static Map<Integer, List<Integer>> edgeMap(Graph graph) {

        Map<Integer, List<Integer>> map = new HashMap<>();
        List<Integer> nodes = graph.getGraphNodes();
        Map<Integer, List<Integer>> edges = graph.getGraphEdges();

        for (int i = 0; i < nodes.size(); i++) {
            List<Integer> addEdge = new ArrayList<>();
            for (int j = 0; j < nodes.size(); j++) {
                if (edges.containsKey(nodes.get(j))) {
                    for (int edge = 0; edge < edges.get(nodes.get(j)).size(); edge++) {
                        if (edges.get(nodes.get(j)).get(edge).equals(nodes.get(i))) {
                            addEdge.add(nodes.get(j));
                        }
                    }
                }
            }
            map.put(nodes.get(i), addEdge);
        }
        return map;
    }

    /**
     * Computes the page-rank value based on the graph
     * @param graph Graph, currentPr the current page-rank value, node - Nodes in the graph
     * @return returns a Map<Integer, Double> with the page rank value for each node
     */
    private Map<Integer, Double> pageRank(Graph graph, Map<Integer, Double> currentPR, Map<Integer, List<Integer>> nodes) {

        double sum;
        Double[] temp = new Double[2];
        Map<Integer, Double> map = new HashMap<>();

        for (Integer i : graph.getGraphNodes()) {
            sum = 0;
            for (Integer node : nodes.get(i)) {
                sum += DAMPING_FACTOR * (currentPR.get(node) / graph.getGraphEdges().get(node).size());
            }
            map.put(i, (1 - DAMPING_FACTOR) + (sum));
            temp[0] = prList.get(i)[1];
            temp[1] = (1 - DAMPING_FACTOR) + (sum);
            prList.replace(i,temp);
        }
        return map;
    }
}