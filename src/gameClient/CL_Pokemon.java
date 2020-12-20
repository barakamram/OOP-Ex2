package gameClient;
import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Pokemon {
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D _pos;
	private double min_dist;
	private int min_ro;
	private double _id;
	
	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		 setID(p);
		_type = t;
	//	_speed = s;
		_value = v;
		set_edge(e);
//		_edge = e;
		_pos = p;
		min_dist = -1;
		min_ro = -1;
	}
	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	public String toString() {return "F:{v="+_value+", t="+_type+"}";}
	public edge_data get_edge() {
		return this._edge;
	}

	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}
	public void setID (Point3D p) {
		this._id = p.x()+p.y()+p.z();
	}
	public double getID(){
		return this._id;
	}
	public Point3D getLocation() {
		return _pos;
	}
	public int getType() {return _type;}
//	public double getSpeed() {return _speed;}
	public double getValue() {return _value;}

	public double getMin_dist() {
		return min_dist;
	}

	public void setMin_dist(double mid_dist) {
		this.min_dist = mid_dist;
	}

	public int getMin_ro() {
		return min_ro;
	}

	public void setMin_ro(int min_ro) {
		this.min_ro = min_ro;
	}


}
