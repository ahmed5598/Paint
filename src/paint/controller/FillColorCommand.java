package paint.controller;

import java.awt.Color;
import paint.model.Memento;
import paint.model.Shape;

public class FillColorCommand extends AbstractCommand implements Command{
    private Shape shape;
    Memento memento;
    private Color newColor;

    public FillColorCommand(Shape shape, Color newColor) {
        this.shape = shape;
        this.newColor = new Color(newColor.getRGB());
        memento = shape.getMemento();
    }

    @Override
    public void execute() {
        super.execute();
        shape.setFillColor(newColor);
    }

    @Override
    public void undo() {
        super.undo();
        shape.setFillColor(memento.getFillColor());
    }
}