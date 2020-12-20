import api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

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
        assertAll(
                ()-> assertEquals(10,key),
                ()-> assertNull(g.getNode(-1)),
                ()-> assertNull(g.getNode(101)),
                ()-> assertNotNull(g.getNode(1))
        );
    }

    @Test
    public void getEdgeTest() {
        init();
        g.connect(0,3,5.5);
        assertAll(
                ()->assertEquals( 5.5,g.getEdge(0,3).getWeight()),
                ()-> g.removeEdge(0,3),
                ()-> assertNull(g.getEdge(0,3))
        );
    }

    @Test
    public void addNodeTest() {
        g=new DWGraph_DS();
        assertTrue(g.getV().isEmpty());
        for (int i = 0; i < 5; i++) {
            node_data n = new NodeData(i);
            g.addNode(n);
        }
        assertAll (
                ()->assertEquals(5,g.nodeSize()),
                ()-> assertNotNull(g.getNode(1)),
                ()-> assertNull(g.getNode(5))
        );
    }
    @Test
    public void connectTest() {
        g.connect(32,78,4.32);
        assertAll(
                ()-> assertEquals(4.32,g.getEdge(32,78).getWeight()),
                ()-> assertNull(g.getEdge(78,32)),
                ()-> g.connect(32,78,11.2),
                ()-> assertEquals(11.2,g.getEdge(32,78).getWeight())
        );
    }

    @Test
    public void getVTest() {
        int size=g.getV().size();
        assertAll(
                ()-> assertEquals(g.nodeSize(),size),
                ()-> g.removeNode(99),
                ()-> assertEquals(g.nodeSize(),size-1)
        );
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
        assertAll(
                ()-> assertEquals(g.getE(3).size(),2),
                ()-> assertEquals(g.getE(2).size(),0),
                ()-> assertEquals(g.getE(1).size(),1),
                ()-> assertNull(g.getE(5))
        );
    }

    @Test
    public void removeNodeTest() {
        node_data n=new NodeData(27);
        g.addNode(n);
        g.connect(27,40,3.05);
        g.connect(40,27,3.15);
        g.removeNode(27);
        assertAll(
                ()-> assertFalse(g.getV().contains(g.getNode(27))),
                ()-> assertNull(g.getEdge(27,40)),
                ()-> assertNull(g.getEdge(40,27)),
                ()-> assertFalse(g.getE(40).contains(27))
        );
    }

    @Test
    public void removeEdge() {
        g.connect(1,5,9.2);
        g.removeEdge(1,5);
        assertNull(g.getEdge(1,5));
    }
}