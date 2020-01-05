package paint.controller;

import java.awt.Color;
import java.awt.Cursor;
import paint.View.View;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JToggleButton;
import paint.model.*;

public class Controller implements DrawEngine {
    private static final Controller firstInstance = new Controller();
    
    private View view;
    private ArrayList<Shape> listOfShapes;
    private boolean shapeHasBeenAddedToList;
    private Stack<Shape> stack;
    CommandStack commandStack;
        
    private Shape currentShape;
    private Color borderColor ;
    private Color fillColor ;
    private boolean fillOrNot;
     
    private Point selectPoint;
    private boolean resize;
    
    private String selectedPaintMode;
    private int selectedShapeIndex;
    private Command command;
    
    private Controller() {
        listOfShapes = new ArrayList<>();
        stack = new Stack<>();
        selectedPaintMode = "rectangle"; //default value
        fillColor = Color.WHITE;
        borderColor = Color.BLACK;
        selectedShapeIndex = -1;
        commandStack = CommandStack.getInstance();
    }
   
    public static Controller getInstance()
    {
        return firstInstance;
    }
    
    public void addShape(Shape shape){
        listOfShapes.add(shape);    
    }
    
    public void removeShape(Shape shape){
        listOfShapes.remove(shape);
    }
    
    @Override
    public void updateShape(Shape oldShape, Shape newShape){
        listOfShapes.remove(oldShape);
        listOfShapes.add(newShape);
    }
    
    public void addCommand(Command command)
    {
        commandStack.addCommand(command);
        view.getRedoButton().setEnabled(false);
    }
    
    public void undo(){
        commandStack.undoLastCommand();
        view.getPaintPanel2().repaint();
        if( commandStack.recentCommandsIsEmpty() )
        {
            view.getUndoButton().setEnabled(false);
        }
        view.getRedoButton().setEnabled(true);
    }
    
    public void redo(){
        commandStack.redo();
        view.getPaintPanel2().repaint();
        if( commandStack.undoneCommandsIsEmpty() )
        {
            view.getRedoButton().setEnabled(false);
        }
        view.getUndoButton().setEnabled(true);
    }

    public void setView(View view) {
        this.view = view;
        setButtonActions();
        implementMouseListeners();
    }

    //create new current shape and set its attributes
    //called inside mousePressed
    //the shape may or may not be added to list of shapes depending on whether mouse is dragged or not
    public void createNewCurrentShape(Point startPoint)
    {
        currentShape = shapeFactory(selectedPaintMode);
        
        currentShape.setColor(borderColor);
        currentShape.setFillColor(fillColor);
        
        currentShape.setStartPos( startPoint );
        
        currentShape.setFillOrNot(fillOrNot);
        
        command = new DrawShapeCommand(currentShape, this);
    }
    
    //receives mouse event point
    //if point is inside a shape: shape becomes current shape & selected shape index is calculated
    //else current shape = null and selected shape index = -1 to force an error to occur
    public void selectCurrentShape( Point mousePosition )
    {
        boolean shapeIsFound = false;
        for(int i = listOfShapes.size() - 1; i >= 0; --i){

            if(listOfShapes.get(i).contains(selectPoint)){
                shapeIsFound = true;
                selectedShapeIndex = i;                       
                break;
            }
        }//END FOR LOOOP
        
        if ( shapeIsFound ) //IF A SHAPE IS SELECTED ON SCREEN
        {
            currentShape = listOfShapes.get(selectedShapeIndex);
            
            view.getResizeButton().setEnabled(true);
            view.getCloneButton().setEnabled(true);
            view.getRemoveButton().setEnabled(true);
            view.repaint();    
        }
        else
        {
            currentShape = null;
            selectedShapeIndex = -1;
            resize = false;
            view.getPaintPanel2().repaint();
            disableSelectionButtons();
        }
    }
    
    public void disableSelectionButtons()
    {
        view.getResizeButton().setEnabled(false);
        view.getResizeButton().setSelected(false);
        view.getCloneButton().setEnabled(false);
        view.getRemoveButton().setEnabled(false);
    }
    
    public void implementMouseListeners()
    {
        view.getPaintPanel2().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if( selectedPaintMode.equals("Select") )//if select button is selected
                {
                    selectPoint = e.getPoint();
                    selectCurrentShape(e.getPoint());

                    if( currentShape != null )
                    {
                        if( resize )
                        {
                            Point newEndPosition = e.getPoint();
                            newEndPosition.x -= currentShape.getOffset().x;
                            newEndPosition.y -= currentShape.getOffset().y;
                            command = new ResizeCommand(currentShape, newEndPosition);
                            currentShape.calculateBounds();
                        }
                        else
                            command = new MoveCommand(currentShape);
                    }
                }
                else{ 
                    createNewCurrentShape(e.getPoint()); //make a shape object with start point = e.getPoint()
                }
            }//END MOUSE PRESSED FUNCTION
            }//END MOUSE ADAPTER
            );//END MOUSE LISTENER
            
        view.getPaintPanel2().addMouseListener( new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if ( selectedPaintMode.equals("Select") && selectedShapeIndex != -1 && !selectPoint.equals(e.getPoint()))
                {
                    addCommand(command);
                    view.repaint();
                }
                else //if in shape drawing mode & shape has been dragged:
                {
                    shapeHasBeenAddedToList = false;
                    //selectedShapeIndex = -1;
                    //currentShape = null;
                }
            }
        });

        view.getPaintPanel2().addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    //must use 3 checks:
                    //1) Check select button is checked
                    //2) Check selectedShapeIndex != -1, meaning a shape is selected
                    //3) Mouse is inside the selected shape
                    // we can be in "select" mode but no shape is selected & thus current shape = null. So seeb el if kda:
                    if ( selectedPaintMode.equals("Select") 
                            && selectedShapeIndex != -1 
                            && currentShape.contains(e.getPoint()) )
                    {
                        if( resize ) view.getPaintPanel2().setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR) );
                        else view.getPaintPanel2().setCursor( Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR) );
                    }
                    else
                    {
                        view.getPaintPanel2().setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR) );
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if( currentShape != null)
                    {
                        if( resize ){
                            Point newEndPosition = e.getPoint();
                            newEndPosition.x -= currentShape.getOffset().x;
                            newEndPosition.y -= currentShape.getOffset().y;
                            ((ResizeCommand) command).setNewEndPosition(newEndPosition);
                            command.execute();
                            view.getPaintPanel2().repaint();
                        }
                        else{
                            switch( selectedPaintMode )
                            {  
                                case "Select":
                                    if( currentShape != null)
                                    {// shape is being dragged
                                        ((MoveCommand) command).calcNewOffset(selectPoint, e.getPoint());
                                        command.execute();
                                        view.getPaintPanel2().repaint();
                                    }
                                    break;
                                default: // If one of the shapes is selected
                                    currentShape.setEndPos( e.getPoint() );
                                    currentShape.calculateBounds();

                                    if( shapeHasBeenAddedToList == false)
                                    {
                                        executeCommand(command); //add shape to list, repaint & add to recentCommands stack
                                        shapeHasBeenAddedToList = true;
                                    }
                                    else //else bas 3ashan ma3mlsh repaint marteen wara ba3d
                                        view.getPaintPanel2().repaint();
                                    break;
                            } //END SWITCH STATEMENT
                        }//END ELSE STATEMENT
                    }//END CHECK IF CURRENT SHAPE != NULL
                }
            }
        );
    }
    
    
    //will use this function for most commands except in mouse drag so they aren't pushed into stack kaza mara
    public void executeCommand( Command command )
    {
        command.execute();
        addCommand(command);
        currentShape.calculateBounds();
        view.getPaintPanel2().repaint();
        //System.out.println(recentCommands.size());
    }
    
    @Override
    public void refresh(Object canvas){
        Graphics2D g = (Graphics2D) canvas;
        
        for(int i = 0 ; i < listOfShapes.size() ; ++i)
        {
            Shape shape = listOfShapes.get(i);
            shape.draw(g);
        }
        try
        {
            if ( selectedPaintMode.equals("Select") && selectedShapeIndex != -1 && listOfShapes.contains(currentShape))
            {
                currentShape.drawSelectionRectangle(g);
            }
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        
    }
    
    class UndoButtonClass implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            undo();   
        }
    }
    
    class RedoButtonClass implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            redo();
        }
    }
    
    class shapesAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            resize = false;
            disableSelectionButtons();
            
            selectedPaintMode = ((JToggleButton)e.getSource()).getActionCommand();
            currentShape = null;
            
            view.repaint(); //get rid of selection rectangle
            selectedShapeIndex = -1;
        }
    }
    
    class borderColorButtonAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            Color color = borderColor;
            Color temp = color;

            color = JColorChooser.showDialog(view.getPaintPanel2().getRootPane(), "Choose border color", color);
            if ( color == null)
                color =  temp; //user pressed cancel aw 7aga

            if ( selectedPaintMode.equals("Select") && selectedShapeIndex != -1)
            {
                command = new BorderColorCommand(currentShape, color);
                command.execute();
                addCommand(command);
                view.repaint();
            }
            else
            {
                System.out.println("Is current = null" + currentShape);
                System.out.println("Is selectedShapeindex = -1" + selectedShapeIndex);
            }
            
            borderColor = (color);
            view.getBorderColorButton().setBackground(color);
        }
    }
        
    class fillColorButtonAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            Color color = fillColor;
            Color temp = color;

            color = JColorChooser.showDialog(view.getPaintPanel2().getRootPane(), "Choose border color", color);
            if ( color == null)
                color =  temp; //user pressed cancel aw 7aga
            
            if ( selectedPaintMode.equals("Select") && selectedShapeIndex != -1)
            {
                command = new FillColorCommand(currentShape, color);
                command.execute();
                addCommand(command);
                view.repaint();
            }
            else
            {
                System.out.println("Is current = null" + currentShape);
                System.out.println("Is selectedShapeindex = -1" + selectedShapeIndex);
            }
            
            
            fillColor = color;
            view.getFillColorButton().setBackground(color);
        }
    }
    
    class selectButtonAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
                selectedPaintMode = "Select";
            }
        }
    class RemoveButtonAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            command = new RemoveCommand(currentShape);
            command.execute();
            addCommand(command);
            resize = false;
            disableSelectionButtons();
        }        
    } 
    
    class CloneButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                command = new CopyCommand(currentShape);
                command.execute();
                addCommand(command);
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } 
    
    class fillToggleButtonAction implements ActionListener {
        //TO-DO: make this change the boolean inside current shape using the command
        @Override
        public void actionPerformed(ActionEvent e) {
            JToggleButton button = view.getFillToggleButton();
            if( button.isSelected() )
            {
                view.getFillColorButton().setEnabled(true);
                fillOrNot = true;
                System.out.println("ENABLE");
            }
            else
            {
                view.getFillColorButton().setEnabled(false);
                fillOrNot = false;
                System.out.println("DISABLE");
            }
            if( currentShape != null )
            {
                command = new FillOrNotCommand(currentShape, fillOrNot);
                executeCommand(command);
            }
        }
    }
    
    class ResizeButtonAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if ( view.getResizeButton().isSelected())
            {
                resize = true;
            }
            else
            {
                resize = false;
            }
        }   
    }

    public void setButtonActions()
    {
        view.getRectangleButton().addActionListener(new shapesAction());
        view.getCircleButton().addActionListener(new shapesAction());
        view.getSquareButton().addActionListener(new shapesAction());
        view.getElipseButton().addActionListener(new shapesAction());
        view.getLineButton().addActionListener(new shapesAction());
        view.getTriangleButton().addActionListener(new shapesAction());

        view.getUndoButton().addActionListener(new UndoButtonClass());
        
        view.getRedoButton().addActionListener(new RedoButtonClass());

        view.getSelectButton().addActionListener(new selectButtonAction());

        view.getRemoveButton().addActionListener(new RemoveButtonAction());
        view.getCloneButton().addActionListener(new CloneButtonAction());
        view.getFillToggleButton().addActionListener(new fillToggleButtonAction());
        view.getBorderColorButton().addActionListener( new borderColorButtonAction());
        view.getFillColorButton().addActionListener( new fillColorButtonAction());
        view.getFillColorButton().setEnabled(false);
        view.getResizeButton().addActionListener(new ResizeButtonAction());
        

        view.getXmlLoadButton().addActionListener(new XmlLoadButtonAction());
        view.getXmlSaveButton().addActionListener(new XmlSaveButtonAction());
        view.getJsonSaveButton().addActionListener(new JsonSaveButtonAction());
        view.getJsonLoadButton().addActionListener(new JsonLoadButton());
        disableSelectionButtons();
        view.getUndoButton().setEnabled(false);
        view.getRedoButton().setEnabled(false);
    }
    
    public static Shape shapeFactory(String type){
        Shape shape = null;
        
        if(type.equalsIgnoreCase("rectangle"))
            shape = new Rectangle();
        if(type.equalsIgnoreCase("square"))
            shape = new Square();
        if(type.equalsIgnoreCase("circle"))
            shape = new Circle();
        if(type.equalsIgnoreCase("line"))
            shape = new LineSegment();
        if(type.equalsIgnoreCase("triangle"))
            shape = new Triangle();
        if(type.equalsIgnoreCase("ellipse"))
            shape = new Ellipse();
        return shape;
    }
    
    class XmlSaveButtonAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Context context = new Context(new XmlReader(Controller.this));
            context.executeWrite();
        }
        
    }
    class XmlLoadButtonAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            Context context = new  Context(new XmlReader(Controller.this));
            //context.executeRead();
            //System.out.println(listOfShapes.size());
            command = new LoadCommand(context);
            command.execute();
            addCommand(command);
            view.repaint();
        }
        
    }
    class JsonSaveButtonAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Context context = new Context(new JsonReader(Controller.this));
            context.executeWrite();
            System.out.println("wrote to json");
        }

    }
    class JsonLoadButton implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        
        Context context = new  Context(new JsonReader(Controller.this));
        //context.executeRead();
        //System.out.println(listOfShapes.size());
        command = new LoadCommand(context);
        command.execute();
        addCommand(command);
        view.repaint();
    }
    }
    
    
    public Color getBorderColor() {
        return borderColor;
    }
    public Color getFillColor() {
        return fillColor;
    }
    public View getView() {
        return view;
    }
    public ArrayList<Shape> getListOfShapes() {
        return listOfShapes;
    }
}