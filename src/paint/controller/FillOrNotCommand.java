package paint.controller;

import java.awt.Color;
import paint.model.Memento;
import paint.model.Shape;

public class FillOrNotCommand extends AbstractCommand implements Command{
    private final Shape shape;
    private final Memento memento;
    boolean newFillOrNot;

    public FillOrNotCommand(Shape shape, boolean newFillOrNot) {
        this.shape = shape;
        this.newFillOrNot = newFillOrNot;
        memento = shape.getMemento();
    }

    @Override
    public void execute() {
        super.execute();
        shape.setFillOrNot(newFillOrNot);
    }

    @Override
    public void undo() {
        super.undo();
        shape.setFillOrNot(memento.isFillOrNot());
    }
}
