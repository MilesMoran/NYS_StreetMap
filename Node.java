/**
 * This class is a comparable that holds information about a location in 
 * geographic space (lattitude and longitude), a location in the window 
 * (x and y), as well as a mapping of each adjacent node to its respective 
 * distance from this node. 
 * 
 * The variables for minimum/maximum Lattitude/Longitude are updated with 
 * each new node. Every other value is unique to the instance.
 *
 * @author Miles Moran
 * @since 2018-12-12
 **/

import java.util.*;

public class Node implements Comparable<Node> {
    
    private static Double minLon = 180.0; 
    private static Double maxLon = 0.0;
    private static Double minLat = 180.0; 
    private static Double maxLat = 0.0;

    private Map<Node, Double> adj;
    
    private Double dist = Double.POSITIVE_INFINITY;
    private Node parent = null;
    private boolean visited = false;
    
    private String name;
    private Double lat, lon;
    private int x, y;
    
    /**
     * This constructor initializes the Node's name, lattitute, and longitude.
     * Also updates the minimum and maximum if applicable.
     * @param name The name of the node
     * @param lat The value of this node's lattitude
     * @param lon The value of this node's longitude
     **/
    public Node(String name, Double lat, Double lon) {
        adj = new HashMap<Node, Double>();
        this.name = name; 
        this.lat = lat; 
        this.lon = lon;
        
        if(lon != Double.NaN && lat != Double.NaN) {
            if(lon < minLon) minLon = lon;
            if(lon > maxLon) maxLon = lon;
            if(lat < minLat) minLat = lat;
            if(lat > maxLat) maxLat = lat;
        } 
    }

    /** Getters **/
    public Map<Node, Double> adj() { return adj; }
    public Double dist() { return dist; }
    public Node parent() { return parent; }
    public boolean visited() { return visited; }
    public String name() { return name; }
    public Double lat() { return lat; }
    public Double lon() { return lon; }
    public int x() { return x; }
    public int y() { return y; }

    /** Setters **/
    public void setDist(Double d) { dist = d; }
    public void setParent(Node n) { parent = n; }
    public void setVisited(boolean v) { visited = v; }
    
    /**
     * Required because of the comparable interface. Allows nodes to be
     * compared by their name (alphabetically)
     * @param n2 The node to be compared to
     * @return int The difference in values
     **/
    public int compareTo(Node n2) { 
        return name.compareTo(n2.name()); 
    }
    
    /**
     * Adds a new node to this node's adjacency list
     * @param newNode The node to be added
     * @return none
     **/
    public void addAdj(Node newNode) {
        adj.put(newNode, computeDist(newNode));
    } 
    
    /**
     * Uses longitude to compute this node's X location on the screen
     * @param outMin The minimum value this can map TO
     * @param outMax the maximum value this can map TO
     * @return none
     **/
    public void computeX(Double outMin, Double outMax) {
        x = (int) Math.round( map(lon, minLon, maxLon, outMin, outMax) );
    }
    
    /**
     * Uses lattitude to compute this node's Y location on the screen
     * @param outMin The minimum value this can map TO
     * @param outMax the maximum value this can map TO
     * @return none
     **/
    public void computeY(Double outMin, double outMax) { 
        y = (int) Math.round( map(lat, minLat, maxLat, outMin, outMax) );
    } 
    
    /**
     * An implementation of the Haversine formula so that the geographic
     * distance between 2 nodes can be computed using their lattitudes
     * and longitudes.
     * @param n2 The node to compute the distance to
     * @return Double The distance to n2 in miles
     **/
    public Double computeDist(Node n2) {
        Double dLat = .5 * Math.toRadians(n2.lat() - lat);
        Double dLon = .5 * Math.toRadians(n2.lon() - lon);
        
        Double tmp = Math.cos(Math.toRadians(lat)) * 
            Math.cos(Math.toRadians(n2.lat()));
        
        Double a = Math.pow(Math.sin(dLat),2) + Math.pow(Math.sin(dLon),2) * tmp;
        return 7917.82 * Math.asin(Math.sqrt(a));
    }
    
    /**
     * A custom implementation of the map function from Arduino - this takes
     * in a value and maps it from one range to another
     * @param n The value to be mapped
     * @param inMin The minimum value of n's starting range
     * @param inMax The maximum value of n's starting range
     * @param outMin The minimum value of n's ending range
     * @param outMax The maximum value of n's ending range
     * @return Double The new value of n
     **/
    public Double map(Double n, Double inMin, Double inMax, Double outMin, Double outMax) { 
        return (n - inMin)*(outMax - outMin) / (inMax - inMin) + outMin; 
    }
    
} 