package paint.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.Map;

public class Triangle extends Shape{

    public Triangle() {
        properties = new HashMap<>();
        properties.put("3PX" , (double) 0); //3PX -> third point x-coordinate
        properties.put("3PY" , (double) 0); //3PY -> third point y-coordinate
    }


    @Override
    public void calculateBounds() {
        int thirdPointX , thirdPointY;
        
        if(startPos.x > endPos.x)
            thirdPointX = endPos.x +  (2 * Math.abs(startPos.x - endPos.x));
        else
            thirdPointX = endPos.x -  (2 * Math.abs(startPos.x - endPos.x));
        
        thirdPointY = endPos.y;
        
        properties.put("3PX" , (double) thirdPointX); //3PX -> third point x-coordinate
        properties.put("3PY" , (double) thirdPointY); //3PY -> third point y-coordinate
    }
    
    
    @Override
    public void drawSelectionRectangle(Graphics2D g) {
        int halfWidth = Math.abs(startPos.x - endPos.x);
        int length = Math.abs(startPos.y - endPos.y);
        
        int minX = Math.min(startPos.x , endPos.x);
        minX = Math.min(minX , properties.get("3PX").intValue()) + offset.x;
        int minY = Math.min(startPos.y , endPos.y) + offset.y;
        
        g.setStroke(dashed);
        g.setPaint(Color.MAGENTA);
        
        if( minX == startPos.x)
            minX -= halfWidth;
        
        g.drawRect(minX, minY, 2* halfWidth, length);
        drawTinyRectAtEndPoint(new Point(endPos.x + offset.x, endPos.y + offset.y), g);
    }
    
    @Override
    public void draw(Object canvas) {
        Graphics2D g = (Graphics2D) canvas;
        
        int thirdPointX , thirdPointY;
        
        thirdPointX = properties.get("3PX").intValue();
        thirdPointY = properties.get("3PY").intValue();

        int[] xArray = {startPos.x + offset.x 
                        ,endPos.x + offset.x
                        , thirdPointX + offset.x};
        
        int[] yArray = { startPos.y  + offset.y
                       ,  endPos.y   + offset.y
                       , thirdPointY + offset.y};
        
        if( isFillOrNot() == true)
        {
            g.setColor(getFillColor());
            g.fillPolygon(xArray,yArray, 3); 
        }
        
        g.setColor( getColor() );
        g.setStroke(getStroke());
        g.drawPolygon(xArray,yArray, 3);
        
    }

    
     @Override
    public Object clone() throws CloneNotSupportedException {
        Shape triangle = new Triangle();
        super.cloneShapeProperties(triangle);
        return triangle;
    }
    
    @Override
    public boolean contains(Point p) {   
        int thirdPointX , thirdPointY;
        thirdPointX = properties.get("3PX").intValue();
        thirdPointY = properties.get("3PY").intValue();
        
        int[] xArray = {startPos.x + offset.x 
                        ,endPos.x + offset.x
                        , thirdPointX + offset.x};
        
        int[] yArray = { startPos.y  + offset.y
                       ,  endPos.y   + offset.y
                       , thirdPointY + offset.y};
        
        Polygon trianglel = new Polygon(xArray, yArray, 3);
        return trianglel.contains(p);
        
    }

}
