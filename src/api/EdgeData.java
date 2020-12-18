package api;

public class EdgeData implements edge_data {
    private int src;
    private int dest;
    private int tag;
    private String info;
    private double weight;

    //constructor
    EdgeData(int s,int d,double w){
        this.src = s;
        this.dest = d;
        this.weight = w;
    }

    /**
     * The id of the source node of this edge.
     * @return
     */
    @Override
    public int getSrc() {
        return this.src;
    }
    /**
     * The id of the destination node of this edge
     * @return
     */
    @Override
    public int getDest() {
        return this.dest;
    }
    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return this.weight;
    }
    /**
     * Returns the remark (meta data) associated with this edge.
     * @return
     */
    @Override
    public String getInfo() {
        return this.info;
    }
    /**
     * Allows changing the remark (meta data) associated with this edge.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info=s;
    }
    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return
     */
    @Override
    public int getTag() {
        return this.tag;
    }
    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag=t;
    }
}

