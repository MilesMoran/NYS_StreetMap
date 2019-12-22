
Created by Miles Moran

CSC172 / Data Structures and Algorithms, Fall 2018

* An old Readme including explanations for ALL methods is available upon request

## SUMMARY

This command-line program constructs a map from 1 of 3 included datasets, then shows the map, gives directions, or both. User commands are as follows:

```
java StreetMap _ur.txt [--show] [--directions startIntersection endIntersection]

--show		shows the map
--directions 	prints in the console the shortest route between start and end,
		and draws a corresponding line if --show is active
```

The code for this program is split into 3 main classes as follows.
* StreetMap 
	- This is the driver class for the code. It follows an 9-step procedure:
	   	1. Parse the user's command-line arguments & assert formatting 
	   	2. Open up the input file (or throw an error)
		3. Create nodes from intersections
	   	4. Sort the nodes Alphabetically so they can be BinarySearched*
	   	5. Create adj-lists from roads
		6. If the user wants to compute directions,
			a. Execute Dijkstra's algorithm
			b. Print out the resulting information
		7. If the user wants to show the window, 
			a. Create a window
			b. Compute the coordinates of each node in the window
		8. Paint the window

* Graph
	- This class is a container for the nodes and the functionality between said nodes (e.g. executing Dijkstra's Alg.)
	
* Node
	- This class is a comparable that holds information about a location in geographic space (lattitude and longitude) and in the window (x and y), as well as a mapping of each adjacent node and its respective distance.

On my To-Do list is to assign each incoming Node a unique NUMERICAL ID so that they can be accessed directly from an Array (and thus drastically improve performance). The original data includes nodes like 981023-0 and 981023-1 which makes direct access a problem.

## NOTABLE OBSTACLES

The biggest struggle for me was figuring out how to implement Dijkstra's algorithm in an EFFICIENT manner. With a data set as small as the UR campus, the runtime changes only negligibly depending on the design. I found, however, that using a map for the adjacent nodes (within the node class) improved my time, as well as putting the elements into the PriorityQueue gradually rather than all-at-once.

Furthermore, it was difficult testing for correctness with no way of knowing whether or not two nodes were ACTUALLY connected. I found that my algorithm worked for ur.txt but not for the other two data sets... not because I implemented it wrong, but because I chose 2 points that weren't connected. Hah. 


## INCLUDED FILES

```
- Source Code: 
	StreetMap.java
	Graph.java
	Node.java
- Readme
	README.txt
- Datasets*
	_ur.txt
	_monroe.txt
	_nys.txt 
```
For an explanation of the data sets, an excerpt from my professors instructions:
```
Intersections start with “i”, followed by a unique string ID, and decimal representations of latitude and longitude. [i.e.]
	i IntersectionID Latitude Longitude
Roads start with “r”, followed by a unique string ID, and the IDs of the two intersections it connects. [i.e.]
	r RoadID Intersection1ID Intersection2ID
```

## DESIGN CHOICES

I tried to use data structures that made a lot of sense in this context, as well as using as many as possible from the Java STL so that I didn't waste time writing my own ADTs that have roughly the same functionality.

For example, Using a HashMap for the <Node, Distance> adj pairs made a lot of sense because EVERY node will have a corresponding distance, and they cannot be separated. sing parallel ArrayLists<> would have worked too, but then I would have to pay close attention to more than one list.

I also tried to make sure every part of the program ONLY executed if the user specified it -- that way I don't waste time on unneeded things. For example, I use flag values for showWindow and getDirections so that, if the user DIDN'T want directions between 2 nodes, I wouldn't waste the time attempting to execute Dijkstra's. 

## EXPECTED RUNTIMES

Note that all of these times are being taken on an **old** Lenovo laptop. Results may be drastically improved on newer machinery. Also note the to and from nodes may change performance

```
- ur.txt (OBRIEN --> WALLIS)
	-- show		: .375 s
	-- directions	: .094 s
	-- both		: .576 s

- monroe.txt (i563 --> i7440)
	-- show		: 6.626 s
	-- directions	: 3.235 s
	-- both		: 7.412 s

- nys.txt (i563 --> i74440)
	-- show		: 31.978 s
	-- directions	: 19.945 s
	-- both		: 33.342 s
```
