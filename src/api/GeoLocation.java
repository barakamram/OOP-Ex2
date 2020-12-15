package api;

public class GeoLocation implements geo_location {
    private double x,y,z;
    //constructor
    public GeoLocation(double x,double y,double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(geo_location g) {
        double dx= Math.pow(x()- g.x(),2);
        double dy= Math.pow(y()- g.y(),2);
        double dz= Math.pow(z()- g.z(),2);
        return Math.sqrt(dx+dy+dz); //Quadratic equation
    }
}

