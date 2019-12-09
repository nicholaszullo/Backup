import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

/**
 * CS 1501 Project 4 due 11/12/19
 * 
 * @author Nick Zullo
 */
public class NetworkAnalysis {
    static Node[] list;
    public static void main(String[] args) throws Exception
    {
       
        BufferedReader input = new BufferedReader(new FileReader(args[0]));
        Scanner scan = new Scanner(System.in);
        int total = Integer.parseInt(input.readLine());                             		//Read the number of verticies from first line of file
        list = new Node[total];                                                             //Stores each vertex at an index, and stores all the vertices reachable from this vertex as linked nodes starting at this vertex ID i.e. an adjacency list
                                               
        for (int i = 0; i < total; i++)                                             		//Initialize all vertices. Connect them together with addEdge as file is read
        {
            list[i] = new Node(new Vertex(total, i));
        }

        while (input.ready())                                                       		//Create graph from file
        {
            String line = input.readLine();
            String[] split = line.split(" ");
            int start = Integer.parseInt(split[0]);                             		    //The first connection of the edge
            int end = Integer.parseInt(split[1]);                             		 	    //The end connection of the edge
            Edge curr = new Edge(start, end, split[2], Integer.parseInt(split[3]), Integer.parseInt(split[4]));     //split[2] is material, split[3] is bandwidth, split[4] is length
            
            if (list[start].getNext() == null)                                      		//Check if the starting vertex has any edges yet
            {
                list[start].setNext(new Node(list[end]));                           		//New path in list needs a new node that holds the same vertex object as other nodes
                list[start].getCurr().addEdge(end, curr);
            }
            else 
            {
                for (Node cur = list[start]; cur != null; cur = cur.getNext())
                {
                    if (cur.getNext() == null){
                        cur.setNext(new Node(list[end]));                                   //Can't link to list[end] because thats the start of another list. Need to make a new path
                        list[start].getCurr().addEdge(end, curr);                           //Add an edge to the starting vertex to the ending vertex
                        break;                                                              //Loop will run one extra time after adding this, not needed because position has been found so break
                    }
                }
            }
            if (list[end].getNext() == null)                                                //The other end of the edge needs to have an edge to the start so same process but reverse start and end
            {
                list[end].setNext(new Node(list[start]));
                list[end].getCurr().addEdge(start, curr);
            }
            else 
            {
                for (Node cur = list[end]; cur != null; cur = cur.getNext())
                {
                    if (cur.getNext() == null){
                        cur.setNext(new Node(list[start]));
                        list[end].getCurr().addEdge(start, curr);                           //Add an edge to the ending vertex to the starting vertex
                        break;                                                              
                    }
                }
            }
        }
        input.close();

        int selection = 0;
        while (selection != 5)                                                              //Loop the menu of choices until the user wants to end
        {
            System.out.println("1) Find the lowest latency path between 2 points\t2) Determine if the graph is coppy only connected\n3) Find the min average\t4) Determine if graph would fail if 2 verticies were removed\n5) Exit ");
            selection = scan.nextInt();
            
            switch (selection)
            {
                case 1:
                {
                    System.out.println("Enter the starting vertex");
                    int start = scan.nextInt();
                    System.out.println("Enter the ending vertex");
                    int end = scan.nextInt();
                    printLowestLatency(start, end);                                         //Find the lowest latency between 2 given nodes
                    break;
                }
                case 2:
                {
                    System.out.println(testCopper(list[0])+ "!");                           //Start at any node in the graph and check if it is connected
                    break;
                }
                case 3:
                {
                    System.out.println("The minimum spanning tree has edges from ");
                    minTree();                                                              //Prints the edges in the MST and the speed of them
                    break;
                }
                case 4:
                {
                    boolean fail = false;
                    for (int i = 0; i < list.length; i++)                                   //Remove a vertex in the graph
                    {
                        if (fail)
                            break;
                        for (int k = 0; k < list.length; k++)                               //For each vertex in the graph, remove another 
                        {
                            if (connected(i, k))                                            //Returns true if the graph is connected ignoring 2 vertices
                                continue;   
                            else
                            {
                                fail = true;
                                System.out.println("FAIL!");                                //If gets here then it fails, break out of everything
                            }
                        }
                    }
                    if (fail)
                        break;
                    System.out.println("PASS!");                                            //If gets here, the graph can survive two verticies failing
                    break;  
                }
                case 5:
                {   
                    System.out.println("Goodbye");                                          //stop program when user requests
                    scan.close();
                    System.exit(0);
                }
                default:
                {
                    System.out.println("Invalid choice!");                                  //Get a new value from the user if a supported one is not chosen
                    break;
                }
            }
        }
    }
    
    /**
     * An implementation of Dijkstras algorithm for finding min path between vertices
     * Prints the min path after finishing and stores path as consecutive vertices in <code>rightOrder</code> array
     *
     * @param start the starting vertex
     * @param end   the ending vertex
     */
    public static void printLowestLatency(int start, int end)
    {  
        if (start >= list.length || end >= list.length)
            throw new IllegalArgumentException("Specified vertex is not in the graph!");

        IndexMinPQ<Double> pq = new IndexMinPQ<Double>(list.length);			            //Need to store info about verticies inside edge. Can one edge still work both directions or make a new edge for backwards path
        double[] latencyTo = new double[list.length];                                       //Stores the min weight to a vertex
        Edge[] edgeTo = new Edge[list.length];                                              //Stores the path to a vertex that follows the min weight
        for (int i = 0; i < latencyTo.length; i++)
            latencyTo[i] = Double.MAX_VALUE;                                                //Fill the latencyTo each node to be the max value
        
        latencyTo[start] = 0;                
        
        pq.insert(start, latencyTo[start]);                                                 //PQ is indexed by vertex ID, and key is the distance to that vertex
        while(!pq.isEmpty())
        {
            int startID = pq.delMin();                                                      //Index of the current vertex that the neighbors will be enumorated from
            for (Node endN = list[startID].getNext(); endN != null; endN = endN.getNext())
            {
                int endID = endN.getCurr().getId();                                         //Index of the current neighbor to startID
                if (latencyTo[endID] > latencyTo[startID] + endN.getCurr().getEdges()[startID].getTime())   //If a new min path has been found
                {
                    edgeTo[endID] = endN.getCurr().getEdges()[startID];                     //Update path and weight
                    latencyTo[endID] = latencyTo[startID] + endN.getCurr().getEdges()[startID].getTime();
                    
                    if (pq.contains(endID))                                                 //If neighbor vertex is yet to be seen decrease its min path
                    {
                        pq.decreaseKey(endID, latencyTo[endID]);
                    }
                    else
                    {
                        pq.insert(endID, latencyTo[endID]);                                 //If this is the first time finding a path add it to pq
                    }
                }
            }
        }
        int[] rightOrder = new int[list.length];                                            //Stores the indicies of the vertices in consecutive order, starting from end and going to start
        int i = end;                                                                        //Starting point for edgeTo 
        int k = 0;                                                                          //Starting point for adding to rightOrder. Can't start at the end because unknown number in path
        int minBandwidth = Integer.MAX_VALUE;                                               //minBandwidth has yet to be found
        while (i != start)
        {
            rightOrder[k++] = i;                                                            //Add the vertex to the array of consecutive vertices
            if (minBandwidth > edgeTo[i].getBandwidth())                                    //Update minbandwidth if necessary
                minBandwidth = edgeTo[i].getBandwidth();
            
            if (i == edgeTo[i].getStart())                                                  //If the current vertex is at the start of the edge, move to the end
                i = edgeTo[i].getEnd();
            else                                                                            //If at the end of an edge move to the start
                i = edgeTo[i].getStart();
        }
        rightOrder[k] = i;                                                                  //Add the start vertex to the end of the array

        for(; k > 0; k--)                                                                   //Start at k because thats where the start is, and go to 1 printing. 
            System.out.print(rightOrder[k] + " to ");
        System.out.println(rightOrder[0] + "\nthe min bandwith is " + minBandwidth);        //print the end node here to not have an extra " to " at the end
    }

    /**
     * Performs a BFS on a graph from a starting point. If a path taken was copper, update copper[].
     * <p>
     * Check at the end if every vertex in graph was seen and used a copper path to find
     * 
     * @param start the starting vertex. Doesn't matter where you start
     * @return True if the graph is connected by copper only cables, false otherwise
     */
    private static boolean testCopper(Node start)
    {
        IndexMinPQ<Vertex> pq = new IndexMinPQ<Vertex>(list.length);
        pq.insert(start.getCurr().getId(), start.getCurr());
        boolean[] seen = new boolean[list.length];                                          //If a vertex has been seen 
        boolean[] copper = new boolean[list.length];                                        //If a copper conenction to the vertex has been found
        for (int i = 0; i < start.getCurr().getEdges().length; i++)                         //Check if there is a copper connection to the start
        {
            if (start.getCurr().getEdges()[i] == null)
                continue;
            if (start.getCurr().getEdges()[i].getType().equals("copper"))                   //No path to staring node needed so make it true for check at end
                copper[start.getCurr().getId()] = true;
        }
        
        while (!pq.isEmpty())
        {
            Node curr = list[pq.delMin()];
            seen[curr.getCurr().getId()] = true;

            for (Node end = curr.getNext(); end != null; end = end.getNext())
            {
                int endID = end.getCurr().getId();
                if (seen[endID])                                                            //If an edge has already been processed no more work is needed
                    continue;

                if (curr.getCurr().getEdges()[endID].getType().equals("copper"))
                {
                    copper[endID] = true;                                                   //A found copper path is valid for both sides of the edge  
                    copper[curr.getCurr().getId()] = true;
                }
                if (!pq.contains(endID))
                    pq.insert(endID, end.getCurr());                                        //Add a seen edge to the pq
                
            }
        }
        for (int i = 0; i < list.length; i++)
        {
            if (!(seen[i] && copper[i])){                                                   //If a copper connection and path to each vertex exists the graph is copper connected
                return false;
            }
        }
        return true;
    }

    /**
     *  Finds the minimum spanning tree of the graph based on latency of edge using Prim's algorithm
     *  <p>
     *  Very similar to earlier implementation of printLowestLatency 
     *  <p>
     *  Prints the edges in the MST after finding them all
     */
    public static void minTree()
    {
        double[] latencyTo = new double[list.length];
        for (int i = 0; i < list.length; i++)
            latencyTo[i] = Double.MAX_VALUE;
        latencyTo[list[0].getCurr().getId()] = 0;
        Edge[] edgeTo = new Edge[list.length];                                             //The min edge to a vertex
//        boolean[] seen = new boolean[list.length];
        IndexMinPQ<Double> pq = new IndexMinPQ<Double>(list.length);
        pq.insert(list[0].getCurr().getId(), (double)0);
        
        while (!pq.isEmpty())
        {
            int startID = pq.delMin();
            Node start = list[startID];
            Vertex startV = start.getCurr();
//            seen[startID] = true;
            for (Node end = start.getNext(); end != null; end = end.getNext())
            {
                int currID = end.getCurr().getId();
//                if (seen[currID])                                                       //Should this be here?
//                    continue;

                if (latencyTo[currID] > startV.getEdges()[currID].getTime())
                {
                    latencyTo[currID] = startV.getEdges()[currID].getTime();
                    edgeTo[currID] = startV.getEdges()[currID];                         //If a new min path to the vertex has been found, update the min edgeTo that vertex
                    if (pq.contains(currID))
                    {
                        pq.decreaseKey(currID, latencyTo[currID]);
                    }
                    else 
                    {
                        pq.insert(currID, latencyTo[currID]);
                    }
                }
            }
        }
        for (int i = 0; i < edgeTo.length; i++)
        {
            if (edgeTo[i] == null)                                                          //The starting vertex does not have a min path to it, so skip it                                         
                continue;
            if (edgeTo[i].getStart() == i)
                System.out.println("The min way to " + i + " is from " + edgeTo[i].getEnd() + " with latency " + latencyTo[i]);
            else
                System.out.println("The min way to " + i + " is from " + edgeTo[i].getStart() + " with latency " + latencyTo[i]);
        }
    }

    /**
     *  Tests if a given graph is connected if ignoring 2 vertices. Shows whether or not a graph can
     *  survive 2 vertices being removed
     *  <p>
     *  Performs a BFS of the graph, and if every vertex other than the 2 "deleted" vertices are seen, the graph is connected
     * 
     * 
     * @param hide1 A vertex to ignore
     * @param hide2 A second vertex to ignore
     * @return True if the graph is connected, False if it is not 
     */
    private static boolean connected(int hide1, int hide2)
    {
        Node start = list[0];
        IndexMinPQ<Vertex> pq = new IndexMinPQ<Vertex>(list.length);
        pq.insert(start.getCurr().getId(), start.getCurr());
        boolean[] seen = new boolean[list.length];
        while (!pq.isEmpty())
        {
            Node curr = list[pq.delMin()];
            seen[curr.getCurr().getId()] = true;                                            //Update seen vertices

            for (Node end = curr.getNext(); end != null; end = end.getNext())               //Look at all neighbors of starting node
            {
                int endID = end.getCurr().getId();
                if (seen[endID] || endID == hide1 || endID == hide2)
                    continue;

                if (!pq.contains(endID))
                    pq.insert(endID, end.getCurr());                                        //Add neighbors to pq
                
            }
        }
        for (int i = 0; i < list.length; i++)                                               //Check if all valid vertices have been seen
        {
            if (i == hide1 || i == hide2)                                                   //No check of seen for the vertices that are being ignored
                continue;
            if (!seen[i]){                                                                  //If a valid vertex was not seen the graph is not connected
                return false;
            }
        }
        return true;
    }
}