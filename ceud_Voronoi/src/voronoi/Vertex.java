package voronoi;

import java.awt.geom.Point2D;

/**
 * Specifies a point on a plane
 * 
 * @author Ceud
 */
public class Vertex extends Point2D.Double
{
    public Vertex(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Converts the Vertex objects coordinates from polar to Cartesian coordinate system
     * @return A new Vertex object with updated coordinate values
     */
    public Vertex polarToCartesian()
    {
        return new Vertex(x * Math.cos(y), x * Math.sin(y));
    }
    
    
    /**
     * Converts the Vertex objects coordinates from Cartesian to polar coordinate system
     * @return A new Vertex object with updated coordinate values
     */
    public Vertex cartesianToPolar()
    {
        return new Vertex(Math.sqrt((y * y) + (x * x)), Math.atan2(y, x));
    }
    
    /**
     * Normalises a value in degrees to the range 0 &lt;&gt; 360
     * @param degrees The value to be normalised
     * @return The normalised value in degrees
     */
    public static double NormaliseTo360Degrees(double degrees)
    {
        while (degrees >= 360) { degrees -= 360; }
        while (degrees < 0) { degrees += 360; }
        return degrees;
    }
    
    
    /**
     * Normalises a value in radians to the range equivalent to 0 &lt;&gt; 360
     * degrees
     * @param radians The value to be normalised
     * @return The normalised value in radians
     */
    public static double NormaliseTo360Radians(double radians)
    {
        while (radians >= 360 * (Math.PI / 180)) { radians -= 360 * (Math.PI / 180); }
        while (radians < 0) { radians += 360 * (Math.PI / 180); }
        return radians;
    }
    
    /**
     * Compares coordinates of the Vertices to check for equality
     * @param vertex Vertex to check against
     * @return True if vertices coordinates match, otherwise return false
     */
    public boolean equals(Vertex vertex)
    {
        return this.equals(vertex.x, vertex.y);
    }
    
    
    /**
     * Compares the Vertex coordinates and the supplied coordinates to check for equality
     * @param x X-coordinate to check against
     * @param y Y-coordinate to check against
     * @return True if vertex coordinates match the specified coordinates, otherwise return false
     */
    public boolean equals(double x, double y)
    {
        if (this.x == x && this.y == y)
        {
            return true;
        }
        return false;
    }
}
