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
public class VoronoiCanvas extends JComponent
{
    private Voronoi voronoi;
    private boolean showPoints;

    public VoronoiCanvas(Voronoi v)
    {
        super();
        this.voronoi = v;
        showPoints = false;
    }

    public VoronoiCanvas(Voronoi v, boolean ShowPoints)
    {
        super();
        this.voronoi = v;
        showPoints = ShowPoints;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        BufferedImage bi = new BufferedImage(voronoi.w, voronoi.h, BufferedImage.TYPE_INT_RGB);

        Graphics2D big = (Graphics2D)bi.getGraphics();
        Graphics2D g1 = (Graphics2D)g;
        
        
        // Enable antialiasing for lines
        if (Generator.EnableAntiAliasing)
        {
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
        
        
        //Draw voronoi edges
        Iterator<Edge> eIt2 = voronoi.eArr.iterator();
        while (eIt2.hasNext())
        {
            Edge eValue = eIt2.next();
            big.drawLine((int)eValue.x1, (int)eValue.y1, (int)eValue.x2, (int)eValue.y2);
            g1.drawLine((int)eValue.x1, (int)eValue.y1, (int)eValue.x2, (int)eValue.y2);
        }
        
        if (showPoints)
        {
            //Draw voronoi vertices
            big.setColor(Color.BLUE);
            g.setColor(Color.BLUE);

            Iterator<Vertex> vIt2 = voronoi.delaunay.vArr.iterator();
            Vertex vertex2;

            while (vIt2.hasNext())
            {
                vertex2 = vIt2.next();
                big.fillRect((int)vertex2.x - 1, (int)vertex2.y - 1, 3, 3);
                g.fillRect((int)vertex2.x - 1, (int)vertex2.y - 1, 3, 3);
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
        return new Dimension(voronoi.w, voronoi.h);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}