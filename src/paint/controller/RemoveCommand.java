package paint.controller;

import paint.model.Shape;

public class RemoveCommand extends AbstractCommand implements Command{

    private final Shape shapeToRemove;
    
    public RemoveCommand(Shape shape) {
        this.shapeToRemove = shape;
    }
    
    @Override
    public void execute() {
        super.execute();
        controller.removeShape(shapeToRemove);
        controller.getView().getPaintPanel2().repaint();
    }

    @Override
    public void undo() {
        super.undo();
        controller.addShape(shapeToRemove);
        controller.getView().getPaintPanel2().repaint();
    }
    
}
