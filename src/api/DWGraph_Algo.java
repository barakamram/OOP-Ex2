package api;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph ga;
    private HashMap<Integer,Integer> prevNode=new HashMap<>();
    private HashMap<Integer, Double> pathSize=new HashMap<>();
    //constructor
    public DWGraph_Algo(){
        this.ga=new DWGraph_DS();
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.ga=g;
    }
    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return this.ga;
    }
    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph g = new DWGraph_DS();
        for (node_data n: ga.getV())
            g.addNode(n);
        for (node_data node: g.getV()) {
            for (edge_data edge: ga.getE(node.getKey())) {
                g.connect(node.getKey(),edge.getDest(),edge.getWeight());
            }
        }
        return g;
    }
    /**
     * change the direction of each edge in this weighted graph.
     * this helps to the function isConnected
     * @return
     */
    public directed_weighted_graph isConnectedHelped(){
        directed_weighted_graph g = new DWGraph_DS();
        for (node_data n: ga.getV())
            g.addNode(n);
        for (node_data node: ga.getV()) {
            for (edge_data edge: ga.getE(node.getKey())){
                g.connect(edge.getDest(),node.getKey(),edge.getWeight());
            }
        }
        return g;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     * @return
     */
    @Override
    public boolean isConnected() {
        if (ga.getV().isEmpty()) return true;
        Iterator<node_data> i=ga.getV().iterator();
        node_data node=i.next();
        dijkstra(ga, node.getKey());
        while (i.hasNext()) {
            node_data n = i.next();
            if(ga.getE(n.getKey()).isEmpty() || pathSize.get(n.getKey()) == -1.0) return false;
        }
        directed_weighted_graph g=isConnectedHelped();
        Iterator<node_data> j=g.getV().iterator();
        node= j.next();
        dijkstra(g, node.getKey());
        while (j.hasNext()) {
            node_data n = j.next();
            if(g.getE(n.getKey()).isEmpty() || pathSize.get(n.getKey()) == -1.0) return false;
        }
        return true;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (ga.getNode(src) == null || ga.getNode(dest) == null) return -1;
        if (src==dest) return 0;
        else {
            dijkstra(ga, src);
            if (pathSize.get(dest) == -1.0) return -1;
            return pathSize.get(dest);
        }
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        LinkedList<node_data> queue = new LinkedList<>();
        double x= ga.nodeSize();
        node_data node = ga.getNode(dest);
        if (ga.getNode(src) == null || node == null) return null;
        queue.push(node);
        if (src==dest) return queue;
        dijkstra(ga, src);
        for (int i = 0; i < x; i++) {
            if (prevNode==null) return null;
            if (prevNode.get(node.getKey())==null) return null;
            node = ga.getNode(prevNode.get(node.getKey()));
            queue.push(node);
            if (node.getKey() == src) break;
        }
        if (node.getKey()!=src) return null;
        if (queue.size()==1) return null;
        return queue;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(DWGraph_DS.class, new DWGraphJsonDeserializer());
        Gson gson = gb.create();
        String json= gson.toJson(getGraph());
        try {
            PrintWriter pw =new PrintWriter(new File(file));
            pw.write(json);
            pw.close();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        directed_weighted_graph g=getGraph();
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(DWGraph_DS.class, new DWGraphJsonDeserializer());
            Gson gson = builder.create();
            FileReader reader = new FileReader(file);
            this.ga = gson.fromJson(reader, DWGraph_DS.class);
            return true;
        }
        catch (Exception e) {
            this.ga=g;
            return false;
        }
    }

    public void dijkstra(directed_weighted_graph g, int src) {
        LinkedList<Integer> queue = new LinkedList();
        node_data[] nodes = g.getV().toArray(new node_data[g.nodeSize()]);
        prevNode=new HashMap<>();
        pathSize=new HashMap<>();
        for (node_data n : nodes){
            n.setInfo("white");
            prevNode.put(n.getKey(),null);
            pathSize.put(n.getKey(),-1.0);
        }
        queue.add(src);
        pathSize.replace(src,0.0);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            g.getNode(u).setInfo("black");
            for (edge_data e : g.getE(u)) {
                node_data n=g.getNode(e.getDest());
                if (!n.getInfo().equals("black")) {
                    if(!queue.contains(n.getKey())) queue.add(n.getKey());
                    if(pathSize.get(n.getKey()) > pathSize.get(u) + e.getWeight() || pathSize.get(n.getKey()) == -1.0) {
                       pathSize.replace(n.getKey(),pathSize.get(u) + e.getWeight());
                       prevNode.replace(n.getKey(),u);
                    }
                }
                if(pathSize.get(n.getKey()) > pathSize.get(u) + e.getWeight()) {
                    pathSize.replace(n.getKey(),pathSize.get(u) + e.getWeight());
                    prevNode.replace(n.getKey(),u);
                    if (!queue.contains(n.getKey())) queue.add(n.getKey());
                }
            }
        }
    }

    private class DWGraphJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {

        public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            directed_weighted_graph graph=new DWGraph_DS();
            JsonArray nodes=jsonObject.get("Nodes").getAsJsonArray();
            for (int i=0;i<nodes.size();i++) {
                JsonElement jsonNode = nodes.get(i);
                int key = jsonNode.getAsJsonObject().get("id").getAsInt();
                node_data n = new NodeData(key);
//                double weight = jsonNode.getAsJsonObject().get("weight").getAsDouble();
//                n.setWeight(weight);
//                String info = jsonNode.getAsJsonObject().get("info").getAsString();
//                n.setInfo(info);
//                int tag = jsonNode.getAsJsonObject().get("tag").getAsInt();
//                n.setTag(tag);

                String pos = jsonNode.getAsJsonObject().get("pos").getAsString();
                String[] ps = pos.split(",");
                double x = Double.parseDouble(ps[0]);
                double y = Double.parseDouble(ps[1]);
                double z = Double.parseDouble(ps[2]);
                geo_location gl = new GeoLocation(x, y, z);
                n.setLocation(gl);
                graph.addNode(n);
            }
            JsonArray edges=jsonObject.get("Edges").getAsJsonArray();
            for (int i=0;i<edges.size();i++) {
                JsonElement jsonEdge = edges.get(i);
                int src=jsonEdge.getAsJsonObject().get("src").getAsInt();
                int dest=jsonEdge.getAsJsonObject().get("dest").getAsInt();
                double w=jsonEdge.getAsJsonObject().get("w").getAsDouble();
                graph.connect(src,dest,w);
            }
            return graph;
        }
    }
}

