package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Ex2 implements Runnable {
    private static XFrame _win;
    private static Arena _ar;

    private static dw_graph_algorithms ga;
    private static directed_weighted_graph g;
    private static List<node_data> path;
    private static double[] distance;
    private static int count=0;
    private static double[][] wayOut;
    private static final HashMap<Integer, CL_Pokemon> attack= new HashMap<>();
    private XLogin loginPage;
    private static int _level;
    private static int _id;
    
    public static void main(String[] a) {
        Thread main = new Thread(new Ex2());
        main.start();
    }

    @Override
    public void run() {
        login();
        game_service game = Game_Server_Ex2.getServer(_level); // you have [0,23] games
        game.login(_id);
        ga= new DWGraph_Algo();
        game.getGraph();
        init(game);

        game.startGame();
        _win.setTitle("Ex2 - OOP:Barak & Liroy ");
        long dt=100;
        while(game.isRunning()) {
            try {
                moveAgents(game);
                _win.repaint();
                _ar.setTime(game.timeToEnd() / 1000);

                Thread.sleep(dt);
                _ar.update_info(game.toString());
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(game.toString());
        System.exit(0);
    }

    /**
     * this method build a login page the user insert id and level of the game
     * and the method change the value of the id & level by the user
     */
    private void login(){
        this.loginPage= new  XLogin();
        while (!this.loginPage.connected()) {
            System.out.print("");
        }
            _level = loginPage.getLevel();
            _id = loginPage.getId();
            loginPage.setVisible(false);
    }
    /**
     *this method init the information of the game
     * and locate the agents on the graph
     * @param game
     */
    private void init(game_service game) {
        _ar = new Arena();
        _ar.setGraph(Arena.JsonToGraph(game.getGraph()));
        g=Arena.JsonToGraph(game.getGraph());
        ga.init(g);
        _ar.setPokemons(Arena.json2Pokemons(game.getPokemons()));
        _win = new XFrame("Ex2 - Barak & Liroy");
        _win.setSize(900, 600);
        _win.setTitle("Pokemon Go!");
        _win.panel.update(_ar);
        _win.show();
        _win.setVisible(true);
        _win.setDefaultCloseOperation(XFrame.EXIT_ON_CLOSE);
        String info =game.toString();
        _ar.setInfo(info);
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int numOfAgents = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            ArrayList <CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
            for (CL_Pokemon pika : pokemons) Arena.updateEdge(pika, g);
            CL_Pokemon pika;
            for (int k = 0; k < numOfAgents; k++) {
            for (int i =0; i< pokemons.size(); i++)
                if(pokemons.size()>2){
                for (int j = i+1; j < pokemons.size(); j++)
                        if (pokemons.get(i).get_edge() == pokemons.get(j).get_edge())
                            pika = pokemons.get(i);
                    }
            pika= pokemons.get(k);
                int pikapik = pika.get_edge().getSrc();
                if (pokemons.get(k).getType()<0)
                    pikapik = pika.get_edge().getDest();
                game.addAgent(pikapik);
                path = ga.shortestPath(pikapik, pika.get_edge().getDest());

            }
        } catch (JSONException e) {e.printStackTrace();}
    }

    /**
     * this method move all the agents to their destination
     * @param game
     */
    private void moveAgents(game_service game) {
        directed_weighted_graph g = _ar.getGraph();
        List<CL_Agent> log = Arena.getAgents(game.move(), g);
        _ar.setAgents(log);
        game.getAgents();

        String fs = game.getPokemons();
        List<CL_Pokemon> pokemons = Arena.json2Pokemons(fs);
        for (CL_Pokemon pika : pokemons) Arena.updateEdge(pika, g);
        _ar.setPokemons(pokemons);
        while (count < 1) {
            count++;
            for (CL_Agent agent : log) attack.put(agent.getID(), null);
        }
        CL_Pokemon pika;
        for (CL_Agent ag : log) {
            int id = ag.getID();
            int dest = ag.getNextNode();
            double v = ag.getValue();

            if (ag.getIte() == null || dest==-1) {
                path = path(ag, attack(ag, pokemons));
                Iterator<node_data> i = path.listIterator();
                i.next();
                ag.setIte(i);
            }
            if (!ag.isMoving() && ag.get_ite().hasNext()) {
                dest = ag.getIte().getKey();
                if (dest != -1) {
                    ag.setNextNode(dest);
                    game.chooseNextEdge(ag.getID(), dest);
                    System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
                }
            }
            if (!ag.get_ite().hasNext() || dest==-1)
                attack.replace(ag.getID(),null);
        }
    }

    /**
     * this method return the path between the agent to the pokemon
     * @param agent
     * @param pika
     * @return
     */
    private List<node_data> path(CL_Agent agent, CL_Pokemon pika) {
        int src = agent.getSrcNode();
        int dest = pika.get_edge().getDest();
        path = ga.shortestPath(src, pika.get_edge().getSrc());
        if (pika.get_edge().getSrc() == agent.getSrcNode())
            path = ga.shortestPath(src, dest);
        return path;
    }

    /**
     * this method return the closest pokemon to the agent in the graph.
     * @param agent
     * @param pokemons
     * @return
     */
    private CL_Pokemon attack(CL_Agent agent,List<CL_Pokemon> pokemons){
        distance = nextPika(agent,pokemons);
        CL_Pokemon pika = null;
        double curr = 0;
        if (ImFree(pokemons.get(0))) {
             pika = pokemons.get(0);
             curr = distance[0];
        }
        for (int i = 1; i < distance.length; i++) {
            if (ImFree(pokemons.get(i))) {
                if(curr > distance[i]) {
                    curr = distance[i];
                    pika = pokemons.get(i);
                    attack.replace(agent.getID(), pika);
                }
            }
        }
        return pika;
    }

    private boolean ImFree(CL_Pokemon c) {
        return !attack.containsValue(c);
    }

    /**
     * this method check what the shortest path from the agent to each pokemon on the graph
     * and return array of doubles.
     * @param agent
     * @param pokemons
     * @return
     */
    private double[] nextPika(CL_Agent agent, List<CL_Pokemon> pokemons){
        if (!ga.isConnected()) {
            wayOut();
            distance = new double[pokemons.size()];
            int src = agent.getSrcNode();
            for (int i = 0; i < pokemons.size(); i++) {
                int dest = pokemons.get(i).get_edge().getSrc();
                if (wayOut[src][dest] != -1 && wayOut[dest][src] != -1)
                    distance[i] = ga.shortestPathDist(src, dest);
            }
        }
        distance =new double[pokemons.size()];
        int src = agent.getSrcNode();
        for (int i = 0; i < pokemons.size(); i++) {
            int dest = pokemons.get(i).get_edge().getSrc();
            distance[i]=ga.shortestPathDist(src,dest);
            if (pokemons.get(i).get_edge().getSrc() == agent.getSrcNode() ||pokemons.get(i).getType() > 0) {
                dest = pokemons.get(i).get_edge().getDest();
                distance[i] = ga.shortestPathDist(src, dest);
            }

        }
        return distance;
    }

    /**
     * while the graph isnt connected this method check where are the path is invalid
     * and do the reverse path to -1 So that there will be no situation that
     * the agent enters to a dead end
     */
    private void noWayOut(){
        int nodeSize=ga.getGraph().nodeSize();
        for(int i=0;i<nodeSize;i++) {
            for(int j=0;j<nodeSize;j++) {
                if((wayOut[i][j]==-1) || (wayOut[j][i]==-1)){
                    wayOut[i][j] = -1;
                    wayOut[j][i] = -1;
                }
            }
        }
    }

    /**
     * this method put all the distances between all the nodes of the graph
     * if the graph isnt connected the method operates the method noWayOut
     */
    private void wayOut(){
        int NS=ga.getGraph().nodeSize();
        wayOut=new double[NS][NS];
        for(int i = 0; i < NS; i++) {
            for (int j = 0; j < NS; j++) {
                wayOut[i][j] = ga.shortestPathDist(i, j);
            }
        }
        if(!ga.isConnected()) noWayOut();
    }

}


