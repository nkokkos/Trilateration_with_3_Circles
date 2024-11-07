/**
 *
 * @author Nick Kokkos, nkokkos@gmail.com
 * Trilateration using 3 circles to pinpoint an intersection. 
 */


package trilateration_with_3_circles;

import java.text.DecimalFormat;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;


// References for JavaFX 
/* https://www.tutorialspoint.com/javafx/index.htm     */
/* http://o7planning.org/en/11125/javafx-menu-tutorial */
// http://java-buddy.blogspot.gr/2012/02/javafx-20-exercise-draw-circle-and.html
// https://examples.javacodegeeks.com/desktop-java/javafx/javafx-borderpane-example/
// http://zetcode.com/gui/javafx/controlsII/
// https://docs.oracle.com/javafx/2/api/javafx/scene/shape/Line.html
// https://docs.oracle.com/javafx/2/ui_controls/slider.htm

// Implemenation based on: 
// https://stackoverflow.com/questions/19723641/find-intersecting-point-of-three-circles-programmatically
// http://paulbourke.net/geometry/circlesphere/


public class Trilateration_with_3_Circles extends Application {
    
private static final double EPSILON = 0.99;

  @Override 
  public void start(Stage stage) { 
        // Create MenuBar
       
        MenuBar menuBar = new MenuBar();
        
        // Create menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        
        // Create MenuItems
        MenuItem newItem = new MenuItem("Trilateration 3 circles");
        MenuItem newItem2 = new MenuItem("Triangulation 2 points");
        
        MenuItem exitItem = new MenuItem("Exit");       
        MenuItem clearItem = new MenuItem("Clear");
        
        // Add menuItems to the Menus
        fileMenu.getItems().addAll(newItem,newItem2, exitItem);
        editMenu.getItems().addAll(clearItem);
        
        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu);
        
        BorderPane root = new BorderPane();
        HBox statusbar = new HBox();
        
        root.setTop(menuBar);
        root.setBottom(statusbar);
        
        statusbar.setStyle("-fx-padding: 10;" + 
                      "-fx-border-style: solid inside;" + 
                      "-fx-border-width: 2;" +
                      "-fx-border-insets: 5;" + 
                      "-fx-border-radius: 5;" + 
                      "-fx-border-color: blue;");
         Text t = new Text();
         t.setText("Choose command from File Menu");           
         statusbar.getChildren().add(t);
        //statusbar.getChildren().add(t);
         
        Scene scene = new Scene(root, 500, 500);
        
        stage.setTitle("Trilateration");
        stage.setScene(scene);
        stage.show();
        
         // When user selects the Coordinates Command.
        newItem.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            showSliderScreen(root,menuBar,statusbar);            
          }
        });
        
        // When user selects the Coordinates Command.
        newItem2.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            showTriangulationScreen(root,menuBar,statusbar);            
          }
        });
        
        clearItem.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
           root.getChildren().clear();
           root.setTop(menuBar);
           //root.setBottom(statusbar);
        
        statusbar.setStyle("-fx-padding: 10;" + 
                      "-fx-border-style: solid inside;" + 
                      "-fx-border-width: 2;" +
                      "-fx-border-insets: 5;" + 
                      "-fx-border-radius: 5;" + 
                      "-fx-border-color: blue;");
         Text t = new Text();
         t.setText("Επιλέξτε διαδικασία απο το File Μενού");           
         statusbar.getChildren().add(t);
          }
        });
        
        // When user click on the Exit item.
        exitItem.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            System.exit(0);
          }
        });
        
        
        
        
        
    }
  
  
  public void computeAndUpdateforTwoPoints(Slider x1,
                                           Slider y1,
                                           Slider theta1, 
                                           Slider x2,
                                           Slider y2,
                                           Slider theta2,
                                           BorderPane primaryroot,
                                           MenuBar menubar,HBox statusBar) 
  {
      
            Double valuex1 = x1.valueProperty().getValue();
            Double valuey1 = y1.valueProperty().getValue();
            Double valuetheta1 = theta1.valueProperty().getValue();
            
            Double valuex2 =  x2.valueProperty().getValue();
            Double valuey2 =  y2.valueProperty().getValue();
            Double valuetheta2 = theta2.valueProperty().getValue();
            
            Circle circle1 = new Circle(valuex1, valuey1, 10);       
            circle1.setFill(Color.BLACK);
            circle1.setStroke(Color.BLACK);
            
            Circle circle2 = new Circle(valuex2, valuey2, 10);       
            circle2.setFill(Color.BLACK);
            circle2.setStroke(Color.BLACK);
             
            // https://en.wikipedia.org/wiki/Triangulation_(surveying)
            
            // double valuex2_x1 = Math.toRadians(valuex2-valuex1);
            // double valuey2_y1 = Math.toRadians(valuey2-valuey1);
            
            // convert to screen from cartesian coordinates
            // screenX = cartX + screen_width/2
            // screenY = screen_height/2 - cartY
            
            double valuex2_x1 = valuex2 - valuex1;
            double valuey2_y1 = valuey1 - valuey2;
            
            //double valuex2_x1 = (valuex2 + 500/2) - (valuex1+500/2);
            //double valuey2_y1 = (500/2) - valuey1 - (500/2) - valuey2;
            
            double theta1InRadians = Math.toRadians(valuetheta1);
            double theta2InRadians = Math.toRadians(valuetheta2);
            
           // Calculate distance between c1 και c2 (reference)
           // https://en.wikipedia.org/wiki/Distance
            double distanceOfTwoPoints = Math.sqrt(Math.pow((valuex2_x1), 2) + Math.pow((valuey2_y1), 2));
            
            System.out.println("distanceOfTwoPoints:");
            System.out.println(distanceOfTwoPoints);
  
            // Calculate distance to point D according to https://en.wikipedia.org/wiki/Triangulation_(surveying)
            // d = L * sin(a) * sin(b) / sin(a+b)
            double distanceToPoint = distanceOfTwoPoints * Math.sin(theta1InRadians) * Math.sin(theta2InRadians) / Math.sin(theta1InRadians+theta2InRadians);
            
            System.out.println("distanceToPoint:");
            System.out.println(distanceToPoint);
      
            //degrees * Math.PI / 180 -> convert to radians
            
            double r = distanceToPoint / Math.sin(theta1InRadians);
                       
            //(x + b·sin(θ), y-b·cos(θ)) this does not work, do not use
            
            
            double x = r * Math.cos(theta1InRadians);
            double y = r * Math.sin(theta1InRadians);
            
            //double x = valuex1 + distanceOfTwoPoints*Math.sin(Math.toRadians(valuetheta1));
            //double y = valuey1 + distanceOfTwoPoints*Math.cos(Math.toRadians(valuetheta1));
            
            //double x2new = valuex2 + distanceOfTwoPoints*Math.sin(Math.toRadians(valuetheta2));
            //double y2new = valuey2 + distanceOfTwoPoints*Math.sin(Math.toRadians(valuetheta2)); 
            

            System.out.println("x,y");
            System.out.println(x + "," + y);
            
            Circle circle3 = new Circle(x,y,15);
            circle3.setFill(Color.GREEN);
            circle3.setStroke(Color.GREEN);
            
            Text c1Text = new Text();
            
            c1Text.setTranslateX(valuex1+15);
            c1Text.setTranslateY(valuey1+15);
            c1Text.setText("c1");
            
            Text c2Text = new Text();
            
            c2Text.setTranslateX(valuex2+15);
            c2Text.setTranslateY(valuey2+15);
            c2Text.setText("c2");
      
            Text lDistanceToPoint = new Text();
            lDistanceToPoint.setTranslateX(20);
            lDistanceToPoint.setTranslateY(480);
            lDistanceToPoint.setText("Απόσταση d: " + Double.toString(distanceToPoint));
            
            Text lGraphPoint = new Text();
            lGraphPoint.setTranslateX(x+15);
            lGraphPoint.setTranslateY(y+15);
            
            DecimalFormat df = new DecimalFormat("#.##");
            
            String xNew = df.format(x).toString();
            String yNew = df.format(y).toString();
            
            
            lGraphPoint.setText("x:" + xNew + "  " + "y:"+yNew);
            
            
         
            Line line1 = new Line();
            line1.setStartX(valuex1);
            line1.setStartY(valuey1);
            line1.setEndX(valuex2);
            line1.setEndY(valuey2);
            
            Line line2 = new Line();
            line2.setStartX(valuex2);
            line2.setStartY(valuey2);
            line2.setEndX(x);
            line2.setEndY(y);
            
            Line line3 = new Line();
            line3.setStartX(valuex1);
            line3.setStartY(valuey1);
            line3.setEndX(x);
            line3.setEndY(y);
        
            //Creating a Group object 
            primaryroot.getChildren().clear();
            primaryroot.setTop(menubar);
            //primaryroot.setBottom(statusBar); 
            
            //primaryroot.getChildren().add(menubar);
            //primaryroot.getChildren().addAll(circle1,circle2,circle3,circle4,
                                            //line1,line2,line3);
            primaryroot.getChildren().addAll(circle1,circle2,circle3,
                                            line1,line2,line3,c1Text,c2Text,
                                            lDistanceToPoint,lGraphPoint); 
            
  }

  /* Trilateration implementation using 3 circles */
  public void computeAndUpdate(Slider x1, Slider y1,Slider r1,
                                   Slider x2, Slider y2,Slider r2,
                                   Slider x3, Slider y3,Slider r3,
                                   BorderPane primaryroot, 
                                   MenuBar menubar, HBox statusBar) 
{
    
    
            Double valuex1 = x1.valueProperty().getValue();
            Double valuey1 = y1.valueProperty().getValue();
            Double valuer1 = r1.valueProperty().getValue();
            
            Double valuex2 = x2.valueProperty().getValue();
            Double valuey2 = y2.valueProperty().getValue();
            Double valuer2 = r2.valueProperty().getValue();
            
            Double valuex3 = x3.valueProperty().getValue();
            Double valuey3 = y3.valueProperty().getValue();
            Double valuer3 = r3.valueProperty().getValue();
            
                        
            Circle circle1 = new Circle(valuex1, valuey1, valuer1);       
            circle1.setFill(Color.TRANSPARENT);
            circle1.setStroke(Color.BLACK);
            
            Circle circle2 = new Circle(valuex2, valuey2, valuer2);       
            circle2.setFill(Color.TRANSPARENT);
            circle2.setStroke(Color.BLACK);
            
            Circle circle3 = new Circle(valuex3, valuey3, valuer3);       
            circle3.setFill(Color.TRANSPARENT);
            circle3.setStroke(Color.BLACK);
             
            Line line1 = new Line();
            line1.setStartX(0.0f);
            line1.setStartY(25.0f);
            line1.setEndX(0.0f);
            line1.setEndY(500.0f);
             
            Line line2 = new Line();
            line2.setStartX(0.0f);
            line2.setStartY(25.0f);
            line2.setEndX(500.0f);
            line2.setEndY(25.0f);
                           
            //Creating a Group object 
            primaryroot.getChildren().clear();
            primaryroot.setTop(menubar);
            primaryroot.setBottom(statusBar);
            
            
            Text r1Text = new Text();
            
            r1Text.setTranslateX(valuex1);
            r1Text.setTranslateY(valuey1);
            r1Text.setText("r1");
            
            Text r2Text = new Text();
            
            r2Text.setTranslateX(valuex2);
            r2Text.setTranslateY(valuey2);
            r2Text.setText("r2");
            
            Text r3Text = new Text();
            
            r3Text.setTranslateX(valuex3);
            r3Text.setTranslateY(valuey3);
            r3Text.setText("r3");
           
            
            //primaryroot.getChildren().add(menubar);
            primaryroot.getChildren().addAll(circle1,circle2,circle3,line1,line2,r1Text,r2Text,r3Text);
            Text t = new Text();

            if (calculateThreeCircleIntersection(valuex1, valuey1, valuer1,
                                                 valuex2, valuey2, valuer2,
                                                 valuex3, valuey3, valuer3)[3].equals("true")) {
                

            String textToShow = calculateThreeCircleIntersection(valuex1, valuey1, valuer1,
                                                 valuex2, valuey2, valuer2,
                                                 valuex3, valuey3, valuer3)[0] +
                    ",x:" + calculateThreeCircleIntersection(valuex1, valuey1, valuer1,
                                                 valuex2, valuey2, valuer2,
                                                 valuex3, valuey3, valuer3)[1] +
                    " y:" + calculateThreeCircleIntersection(valuex1, valuey1, valuer1,
                                                 valuex2, valuey2, valuer2,
                                                 valuex3, valuey3, valuer3)[2];
                    
                
            t.setText(textToShow);
            
            String foundPointxString = calculateThreeCircleIntersection(valuex1, valuey1, valuer1,
                                                 valuex2, valuey2, valuer2,
                                                 valuex3, valuey3, valuer3)[1];

            String foundPointyString = calculateThreeCircleIntersection(valuex1, valuey1, valuer1,
                                                 valuex2, valuey2, valuer2,
                                                 valuex3, valuey3, valuer3)[2];

         
            Circle foundPoint = new Circle(Double.parseDouble(foundPointxString),
                                           Double.parseDouble(foundPointyString),15);

            foundPoint.setFill(Color.GREEN);
            foundPoint.setStroke(Color.GREEN);
            
            Circle circle1center = new Circle(valuex1, valuey1, 8);
            
            circle1center.setFill(Color.GREEN);
            circle1center.setStroke(Color.GREEN);

            Circle circle2center = new Circle(valuex2, valuey2, 8);
            circle2center.setFill(Color.GREEN);
            circle2center.setStroke(Color.GREEN);
            
            Circle circle3center = new Circle(valuex3, valuey3, 8);
            circle3center.setFill(Color.GREEN);
            circle3center.setStroke(Color.GREEN);

           
            Line line1foundPoint = new Line();
            line1foundPoint.setStartX(valuex1);
            line1foundPoint.setStartY(valuey1);
            line1foundPoint.setEndX(Double.parseDouble(foundPointxString));
            line1foundPoint.setEndY(Double.parseDouble(foundPointyString));
            line1foundPoint.setStroke(Color.GREEN);
            
            Line line2foundPoint = new Line();
            line2foundPoint.setStartX(valuex2);
            line2foundPoint.setStartY(valuey2);
            line2foundPoint.setEndX(Double.parseDouble(foundPointxString));
            line2foundPoint.setEndY(Double.parseDouble(foundPointyString));
            line2foundPoint.setStroke(Color.GREEN);
            
            Line line3foundPoint = new Line();
            line3foundPoint.setStartX(valuex3);
            line3foundPoint.setStartY(valuey3);
            line3foundPoint.setEndX(Double.parseDouble(foundPointxString));
            line3foundPoint.setEndY(Double.parseDouble(foundPointyString));
            line3foundPoint.setStroke(Color.GREEN);
            
          
            statusBar.getChildren().clear();
            statusBar.setStyle("-fx-padding: 10;" + 
                      "-fx-border-style: solid inside;" + 
                      "-fx-border-width: 2;" +
                      "-fx-border-insets: 5;" + 
                      "-fx-border-radius: 5;" + 
                      "-fx-border-color: green;");
            
            primaryroot.getChildren().addAll(foundPoint,
                                             circle1center,
                                             circle2center,
                                             circle3center,
                                             line1foundPoint,
                                             line2foundPoint,
                                             line3foundPoint);
            
            statusBar.getChildren().add(t);
         } else {
            System.out.println(calculateThreeCircleIntersection(valuex1, valuey1, valuer1,
                                                 valuex2, valuey2, valuer2,
                                                 valuex3, valuey3, valuer3)[0]);
            
                
            t.setText(calculateThreeCircleIntersection(valuex1, valuey1, valuer1,
                                                 valuex2, valuey2, valuer2,
                                                 valuex3, valuey3, valuer3)[0]);
            
            statusBar.getChildren().clear();
            statusBar.setStyle("-fx-padding: 10;" + 
                      "-fx-border-style: solid inside;" + 
                      "-fx-border-width: 2;" +
                      "-fx-border-insets: 5;" + 
                      "-fx-border-radius: 5;" + 
                      "-fx-border-color: red;");  
            
            statusBar.getChildren().add(t);

            }     

}
 
  
// Implementation based on the following link:  
// https://stackoverflow.com/questions/19723641/find-intersecting-point-of-three-circles-programmatically
  
 public static String[] calculateThreeCircleIntersection(double x0, double y0, double r0,
                                                 double x1, double y1, double r1,
                                                 double x2, double y2, double r2)
{


    double a, dx, dy, d, h, rx, ry;
    double point2_x, point2_y;
    
    /* dx and dy are the vertical and horizontal distances between
    * the circle centers.
    */
    dx = x1 - x0;
    dy = y1 - y0;

    /* Determine the straight-line distance between the centers. */
    d = Math.sqrt((dy*dy) + (dx*dx));

    /* Check for solvability. */
    if (d > (r0 + r1))
    {
        /* no solution. circles do not intersect. */
        return new String[] {"Circles do not intersect","none","none","false"};    
    }
    if (d < Math.abs(r0 - r1))
    {
        /* no solution. one circle is contained in the other */
        return new String[] { "one circle is contained in the other","none","none","false"};
    }

    /* 'point 2' is the point where the line through the circle
    * intersection points crosses the line between the circle
    * centers.
    */

    /* Determine the distance from point 0 to point 2. */
    a = ((r0*r0) - (r1*r1) + (d*d)) / (2.0 * d) ;

    /* Determine the coordinates of point 2. */
    point2_x = x0 + (dx * a/d);
    point2_y = y0 + (dy * a/d);

    /* Determine the distance from point 2 to either of the
    * intersection points.
    */
    h = Math.sqrt((r0*r0) - (a*a));

    /* Now determine the offsets of the intersection points from
    * point 2.
    */
    rx = -dy * (h/d);
    ry = dx * (h/d);

    /* Determine the absolute intersection points. */
    double intersectionPoint1_x = point2_x + rx;
    double intersectionPoint2_x = point2_x - rx;
    double intersectionPoint1_y = point2_y + ry;
    double intersectionPoint2_y = point2_y - ry;

    //Log.d("INTERSECTION Circle1 AND Circle2:", "(" + intersectionPoint1_x + "," + intersectionPoint1_y + ")" + " AND (" + intersectionPoint2_x + "," + intersectionPoint2_y + ")");

    /* Lets determine if circle 3 intersects at either of the above intersection points. */
    dx = intersectionPoint1_x - x2;
    dy = intersectionPoint1_y - y2;
    double d1 = Math.sqrt((dy*dy) + (dx*dx));

    dx = intersectionPoint2_x - x2;
    dy = intersectionPoint2_y - y2;
    double d2 = Math.sqrt((dy*dy) + (dx*dx));

    System.out.println(d1-r2);
    System.out.println(d2-r2);
    
    if(Math.abs(d1 - r2) < EPSILON) {
      return new String[] {"INTERSECTION Circle1 AND Circle2 AND Circle3:", Double.toString(intersectionPoint1_x),Double.toString(intersectionPoint1_y), "true"};
    }
    else if(Math.abs(d2 - r2) < EPSILON) {
      return new String[] {"INTERSECTION Circle1 AND Circle2 AND Circle3:",Double.toString(intersectionPoint2_x),Double.toString(intersectionPoint2_y), "true"};
    }
    else {
      return new String[] {"INTERSECTION Circle1 AND Circle2 AND Circle3:","NONE", "NONE", "false"};
    }
}
 
 // Starter Form with sliders for Triangulation Calculation 
 public void showTriangulationScreen(BorderPane primaryroot, MenuBar menubar, HBox statusBar) {
     
     Stage stage = new Stage();
     
     //statusBar.getChildren().add(new Button(""));
        
        VBox box = new VBox();
        box.setPadding(new Insets(15));
        //box.setAlignment(Pos.CENTER);

        Label label = new Label("Enter coordinates (x,y) and angle r");
        
        
        /* ############################### x1,y1, theta1 ########################### */
        
        Label lx1 = new Label("x1:");
        Slider x1 = new Slider();
        x1.setMin(25);
        x1.setMax(450);
        x1.setValue(125);
        x1.setShowTickLabels(true);
        x1.setShowTickMarks(true);
        x1.setMajorTickUnit(50);
        x1.setMinorTickCount(5);
        x1.setBlockIncrement(1);
        
        Label ly1 = new Label("y1:");
        Slider y1 = new Slider();
        y1.setMin(25);
        y1.setMax(450);
        y1.setValue(175);
        y1.setShowTickLabels(true);
        y1.setShowTickMarks(true);
        y1.setMajorTickUnit(50);
        y1.setMinorTickCount(5);
        y1.setBlockIncrement(1);
     
        Label ltheta1 = new Label("Θ1:");
        Slider theta1 = new Slider();
        theta1.setMin(0);
        theta1.setMax(180);
        theta1.setValue(65);
        theta1.setShowTickLabels(true);
        theta1.setShowTickMarks(true);
        theta1.setMajorTickUnit(50);
        theta1.setMinorTickCount(5);
        theta1.setBlockIncrement(1);
     
        /* ############################### x2,y2, theta2 ########################### */
        
        Label lx2 = new Label("x2:");
        Slider x2 = new Slider();
        x2.setMin(25);
        x2.setMax(450);
        x2.setValue(75);
        x2.setShowTickLabels(true);
        x2.setShowTickMarks(true);
        x2.setMajorTickUnit(50);
        x2.setMinorTickCount(5);
        x2.setBlockIncrement(1);
        
        Label ly2 = new Label("y2:");
        Slider y2 = new Slider();
        y2.setMin(25);
        y2.setMax(450);
        y2.setValue(185);
        y2.setShowTickLabels(true);
        y2.setShowTickMarks(true);
        y2.setMajorTickUnit(50);
        y2.setMinorTickCount(5);
        y2.setBlockIncrement(1);
     
     
        Label ltheta2 = new Label("Θ2:");
        Slider theta2 = new Slider();
        theta2.setMin(0);
        theta2.setMax(180);
        theta2.setValue(88);
        theta2.setShowTickLabels(true);
        theta2.setShowTickMarks(true);
        theta2.setMajorTickUnit(50);
        theta2.setMinorTickCount(5);
        theta2.setBlockIncrement(1);
        
        
        // Handle Slider value change events.
          x1.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdateforTwoPoints(x1,y1,theta1, x2,y2,theta2,primaryroot,menubar,statusBar);
          });
          y1.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdateforTwoPoints(x1,y1,theta1, x2,y2,theta2,primaryroot,menubar,statusBar);
          });
          theta1.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdateforTwoPoints(x1,y1,theta1, x2,y2,theta2,primaryroot,menubar,statusBar);
          });
          x2.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdateforTwoPoints(x1,y1,theta1, x2,y2,theta2,primaryroot,menubar,statusBar);
          });
          y2.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdateforTwoPoints(x1,y1,theta1, x2,y2,theta2,primaryroot,menubar,statusBar);
          });
          theta2.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdateforTwoPoints(x1,y1,theta1, x2,y2,theta2,primaryroot,menubar,statusBar);
          });
          
       
        box.getChildren().add(lx1);
        box.getChildren().add(x1);
        box.getChildren().add(ly1);
        box.getChildren().add(y1);
        box.getChildren().add(ltheta1);
        box.getChildren().add(theta1);
        
        box.getChildren().add(lx2);
        box.getChildren().add(x2);
        box.getChildren().add(ly2);
        box.getChildren().add(y2);
        box.getChildren().add(ltheta2);
        box.getChildren().add(theta2);
        
        Scene scene = new Scene(box, 300, 360);
        stage.setScene(scene);
        stage.show();
     
 }
 
 
   // for Trilateration slider screen
   public void showSliderScreen(BorderPane primaryroot, MenuBar menubar, HBox statusBar) {
        
        Stage stage = new Stage();
        
        VBox box = new VBox();
        box.setPadding(new Insets(15));
        //box.setAlignment(Pos.CENTER);

        Label label = new Label("Enter coordinates (x,y) and distance r");
               
        /* ############################### x1,y1, r1 ########################### */
        
        Label lx1 = new Label("x1:");
        Slider x1 = new Slider();
        x1.setMin(25);
        x1.setMax(400);
        x1.setValue(319);
        x1.setShowTickLabels(true);
        x1.setShowTickMarks(true);
        x1.setMajorTickUnit(50);
        x1.setMinorTickCount(5);
        x1.setBlockIncrement(1);
        
        Label ly1 = new Label("y1:");
        Slider y1 = new Slider();
        y1.setMin(120);
        y1.setMax(400);
        y1.setValue(302);
        y1.setShowTickLabels(true);
        y1.setShowTickMarks(true);
        y1.setMajorTickUnit(50);
        y1.setMinorTickCount(5);
        y1.setBlockIncrement(1);
        
        Label lr1 = new Label("distance r1:");
        Slider r1 = new Slider();
        r1.setMin(1);
        r1.setMax(100);
        r1.setValue(60);
        r1.setShowTickLabels(true);
        r1.setShowTickMarks(true);
        r1.setMajorTickUnit(50);
        r1.setMinorTickCount(5);
        r1.setBlockIncrement(1);
        
        /* ############################### x2,y2, r2 ########################### */       
        Label lx2 = new Label("x2:");
        Slider x2 = new Slider();
        x2.setMin(25);
        x2.setMax(400);
        x2.setValue(266);
        x2.setShowTickLabels(true);
        x2.setShowTickMarks(true);
        x2.setMajorTickUnit(50);
        x2.setMinorTickCount(5);
        x2.setBlockIncrement(1);
             
        Label ly2 = new Label("y2:");
        Slider y2 = new Slider();
        y2.setMin(25);
        y2.setMax(400);
        y2.setValue(175);
        y2.setShowTickLabels(true);
        y2.setShowTickMarks(true);
        y2.setMajorTickUnit(50);
        y2.setMinorTickCount(5);
        y2.setBlockIncrement(1);
        
        Label lr2 = new Label("distance r2:");
        Slider r2 = new Slider();
        r2.setMin(1);
        r2.setMax(100);
        r2.setValue(70);
        r2.setShowTickLabels(true);
        r2.setShowTickMarks(true);
        r2.setMajorTickUnit(50);
        r2.setMinorTickCount(5);
        r2.setBlockIncrement(1);
        
        
        /* ############################### x3,y3, r3 ########################### */       
        Label lx3 = new Label("x3:");
        Slider x3 = new Slider();
        x3.setMin(25);
        x3.setMax(400);
        x3.setValue(64);
        x3.setShowTickLabels(true);
        x3.setShowTickMarks(true);
        x3.setMajorTickUnit(50);
        x3.setMinorTickCount(5);
        x3.setBlockIncrement(1);
             
        Label ly3 = new Label("y3:");
        Slider y3 = new Slider();
        y3.setMin(25);
        y3.setMax(400);
        y3.setValue(292);
        y3.setShowTickLabels(true);
        y3.setShowTickMarks(true);
        y3.setMajorTickUnit(50);
        y3.setMinorTickCount(5);
        y3.setBlockIncrement(1);
        
        Label lr3 = new Label("distance r3:");
        Slider r3 = new Slider();
        r3.setMin(1);
        r3.setMax(100);
        r3.setValue(64);
        r3.setShowTickLabels(true);
        r3.setShowTickMarks(true);
        r3.setMajorTickUnit(50);
        r3.setMinorTickCount(5);
        r3.setBlockIncrement(1);
        
        // Handle Slider value change events.
          x1.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdate(x1,y1,r1, x2,y2,r2,x3,y3,r3,primaryroot,menubar,statusBar);
          });
        // Handle Slider value change events.
          y1.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdate(x1,y1,r1, x2,y2,r2,x3,y3,r3,primaryroot,menubar,statusBar);
          });
          // Handle Slider value change events.
          r1.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdate(x1,y1,r1, x2,y2,r2,x3,y3,r3,primaryroot,menubar,statusBar);
          });
          // Handle Slider value change events.
          x2.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdate(x1,y1,r1, x2,y2,r2,x3,y3,r3,primaryroot,menubar,statusBar);
          });
          // Handle Slider value change events.
          y2.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdate(x1,y1,r1, x2,y2,r2,x3,y3,r3,primaryroot,menubar,statusBar);
          });
          // Handle Slider value change events.
          r2.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdate(x1,y1,r1, x2,y2,r2,x3,y3,r3,primaryroot,menubar,statusBar);
          });
          // Handle Slider value change events.
          x3.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdate(x1,y1,r1, x2,y2,r2,x3,y3,r3,primaryroot,menubar,statusBar);
          });
          // Handle Slider value change events.
          y3.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdate(x1,y1,r1, x2,y2,r2,x3,y3,r3,primaryroot,menubar,statusBar);
          });
          // Handle Slider value change events.
          r3.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
            computeAndUpdate(x1,y1,r1, x2,y2,r2,x3,y3,r3,primaryroot,menubar,statusBar);
          });
             
        Button btnLogin = new Button();
        btnLogin.setText("Information");
             
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
            
            Double valuex1 = x1.valueProperty().getValue();
            Double valuey1 = y1.valueProperty().getValue();
            Double valuer1 = r1.valueProperty().getValue();
            
            Double valuex2 = x2.valueProperty().getValue();
            Double valuey2 = y2.valueProperty().getValue();
            Double valuer2 = r2.valueProperty().getValue();
            
            Double valuex3 = x3.valueProperty().getValue();
            Double valuey3 = y3.valueProperty().getValue();
            Double valuer3 = r3.valueProperty().getValue();
            
                        
            Circle circle1 = new Circle(valuex1, valuey1, valuer1);       
            circle1.setFill(Color.TRANSPARENT);
            circle1.setStroke(Color.BLACK);
            
            Circle circle2 = new Circle(valuex2, valuey2, valuer2);       
            circle2.setFill(Color.TRANSPARENT);
            circle2.setStroke(Color.BLACK);
            
            Circle circle3 = new Circle(valuex3, valuey3, valuer3);       
            circle3.setFill(Color.TRANSPARENT);
            circle3.setStroke(Color.BLACK);
             
            Line line1 = new Line();
            line1.setStartX(0.0f);
            line1.setStartY(25.0f);
            line1.setEndX(0.0f);
            line1.setEndY(500.0f);
             
            Line line2 = new Line();
            line2.setStartX(0.0f);
            line2.setStartY(25.0f);
            line2.setEndX(500.0f);
            line2.setEndY(25.0f);
                           
            //Creating a Group object 
            primaryroot.getChildren().clear();
            primaryroot.setTop(menubar);
            primaryroot.setBottom(statusBar);
            
            //primaryroot.getChildren().add(menubar);
            primaryroot.getChildren().addAll(circle1,circle2,circle3,line1,line2);
           
            
            statusBar.getChildren().clear();
            statusBar.setStyle("-fx-padding: 10;" + 
                      "-fx-border-style: solid inside;" + 
                      "-fx-border-width: 2;" +
                      "-fx-border-insets: 5;" + 
                      "-fx-border-radius: 5;" + 
                      "-fx-border-color: blue;");
            
            Text t = new Text();
            t.setText("Move the sliders to place the circles onto the area");
            statusBar.getChildren().add(t);
            
            }
                      
        });
      
      
        box.getChildren().add(label);
        
        box.getChildren().add(lx1);
        box.getChildren().add(x1);
        
        box.getChildren().add(ly1);
        box.getChildren().add(y1);
        
        box.getChildren().add(lr1);
        box.getChildren().add(r1);
        
        box.getChildren().add(lx2);
        box.getChildren().add(x2);
        
        box.getChildren().add(ly2);
        box.getChildren().add(y2);
        
        box.getChildren().add(lr2);
        box.getChildren().add(r2);
        
        box.getChildren().add(lx3);
        box.getChildren().add(x3);
        
        box.getChildren().add(ly3);
        box.getChildren().add(y3);
        
        box.getChildren().add(lr3);
        box.getChildren().add(r3);
        
        box.getChildren().add(btnLogin);
        
        Scene scene = new Scene(box, 350, 580);
        stage.setScene(scene);
        stage.show();
    }
  
  
    public static void main(String[] args) {
        Application.launch(args);
    }
}
    