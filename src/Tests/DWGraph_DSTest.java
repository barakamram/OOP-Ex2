package Tests;
import api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DWGraph_DSTest {
    private directed_weighted_graph g=new DWGraph_DS();
    @BeforeEach
    public void init() {
        for (int i = 0; i < 100; i++) {
            node_data n = new NodeData(i);
            g.addNode(n);
        }
        while (g.edgeSize() < 1000) {
            Random r = new Random();
            int l = 0;
            int h = 99;
            int a = r.nextInt(h - l);
            int b = r.nextInt(h - l);
            g.connect(a, b, Math.random() * 10);
        }
    }

    @Test
    public void getNodeTest() {
        int key=g.getNode(10).getKey();
        assertEquals(10,key);
        assertEquals(g.getNode(-1),null);
        assertEquals(g.getNode(101),null);
    }

    @Test
    public void getEdgeTest() {
        init();
        g.connect(0,3,5.5);
        assertEquals( 5.5,g.getEdge(0,3).getWeight());
        g.removeEdge(0,3);
        assertEquals(null,g.getEdge(0,3));
    }

    @Test
    public void addNodeTest() {
        g=new DWGraph_DS();
        assertEquals(true,g.getV().isEmpty());
        for (int i = 0; i < 5; i++) {
            node_data n = new NodeData(i);
            g.addNode(n);
        }
        assertEquals(5,g.nodeSize());

    }
    @Test
    public void connectTest() {
        g.connect(32,78,4.32);
        assertEquals(4.32,g.getEdge(32,78).getWeight());
        assertEquals(null,g.getEdge(78,32));
        g.connect(32,78,11.2);
        assertEquals(11.2,g.getEdge(32,78).getWeight());
    }

    @Test
    public void getVTest() {
        int size=g.getV().size();
        assertEquals(g.nodeSize(),size);
    }

    @Test
    public void getETest() {
        g= new DWGraph_DS();
        for (int i = 0; i < 5; i++) {
            node_data n = new NodeData(i);
            g.addNode(n);
        }
        g.connect(0,1,3);
        g.connect(3,0,2.3);
        g.connect(3,4,1.25);
        g.connect(4,2,90);
        g.connect(1,2,12);
        assertEquals(g.getE(3).size(),2);
        assertEquals(g.getE(2).size(),0);
    }

    @Test
    public void removeNodeTest() {
        node_data n=new NodeData(27);
        g.addNode(n);
        g.connect(27,40,3.05);
        g.removeNode(27);
        assertEquals(false,g.getV().contains(g.getNode(27)));
        assertEquals(null,g.getEdge(27,40));
    }
    @Test
    public void removeEdge() {
        g.connect(1,5,9.2);
        g.removeEdge(1,5);
        assertEquals(null,g.getEdge(1,5));
    }
}