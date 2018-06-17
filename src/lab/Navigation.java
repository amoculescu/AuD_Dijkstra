package lab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * The class Navigation finds the shortest (and/or) path between points on a map
 * using the Dijkstra algorithm
 */
public class Navigation {
	/**
	 * Return codes: -1 if the source is not on the map -2 if the destination is
	 * not on the map -3 if both source and destination points are not on the
	 * map -4 if no path can be found between source and destination
	 */

	public static final int SOURCE_NOT_FOUND = -1;
	public static final int DESTINATION_NOT_FOUND = -2;
	public static final int SOURCE_DESTINATION_NOT_FOUND = -3;
	public static final int NO_PATH = -4;


	private ArrayList<Node> cities;


	/**
	 * The constructor takes a filename as input, it reads that file and fill
	 * the nodes and edges Lists with corresponding node and edge objects
	 * 
	 * @param filename
	 *            name of the file containing the input map
	 */
	public Navigation(String filename) {
		this.cities = new ArrayList<>();

		try{
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            ArrayList<String> lines = new ArrayList<>();



            String currentLine;
            boolean finished = false;
            //create cities and edges

            while(!finished) {
                currentLine = br.readLine();
                if(currentLine != null) {
                    lines.add(currentLine);
                }
                else
                    finished = true;
            }

            for (int k = 0; k < lines.size(); k++) {
                currentLine = lines.get(lines.size() -1 - k);
                //if vertex
                if (currentLine.matches("[a-z|A-Z]* \\[.*")) {
                    String name = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.indexOf(","));
                    int delay = Integer.parseInt(currentLine.substring(currentLine.indexOf(",") + 1, currentLine.lastIndexOf("\"")));
                    Node newNode = new Node(name, delay);
                    cities.add(newNode);
                }
                //if edge
                else if (currentLine.matches("[a-z|A-Z]* ->.*")) {
                    String start = "" + currentLine.substring(0, currentLine.indexOf("-") - 1);
                    String end = "" + currentLine.substring(currentLine.indexOf(">") + 2, currentLine.indexOf("[") - 1);
                    int distance = Integer.parseInt(currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf(",")));
                    int maxSpeed = Integer.parseInt(currentLine.substring(currentLine.indexOf(",") + 1, currentLine.lastIndexOf("\"")));

                    Node startNode = null;
                    Node endNode = null;
                    for (int i = 0; i < cities.size(); i++) {
                        if (cities.get(i).getName().equals(start)) {
                            startNode = cities.get(i);
                        } else if (cities.get(i).getName().equals(end)) {
                            endNode = cities.get(i);
                        }
                    }
                    if (startNode != null && endNode != null) {
                        Edge connection = new Edge(startNode, endNode, distance, maxSpeed);
                        startNode.addEdge(connection);
                    }
                }
            }

        } catch (IOException e){e.printStackTrace();}
	}

	/**
	 * This methods finds the shortest route (distance) between points A and B
	 * on the map given in the constructor.
	 * 
	 * If a route is found the return value is an object of type
	 * ArrayList<String>, where every element is a String representing one line
	 * in the map. The output map is identical to the input map, apart from that
	 * all edges on the shortest route are marked "bold". It is also possible to
	 * output a map where all shortest paths starting in A are marked bold.
	 * 
	 * The order of the edges as they appear in the output may differ from the
	 * input.
	 * 
	 * @param A
	 *            Source
	 * @param B
	 *            Destinaton
	 * @return returns a map as described above if A or B is not on the map or
	 *         if there is no path between them the original map is to be
	 *         returned.
	 */
	public ArrayList<String> findShortestRoute(String A, String B) {
		if (!A.equals(B)) {
			Node result = findPath(A, B, "Route, Distance");
			if (!result.getName().equals("null") && !result.getName().equals("ToD"))
				return makeMap(result);
		}
		return makeMap(null);
	}

	/**
	 * This methods finds the fastest route (in time) between points A and B on
	 * the map given in the constructor.
	 *
	 * If a route is found the return value is an object of type
	 * ArrayList<String>, where every element is a String representing one line
	 * in the map. The output map is identical to the input map, apart from that
	 * all edges on the shortest route are marked "bold". It is also possible to
	 * output a map where all shortest paths starting in A are marked bold.
	 *
	 * The order of the edges as they appear in the output may differ from the
	 * input.
	 *
	 * @param A
	 *            Source
	 * @param B
	 *            Destinaton
	 * @return returns a map as described above if A or B is not on the map or
	 *         if there is no path between them the original map is to be
	 *         returned.
	 */
	public ArrayList<String> findFastestRoute(String A, String B) {
		if(!A.equals(B)) {
			Node result = findPath(A, B, "Time, Distance");
			if (!result.getName().equals("null") && !result.getName().equals("ToD"))
				return makeMap(result);
		}
		return makeMap(null);
	}

	/**
	 * Finds the shortest distance in kilometers between A and B using the
	 * Dijkstra algorithm.
	 *
	 * @param A
	 *            the start point A
	 * @param B
	 *            the destination point B
	 * @return the shortest distance in kilometers rounded upwards.
	 *         SOURCE_NOT_FOUND if point A is not on the map
	 *         DESTINATION_NOT_FOUND if point B is not on the map
	 *         SOURCE_DESTINATION_NOT_FOUND if point A and point B are not on
	 *         the map NO_PATH if no path can be found between point A and point
	 *         B
	 */
	public int findShortestDistance(String A, String B) {
		if(!A.equals(B)) {
			Node result = findPath(A, B, "Distance");
			return evaluateTimeAndDistance(result);
		}
		return 0;
	}

	/**
	 * Find the fastest route between A and B using the dijkstra algorithm.
	 *
	 * @param pointA
	 *            Source
	 * @param pointB
	 *            Destination
	 * @return the fastest time in minutes rounded upwards. SOURCE_NOT_FOUND if
	 *         point A is not on the map DESTINATION_NOT_FOUND if point B is not
	 *         on the map SOURCE_DESTINATION_NOT_FOUND if point A and point B
	 *         are not on the map NO_PATH if no path can be found between point
	 *         A and point B
	 */
	public int findFastestTime(String pointA, String pointB) {
		if(!pointA.equals(pointB)) {
			Node result = findPath(pointA, pointB, "Time");
			return evaluateTimeAndDistance(result);
		}
		return 0;
	}



	//TODO javadoc

    /**
     * takes the result Node returned by findPath() and returns the time or distance required from point a to point b.
     * @param result
     *      Node returned by findPath(), with the name "null" and the distance or time required from point a to point b as the delay
     * @return
     *      time or distance required from point a to point b in km or minutes
     */
	private int evaluateTimeAndDistance(Node result){
	    return (int)Math.ceil(result.getDelay());
	}

	//TODO JAVADOC

    /**
     * Dijkstra's single source pathfinding algorithm
     * @param A
     *      source
     * @param B
     *      destination
     * @param type
     *      identifier which function called findPath and what result-type is going to be returned
     * @return
     *      End node if a path is found, else a new node with name="null" and delay as the error value
     */
	private Node findPath(String A, String B, String type){
		//initialize nodes for searching, find start and end Nodes if existing
        //creating auxillary variables
		boolean foundStart = false;
		boolean foundEnd = false;
		boolean foundPath = false;
		Node start = null;
		Node end = null;
		PriorityQueue<Node> queue = new PriorityQueue<>(1, (Node n1, Node n2) -> n1.getDistanceToStart() < n2.getDistanceToStart() ? -1 : 1);

		//set distance to start of every node to +infinity, and previous in path to null, basically resetting
        //also looking if A and B are included in the map, finally add everything into a priority queue
		for(int i = 0; i < cities.size(); i++){
			Node currentNode = cities.get(i);
			currentNode.setDistanceToStart(Double.POSITIVE_INFINITY);
			currentNode.setPreviousInPath(null);
			if (currentNode.getName().equals(A)){
				start = currentNode;
				start.setDistanceToStart(0);
				foundStart = true;
			}
			if(cities.get(i).getName().equals(B)){
				end = currentNode;
				foundEnd = true;
			}
			queue.offer(currentNode);
		}
		if (foundStart)
		    start.updateNeighbors(type);
		queue.remove(start);
		//end of initialization

		//start and/or end not found
		if(!foundStart && !foundEnd)
			return new Node("null", SOURCE_DESTINATION_NOT_FOUND);
		if(!foundStart){
			return new Node("null", SOURCE_NOT_FOUND);
		}
		if(!foundEnd){
			return new Node("null", DESTINATION_NOT_FOUND);
		}//actual algorithm
		else{
			Node currentNode;
			updateQueue(start, queue);

			while(queue.size() > 0) {
                //check if there is an edge connecting the next node in the queue to the path, if not remove the item
				if(queue.peek().getPreviousInPath() == null)
					queue.poll();
				else {
					currentNode = queue.poll();
					if (currentNode == end)
						foundPath = true;
					//update distanceToStart of every node connected to ucrrent node and set previousInPath to current Node
					currentNode.updateNeighbors(type);
					updateQueue(currentNode, queue);
				}
			}
			if(foundPath){
			    //if we are interested in the distance or time required from point a to point b, create a new node
                // with "ToD" as the name and the distance/time to start as the delay
				if(type.equals("Distance"))
					return new Node("ToD", (int)Math.ceil(end.getDistanceToStart()));
				else if(type.equals("Time"))
					return new Node("ToD", (int)Math.ceil(end.getDistanceToStart() - start.getDelay()));
				//else we return the last node of the path, from which makeMap() will reconstruct the path
				return end;
			}else{
			    //if no path is found return a new node with the name "null" and NO_PATH as delay
				return new Node("null", NO_PATH);
			}
		}
	}

    /**
     * removes and reinserts every node connected to ...
     * @param n
     *      ... in order to refresh priority
     * @param q
     *      queue that is being used
     */
	private void updateQueue(Node n, PriorityQueue q){
		for (int i = 0; i < n.getEdges().size(); i++){
			if(q.remove(n.getEdge(i).getB()))
				q.offer(n.getEdge(i).getB());
		}
	}

	/**
	 * creates a String map of an Arraylist. If endpoint is specified/ not null it will create a map with the
	 * shortest path to the end point
	 * @param endpoint
	 * 				if algorithm found a shortest path this is the end point of that path else null
	 * @return
	 * 		Arraylist of strings of the map
	 */
	public ArrayList<String> makeMap(Node endpoint){
		//no endpoint found
		if(endpoint == null){
			ArrayList<String> map = new ArrayList<>();
			String currentLine = "Digraph {";
			Node currentNode;
			Edge currentEdge;
			map.add(currentLine);
			for(int i = 0; i < cities.size(); i ++){
				currentNode = cities.get(i);
				for(int j = 0; j < currentNode.getEdges().size(); j++) {
					currentEdge = currentNode.getEdge(j);
					map.add(map.size() - i, currentNode.getName() + " -> " + currentEdge.getB().getName() + " [label=\" " + currentEdge.getDistance() + "," + currentEdge.getMaxSpeed() + "\"];");
				}
				map.add(currentNode.getName() + " [label=\"" + currentNode.getName() + "," + currentNode.getDelay()+ "\"];");
			}
			map.add("}");
			return map;
		}else {
			Node currentNode = endpoint;
			ArrayList<Node> path = new ArrayList<>();
			while(currentNode != null){
				path.add(currentNode);
				currentNode = currentNode.getPreviousInPath();
			}

			ArrayList<String> map = new ArrayList<>();
			Edge currentEdge;
			map.add("Digraph {");
			boolean partOfPath;
			for(int i = 0; i < cities.size(); i ++) {
				currentNode = cities.get(i);
				if (path.contains(currentNode))
					partOfPath = true;
				else
					partOfPath = false;
				for (int j = 0; j < currentNode.getEdges().size(); j++) {
					currentEdge = currentNode.getEdge(j);
					if (partOfPath && path.contains(currentEdge.getB()) && currentEdge.getB().getPreviousInPath() == currentNode)
						map.add(map.size() - i, currentNode.getName() + " -> " + currentEdge.getB().getName() + " [label=\"" + currentEdge.getDistance() + "," + currentEdge.getMaxSpeed() + "\"][style=bold];");
					else
						map.add(map.size() - i, currentNode.getName() + " -> " + currentEdge.getB().getName() + " [label=\"" + currentEdge.getDistance() + "," + currentEdge.getMaxSpeed() + "\"];");
				}
				if (partOfPath)
					map.add(currentNode.getName() + " [label=\"" + currentNode.getName() + "," + currentNode.getDelay() + "\"][style=bold];");
				else
					map.add(currentNode.getName() + " [label=\"" + currentNode.getName() + "," + currentNode.getDelay() + "\"];");
			}
			map.add("}");
			return map;
		}
	}
}
