package paint.controller;

import java.awt.Point;
import paint.model.Memento;
import paint.model.Shape;

public class ResizeCommand extends AbstractCommand implements Command{

    private final Shape shape;
    private final Memento memento;
    private Point newEndPosition;

    public ResizeCommand(Shape shape, Point newEndPosition) {
        this.shape = shape;
        memento = shape.getMemento();
        this.newEndPosition = newEndPosition;
    }
    
    @Override
    public void execute() {
        super.execute();
        shape.setEndPos(newEndPosition);
        shape.calculateBounds();
    }

    @Override
    public void undo() {
        super.undo();
        shape.setEndPos(memento.getEndPos());
        shape.calculateBounds();
    }

    void setNewEndPosition(Point newEndPosition) {
        this.newEndPosition = newEndPosition;
    }
}
