
/**
 * 
 * @author Rob Thomas
 * 
 * Description: This class creates a GUI in which the user can freely draw
 * polygons of an arbitrary number of sides. Polygons are drawn by clicking 
 * inside the GUI to place at least three vertices, then pressing any key
 * besides the backspace key. Polygons are assigned a random color when they
 * are drawn. The last polygon to be drawn can be erased by pressing the 
 * backspace key. This clas makes use of Java's AWT and Swing libraries.
 * 
 */

package drawingpolygons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class DrawingPolygons 
{
    public static final int DOT_RADIUS = 2;

    public static void main(String[] args) 
    {
        JFrame frame = new JFrame("Draw some Polygons!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 600);
        
        PolygonPanel panel = new PolygonPanel();
        frame.add(panel);
        frame.setVisible(true);
    }
    
    public static class PolygonPanel extends JPanel
    {
        private ArrayList<Point> currentPoints;
        private ArrayList<Polygon> polygonList;
        private ArrayList<Color> colorList;
        
        public PolygonPanel()
        {
            setBackground(Color.white);
            setForeground(Color.black);
            //Initialize the three lists.
            currentPoints = new ArrayList<>();
            polygonList = new ArrayList<>();
            colorList = new ArrayList<>();
            
            addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    requestFocus();
                    currentPoints.add(e.getPoint());
                    repaint();
                }
            });
            
            addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyPressed(KeyEvent e)
                {
                    //If the key pressed was backspace, delete the last polygon.
                    if (e.getKeyCode() == 8)
                    {
                        polygonList.remove(polygonList.size() - 1);
                        repaint();
                    }
                    //Otherwise, add a new polygon.
                    else
                    {
                        drawPolygon();
                        repaint();
                    }
                }
            });
        }
        
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            
            //Draw the polygons saved in polygonList.
            for (int x = 0; x < polygonList.size(); x++)
            {
                g.setColor(colorList.get(x));
                g.fillPolygon(polygonList.get(x));
            }
            
            //Draw points. Radius of each dot is 2.
            g.setColor(Color.black);
            for (Point p: currentPoints)
            {
                g.fillOval(p.x - DOT_RADIUS, p.y - DOT_RADIUS,
                    DOT_RADIUS * 2, DOT_RADIUS * 2);
            }
            
            //Draw lines between points to outline the current polygon.
            for (int x = 0; x < currentPoints.size(); x++)
            {
                //If this is the last point, draw a line between it and the
                    //first point.
                if (x == currentPoints.size() - 1)
                {
                    g.drawLine(currentPoints.get(x).x, currentPoints.get(x).y,
                        currentPoints.get(0).x, currentPoints.get(0).y);
                }
                //Otherwise, draw a line between this point and the next one.
                else
                {
                    g.drawLine(currentPoints.get(x).x, currentPoints.get(x).y,
                        currentPoints.get(x+1).x, currentPoints.get(x+1).y);
                }
            }
        }
        
        public void drawPolygon()
        {
            int[] X = new int[currentPoints.size()];
            int[] Y = new int[currentPoints.size()];
            
            //Construct the x and y coordinate arrays.
            for (int i = 0; i < currentPoints.size(); i++)
            {
                X[i] = currentPoints.get(i).x;
                Y[i] = currentPoints.get(i).y;
            }
            
            //Add a new polygon to polygonList.
            polygonList.add(new Polygon(X, Y, currentPoints.size()));
            //Add a corresponding color to the color list.
            colorList.add(genNewColor());
            //Reset currentPoints to empty.
            currentPoints.clear();
        }
        
        public Color genNewColor()
        {
            Random gen = new Random();
            
            int red = gen.nextInt(256);
            int green = gen.nextInt(256);
            int blue = gen.nextInt(256);
            
            return new Color(red, green, blue);
        }
    }
    
}
