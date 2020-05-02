package voronoi;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JFrame;

/**
 * Example application to demonstrate usage of voronoi and delaunay classes<br />
 * <strong>TODO</strong> Still have to load points from file
 * 
 * @author Ceud
 */
public class Generator {

    public static String FileInput = "";
    public static String ImageOutputName = "random-" + System.currentTimeMillis() + ".png";
    public static String OutputType = "mixed";
    public static int Width = 500;
    public static int Height = 500;
    public static int RandomPoints = 0;
    public static boolean EnableAntiAliasing = false;
    
    public static ArrayList<Vertex> vertices = new ArrayList<Vertex>();
    
    /**
     * Entry point for the application
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        if (args.length > 0)
        {
            for (int i = 0; i < args.length; i++)
            {
                if (args[i].startsWith("-i:"))
                {
                    FileInput = args[i].substring(3).trim();
                    ImageOutputName = FileInput + "-" + System.currentTimeMillis() + ".png";
                }
                else if (args[i].startsWith("-w:"))
                {
                    int wInt = 0;
                    try {
                        wInt = Integer.parseInt(args[i].substring(3));
                    } catch (NumberFormatException nfe) {
                        wInt = 500;
                    }
                    if (wInt > 0)
                    {
                        Width = wInt;
                    }
                }
                else if (args[i].startsWith("-h:"))
                {
                    int hInt = 0;
                    try {
                        hInt = Integer.parseInt(args[i].substring(3));
                    } catch (NumberFormatException nfe) {
                        hInt = 500;
                    }
                    if (hInt > 0)
                    {
                        Height = hInt;
                    }
                }
                else if (args[i].startsWith("-r:"))
                {
                    int rInt = 0;
                    try {
                        rInt = Integer.parseInt(args[i].substring(3));
                    } catch (NumberFormatException nfe) {
                        rInt = 0;
                    }
                    if (rInt > 0)
                    {
                        RandomPoints = rInt;
                    }
                }
                else if (args[i].startsWith("-a:"))
                {
                    boolean aaBool = false;
                    try {
                        aaBool = Boolean.parseBoolean(args[i].substring(3));
                    } catch (NumberFormatException nfe) {
                        aaBool = false;
                    }
                    if (aaBool)
                    {
                        EnableAntiAliasing = aaBool;
                    }
                }
                else if (args[i].startsWith("-t:"))
                {
                    String tmpOT = args[i].substring(3).trim();
                    if (tmpOT.equals("mixed") ||
                        tmpOT.equals("delaunay") ||
                        tmpOT.equals("voronoi") ||
                        tmpOT.equals("mixed2") ||
                        tmpOT.equals("delaunay2") ||
                        tmpOT.equals("voronoi2"))
                    {
                        OutputType = tmpOT;
                    }
                }
            }
        }
        
        /*
        /////////////////////
        //start debug lines//
        /////////////////////
        Width = 500;
        Height = 400;
        OutputType = "mixed2";
        EnableAntiAliasing = false;
        //RandomPoints = 23;
        FileInput = "mypoints.txt";
        ImageOutputName = FileInput + "-" + System.currentTimeMillis() + ".png";
        ///////////////////
        //end debug lines//
        ///////////////////
        */
        
        //Load points from file
        //check if file is set and if a file exists with that name
        if (!FileInput.isEmpty())
        {
            try {
                // Open the file
                FileInputStream fstream = new FileInputStream(FileInput);
                
                // Get the object of DataInputStream
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                String[] strArr = new String[2];
                
                //Read File Line By Line
                while ((strLine = br.readLine()) != null)
                {
                    //Load entries into vertices arraylist
                    strArr = strLine.split(",");
                    vertices.add(new Vertex(Double.parseDouble(strArr[0]), Double.parseDouble(strArr[1])));
                }
                
                //Close the input stream
                in.close();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        
        //Load random points
        if (RandomPoints > 0)
        {
            //generate vertices with random coordinates
            Random randomGenerator = new Random();
            for (int idx = 0; idx < RandomPoints; idx++)
            {
                vertices.add(new Vertex(randomGenerator.nextInt(Width), randomGenerator.nextInt(Height)));
            }
        }
        
        //Ensure there are some vertices to use before proceeding
        if (vertices.isEmpty())
        {
            System.out.println("Error: No input points specified!");
            System.exit(0);
        }
        
        
        long sysTime1 = System.currentTimeMillis();
        Delaunay del = new Delaunay(Generator.Width, Generator.Height);
        
        Iterator<Vertex> it = vertices.iterator();
        Vertex v;
        while (it.hasNext())
        {
            v = it.next();
            del.addDelaunay(v);
        }
        
        Voronoi vor = Voronoi.Create(del);
        del.removeSuperTriangles();
        JFrame mainFrame;
        
        if (Generator.OutputType.equals("delaunay"))
        {
            mainFrame = new JFrame("Delaunay Diagrams");
            mainFrame.getContentPane().add(new DelaunayCanvas(del));
        }
        else if (Generator.OutputType.equals("delaunay2"))
        {
            mainFrame = new JFrame("Delaunay Diagrams");
            mainFrame.getContentPane().add(new DelaunayCanvas(del, true));
        }
        else if (Generator.OutputType.equals("voronoi"))
        {
            mainFrame = new JFrame("Voronoi Diagrams");
            mainFrame.getContentPane().add(new VoronoiCanvas(vor));
        }
        else if (Generator.OutputType.equals("voronoi2"))
        {
            mainFrame = new JFrame("Voronoi Diagrams");
            mainFrame.getContentPane().add(new VoronoiCanvas(vor, true));
        }
        else if (Generator.OutputType.equals("mixed"))
        {
            mainFrame = new JFrame("Voronoi Diagrams");
            mainFrame.getContentPane().add(new MixedCanvas(del, vor));
        }
        else  //  mixed2 (both, inc points)
        {
            mainFrame = new JFrame("Delaunay & Voronoi Diagrams");
            mainFrame.getContentPane().add(new MixedCanvas(del, vor, true));
        }
        
        mainFrame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);//throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowIconified(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowActivated(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        mainFrame.pack();
        mainFrame.setVisible(true);
        
        System.out.println("\nDrawing canvas. " + (double)((System.currentTimeMillis() - sysTime1) / 1000d) + " seconds");
    }
}