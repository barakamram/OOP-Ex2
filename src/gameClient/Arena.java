package gameClient;

import api.*;
import com.google.gson.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 * @author boaz.benmoshe
 *
 */
public class Arena {
	public static final double EPS1 = 0.001, EPS2=EPS1*EPS1, EPS=EPS2;
	private directed_weighted_graph _gg;
	private List<CL_Agent> _agents;
	private List<CL_Pokemon> _pokemons;
	private List<String> _info;
	private static Point3D MIN = new Point3D(0, 100,0);
	private static Point3D MAX = new Point3D(0, 100,0);
	private long _time;
	private List<Integer> info;
	private int numberOfAgents;

	public Arena() {;
		_info = new ArrayList<String>();
		info = new ArrayList<>();
	}
	private Arena(directed_weighted_graph g, List<CL_Agent> r, List<CL_Pokemon> p) {
		_gg = g;
		this.setAgents(r);
		this.setPokemons(p);
	}
	public void setPokemons(List<CL_Pokemon> f) {
		this._pokemons = f;
	}
	public void setAgents(List<CL_Agent> f) {
		this._agents = f;
	}
	public void setGraph(directed_weighted_graph g) {this._gg =g;}//init();}
	private void init( ) {
		MIN=null; MAX=null;
		double x0=0,x1=0,y0=0,y1=0;
		for (api.node_data node_data : _gg.getV()) {
			geo_location c = node_data.getLocation();
			if (MIN == null) {
				x0 = c.x();
				y0 = c.y();
				x1 = x0;
				y1 = y0;
				MIN = new Point3D(x0, y0);
			}
			if (c.x() < x0) {
				x0 = c.x();
			}
			if (c.y() < y0) {
				y0 = c.y();
			}
			if (c.x() > x1) {
				x1 = c.x();
			}
			if (c.y() > y1) {
				y1 = c.y();
			}
		}
		double dx = x1-x0, dy = y1-y0;
		MIN = new Point3D(x0-dx/10,y0-dy/10);
		MAX = new Point3D(x1+dx/10,y1+dy/10);

	}
	public List<CL_Agent> getAgents() {return _agents;}
	public List<CL_Pokemon> getPokemons() {return _pokemons;}
	public void setTime(long t){
		this._time=t;
	}
	public long getTime(){
		return this._time;
	}

	public directed_weighted_graph getGraph() {
		return _gg;
	}
	public List<String> get_info() {
		return _info;
	}
	public void set_info(List<String> _info) {
		this._info = _info;
	}
//
//	public List<String> getInfo() {
//	}

	public List<Integer> getInfo(){
		return this.info;
	}
	public void setInfo(String s)  {
		try {
			JSONObject game = new JSONObject(s);
			JSONObject Info = game.getJSONObject("GameServer");
			int moves = Info.getInt("moves");
			int grade = Info.getInt("grade");
			info.add(0,moves);
			info.add(1,grade);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void update_info(String s)  {
		try {
			JSONObject game = new JSONObject(s);
			JSONObject Info = game.getJSONObject("GameServer");
			int moves = Info.getInt("moves");
			int grade = Info.getInt("grade");
			info.add(0,moves);
			info.add(1,grade);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}




	////////////////////////////////////////////////////
	public static List<CL_Agent> getAgents(String aa, directed_weighted_graph gg) {
		ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
		try {
			JSONObject ttt = new JSONObject(aa);
			JSONArray ags = ttt.getJSONArray("Agents");
			for(int i=0;i<ags.length();i++) {
				CL_Agent c = new CL_Agent(gg,0);
				c.update(ags.get(i).toString());
				ans.add(c);
			}
			//= getJSONArray("Agents");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public static ArrayList<CL_Pokemon> json2Pokemons(String fs) {
		ArrayList<CL_Pokemon> ans = new  ArrayList<CL_Pokemon>();
		try {
			JSONObject ttt = new JSONObject(fs);
			JSONArray ags = ttt.getJSONArray("Pokemons");
			for(int i=0;i<ags.length();i++) {
				JSONObject pp = ags.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int t = pk.getInt("type");
				double v = pk.getDouble("value");
				//double s = 0;//pk.getDouble("speed");
				String p = pk.getString("pos");
				CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, 0, null);
				ans.add(f);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		return ans;
	}
	public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
		//	oop_edge_data ans = null;
		for (node_data v : g.getV()) {
			for (edge_data e : g.getE(v.getKey())) {
				boolean f = isOnEdge(fr.getLocation(), e, fr.getType(), g);
				if (f) {
					fr.set_edge(e);
				}
			}
		}
	}

	private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest ) {

		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(p) + p.distance(dest);
		if(dist>d1-EPS2) {ans = true;}
		return ans;
	}
	private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(p,src,dest);
	}
	private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
		int src = g.getNode(e.getSrc()).getKey();
		int dest = g.getNode(e.getDest()).getKey();
		if(type<0 && dest>src) {return false;}
		if(type>0 && src>dest) {return false;}
		return isOnEdge(p,src, dest, g);
	}

	private static Range2D GraphRange(directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		double x0=0,x1=0,y0=0,y1=0;
		boolean first = true;
		while(itr.hasNext()) {
			geo_location p = itr.next().getLocation();
			if(first) {
				x0=p.x(); x1=x0;
				y0=p.y(); y1=y0;
				first = false;
			}
			else {
				if(p.x()<x0) {x0=p.x();}
				if(p.x()>x1) {x1=p.x();}
				if(p.y()<y0) {y0=p.y();}
				if(p.y()>y1) {y1=p.y();}
			}
		}
		Range xr = new Range(x0,x1);
		Range yr = new Range(y0,y1);
		return new Range2D(xr,yr);
	}
	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		Range2Range ans = new Range2Range(world, frame);
		return ans;
	}

	static directed_weighted_graph JsonToGraph(String s) {
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapter(directed_weighted_graph.class, new graphJsonDeserializer());
		Gson gson = gb.create();
		return gson.fromJson(s, directed_weighted_graph.class);
	}

	private static class graphJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {
		@Override
		public  directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			JsonArray nodes=jsonObject.get("Nodes").getAsJsonArray();
			node_data node;
			directed_weighted_graph graph=new DWGraph_DS();
			for(JsonElement curr: nodes){
				node=new NodeData(curr.getAsJsonObject().get("id").getAsInt(),posHelp(curr.getAsJsonObject().get("pos").getAsString()));
				graph.addNode(node);
			}
			JsonArray edges=jsonObject.get("Edges").getAsJsonArray();
			for(JsonElement curr :edges){
				int src=curr.getAsJsonObject().get("src").getAsInt();
				int dest=curr.getAsJsonObject().get("dest").getAsInt();
				double w=curr.getAsJsonObject().get("w").getAsDouble();
				graph.connect(src,dest,w);
			}
			return graph;
		}
	}

	private static GeoLocation posHelp(String str){
		String[] a = str.split(",");
		GeoLocation curr =new GeoLocation(Double.parseDouble(a[0]),Double.parseDouble(a[1]),Double.parseDouble(a[2]));
		return curr;
	}



}
