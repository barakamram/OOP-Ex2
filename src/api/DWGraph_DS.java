package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements  directed_weighted_graph {
    private int MC;
    private HashMap<Integer, node_data> myDWGraph;
    private  HashMap<Integer, HashMap<Integer,edge_data>> myEdges;
    private int numOfNodes;
    private int numOfEdges;

    //constructor
    public DWGraph_DS(){
        this.myDWGraph = new HashMap<>();
        this.myEdges = new HashMap<>();
        numOfNodes = 0;
        numOfEdges = 0;
        MC = 0;
    }

    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return myDWGraph.get(key);
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(!myDWGraph.containsKey(src) || !myDWGraph.containsKey(dest)) return null;
        if (!myEdges.get(src).containsKey(dest)) return null;
        return myEdges.get(src).get(dest);
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if(!myDWGraph.containsKey(n.getKey())) {
            myDWGraph.put(n.getKey(), n);
            myEdges.put(n.getKey(), new HashMap<>());
            numOfNodes++;
            MC++;
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (!myDWGraph.containsKey(src) || !myDWGraph.containsKey(dest)) return;
        if (src == dest) return;
        edge_data ed=getEdge(src,dest);
        edge_data e = new EdgeData(src, dest, w);
        if (!myEdges.get(src).containsKey(dest)) {
            myEdges.get(src).put(dest, e);
            numOfEdges++;
            MC++;
        }
        else if (myEdges.get(src).containsKey(dest) && w!=ed.getWeight()) {
            myEdges.get(src).replace(dest, e);
            MC++;
        }
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return myDWGraph.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (!myDWGraph.containsKey(node_id)) return null;
        return myEdges.get(node_id).values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     * @return the data of the removed node (null if none).
     * @param key
     */
    @Override
    public node_data removeNode(int key) {
        if (myDWGraph.containsKey(key)){
            for (node_data n:myDWGraph.values()) {
                if(myEdges.get(n.getKey()).containsKey(key)) {
                    myEdges.get(n.getKey()).remove(key);
                    numOfEdges--;
                    MC++;
                }
            }
            myDWGraph.remove(key);
            myEdges.remove(key);
            numOfNodes--;
            MC++;
        }
        return myDWGraph.get(key);
    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (!myDWGraph.containsKey(src) || !myDWGraph.containsKey(dest)) return null;
        if (!myEdges.get(src).containsKey(dest)) return null;
        myEdges.get(src).remove(dest);
        numOfEdges--;
        MC++;
        return getEdge(src,dest);
    }

    /** Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int nodeSize() {
        return numOfNodes;
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int edgeSize() {
        return numOfEdges;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return
     */
    @Override
    public int getMC() {
        return MC;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (this == o) return true;
        boolean flag = false;
        DWGraph_DS graph = (DWGraph_DS) o;
        boolean NON = numOfNodes == graph.numOfNodes;
        boolean NOE = numOfEdges == graph.numOfEdges;
        if ( NON && NOE ) {
            flag = true;
            for (int src : myDWGraph.keySet()) {
                for (int dest : myEdges.get(src).keySet()) {
                    boolean s = graph.getEdge(src,dest).getSrc() == src;
                    boolean d = graph.getEdge(src,dest).getDest() == dest;
                    if (!s || !d)  flag = false;
                }
            }
        }
        return flag;

    }

}

