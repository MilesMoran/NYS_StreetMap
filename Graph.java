/**
 * This class is more-or-less a container for the nodes and the 
 * functionality between said nodes (e.g. executing Dijkstra's Alg.)
 *
 * @author Miles Moran
 * @since 2018-12-12
 **/

import java.util.*;

public class Graph {
    private ArrayList<Node> nodes;
    
    /**
     * This constructor does nothing but initializes the ArrayList of nodes.
     * @param none
     * @return none
     **/
    public Graph() { nodes = new ArrayList<Node>(); }
    
    /**
     * Returns the ArrayList of nodes
     * @param none
     * @return ArrayList<Node> the ArrayList of nodes
     **/
    public ArrayList<Node> nodes() { return nodes; }
    
    /**
     * Adds a node to the list of nodes
     * @param n The node to be added
     * @return none
     **/
    public void addNode(Node n) { nodes.add(n); }
    
    /**
     * Sorts the list of nodes using Collections.sort()
     * @param none
     * @return none
     **/
    public void sortNodes() { Collections.sort(nodes); }
    
    /**
     * This method picks out 2 nodes from the ArrayList of nodes and then
     * adds each node to the other's adjacency list. This creates an edge.
     * @param node1Name The name of the first node
     * @param node2Name The name of the second node
     * @return none
     **/
    public void addEdge(String node1Name, String node2Name) {
        Node n1 = findNode(node1Name);
        Node n2 = findNode(node2Name);
        n1.addAdj(n2);
        n2.addAdj(n1);
    } 

    /**
     * Finds a node in the ArrayList by BinarySearching for its name.
     * @param name The name of the node
     * @return Node The node being looked for
     **/
    public Node findNode(String name) {
        int i = Collections.binarySearch(nodes, new Node(name, Double.NaN, Double.NaN) );
        assert (i >= 0) : "NODE CANNOT BE GOTTEN";
        return nodes.get(i);
    } 
  
    /**
     * Uses Dijkstra's Algorithm to compute and save the smallest distance 
     * between the source node and ALL other nodes. Uses a custom comparator 
     * b/c It's useful to compare these nodes by distance INSTEAD of by name.
     * @param source The source node
     * @return none
     **/
    public void dijkstra(Node source) {
        
        Comparator<Node> distComparator = new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                return Double.compare(n1.dist(), n2.dist());
            }
        };

        PriorityQueue<Node> unvisited = new PriorityQueue<Node>(distComparator);
        source.setDist(0.0);
        unvisited.add(source);
        
        Node u;
        while( (u = unvisited.poll()) != null) {
            if(u.visited()) continue;
            u.setVisited(true);

            for(Map.Entry<Node, Double> neighbor : u.adj().entrySet()) {
                Double newDist = u.dist() + neighbor.getValue();
                Node v = neighbor.getKey();
                if( (newDist < v.dist()) && !v.visited() ) {
                    v.setDist(newDist);
                    v.setParent(u);
                    unvisited.add(v);
                }
            }
        }
        return;
    } 

    /**
     * Uses recursion to add each parent node to the list of nodes that
     * makeup the shortest path between fromNode and toNode.
     * @param fromNode The starting node
     * @param toNode The ending node
     * @param path The list that stores the path of Nodes
     * @return none
     **/
    public void getShortestPath(Node fromNode, Node toNode, LinkedList<Node> path) {
        if(toNode.parent() != null) {
            path.addFirst(toNode);
            getShortestPath(fromNode, toNode.parent(), path);
        } else if (toNode.compareTo(fromNode) == 0) {
            path.addFirst(toNode);
        }
    }
} 