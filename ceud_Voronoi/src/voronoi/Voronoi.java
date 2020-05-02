package voronoi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Ceud
 */
public class Voronoi {
    protected Delaunay delaunay;
    protected ArrayList<Vertex> vArr;
    protected ArrayList<Edge> eArr;
    protected int w, h;
    
    public Voronoi(int width, int height)
    {
        vArr = new ArrayList<Vertex>();
        eArr = new ArrayList<Edge>();
        w = width;
        h = height;
    }
    
    public Voronoi(Delaunay Delaunay)
    {
        vArr = new ArrayList<Vertex>();
        eArr = new ArrayList<Edge>();
        w = Delaunay.w;
        h = Delaunay.h;
        delaunay = Delaunay;
    }
    
    public void createVoronoiVertices(ArrayList<Triangle> tArr)
    {
        ArrayList<Vertex> vertices = new ArrayList<Vertex>();
        Iterator<Triangle> trIt = tArr.iterator();
        Triangle t;
        while (trIt.hasNext())
        {
            t = trIt.next();
            vertices.add(t.circumcentre());
        }
        vArr = vertices;
    }
    
    public void createVoronoiEdges(ArrayList<Triangle> tArr)
    {
        HashMap<String, Triangle> tMap = new HashMap<String, Triangle>();
        
        
        Iterator<Triangle> tIt1 = tArr.iterator();
        Triangle t1;
        while (tIt1.hasNext())
        {
            t1 = tIt1.next();
            tMap.put(t1.circumcentre().x + "," + t1.circumcentre().y, t1);
        }
        
        ArrayList<Edge> edges = new ArrayList<Edge>();
        Iterator<Triangle> vIt1 = tArr.iterator();
        Iterator<Triangle> vIt2;
        Triangle v1;
        Triangle v2;
        while (vIt1.hasNext())
        {
            v1 = vIt1.next();
            vIt2 = tArr.iterator();
            while (vIt2.hasNext())
            {
                v2 = vIt2.next();
                
                //if triangles are adjacent, connect vertices (v1 & v2)
                if (!v2.equals(v1))
                {
                    if (v1.adjacentEdge(v2) != null)
                    {
                        edges.add(new Edge(v1.circumcentre().x, v1.circumcentre().y, v2.circumcentre().x, v2.circumcentre().y));
                    }
                }
            }
        }
        eArr = edges;
    }
    
    public static Voronoi Create(Delaunay delaunay)
    {
        //Voronoi voronoi = new Voronoi(delaunay.w, delaunay.h);
        Voronoi voronoi = new Voronoi(delaunay);
        voronoi.createVoronoiVertices(delaunay.tArr);
        voronoi.createVoronoiEdges(delaunay.tArr);
        return voronoi;
    }
}