/**
 * This program takes in command-line paramaters and then uses those to 
 * construct a basic visual map. The user may choose to 
 * (a) display a map of their choice, to 
 * (b) map the distance/route between 2 intersections, or 
 * (c) both.
 * 
 * StreetMap is the driver class for this program. It gets the user's
 * command-line arguments, parses them, gets file input, and then
 * combines everything to create a map and show it / execute Dijkstra's
 * Algorithm on it.
 * 
 * @author Miles Moran
 * @version 1.0
 * @since 2018-12-12
 **/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class StreetMap extends Canvas {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 1200, HEIGHT = 700;
    
    private static LinkedList<Node> shortestPath;
    private static Node fromNode, toNode;
    private static Graph myGraph;
    
    private static boolean getDirections = false;
    private static boolean showWindow = false;
    
    /**
     * Main() is the driving method. See the class description above or
     * the README summary for a more detailed explanation.
     * @param args The list of command-line arguments from the user
     * @return none
     */
    public static void main(String[] args) {
        
        assert (args.length > 0) : "Improper argument formatting; try again.";

        String filename = args[0].trim();
        String fromName = "", toName = "";
        
        for(int i=1; i<args.length; i++) {
            if(args[i].trim().equalsIgnoreCase("--show")) {
                showWindow = true;
            } else if (args[i].trim().equalsIgnoreCase("--directions")) {
                getDirections = true;
                fromName = args[i+1];
                toName = args[i+2];
            }
        }
        
        
        try { 
            myGraph = new Graph();
            shortestPath = new LinkedList<Node>();

            getInput(filename); 
        } catch(IOException e) {
            System.out.println("Your file, " + filename + " could not be read; try again.");
            return;
        }
        
        if(getDirections) { 
            fromNode = myGraph.findNode(fromName);
            toNode = myGraph.findNode(toName);

            myGraph.dijkstra(fromNode);
            myGraph.getShortestPath(fromNode, toNode, shortestPath);
            printUsefulInfo();
        } 
        if(showWindow) { 
            for(Node n : myGraph.nodes()) {
                n.computeX(WIDTH-50.0, 50.0);
                n.computeY(HEIGHT-50.0, 50.0);
            } 

            createWindow();
        } 
    } 
    
    /**
     * Part of the Canvas component, this is the awt/swing method that
     * draws the nodes and edges of the graph onto the screen.
     * @param g The graphics object used behind-the-scenes by AWT/Swing
     * @return none
     **/
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        for(Node n1 : myGraph.nodes()) {
            for(Node n2 : n1.adj().keySet()) {
                if(n1.compareTo(n2) < 0)
                    g.drawLine(n1.x(), n1.y(), n2.x(), n2.y());
            }
        }
        
        if(getDirections) {
            g.setColor(Color.ORANGE);
            Node first = fromNode;
            for(Node next : shortestPath) {              
                g.drawLine(first.x(), first.y(), next.x(), next.y());
                g.fillOval(first.x(), first.y(), 3, 3);
                first = next;
            }
        }
    
    }
    
    /**
     * This method takes in a filename and attempts to read the file with
     * a BufferedReader. It reads the file line-by-line and parses each line
     * into either a node or an edge (which is subsequently added to the graph)
     * @param inputFile The name of the file to be read
     * @return none
     **/
    private static void getInput(String inputFile) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(inputFile));
        String nextLine;
        String[] params = null;
        
        while((nextLine = in.readLine()) != null) {
            params = nextLine.split("\\s+", 4);
            
            if(params[0].equals("i")) {
                parseIntersection(params);
            } else if(params[0].equals("r")) {
                myGraph.sortNodes();
                parseRoad(params);
                break;
            } else { 
                in.close();
                throw new IOException();
            }
            
        } 
        
        while((nextLine = in.readLine()) != null) {
            params = nextLine.split("\\s+", 4);
            
            if(params[0].equals("r")) {
                parseRoad(params);
            } else { 
                in.close();
                throw new IOException();
            }
            
        } 
        
        in.close();
    }
        
    /**
     * Takes in a list of paramaters (name, lattitude, longitude) and 
     * makes a node out of them
     * @param params The list of strings that are parsed into valuable data
     * @return none
     **/
    private static void parseIntersection(String[] params) {
        Double lat = Double.parseDouble(params[2]);
        Double lon = Double.parseDouble(params[3]) * -1.0;
        Node n = new Node(params[1], lat, lon);
        myGraph.addNode(n); 
    } 
    
    /** 
     * Takes in a list of paramters (name1, name2) and makes an edge out of them
     * @param params The list of strings that are parsed into valuable data
     * @return none
     **/
    private static void parseRoad(String[] params) {
        myGraph.addEdge(params[2], params[3]);
    } 
    
    /**
     * Prints some basic information about the shortest path onto the console
     * for the user to read (assuming they chose to get directions between
     * two nodes). If there is no path, it is stated.
     * @return none
     **/
    private static void printUsefulInfo() {
        if(shortestPath.size() > 0) {
            System.out.printf("THE ROUTE FROM %s TO %s IS AS FOLLOWS:\n\n",
                              fromNode.name(), toNode.name());
            
            for(Node n : shortestPath)
                System.out.printf("%s --> ", n.name());
            System.out.printf("DONE!\n\n");
            
            System.out.printf("GETTING FROM %s TO %s IS APPROX %f MILES.\n\n", 
                              fromNode.name(), toNode.name(), toNode.dist());
        } else {
            System.out.printf("THERE EXISTS NO ROUTE BETWEEN %s AND %s!\n\n", 
                               fromNode.name(), toNode.name());
        }
    }
    
    /**
     * Uses the AWT/SWING components to create a window and add all the 
     * features of the graph to it before painting. Also sets some basic
     * attributes about the window (e.g. what to do when it is closed).
     * @return none
     **/
    private static void createWindow() {
        StreetMap map = new StreetMap();
        
        JFrame window = new JFrame("Miles Moran's Amazing Mapping Project!");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.add(map);
        window.setSize(WIDTH, HEIGHT);
        window.setVisible(true);
    } 
    
} 