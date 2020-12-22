package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class XPanel extends JPanel {
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private JLabel _info;
    private Font _font;
    private BufferedImage _backGround;
    private BufferedImage _pokeBall;
    private BufferedImage _pika;

    XPanel(Arena arena) {
        super();
        this._ar=arena;
        this._info=new JLabel();
        this._font= new Font("font", Font.ROMAN_BASELINE,20);
        this._info.setFont(this._font);
        this._info.setBackground(Color.white);
        this._info.setForeground(new Color(200, 10, 255));
        this._info.setOpaque(true);
        this.add(this._info);
        try {
            this._backGround = ImageIO.read(new File("data/back.jpg"));
            this._pokeBall = ImageIO.read(new File("data/pokeBall.png"));
            this._pika = ImageIO.read(new File("data/pika.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        updateFrame();
        drawBackGround(g);
        drawPokemons(g);
        drawGraph(g);
        drawAgents(g);
        drawInfo(g);
        drawClock(g);
    }

    private void drawBackGround(Graphics g){
        g.drawImage(_backGround,0,0,getWidth(),getHeight(),null);
    }
    private void drawInfo(Graphics g) {
        List<Integer> info = _ar.getInfo();
        this._info.setText(" Moves: " + info.get(0) + " Grade: " + info.get(1));
    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while(iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.white);
            drawNode(n,6,g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while(itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.white);
                drawEdge(e, g);
            }
        }
    }

    private void drawPokemons(Graphics g) {
        for (CL_Pokemon pika : _ar.getPokemons()) {
            Point3D p = pika.getLocation();
            if (p != null) {
                geo_location fp = this._w2f.world2frame(p);
                g.drawImage(this._pika, (int) fp.x(), (int) fp.y(), 25, 25, null);
            }
        }
    }

    private void drawAgents(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        g.setColor(Color.red);
        int i=0;
        while(rs!=null && i<rs.size()) {
            geo_location c = rs.get(i).getLocation();
            i++;
            if(c!=null) {
                geo_location fp = this._w2f.world2frame(c);
                g.drawImage(this._pokeBall, (int) fp.x(), (int) fp.y(), 25, 25, null);
            }
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
    }

    private void drawClock(Graphics g){
        g.setColor(new Color(130, 10, 70));
        g.drawOval(25,25,60,60);
        g.setColor(new Color(200, 10, 255));
        g.setFont(new Font("Clock", Font.LAYOUT_RIGHT_TO_LEFT, 20));
        g.drawString(""+_ar.getTime(),45,65);


    }
}
