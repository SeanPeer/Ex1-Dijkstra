Dijkstra Algorithem
src
#WGraph_DS: #boolean equals(Object o): compare between non-primitive object such as a graph.

#node_info getNode(int key): returns the node by his key.

#boolean hasEdge(int node1, int node2): return true or false is there is an edge between the given nodes.

#getEdge(int node1, int node2): returns the edge weight if there is one between the

#given nodes.

#void addNode(int key): adding a node to the graph.

#void connect(int node1, int node2, double w): connecting between two node and setting the weight of the edge.

#String buildWeiKey(int node1, int node2): building a string to identify if there is an edge

#NodeInfo:

int getKey(): returns the node key.

String getInfo(): returns the node infp.

void setInfo(String s): Allows changing the remark (meta data) associated with this.node.

double getTag(): returns the node's weight.

void setTag(double t): allow changing the node's weight.

int compareTo(node_info node): compare for priority queue - the min weight is the first.

boolean equals(Object o):compare between non-primitive object.

#WGraph_Algo

void dijkstra(int source): doing dijkstra algorithm.

void init(weighted_graph g):initialize the graph.

weighted_graph getGraph(): returns the graph.

weighted_graph copy(): function that using the copy constructor.

boolean isConnected(): returns true iff there is a valid path from every node to each other.

double shortestPathDist(int src, int dest):returns the sum of the shortest path weight

List<node_info> shortestPath(int src, int dest): returns a list of the shortest path it self.

boolean save(String file): writng the file.

boolean load(String file): reading the file by the given name.

ReadObjectFromFile(String filepath)

WriteObjectToFile(String filepath)

tests
WGraph_DSTest

WGraph_AlgoTest
