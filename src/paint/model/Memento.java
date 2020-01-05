package paint.model;

import java.awt.Color;
import java.awt.Point;
import java.util.Map;

public class Memento {
    private Point startPos;
    private Point endPos;
    private Point offset;
    
    private Color color;
    private Color fillColor;
    private boolean fillOrNot;

    public Memento(Point startPos, Point endPos, Point offset, Color color, Color fillColor, boolean fillOrNot) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.offset = offset;
        this.color = color;
        this.fillColor = fillColor;
        this.fillOrNot = fillOrNot;
    }

    public Point getStartPos() {
        return startPos;
    }

    public void setStartPos(Point startPos) {
        this.startPos = startPos;
    }

    public Point getEndPos() {
        return endPos;
    }

    public void setEndPos(Point endPos) {
        this.endPos = endPos;
    }

    public Point getOffset() {
        return offset;
    }

    public void setOffset(Point offset) {
        this.offset = offset;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public boolean isFillOrNot() {
        return fillOrNot;
    }

    public void setFillOrNot(boolean fillOrNot) {
        this.fillOrNot = fillOrNot;
    }

    
    
}
