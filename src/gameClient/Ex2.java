package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Ex2 implements Runnable {
    private static XFrame _win;
    private static Arena _ar;
    private static dw_graph_algorithms ga;
    private static directed_weighted_graph g;
    public static void main(String[] a) {
        Thread client = new Thread(new Ex2());
        client.start();
    }

    @Override
    public void run() {
        int scenario_num = 7;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
        String g = game.getGraph();
        String pks = game.getPokemons();
        ga= new DWGraph_Algo();
        game.getGraph();
        System.out.println(game.getGraph());
        init(game);

        game.startGame();
        _win.setTitle("Ex2 - OOP:Barak & Liroy " + game.toString());
        int ind=0;
        long dt=100;

        while(game.isRunning()) {
            moveAgents(game);
            try {
                if(ind%1==0) {_win.repaint();}
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            game.move();

        }
        System.out.println(game.toString());
        System.exit(0);
    }


    private static void moveAgents(game_service game) {
        directed_weighted_graph g = _ar.getGraph();
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, g);
        _ar.setAgents(log);
        //ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
        String fs = game.getPokemons();
        List<CL_Pokemon> pokemons = Arena.json2Pokemons(fs);
        _ar.setPokemons(pokemons);

        for (CL_Agent ag : log) {
            int id = ag.getID();
            int dest = ag.getNextNode();
            double v = ag.getValue();
            if (dest == -1) {
                dest = nextNode(ag, pokemons);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
            }
        }
    }

    private static int nextNode(CL_Agent agent, List<CL_Pokemon> pokemons) {
        for (CL_Pokemon pika : pokemons) Arena.updateEdge(pika, g);
        agent.update(agent.toJSON());
        int nextNode= 0;
        int destAgent = agent.getNextNode();
        CL_Pokemon c= pokemons.get(0);
        double dist1 = ga.shortestPathDist(destAgent,c.get_edge().getSrc());
        for(int i=1; i < pokemons.size(); i++){
            CL_Pokemon pika = pokemons.get(i);
            int pikapik = pika.get_edge().getDest();
            if(pika.getType()<0 ) pikapik = pika.get_edge().getSrc();
            double dist2 = ga.shortestPathDist(destAgent,pikapik);

            if (dist2 < dist1) {
                dist1 = dist2;
                nextNode = pikapik;
            }
        }
        return nextNode;
    }


    private void init(game_service game) {
        _ar = new Arena();
        _ar.setGraph(Arena.JsonToGraph(game.getGraph()));
        this.g=Arena.JsonToGraph(game.getGraph());
        ga.init(g);
        _ar.setPokemons(Arena.json2Pokemons(game.getPokemons()));

        _win = new XFrame("Ex2 - Barak & Liroy");
        _win.setSize(1100, 800);
        _win.setTitle("Pokemon Go!");
        _win.panel.update(_ar);
        _win.show();
        _win.setVisible(true);
        _win.setDefaultCloseOperation(XFrame.EXIT_ON_CLOSE);

        String info =game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            ArrayList <CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
            for (CL_Pokemon pika : pokemons) Arena.updateEdge(pika, g);
            for(int i=0; i< rs; i++) {
                CL_Pokemon pika = pokemons.get(i);
                int pikapik = pika.get_edge().getDest();
                if(pika.getType()<0 ) pikapik = pika.get_edge().getSrc();
                game.addAgent(pikapik);
            }
        } catch (JSONException e) {e.printStackTrace();}

    }

}
