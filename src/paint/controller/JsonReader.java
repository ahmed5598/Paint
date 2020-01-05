package paint.controller;
import java.awt.Color;
import java.awt.Point;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.*;
import paint.model.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Ahmed
 */
public class JsonReader implements FilesManagement{
    Controller controller;
    Point p1 = new Point();
    Point p2 = new Point();
    Point offset = new Point();

    public JsonReader(Controller controller) {
        this.controller = controller;
    }
    @Override
    public void write() {
        JSONObject shapeDetails = null;
        JSONArray employeeList = new JSONArray();
        String type = "";
        for(int i = 0; i < controller.getListOfShapes().size(); i++){
               if(controller.getListOfShapes().get(i) instanceof Rectangle)
                    type = "rectangle";
               if(controller.getListOfShapes().get(i) instanceof Square)
                    type = "square";
               if(controller.getListOfShapes().get(i) instanceof Triangle)
                    type = "triangle";
               if(controller.getListOfShapes().get(i) instanceof Circle)
                    type = "circle";
               if(controller.getListOfShapes().get(i) instanceof Ellipse)
                    type = "ellipse";
               if(controller.getListOfShapes().get(i) instanceof LineSegment)
                    type = "line";
            shapeDetails = new JSONObject();
            shapeDetails.put("type",type);
            String x1 = String.format("%d", controller.getListOfShapes().get(i).getStartPos().x);
            String y1 = String.format("%d", controller.getListOfShapes().get(i).getStartPos().y);
            String x2 = String.format("%d", controller.getListOfShapes().get(i).getEndPos().x);
            String y2 = String.format("%d", controller.getListOfShapes().get(i).getEndPos().y);
            String o1 = String.format("%d", controller.getListOfShapes().get(i).getOffset().x);
            String o2 = String.format("%d", controller.getListOfShapes().get(i).getOffset().y);
            String color = String.format("%d", controller.getListOfShapes().get(i).getColor().getRGB());
            String fillColor = String.format("%d", controller.getListOfShapes().get(i).getFillColor().getRGB());
            String fill = Boolean.toString(controller.getListOfShapes().get(i).isFillOrNot());
            
            shapeDetails.put("x1", x1);
            shapeDetails.put("y1", y1);
            shapeDetails.put("x2", x2);
            shapeDetails.put("y2", y2);
            shapeDetails.put("o1", o1);
            shapeDetails.put("o2", o2);
            shapeDetails.put("color", color);
            shapeDetails.put("fillColor", fillColor);
            shapeDetails.put("fill", fill);
            JSONObject obj = new JSONObject();
            obj.put("shape", shapeDetails);
            employeeList.add(obj);
        }
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choose a directory to save your file: ");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (jfc.getSelectedFile().isDirectory()) {
				System.out.println("You selected the directory: " + jfc.getSelectedFile());
			}
		}

        
        try
        {
            String x = jfc.getSelectedFile().toString() + "/myJSON.json";
            FileWriter file = new FileWriter(x);
            file.write(employeeList.toJSONString());
            file.flush();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(controller.getView(), "User cancelled save");
        }

        
    }

    @Override
    public void read() {
                //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setFileFilter(new FileNameExtensionFilter("JSON Save files", "json"));
        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);
        File selectedFile = null;
        if (returnValue == JFileChooser.APPROVE_OPTION) {
             selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
        }
       
            FileReader reader = new FileReader(selectedFile.getAbsolutePath());

            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray list = (JSONArray) obj;
            System.out.println(list);

            //Iterate over list
            list.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );

        }
        catch(NullPointerException e)
        {
            JOptionPane.showMessageDialog(controller.getView(), "User cancelled load");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
        public void parseEmployeeObject(JSONObject obj)
    {
        //Get obj object within list
        JSONObject shapeObject = (JSONObject) obj.get("shape");

        String type = (String) shapeObject.get("type");
        Shape shape = Controller.shapeFactory(type);

        
        String x1 =  (String) shapeObject.get("x1");
        String y1 =  (String) shapeObject.get("y1");
        String x2 =  (String) shapeObject.get("x2");
        String y2 =  (String) shapeObject.get("y2");
        String o1 =  (String) shapeObject.get("o1");
        String o2 =  (String) shapeObject.get("o2");
        String color = (String) shapeObject.get("color");
        String fillColor = (String) shapeObject.get("fillColor"); 
        String fill = (String) shapeObject.get("fill"); 
        p1 = new Point();
        p2 = new Point();
        offset = new Point();
        
        p1.x = Integer.parseInt(x1);
        p1.y = Integer.parseInt(y1);
        p2.x = Integer.parseInt(x2);
        p2.y = Integer.parseInt(y2);
        offset.x = Integer.parseInt(o1);
        offset.y = Integer.parseInt(o2);
       
        shape.setStartPos(p1);
        shape.setEndPos(p2);
        shape.setOffset(offset);
        shape.setColor(new Color(Integer.parseInt(color)));
        shape.setFillColor(new Color(Integer.parseInt(fillColor)));
        shape.setFillOrNot(Boolean.parseBoolean(fill));
        shape.calculateBounds();
        controller.addShape(shape);

    }
    
}
