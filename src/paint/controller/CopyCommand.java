package paint.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import paint.model.Shape;

public class CopyCommand extends AbstractCommand implements Command{

    private final Shape shape;
    
    public CopyCommand(Shape shapeToClone) throws CloneNotSupportedException {
        this.controller = Controller.getInstance();
        if( shapeToClone != null)
        {
            this.shape = (Shape)shapeToClone.clone();
            this.shape.calculateBounds();
        }
        else
            this.shape = null;
    }
    
    @Override
    public void execute() {
        super.execute();
        controller.addShape(shape);
        controller.getView().getPaintPanel2().repaint();
    }

    @Override
    public void undo() {
        super.undo();
        controller.removeShape(shape);
        controller.getView().getPaintPanel2().repaint();
    }
}
