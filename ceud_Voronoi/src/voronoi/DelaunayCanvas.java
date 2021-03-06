package voronoi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author Ceud
 */
public class DelaunayCanvas extends JComponent
{
    private Delaunay delaunay;
    private boolean showPoints;

    public DelaunayCanvas(Delaunay d)
    {
        super();
        this.delaunay = d;
        showPoints = false;
    }

    public DelaunayCanvas(Delaunay d, boolean ShowPoints)
    {
        super();
        this.delaunay = d;
        showPoints = ShowPoints;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        BufferedImage bi = new BufferedImage(delaunay.w, delaunay.h, BufferedImage.TYPE_INT_RGB);

        Graphics2D big = (Graphics2D)bi.getGraphics();
        Graphics2D g1 = (Graphics2D)g;
        
        if (Generator.EnableAntiAliasing)
        {
            // Enable antialiasing for shapes
            big.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);
            g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_ON);
        }
        
        // draw entire component white
        big.setColor(Color.white);
        big.fillRect(0, 0, getWidth(), getHeight());
        g1.setColor(Color.white);
        g1.fillRect(0, 0, getWidth(), getHeight());
        
        
        big.setColor(Color.black);
        g1.setColor(Color.black);
        
        
        //Draw delaunay triangle edges
        Iterator<Triangle> eIt = delaunay.tArr.iterator();
        while (eIt.hasNext())
        {
            Triangle eValue = eIt.next();
            big.drawLine((int)eValue.a.x1, (int)eValue.a.y1, (int)eValue.a.x2, (int)eValue.a.y2);
            big.drawLine((int)eValue.b.x1, (int)eValue.b.y1, (int)eValue.b.x2, (int)eValue.b.y2);
            big.drawLine((int)eValue.c.x1, (int)eValue.c.y1, (int)eValue.c.x2, (int)eValue.c.y2);
            g.drawLine((int)eValue.a.x1, (int)eValue.a.y1, (int)eValue.a.x2, (int)eValue.a.y2);
            g.drawLine((int)eValue.b.x1, (int)eValue.b.y1, (int)eValue.b.x2, (int)eValue.b.y2);
            g.drawLine((int)eValue.c.x1, (int)eValue.c.y1, (int)eValue.c.x2, (int)eValue.c.y2);
        }
        
        if (showPoints)
        {
            //Draw delaunay vertices
            big.setColor(Color.BLUE);
            g1.setColor(Color.BLUE);

            Iterator<Vertex> vIt = delaunay.vArr.iterator();
            Vertex vertex;

            while (vIt.hasNext())
            {
                vertex = vIt.next();
                big.fillRect((int)vertex.x - 1, (int)vertex.y - 1, 3, 3);
                g1.fillRect((int)vertex.x - 1, (int)vertex.y - 1, 3, 3);
            }
        }
        
        //save big to file!
        try {
            File outputfile = new File(Generator.ImageOutputName);
            ImageIO.write(bi, "png", outputfile);
        } catch (IOException e) {
            System.out.println("Unable to save to file!\n" + e.getMessage());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(delaunay.w, delaunay.h);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}