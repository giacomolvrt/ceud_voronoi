package voronoi;

import java.awt.geom.Line2D;

/**
 *
 * @author Ceud
 */
public class Edge extends Line2D.Double
{
    public Edge(double x1, double y1, double x2, double y2)
    {
        super(x1, y1, x2, y2);
    }
    
    public Edge(Vertex v1, Vertex v2)
    {
        super(v1, v2);
    }
    
    public double length()
    {
        return Vertex.distance(x1, y1, x2, y2);
    }
    
    public double lengthSquared()
    {
        return Vertex.distanceSq(x1, y1, x2, y2);
    }
    
    public Vertex midPoint()
    {
        return new Vertex(this.x1 + ((this.x2 - this.x1) / 2), this.y1 + ((this.y2 - this.y1) / 2));
    }
    
    public Edge rotate(Vertex origin, double degrees)
    {
        double theta = Vertex.NormaliseTo360Degrees(degrees) * (Math.PI / 180);
        
        Vertex lineStart = new Vertex(this.x1 - origin.x, this.y1 - origin.y);
        Vertex lineEnd = new Vertex(this.x2 - origin.x, this.y2 - origin.y);
        
        Vertex startPolar = new Vertex(lineStart.x, lineStart.y).cartesianToPolar();
        Vertex endPolar = new Vertex(lineEnd.x, lineEnd.y).cartesianToPolar();
        
        //update y (theta) to reflect rotation angle
        startPolar.y = Vertex.NormaliseTo360Radians(startPolar.y + theta);
        endPolar.y = Vertex.NormaliseTo360Radians(endPolar.y + theta);
        
        lineStart = startPolar.polarToCartesian();
        lineEnd = endPolar.polarToCartesian();
        
        return new Edge(lineStart.x+origin.x, lineStart.y+origin.y, lineEnd.x+origin.x, lineEnd.y+origin.y);
        
    }
    
    public Edge extend(double max)
    {
        Vertex m = this.midPoint();
        Vertex v1 = new Vertex(this.x1 - m.x, this.y1 - m.y).cartesianToPolar();
        Vertex v2 = new Vertex(this.x2 - m.x, this.y2 - m.y).cartesianToPolar();
        v1.x = max;
        v2.x = max;
        
        v1 = v1.polarToCartesian();
        v2 = v2.polarToCartesian();
        
        v1.x += m.x;
        v1.y += m.y;
        v2.x += m.x;
        v2.y += m.y;
        
        return new Edge(v1, v2);
    }
    
    public Edge perpendicularBisector()
    {
        return this.rotate(this.midPoint(), 90d).extend(50000);
    }
    
    public Vertex intersection(Edge e)
    {
        if (!this.intersectsLine(e))
            return null;

        double x = det(det(x1, y1, x2, y2), x1 - x2,
                       det(e.x1, e.y1, e.x2, e.y2), e.x1 - e.x2)/
                       det(x1 - x2, y1 - y2, e.x1 - e.x2, e.y1 - e.y2);
        double y = det(det(x1, y1, x2, y2), y1 - y2,
                       det(e.x1, e.y1, e.x2, e.y2), e.y1 - e.y2)/
                       det(x1 - x2, y1 - y2, e.x1 - e.x2, e.y1 - e.y2);

        return new Vertex(x, y);
    }
    
    public boolean booleanIntersection(Edge line)
    {
        if (!this.intersectsLine(line))
            return false;

        return true;
    }

    static double det(double a, double b, double c, double d)
    {
        return a * d - b * c;
    }
    
    public boolean equals(Edge e)
    {
        if (((x1 == e.x1 && y1 == e.y1) && 
             (x2 == e.x2 && y2 == e.y2)) ||
            ((x1 == e.x2 && y1 == e.y2) && 
             (x2 == e.x1 && y2 == e.y1)))
        {
            return true;
        }
        return false;
    }
}
