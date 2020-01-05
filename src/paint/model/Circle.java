package paint.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ahmed
 */
public class Circle extends Shape {

    public Circle() {
        properties = new HashMap<>();
        properties.put("r", 0.0);
        properties.put("x", 0.0);
        properties.put("y", 0.0);
    }
    
    @Override
    public void calculateBounds() {
        int minX = Math.min( startPos.x , endPos.x);
        int minY = Math.min( startPos.y , endPos.y);
        int maxX = Math.max( startPos.x , endPos.x);
        int maxY = Math.max( startPos.y , endPos.y);
        
        int radius = Math.min(maxX - minX, maxY - minY);
        
        if (minX < startPos.x) {
            minX = startPos.x - radius;
        }
        if (minY < startPos.y) {
            minY = startPos.y - radius;
        }
        
        properties.put("x", (double) minX);
        properties.put("y", (double) minY);
        properties.put("r", (double) radius);
    }
    
    @Override
    public void draw(Object canvas) {
        Graphics2D g = (Graphics2D) canvas;
        int minX   = properties.get("x").intValue() + offset.x;
        int minY   = properties.get("y").intValue() + offset.y;
        int radius = properties.get("r").intValue();
        
        if( isFillOrNot() == true)
        {
            g.setColor(getFillColor());
            g.fillOval( minX , minY  , radius, radius);
        }

        g.setColor( getColor() );
        g.setStroke(getStroke());
        g.drawOval( minX , minY , radius, radius);
    }

    
    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape circle = new Circle();
        super.cloneShapeProperties(circle);
        return circle;
    }
    
    @Override
    public boolean contains(Point p) {
        int minX = properties.get("x").intValue() + offset.x;
        int minY = properties.get("y").intValue() + offset.y;
        int radius = properties.get("r").intValue() ;
        
        Ellipse2D ellipse = new Ellipse2D.Double(minX, minY, radius, radius);
        return ellipse.contains(p);
    }

    @Override
    public void drawSelectionRectangle(Graphics2D g) {
        int minX = properties.get("x").intValue() + offset.x;
        int minY = properties.get("y").intValue() + offset.y;
        int radius = properties.get("r").intValue() ;
        g.setStroke(dashed);
        g.setPaint(Color.MAGENTA);
        g.drawRect(minX , minY, radius, radius);
        
        int X = endPos.x < startPos.x ?
                minX : minX + radius;
        int Y = endPos.y < startPos.y ?
                minY : minY + radius;
        
        drawTinyRectAtEndPoint(new Point(X, Y), g);
    }
}
