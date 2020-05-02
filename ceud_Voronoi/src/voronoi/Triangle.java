package voronoi;

import java.util.HashMap;

/**
 *
 * @author Ceud
 */
public class Triangle {
    protected Edge a;
    protected Edge b;
    protected Edge c;
    
    public Triangle(Edge A, Edge B, Edge C)
    {
        a = A;
        b = B;
        c = C;
    }
    
    public Triangle(Vertex A, Vertex B, Vertex C)
    {
        a = new Edge(B, C);
        b = new Edge(A, C);
        c = new Edge(A, B);
    }
    
    public Vertex circumcentre()
    {
        return a.perpendicularBisector().intersection(b.perpendicularBisector());
    }
    
    public Vertex[] distinctPoints()
    {
        Vertex[] vArray = new Vertex[3];
        vArray[0] = new Vertex(a.x1, a.y1);
        vArray[1] = new Vertex(a.x2, a.y2);
        if ((a.x1 == b.x1 && a.y1 == b.y1) ||
            (a.x2 == b.x1 && a.y2 == b.y1))
        {
            vArray[2] = new Vertex(b.x2, b.y2);
        }
        else
        {
            vArray[2] = new Vertex(b.x1, b.y1);
        }
        return vArray;
    }
    
    public boolean containsVertex(Vertex vertex)
    {
        boolean contained = true;
        
        //test intersect of lines : pc > a | b | c
        Edge pc = new Edge(vertex, this.centroid());
        Vertex pca = pc.intersection(a);
        Vertex pcb = pc.intersection(b);
        Vertex pcc = pc.intersection(c);
        
        if (pca != null || pcb != null || pcc != null)
        {
            contained = false;
        }
        
        return contained;
    }
    
    public boolean containsVertex(Vertex vertex, Vertex circumcentre)
    {
        boolean contained = true;
        double ca = Vertex.distanceSq(a.x1, a.y1, circumcentre.x, circumcentre.y);
        double vc = Vertex.distanceSq(circumcentre.x, circumcentre.y, vertex.x, vertex.y);
        
        if (ca < vc)
        {
            contained = false;
        }
        return contained;
    }
    
    public Vertex centroid()
    {
        
        Vertex[] vArray = distinctPoints();
        
        double xVal = (vArray[0].x + vArray[1].x + vArray[2].x) / 3;
        double yVal = (vArray[0].y + vArray[1].y + vArray[2].y) / 3;
        return new Vertex(xVal, yVal);
    }
    
    public double area()
    {
        return Math.sqrt(areaSquared());
    }
    
    public double areaSquared()
    {
        double s = (a.length() + b.length() + c.length()) / 2;
        return s * (s - a.length()) * (s - b.length()) * (s - c.length());
    }
    
    public boolean onEdge(Vertex v)
    {
        boolean onEdge = false;
        
        if (this.a.intersectsLine(v.x, v.y, v.x, v.y) ||
            this.b.intersectsLine(v.x, v.y, v.x, v.y) ||
            this.c.intersectsLine(v.x, v.y, v.x, v.y))
        {
            onEdge = true;
        }
        
        return onEdge;
    }
    
    public Edge adjacentEdge(Triangle t)
    {
        Edge edge = null;
        Vertex[] vA = distinctPoints();
        Vertex[] tA = t.distinctPoints();
        
        if (a.equals(t.a)) { edge = a; }
        else if (b.equals(t.a)) { edge = b; }
        else if (c.equals(t.a)) { edge = c; }
        else if (a.equals(t.b)) { edge = a; }
        else if (b.equals(t.b)) { edge = b; }
        else if (c.equals(t.b)) { edge = c; }
        else if (a.equals(t.c)) { edge = a; }
        else if (b.equals(t.c)) { edge = b; }
        else if (c.equals(t.c)) { edge = c; }
        
        return edge;
    }
    
    public boolean onPoint(Vertex v)
    {
        boolean onPoint = false;
        Vertex[] vArray = distinctPoints();
        
        if ((vArray[0].x == v.x && vArray[0].y == v.y) ||
            (vArray[1].x == v.x && vArray[1].y == v.y) ||
            (vArray[2].x == v.x && vArray[2].y == v.y))
        {
            onPoint = true;
        }
        
        return onPoint;
    }
    
    public Edge[] otherEdges(Edge e)
    {
        Edge[] otherEdges = new Edge[2];
        if (a.equals(e))
        {
            otherEdges[0] = b;
            otherEdges[1] = c;
        }
        else if (b.equals(e))
        {
            otherEdges[0] = a;
            otherEdges[1] = c;
        }
        else if (c.equals(e))
        {
            otherEdges[0] = a;
            otherEdges[1] = b;
        }
        return otherEdges;
    }
    
    public Edge oppositeEdge(double x, double y)
    {
        Edge edge = null;
        HashMap<String, Edge> eMap = new HashMap<String, Edge>();
        eMap.put("a", a);
        eMap.put("b", b);
        eMap.put("c", c);
        
        if ((a.x1 == x && a.y1 == y) || (a.x2 == x && a.y2 == y))
        {
            eMap.remove("a");
        }
        if ((b.x1 == x && b.y1 == y) || (b.x2 == x && b.y2 == y))
        {
            eMap.remove("b");
        }
        if ((c.x1 == x && c.y1 == y) || (c.x2 == x && c.y2 == y))
        {
            eMap.remove("c");
        }
        
        if (eMap.containsKey("a"))
        {
            edge = new Edge(a.x1, a.y1, a.x2, a.y2);
        }
        else if (eMap.containsKey("b"))
        {
            edge = new Edge(b.x1, b.y1, b.x2, b.y2);
        }
        else if (eMap.containsKey("c"))
        {
            edge = new Edge(c.x1, c.y1, c.x2, c.y2);
        }
        return edge;
    }
    
    public boolean equals(Triangle t)
    {
        if ((a.equals(t.a) && b.equals(t.b) && c.equals(t.c)) ||
            (a.equals(t.a) && b.equals(t.c) && c.equals(t.b)) ||
            (a.equals(t.b) && b.equals(t.a) && c.equals(t.c)) ||
            (a.equals(t.b) && b.equals(t.c) && c.equals(t.a)) ||
            (a.equals(t.c) && b.equals(t.a) && c.equals(t.b)) ||
            (a.equals(t.c) && b.equals(t.b) && c.equals(t.a)))
        {
            return true;
        }
        return false;
    }
}
