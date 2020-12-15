package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Ex2  {
    public static void main(String[] args) {
        int level_num = 0;
        game_service game = Game_Server_Ex2.getServer(level_num);
        System.out.println("\n"+game.getGraph());
        System.out.println(game.getPokemons());

        game.addAgent(0);
        game.startGame();
       // System.out.println(game.getAgents());
    }
}