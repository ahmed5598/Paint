package paint.controller;

import java.util.ArrayList;
import paint.model.Shape;

public class LoadCommand extends AbstractCommand implements Command{

    int oldArrayListSize;
    ArrayList<Shape> shapesRemoved;
    Context context;
    
    public LoadCommand(Context context) {
        this.context = context;
        shapesRemoved = new ArrayList<>();
        oldArrayListSize = controller.getListOfShapes().size();
    }

    @Override
    public void execute() {
        super.execute();
        if( shapesRemoved.isEmpty() )
        {
            context.executeRead();
        }
        else
        {
            for( int i = shapesRemoved.size() - 1 ; i >= 0; --i )
            {
                Shape shape = shapesRemoved.remove(i);
                controller.addShape(shape);
            }
        }
        
    }

    @Override
    public void undo() {
        super.undo();
        int newArraylistSize = controller.getListOfShapes().size();
        for( int i = newArraylistSize - 1 ; i >= oldArrayListSize ; i--)
        {
            Shape shape = controller.getListOfShapes().get(i);
            controller.removeShape(shape);
            shapesRemoved.add(shape);
        }
        controller.getView().getPaintPanel2().repaint();
    }
}
