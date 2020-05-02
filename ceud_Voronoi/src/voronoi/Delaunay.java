package voronoi;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Allows generation of a Delaunay triangulation of a set of random or specified
 * points on a plane.
 * 
 * @author Ceud
 */
public class Delaunay {
    /**
     * List of all added vertices
     */
    protected ArrayList<Vertex> vArr;
    
    /**
     * List of all calculated edges
     */
    private ArrayList<Edge> eArr;
    
    /**
     * List of all calculated triangles
     */
    protected ArrayList<Triangle> tArr;
    protected int w, h;
    
    /**
     * Initialises a Delaunay object with default dimensions
     */
    public Delaunay()
    {
        this(500, 500);
    }
    
    /**
     * Initialises a Delaunay object with specified dimensions
     */
    public Delaunay(int w, int h)
    {
        this.w = w;
        this.h = h;
        vArr = new ArrayList<Vertex>();
        eArr = new ArrayList<Edge>();
        tArr = new ArrayList<Triangle>();
        tArr.add(new Triangle(new Vertex((w - (w * 2)), (w - (w * 2))), 
                              new Vertex((w * 2), (w - (w * 2))), 
                              new Vertex((w / 2), (w * 2))));
    }
    
    /**
     * Determines if a specified point lies within the circumcircle of any of a 
     * set of triangles and stores affected edges in buffer 
     * 
     * @param x X-coordinate of point to check
     * @param y Y-coordinate of point to check
     */
    protected void isInCircumcircle(double x, double y)
    {
        ArrayList<Triangle> remTri = new ArrayList<Triangle>();
        
        Iterator<Triangle> tIt = tArr.iterator();
        Triangle t;
        while (tIt.hasNext())
        {
            t = tIt.next();
            Vertex cc = t.circumcentre();
            if (cc == null)
            {
                remTri.add(t);
            }
            
            if (!remTri.contains(t))
            {
                //store triangles edges in edgebuffer and remove triangle
                if (t.containsVertex(new Vertex(x, y), cc))
                {
                    eArr.add(new Edge(t.a.x1, t.a.y1, t.a.x2, t.a.y2));
                    eArr.add(new Edge(t.b.x1, t.b.y1, t.b.x2, t.b.y2));
                    eArr.add(new Edge(t.c.x1, t.c.y1, t.c.x2, t.c.y2));
                    
                    remTri.add(t);
                }
            }
        }
        removeTriangles(remTri);
    }
    
    /**
     * 
     * @param remTriangles List of Triangle objects to be removed
     */
    protected void removeTriangles(ArrayList<Triangle> remTriangles)
    {
        Iterator<Triangle> trIt = remTriangles.iterator();
        Triangle t;
        while (trIt.hasNext())
        {
            t = trIt.next();
            tArr.remove(t);
        }
        tArr.trimToSize();
    }
    
    /**
     * Checks for and removes all double edges from the delaunay triangulation 
     * (i.e. matching edges)
     */
    protected void removeDoubleEdges()
    {
        ArrayList<Edge> remEdges = new ArrayList<Edge>();
        
        Iterator<Edge> e1It = eArr.iterator();
        Iterator<Edge> e2It;
        Edge e1;
        Edge e2;
        
        while (e1It.hasNext())
        {
            e1 = e1It.next();
            e2It = eArr.iterator();
            while (e2It.hasNext())
            {
                e2 = e2It.next();
                
                //remove double edges
                if (e1.hashCode() != e2.hashCode() && e1.equals(e2) && !remEdges.contains(e2))
                {
                    remEdges.add(e2);
                }
            }
        }
        
        Iterator<Edge> e3It = remEdges.iterator();
        
        while (e3It.hasNext())
        {
            eArr.remove(e3It.next());
        }
        eArr.trimToSize();
    }
    
    /**
     * Creates new Triangle objects based upon edges remaining after removing 
     * double edges
     * 
     * @param x X-coordinate of new point to create Triangle objects for
     * @param y Y-coordinate of new point to create Triangle objects for
     */
    protected void createTriangles(double x, double y)
    {
        ArrayList<Edge> remEdges = new ArrayList<Edge>();
        
        Iterator<Edge> eIt = eArr.iterator();
        Edge e;
        
        while (eIt.hasNext())
        {
            e = eIt.next();
            
            //create triangle and add to list
            tArr.add(new Triangle(new Vertex(e.x1, e.y1), new Vertex(e.x2, e.y2), new Vertex(x, y)));
            remEdges.add(e);
        }
        
        //remove edges
        Iterator<Edge> e3It = remEdges.iterator();
        
        while (e3It.hasNext())
        {
            eArr.remove(e3It.next());
        }
        eArr.trimToSize();
    }
    
    /**
     * Removes super-triangle (initial all-encompassing triangle to facilitate 
     * Delaunay generation)
     */
    protected void removeSuperTriangles()
    {
        ArrayList<Triangle> remTri = new ArrayList<Triangle>();
        Triangle[] triArr;
        Iterator<Triangle> trIt = tArr.iterator();
        Triangle tri;
        while (trIt.hasNext())
        {
            tri = trIt.next();
            if (tri.a.x1 < 0 || tri.a.x1 >= w || tri.a.y1 < 0 || tri.a.y1 >= h ||
                tri.a.x2 < 0 || tri.a.x2 >= w || tri.a.y2 < 0 || tri.a.y2 >= h ||
                tri.b.x1 < 0 || tri.b.x1 >= w || tri.b.y1 < 0 || tri.b.y1 >= h ||
                tri.b.x2 < 0 || tri.b.x2 >= w || tri.b.y2 < 0 || tri.b.y2 >= h ||
                tri.c.x1 < 0 || tri.c.x1 >= w || tri.c.y1 < 0 || tri.c.y1 >= h ||
                tri.c.x2 < 0 || tri.c.x2 >= w || tri.c.y2 < 0 || tri.c.y2 >= h)
            {
                remTri.add(tri);
            }
        }
        triArr = new Triangle[remTri.size()];
        remTri.toArray(triArr);
        
        for (int i = 0; i < triArr.length; i++)
        {
            tArr.remove(triArr[i]);
        }
        tArr.trimToSize();
    }
    
    /**
     * Adds new Vertex with specified coordinates and updates the existing 
     * Delaunay triangulation to accomodate it
     * @param x X-coordinate of new Vertex
     * @param y Y-coordinate of new Vertex
     */
    public void addDelaunay(double x, double y)
    {
        //add vertex to list of points
        vArr.add(new Vertex(x, y));
        
        // determine vertex is inside triangles circumcircle
        // & remove any triangles where its circumcircle contains vertex(x, y)
        isInCircumcircle(x, y);
        
        //remove double edges
        removeDoubleEdges();
        
        //create new triangle between vertex and each edge in edgebuffer
        createTriangles(x, y);
    }
    
    /**
     * Adds new Vertex with specified coordinates and updates the existing 
     * Delaunay triangulation to accomodate it
     * @param v Vertex to add
     */
    public void addDelaunay(Vertex v)
    {
        addDelaunay(v.x, v.y);
    }
}