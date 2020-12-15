package api;

public class EdgeLocation implements edge_location {
    private edge_data e;
    //constructor
    public EdgeLocation(int s,int d,double w){
        e=new EdgeData(s,d,w);
    }
    /**
     * Returns the edge on which the location is.
     * @return
     */
    @Override
    public edge_data getEdge() {
        return e;
    }
    /**
     * Returns the relative ration [0,1] of the location between src and dest.
     * @return
     */
    @Override
    public double getRatio() {
        return 0;
    }

}

