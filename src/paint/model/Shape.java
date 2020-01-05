package paint.model;
import java.awt.*;
import java.util.*;

public abstract class Shape implements Ishape , MementoOriginator{
    protected static final BasicStroke stroke = new BasicStroke(3.0f);
    protected static float dash[] = {10.0f};
    protected static BasicStroke dashed =
        new BasicStroke(2.0f,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10.0f, dash, 0.0f);

    protected Point startPos;
    protected Point endPos;
    protected Point offset;
    protected Map<String,Double> properties;
    protected Color color;
    protected Color fillColor;
    protected boolean fillOrNot;

    public Shape() {
        offset = new Point(0, 0);
    }
    
    public void cloneShapeProperties(Shape shape)
    {
        Point newStart = new Point();
        Point newEnd = new Point();
        
        //new starting position with difference of 100
        newStart.x = startPos.x + 100;
        newStart.y = startPos.y + 100 ;
        
        //new ending position with difference of 100
        newEnd.x = endPos.x + 100;
        newEnd.y = endPos.y + 100 ;
        
        //set offset of new change equal to old shape
        shape.setOffset(new Point( offset.x , offset.y));
        shape.setStartPos(newStart);
        shape.setEndPos(newEnd);
        
        Color borderColor = new Color(color.getRGB());
        Color rectFillColor   = new Color(this.fillColor.getRGB());
        
        shape.setColor(borderColor);
        shape.setFillColor(rectFillColor);
        shape.setFillOrNot(this.fillOrNot);
        
        Map newprop = new HashMap<>();
        //for (Map.Entry s: properties.entrySet()) newprop.put(s.getKey(), s.getValue());
        //this.calculateBounds();
        shape.setProperties(newprop);
    }
    
    public void drawTinyRectAtEndPoint(Point endPoint , Graphics2D g)
    {
        g.setColor(Color.RED);
        g.fillRect(endPoint.x - 5, endPoint.y -5 , 5, 5);
        g.setColor(Color.YELLOW);
        g.fillRect(endPoint.x - 5, endPoint.y, 5, 5);
        g.setColor(Color.GREEN);
        g.fillRect(endPoint.x , endPoint.y -5 , 5, 5);
        g.setColor(Color.BLUE);
        g.fillRect(endPoint.x , endPoint.y, 5, 5);
        g.setColor(Color.BLACK);
        g.drawRect(endPoint.x - 5, endPoint.y - 5, 10, 10);
        
        
    }
    
    public void changeOffsetBy(Point p)
    {
        offset.x += p.x;
        offset.y += p.y;
    }
    
    public Memento getMemento()
    {
        Point mStartPos = new Point(startPos);
        Point mEndPos = new Point(endPos);
        Point mOffset = new Point(offset);
        
        Color mBorderColor = new Color(color.getRGB());
        Color mFillColor = new Color(fillColor.getRGB());
        Memento m = new Memento(mStartPos , mEndPos , mOffset , mBorderColor , mFillColor , fillOrNot);
        return m;
    }
    
    public void setMemento(Memento memento)
    {
        this.startPos = memento.getStartPos();
        this.endPos = memento.getEndPos();
        this.color = memento.getColor();
        this.fillColor = memento.getFillColor();
        this.fillOrNot = memento.isFillOrNot();
        calculateBounds();
    }
    
    //***************************SETTERS AND GETTERS THEN ABSTRACT METHODS**********************************************//
    
    public Map<String, Double> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Double> properties) {
        this.properties = properties;
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

    public static BasicStroke getStroke() {
        return stroke;
    }

    public boolean isFillOrNot() {
        return fillOrNot;
    }

    public void setFillOrNot(boolean fillOrNot) {
        this.fillOrNot = fillOrNot;
    }
    
    public Point getOffset() {
        return offset;
    }

    public void setOffset(Point offset) {
        this.offset = offset;
    }
    
    public abstract void calculateBounds();
    public abstract void draw(Object canvas);
    @Override
    public abstract Object clone() throws CloneNotSupportedException;
    public abstract boolean contains( Point p);
    public abstract void drawSelectionRectangle(Graphics2D g);
    
    @Override
    public String toString() {
        return "Object of type : " + getClass() + "\nStart point : " + getStartPos() + "\n" +"End point : " + getEndPos()
                +"\nFill or not : " + fillOrNot + "\nBorder color: " + color + "\nFill color: " + fillColor;
    }
    
    public float[] getDash() {
        return dash;
    }

    public void setDash1(float[] dash) {
        this.dash = dash;
    }

    public BasicStroke getDashed() {
        return dashed;
    }

    public void setDashed(BasicStroke dashed) {
        this.dashed = dashed;
    }
}
