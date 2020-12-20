import api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class DWGraph_AlgoTest {
    private directed_weighted_graph graph = new DWGraph_DS();
    private dw_graph_algorithms ga = new DWGraph_Algo();

    @BeforeEach
    public void init() {
        for (int i = 0; i < 100; i++) {
            node_data n = new NodeData(i);
            graph.addNode(n);
        }
        while (graph.edgeSize() < 1000) {
            Random r = new Random();
            int l = 0;
            int h = 99;
            int a = r.nextInt(h - l);
            int b = r.nextInt(h - l);
            graph.connect(a, b, Math.random() * 10);
        }
    }

    @Test
    public void initTest() {
        ga.init(graph);
        assertEquals(graph, ga.getGraph());
    }

    @Test
    public void getGraphTest() {
        ga.init(graph);
        directed_weighted_graph g = ga.getGraph();
        assertEquals(g, graph);
    }

    @Test
    public void isConnectedTest() {

        //graph with two nodes and two edges
        directed_weighted_graph g = new DWGraph_DS();
        node_data n = new NodeData(1);
        g.addNode(n);
        n = new NodeData(2);
        g.addNode(n);
        g.connect(1, 2, 2.25);
        g.connect(2, 1, 2);
        ga.init(g);
        assertTrue(ga.isConnected());

        //graph with two nodes and one edge
        g.removeEdge(2, 1);
        assertFalse(ga.isConnected());

        //empty graph
        g = new DWGraph_DS();
        ga.init(g);
        assertTrue(ga.isConnected());

        //graph with only one node
        g = new DWGraph_DS();
        n = new NodeData(3);
        g.addNode(n);
        ga.init(g);
        assertTrue(ga.isConnected());

        //graph with three nodes and proper path
        g = new DWGraph_DS();
        for (int i = 4; i <= 6; i++) {
            n = new NodeData(i);
            g.addNode(n);
        }
        g.connect(4, 5, 4);
        g.connect(5, 6, 6);
        g.connect(6, 4, 3);
        ga.init(g);
        assertTrue(ga.isConnected());

        //graph with three nodes and invalid path
        g = new DWGraph_DS();
        for (int i = 7; i <= 9; i++) {
            n = new NodeData(i);
            g.addNode(n);
        }
        g.connect(7, 8, 3);
        g.connect(9, 7, 4);
        g.connect(9, 8, 6);
        ga.init(g);
        assertFalse(ga.isConnected());
    }

    @Test
    public void shortestPathTest() {
        directed_weighted_graph g = new DWGraph_DS();
        for (int i = 0; i <= 5; i++) {
            node_data n = new NodeData(i);
            g.addNode(n);
        }
        g.connect(0, 1, 1);
        g.connect(0, 4, 4);
        g.connect(0, 5, 2.99);
        g.connect(1, 4, 1.5);
        g.connect(5, 3, 3);
        g.connect(0, 3, 6);
        ga.init(g);
        List<node_data> sp = ga.shortestPath(0,4);
        int[] arr = {0,1,4};
        int i=0;
        for(node_data n: sp) {
            assertEquals(n.getKey(), arr[i]); // check if the path is equal to {0,1,4}
            i++;
        }
        sp=ga.shortestPath(0,3);
        int[] arr2 = {0,5,3};
        i = 0;
        for(node_data n: sp) {
            assertEquals(n.getKey(), arr2[i]); // check if the path is equal to {0,5,3}
            i++;
        }
        assertNull(ga.shortestPath(3,5)); // check if the path is null
        assertNull(ga.shortestPath(2,1)); // check if the path is null
    }

    @Test
    public void shortestPathDistTest() {
        directed_weighted_graph g = new DWGraph_DS();
        for (int i = 0; i <= 5; i++) {
            node_data n = new NodeData(i);
            g.addNode(n);
        }
        g.connect(0, 1, 1);
        g.connect(0, 4, 4);
        g.connect(0, 5, 2.99);
        g.connect(1, 4, 1.5);
        g.connect(5, 3, 3);
        g.connect(0, 3, 6);
        g.connect(2, 3, 0.5);
        ga.init(g);
        assertAll(
                ()-> assertEquals(2.5, ga.shortestPathDist(0, 4)), //shorter path then 0->4 directly
                ()-> assertEquals(5.99, ga.shortestPathDist(0, 3)), //shorter path then 0->3 directly
                ()-> assertEquals(-1, ga.shortestPathDist(2, 1)), //no path between nodes
                ()-> assertEquals(-1, ga.shortestPathDist(3, 2)) //no path between nodes

        );

    }

    @Test
    public void saveTest() {
        ga.init(graph);
        ga.save("graph.json");
    }

    @Test
    public void loadTest() {
        assertTrue(ga.load("data/A0"));
    }
}
