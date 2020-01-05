package paint.controller;

import java.awt.Color;
import paint.model.Memento;
import paint.model.Shape;

public class BorderColorCommand extends AbstractCommand implements Command{
    Shape shape;
    private final Memento memento;
    Color newColor;

    public BorderColorCommand(Shape shape, Color newColor) {
        this.shape = shape;
        this.newColor = new Color(newColor.getRGB());
        memento = shape.getMemento();
    }

    @Override
    public void execute() {
        super.execute();
        shape.setColor(newColor);
    }

    @Override
    public void undo() {
        super.undo();
        shape.setColor(memento.getColor());
    }
}
