package ex1.src;

import java.io.*;
import java.util.*;
import java.io.Serializable;


public class WGraph_Algo implements weighted_graph_algorithms, Serializable {
    private weighted_graph g;
    private HashMap<Integer,Integer> parent;


    //Default constructor
    public WGraph_Algo(){
        this.g = new WGraph_DS();
        this.parent = new HashMap<>();
    }

    @Override
    public int hashCode() {
        return Objects.hash(g, parent);
    }

    public void dijkstra(int source){
        node_info node = this.g.getNode(source);
        PriorityQueue<node_info>queue = new PriorityQueue<>();
        for (node_info n:this.g.getV()){//setting all the tags in the graph to infinity
            n.setTag(Double.POSITIVE_INFINITY);
            n.setInfo("");
        }
        node.setTag(0);//setting the source node to tag 0
        queue.add(node);
        this.parent.put(node.getKey(),null);//adding the source node to a HashMap to contain the parent of each node

        while(queue.size() != 0){
            node_info current = queue.poll();
            //pulling the first node from the queue with the minimal tag
            for(node_info current_neighbor:this.g.getV(current.getKey())){
              if(current_neighbor.getInfo().equals("")){//info unvisited
                  double weight = current.getTag() + this.g.getEdge(current.getKey(), current_neighbor.getKey());
                  if(weight < current_neighbor.getTag()){
                      current_neighbor.setTag(weight);//set the minimal tag to the neighbor
                      queue.add(current_neighbor);
                      this.parent.put(current_neighbor.getKey(),current.getKey());//adding the neighbor and parent to parent Hash
                  }
              }
            }
            current.setInfo("visited");
        }
    }




    //initialize the graph
    @Override
    public void init(weighted_graph g) {
        this.g = g;
    }

    // Graph getter
    @Override
    public weighted_graph getGraph() {
        return this.g;
    }

    //Building a new clone graph by using the copy constructor
    @Override
    public weighted_graph copy() {
      weighted_graph graph = new WGraph_DS(this.g);
      graph.nodeSize();
      return graph;
    }

    //returns true iff there is a valid path from every node to each other
    @Override
    public boolean isConnected() {
        if(this.g.nodeSize() == 0 || this.g.nodeSize() == 1)
            return true;

        List<node_info>nodes = new ArrayList<>(this.g.getV());
        dijkstra(nodes.get(0).getKey());

        //checks all nodes. if not visited -> return false.
        for (node_info n:nodes){
            if(!n.getInfo().equals("visited"))
                return false;
        }
        return true;
    }

    //returns the sum of the shortest path weight
    @Override
    public double shortestPathDist(int src, int dest) {
        //check if the node exists in our graph
        if(this.g.getNode(src) == null && this.g.getNode(dest) == null)
            return -1;
        dijkstra(src);
        node_info dest_node = this.g.getNode(dest);
        if(dest_node.getTag() != Double.POSITIVE_INFINITY)
            return dest_node.getTag();
        return -1;
    }
    //returns a list of the shortest path it self
    @Override
    public List<node_info> shortestPath(int src, int dest) {

        //if there is no path -> return null
        if(shortestPathDist(src,dest) == -1)
            return null;
        List<node_info> path = new ArrayList<>();
        List<node_info> rev_path = new ArrayList<>();//reverse List
        int tmp = dest;
        while(true){
            path.add(this.g.getNode(tmp));
            tmp = this.parent.get(tmp);
            if (tmp == src) {
                path.add(this.g.getNode(tmp));
                break;
            }
        }
        for(int i = path.size() - 1; i >= 0;i--){//reverse the path that we got.
            rev_path.add(path.get(i));
        }
        return rev_path;
    }

    @Override
    public boolean save(String file) {
        return WriteObjectToFile(file);
    }

    @Override
    public boolean load(String file) {

        weighted_graph g_load = (weighted_graph)ReadObjectFromFile(file);
        if (g_load != null){
            this.g = g_load;
            return true;
        }
        else return false;
    }

    public boolean WriteObjectToFile(String filepath) {

        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this.g);
            objectOut.close();
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    public Object ReadObjectFromFile(String filepath) {

        try {

            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();
            objectIn.close();
            return obj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
