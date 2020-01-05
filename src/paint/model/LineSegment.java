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
public class LineSegment extends Shape {

    @Override
    public void draw(Object canvas) {
        Graphics2D g = (Graphics2D) canvas;
        g.setStroke(stroke);
        g.setColor( getColor() );
        g.drawLine( startPos.x + offset.x , startPos.y + offset.y , endPos.x + offset.x , endPos.y + offset.y);
    }

    
    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape line = new LineSegment();
        super.cloneShapeProperties(line);
        return line;
    }

    @Override
    public boolean contains(Point p) {
        /*Line2D line = new Line2D.Double(startPos.x, startPos.y, endPos.x, endPos.y);
        return line.contains(p);*/
        Ellipse2D e1 = new Ellipse2D.Double(startPos.x + offset.x - 15, startPos.y + offset.y - 15, 30, 30);
        Ellipse2D e2 = new Ellipse2D.Double(endPos.x + offset.x - 15, endPos.y + offset.y- 15, 30, 30);
        return e1.contains(p) || e2.contains(p);
    }

    @Override
    public void calculateBounds() {
        // do nothing :)
    }

    @Override
    public void drawSelectionRectangle(Graphics2D g) {
        g.setStroke(dashed);
        g.setColor(Color.MAGENTA);
        g.drawLine( startPos.x + offset.x, startPos.y + offset.y , endPos.x + offset.x, endPos.y + offset.y);
        drawTinyRectAtEndPoint(new Point(endPos.x + offset.x, endPos.y + offset.y), g);
    }
    
}
