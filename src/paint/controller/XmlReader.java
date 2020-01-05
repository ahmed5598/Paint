/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint.controller;
import java.awt.Color;
import java.awt.Point;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import paint.model.*;


/**
 *
 * @author Ahmed
 */
public class XmlReader implements FilesManagement{
    Controller controller;

    public XmlReader(Controller controller) {
        this.controller = controller;
    }
    
    
    @Override
    public void write() {
        try {
           Document doc = new Document();
           Element theRoot = new Element("shapes");
           doc.setRootElement(theRoot);
           for(int i = 0;i < controller.getListOfShapes().size();i++){
               Element type = new Element("null"); 
               if(controller.getListOfShapes().get(i) instanceof Rectangle)
                    type = new Element("rectangle");
               if(controller.getListOfShapes().get(i) instanceof Square)
                    type = new Element("square");
               if(controller.getListOfShapes().get(i) instanceof Triangle)
                    type = new Element("triangle");
               if(controller.getListOfShapes().get(i) instanceof Circle)
                    type = new Element("circle");
               if(controller.getListOfShapes().get(i) instanceof Ellipse)
                    type = new Element("ellipse");
               if(controller.getListOfShapes().get(i) instanceof LineSegment)
                    type = new Element("line");
               
                Element x1 = new Element("x1");
                Element x2 = new Element("x2");
                Element y1 = new Element("y1");
                Element y2 = new Element("y2");
                Element o1 = new Element("o1");
                Element o2 = new Element("o2");
                Element color = new Element("color");
                Element fillColor = new Element("fillColor");
                Element fill = new Element("isFill");
                
                x1.addContent(new Text(String.format("%d", controller.getListOfShapes().get(i).getStartPos().x)));
                y1.addContent(new Text(String.format("%d", controller.getListOfShapes().get(i).getStartPos().y)));
                x2.addContent(new Text(String.format("%d", controller.getListOfShapes().get(i).getEndPos().x)));
                y2.addContent(new Text(String.format("%d", controller.getListOfShapes().get(i).getEndPos().y)));
                o1.addContent(new Text(String.format("%d", controller.getListOfShapes().get(i).getOffset().x)));
                o2.addContent(new Text(String.format("%d", controller.getListOfShapes().get(i).getOffset().y)));
                color.addContent(new Text(String.format("%d", controller.getListOfShapes().get(i).getColor().getRGB())));
                fillColor.addContent(new Text(String.format("%d", controller.getListOfShapes().get(i).getFillColor().getRGB())));
                fill.addContent(new Text(Boolean.toString(controller.getListOfShapes().get(i).isFillOrNot())));
                type.addContent(x1);
                type.addContent(y1);
                type.addContent(x2);
                type.addContent(y2);
                type.addContent(o1);
                type.addContent(o2);
                type.addContent(color);
                type.addContent(fillColor);
                type.addContent(fill);

                
                theRoot.addContent(type);
                
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
        String x = jfc.getSelectedFile() + "/myXML.xml";
           XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
           xmlOutput.output(doc, new FileOutputStream(new File(x)));

           System.out.println("Wrote to file");
       }
        catch(FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(controller.getView(), "User cancelled save");
        }
        catch (Exception e){
           e.printStackTrace();
       }
        
    }

    @Override
    public void read() {
        SAXBuilder builder = new SAXBuilder();
        try {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        jfc.setFileFilter(filter);
        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);
        File selectedFile = null;
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
        }
            Document readDoc = builder.build(new File(selectedFile.getAbsolutePath()));
            
            Element root = readDoc.getRootElement();
            Shape shape;
            Point p1;
            Point p2;
            Point offset;
            for (Element curEle : root.getChildren()){
                
                shape = Controller.shapeFactory(curEle.getName());
                p1 = new Point();
                p2 = new Point();
                offset = new Point();
                p1.x = Integer.parseInt(curEle.getChildText("x1"));
                p1.y = Integer.parseInt(curEle.getChildText("y1"));
                p2.x = Integer.parseInt(curEle.getChildText("x2"));
                p2.y = Integer.parseInt(curEle.getChildText("y2"));
                offset.x = Integer.parseInt(curEle.getChildText("o1"));
                offset.y = Integer.parseInt(curEle.getChildText("o2"));
                int color = Integer.parseInt(curEle.getChildText("color"));
                int fillColor = Integer.parseInt(curEle.getChildText("fillColor"));
                boolean fill = Boolean.parseBoolean(curEle.getChildText("isFill"));
                
                shape.setStartPos(p1);
                shape.setEndPos(p2);
                shape.setColor(new Color(color));
                shape.setFillColor(new Color(fillColor));
                shape.setFillOrNot(fill);
                shape.setOffset(offset);
                
                shape.calculateBounds();
                
                controller.addShape(shape);
                
            }
        }
        catch(NullPointerException e)
        {
            JOptionPane.showMessageDialog(controller.getView(), "User cancelled load");
        }
        catch (JDOMException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
    }
    
}

