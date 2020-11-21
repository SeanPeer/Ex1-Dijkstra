package ex1.src;

import java.io.Serializable;
import java.util.*;

public class WGraph_DS implements weighted_graph, Serializable {
    private HashMap<Integer, node_info> g;
    private HashMap<Integer, HashMap<Integer, node_info>> neighbors;
    private TreeMap<String, Double> weight;
    private int edgeCounter;
    private int mc;

    //Default constructor.
    public WGraph_DS() {
        this.g = new HashMap<>();
        this.neighbors = new HashMap<>();
        this.weight = new TreeMap<>();
        this.edgeCounter = 0;
        this.mc = 0;
    }

    //Copy constructor.
    public WGraph_DS(weighted_graph graph) {
        //creat new hashmaps:
        this.g = new HashMap<>();
        this.neighbors = new HashMap<>();
        this.weight = new TreeMap<>();

        //copy values of mc and edge
        this.edgeCounter = graph.edgeSize();
        this.mc = graph.getMC();

        //creating copy of g by creating new nodes.
        for (node_info node : graph.getV()) {
            node_info new_node = new NodeInfo(node);
            this.g.put(new_node.getKey(), new_node);
            this.neighbors.put(new_node.getKey(), new HashMap<>());
        }

        //copy neighbors.
        for (node_info n : graph.getV()) {
            //current Key for coping his neighbors.
            int k1 = n.getKey();
            //copy the neighbors for speific key.
            for (node_info nei : graph.getV(k1)) {
                //hold the node neighbor.
                node_info current = this.g.get(nei.getKey());
                //this is the key of the specific neighbor.
                int k2 = current.getKey();
                //put into hashmap of neighbors.
                this.neighbors.get(k1).put(k2, current);
                double w = graph.getEdge(k1, k2);
                this.weight.put(k1 + "_" + k2, w);
                this.weight.put(k2 + "_" + k1, w);
            }
        }

        //this.neighbors.keySet();
    }

    @Override
    public boolean equals(Object o) {
        //if the object compare with it self ->return true.
        if (this == o) return true;
        //if o is an instance of Wgraph_ds ->false
        if (!(o instanceof WGraph_DS))
            return false;
        //casting o to a new WGraph_DS.
        WGraph_DS object = (WGraph_DS) o;
        boolean ans = false;

        //checks both objects g.
        if (this.g.size() == object.g.size()) {
            ans = true;
            ArrayList<node_info> object_list = new ArrayList<>(object.g.values());
            ArrayList<node_info> this_list  = new ArrayList<>(this.g.values());
            for (int i = 0; i < object_list.size(); i++) {
                ans = this_list.get(i).equals(object_list.get(i));
                if (!ans) {
                    return false;
                }
            }

            //boolean ans_g = this.g.keySet().equals(object.g.keySet());
            //boolean ans_value = this.g.values().equals(new ArrayList<>(object.g.values()));

            //checking both weight
            if(this.weight.size() == object.weight.size()){
                boolean ans_key = this.weight.keySet().equals(object.weight.keySet());
                if(!ans_key) return false;
                boolean ans_value = (new ArrayList<>(this.weight.values()).equals(new ArrayList<>(object.weight.values())));

                if(!ans_value) return false;
            }

            //cheking both neighbors
            ArrayList<Integer> key_list = new ArrayList<Integer>(this.neighbors.keySet());
            for(int i = 0;i < key_list.size();i++){
                boolean ans_nei = this.neighbors.get(key_list.get(i)).keySet().equals(object.neighbors.get(key_list.get(i)).keySet());
                if(!ans_nei)return false;
            }

            if(this.edgeCounter != object.edgeCounter) return  false;//checks edgeCounter of both objects
            if(this.mc != object.mc)return false;////checks changesCounter of both objects

        }
        // if everything is equal -> return true
        return ans;

    }


    //node getter - returns the node if there is one with this key.
    @Override
    public node_info getNode(int key) {
        return this.g.get(key);
    }

    //Returns return true if there is an edge between 2 node and false if there isn't.
    @Override
    public boolean hasEdge(int node1, int node2) {
        if (this.weight.containsKey(buildWeiKey(node1, node2)))
            return true;
        return false;
    }

    //Edge getter - returns the edge weight if there is one between the given nodes.
    @Override
    public double getEdge(int node1, int node2) {
        if (node1 != node2) {
            if (hasEdge(node1, node2)) {
                return this.weight.get(buildWeiKey(node1, node2));
            }
        }
        return -1;
    }

    //Adding a new node to the graph.
    @Override
    public void addNode(int key) {
        if (!this.g.containsKey(key)) {
            node_info node = new NodeInfo(key);
            this.g.put(key, node);
            this.neighbors.put(key, new HashMap<>());
            mc++;
        }
    }

    //connecting between to nodes
    @Override
    public void connect(int node1, int node2, double w) {
        //if there is no edge between them and they are not the same
        if (!hasEdge(node1, node2) && node1 != node2 && this.g.containsKey(node1) && this.g.containsKey(node2)) {
            this.weight.put(buildWeiKey(node1, node2), w);
            this.weight.put(buildWeiKey(node2, node1), w);
            this.neighbors.get(node1).put(node2, this.g.get(node2));
            this.neighbors.get(node2).put(node1, this.g.get(node1));
            this.edgeCounter++;
            this.mc++;
        }
    }

    //building a string to identify if there is an edge
    public String buildWeiKey(int node1, int node2) {
        return node1 + "_" + node2;
    }

    @Override
    public Collection<node_info> getV() {
        return this.g.values();
    }

    //returns a collection of the node's neighbors
    @Override
    public Collection<node_info> getV(int node_id) {
        return this.neighbors.get(node_id).values();
    }

    //Removes a node by a given key from the graph
    @Override
    public node_info removeNode(int key) {

        if (this.g.containsKey(key)) {
            if (this.neighbors.get(key) != null) {
                ArrayList<node_info> ni = new ArrayList<node_info>(this.neighbors.get(key).values());
                while (ni.size() != 0) {
                    int n = ni.get(0).getKey();
                    removeEdge(key, n);
                    ni.remove(0);
                }

            }
            this.neighbors.remove(key);
            this.mc++;
            return this.g.remove(key);
        }
        return null;
    }

    //removes an edge between two nodes if exist
    @Override
    public void removeEdge(int node1, int node2) {
        if (this.weight.containsKey(buildWeiKey(node1, node2))) {
            this.weight.remove(buildWeiKey(node1, node2));
            this.weight.remove(buildWeiKey(node2, node1));
            this.neighbors.get(node1).remove(node2);
            this.neighbors.get(node2).remove(node1);
            this.edgeCounter--;
            this.mc++;
        } else System.out.println("there is no edge between " + node1 + " and " + node2);
    }

    //returns the amount of nodes we have in the graph
    @Override
    public int nodeSize() {
        return this.g.size();
    }

    //returns the amount of edges we have in the graph
    @Override
    public int edgeSize() {
        return this.edgeCounter;
    }

    //returns the amount of changes since the graph was created
    @Override
    public int getMC() {
        return this.mc;
    }

    public class NodeInfo implements node_info, Comparable<node_info>, Serializable {
        private int key;
        private double tag;
        private String info;

        public NodeInfo() {
            this.key = 0;
            this.tag = 0;
            this.info = "";
        }

        public NodeInfo(int key) {
            this.key = key;
            this.tag = tag;
            this.info = "";
        }

        public NodeInfo(node_info node) {
            this.key = node.getKey();
            this.tag = node.getTag();
            this.info = node.getInfo();
        }

        //key getter.
        @Override
        public int getKey() {
            return this.key;
        }

        //info getter
        @Override
        public String getInfo() {
            return this.info;
        }

        //info setter
        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        //tag getter
        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        //compare for priority queue - the min weight is the first.
        public int compareTo(node_info node) {
            if (this.getTag() < node.getTag())
                return -1;
            else if (this.getTag() > node.getTag())
                return 1;
            else return 0;
        }
        //using my own equal function because im checking the equalises of an objects
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NodeInfo)) return false;
            NodeInfo object = (NodeInfo) o;
            if (this.key == object.getKey() && this.tag == object.getTag() && this.info.equals(object.getInfo()))
                return true;
            return false;
        }

    }

}
