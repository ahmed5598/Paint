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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author FAST
 */
public class Square extends Shape {

    public Square() {
        properties = new HashMap<>();
        properties.put("x", 0.0);
        properties.put("y", 0.0);
        properties.put("s", 0.0);
    }

    @Override
    public void calculateBounds() {
        int minX = Math.min( startPos.x , endPos.x);
        int minY = Math.min( startPos.y , endPos.y);
        int side = Math.abs( startPos.x - endPos.x );
        
        properties.put("x", (double) minX);
        properties.put("y", (double) minY);
        properties.put("s", (double) side);
    }
    
    @Override
    public void draw(Object canvas) {
        Graphics2D g = (Graphics2D) canvas;
        
        int minX = properties.get("x").intValue() + offset.x;
        int minY = properties.get("y").intValue() + offset.y;
        int side = properties.get("s").intValue();
        
        if( isFillOrNot() == true)
        {
            g.setColor(getFillColor());
            g.fillRect( minX  , minY , side, side);     
        }
        
        g.setColor( getColor() );
        g.setStroke(getStroke());
        g.drawRect( minX , minY , side, side); 
        
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape square = new Square();
        super.cloneShapeProperties(square);
        return square;
    }
    
        public boolean contains( Point point )
    {
        int minX = properties.get("x").intValue() + offset.x;
        int minY = properties.get("y").intValue() + offset.y;
        int side = properties.get("s").intValue();
        
        java.awt.Rectangle rectangle = new java.awt.Rectangle( minX, minY, side, side);
        return rectangle.contains(point);
    }

    @Override
    public void drawSelectionRectangle(Graphics2D g) {
        int minX = properties.get("x").intValue() + offset.x;
        int minY = properties.get("y").intValue() + offset.y;
        int side = properties.get("s").intValue();

        g.setStroke(dashed);
        g.setPaint(Color.MAGENTA);
        g.drawRect(minX , minY, side, side);
        
        int X = endPos.x < startPos.x ?
                minX : minX + side;
        int Y = endPos.y < startPos.y ?
                minY : minY + side;
        
        drawTinyRectAtEndPoint(new Point(X, Y), g);
    }
}