/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author FAST
 */
public class Rectangle extends Shape {

    public Rectangle() {
        properties = new HashMap<>();
        properties.put("x", 0.0);
        properties.put("y", 0.0);
        properties.put("w", 0.0);
        properties.put("l", 0.0);
    }
    
    public void calculateBounds()
    {
        int minX = Math.min( startPos.x , endPos.x);
        int minY = Math.min( startPos.y , endPos.y);
        int width = Math.abs( startPos.x - endPos.x );
        int length = Math.abs( startPos.y - endPos.y );
        properties.put("x", (double)minX);
        properties.put("y", (double)minY);
        properties.put("w",(double) width);
        properties.put("l",(double) length);
    }

    
    @Override
    public void draw(Object canvas) {
            Graphics2D g = (Graphics2D) canvas;

            int minX = (properties.get("x")).intValue() + offset.x;
            int minY = (properties.get("y")).intValue() + offset.y;
            int width = (properties.get("w")).intValue();
            int length  = (properties.get("l")).intValue();
            
            if( isFillOrNot() == true)
            {
                g.setColor(getFillColor());
                g.fillRect( minX , minY , width , length);    
            }
            
            g.setColor( getColor() );
            g.setStroke(getStroke());
            g.drawRect( minX , minY , width , length);
    }

    
    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape rectangle = new Rectangle();
        super.cloneShapeProperties(rectangle);
        return rectangle;
    }
    
    
    public boolean contains( Point point )
    {
        int minX = properties.get("x").intValue() + offset.x;
        int minY = properties.get("y").intValue() + offset.y;
        int width = properties.get("w").intValue();
        int length = properties.get("l").intValue();
        
        java.awt.Rectangle rectangle = new java.awt.Rectangle( minX, minY, width, length);
        return rectangle.contains(point);
    }

    @Override
    public void drawSelectionRectangle(Graphics2D g) {
        int minX = properties.get("x").intValue() + offset.x;
        int minY = properties.get("y").intValue() + offset.y;
        int width = properties.get("w").intValue();
        int length = properties.get("l").intValue();
        
        g.setStroke(dashed);
        g.setPaint(Color.MAGENTA);
        g.drawRect(minX , minY, width, length);
        drawTinyRectAtEndPoint(new Point(endPos.x + offset.x, endPos.y + offset.y), g);
    }
}
