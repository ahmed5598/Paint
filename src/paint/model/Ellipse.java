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
import java.awt.geom.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author FAST
 */
public class Ellipse extends Shape{

    public Ellipse() {
        properties = new HashMap<>();
        properties.put("x", 0.0);
        properties.put("y", 0.0);
        properties.put("w", 0.0);
        properties.put("l", 0.0);
    }
    
    @Override
    public void calculateBounds() {
        
        int minX = Math.min( startPos.x , endPos.x);
        int minY = Math.min( startPos.y , endPos.y);
        int width = Math.abs( startPos.x - endPos.x );
        int length = Math.abs( startPos.y - endPos.y );
    
        properties.put("x",(double) minX);
        properties.put("y",(double) minY);
        properties.put("w",(double) width);
        properties.put("l", (double)length);
    
    }

    @Override
    public void draw(Object canvas) {
        Graphics2D g = (Graphics2D) canvas;
        
        int minX   = properties.get("x").intValue() + offset.x;
        int minY   = properties.get("y").intValue() + offset.y;
        int width  = properties.get("w").intValue();
        int length = properties.get("l").intValue();
        
        if( isFillOrNot() == true)
        {
            g.setColor(getFillColor());
            g.fillOval(minX, minY, width, length);    
        }
        
        g.setColor( getColor() );
        g.setStroke(getStroke());
        g.drawOval( minX , minY , width, length);
    }

    
    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape ellipse = new Ellipse();
        super.cloneShapeProperties(ellipse);
        return ellipse;
    }

    @Override
    public boolean contains(Point p) {
        int minX   = properties.get("x").intValue() + offset.x;
        int minY   = properties.get("y").intValue() + offset.y;
        int width  = properties.get("w").intValue();
        int length = properties.get("l").intValue();
        
        Ellipse2D ellipse = new Ellipse2D.Double(minX, minY, width, length);
        return ellipse.contains(p);
        
    }

    @Override
    public void drawSelectionRectangle(Graphics2D g) {
        int minX   = properties.get("x").intValue() + offset.x;
        int minY   = properties.get("y").intValue() + offset.y;
        int width  = properties.get("w").intValue();
        int length = properties.get("l").intValue();
        
        g.setStroke(dashed);
        g.setPaint(Color.MAGENTA);
        g.drawRect(minX , minY, width, length);
        drawTinyRectAtEndPoint(new Point(endPos.x + offset.x, endPos.y + offset.y), g);
    }
}


  
