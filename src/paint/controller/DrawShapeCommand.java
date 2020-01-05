package paint.controller;

import paint.model.Shape;

public class DrawShapeCommand extends AbstractCommand implements Command{
    private final Shape shape;
    

    public DrawShapeCommand(Shape shape, Controller controller) {
        this.shape = shape;
        this.controller = controller;
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
