package paint.controller;
import java.awt.Point;
import paint.model.Memento;
import paint.model.Shape;

public class MoveCommand extends AbstractCommand implements Command{

    private Shape shape;
    private Memento memento;
    private Point newOffset;

    public MoveCommand(Shape shape) {
        this.shape = shape;
        memento = shape.getMemento();
    }

    public void calcNewOffset( Point startDragging , Point endDragging )
    {
        newOffset = new Point();
        newOffset.x = endDragging.x - startDragging.x;
        newOffset.y = endDragging.y - startDragging.y;
    }
    
    @Override
    public void execute() {
        super.execute();
        Point totalOffset = new Point();
        totalOffset.x = newOffset.x + memento.getOffset().x;
        totalOffset.y = newOffset.y + memento.getOffset().y;
        shape.setOffset(totalOffset);
    }

    @Override
    public void undo() {
        super.undo();
        shape.setOffset( memento.getOffset() );
    }
}