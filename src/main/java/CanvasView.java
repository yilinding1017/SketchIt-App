import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Translate;

import java.util.ArrayList;

public class CanvasView extends Pane implements IView {
    private Model model;

    // List of Shapes (in order)
    public static ArrayList<Shape> shapes;

    static double minX = 30;
    static double minY = 30;
    static double maxX = 900;
    static double maxY = 820;

    double startX, startY;
    static Shape selectedShape = null;
    static int selectedIndex;
    Line createdLine = null;
    Circle createdCircle = null;
    Rectangle createdRec = null;
    static Boolean isTriggered = false;

    CanvasView(Model model) {
        super();
        this.model = model;

        // Add Layout
        shapes = new ArrayList<>();

        this.setMaxWidth(1800);
        this.setPrefWidth(950);
        this.setPrefHeight(830);
        this.setMaxHeight(850);


        this.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.DOTTED, new CornerRadii(2), BorderWidths.DEFAULT)));
        //this.setPadding(new Insets(20,20,20,20));

        this.registerControllers();


        minX = this.getLayoutX()+2;
        minY = this.getLayoutY()+2;
        maxX = 950;
        maxY = 810;
        model.addView(this);
    }

    private void registerControllers() {
        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startX = event.getX();
                startY = event.getY();
                if(model.mode == 1) {
                    // Select Mode
                    for(int i = shapes.size()-1; i >= 0; i--) {
                        if(shapes.get(i).contains(startX,startY)) {
                            isTriggered = true;
                            if(selectedShape != null) {
                                // unmark
                                selectedShape.setEffect(null);
                                selectedShape = null;
                            }
                            selectedShape = shapes.get(i);
                            selectedIndex = i;

                            if(selectedShape.getId() != "Line") model.changeFillColour((Color) selectedShape.getFill());
                            model.changeLineColour((Color) selectedShape.getStroke());
                            model.changeThickness(getThickness(selectedShape));
                            model.changeLineStyle(getStyle(selectedShape));
                            ToolbarView.fillColourPicker.setDisable(false);
                            ToolbarView.lineColourPicker.setDisable(false);
                            ToolbarView.thickness1.setDisable(false);
                            ToolbarView.thickness2.setDisable(false);
                            ToolbarView.thickness3.setDisable(false);
                            ToolbarView.style1.setDisable(false);
                            ToolbarView.style2.setDisable(false);
                            ToolbarView.style3.setDisable(false);
                            // Mark this selected shape
                            selectedShape.setEffect(new DropShadow(20, Color.BLACK));
                            break;
                        }
                    }
                    if(isTriggered == false) {
                        ToolbarView.selectTool.setSelected(false);
                        // unmark the selected shape
                        // change mode
                    }
                } else if(model.mode == 2) {
                    // Erase Mode
                    for(int i = shapes.size()-1; i >= 0; i--) {
                        if(shapes.get(i).contains(startX,startY)) {
                            getChildren().remove(shapes.get(i));
                            shapes.remove(i);
                            break;
                        }
                    }
                } else if(model.mode == 3) {
                    // Sketch Mode
                    isTriggered = true;
                    if(model.shapeType == 1) {
                        // Draw Line
                        createdLine = new Line();
                        createdLine.setId("Line");
                        createdLine.setStartX(startX);
                        createdLine.setStartY(startY);
                        createdLine.setEndX(startX);
                        createdLine.setEndY(startY);

                        if(model.lineStyle != 0 && model.thickness != 0) {
                            createdLine.setStroke(model.lineColour);
                            setThickness(createdLine, model.thickness);
                            setStyle(createdLine, model.lineStyle);
                            shapes.add(createdLine);
                            getChildren().add(createdLine);
                        }
                    } else if(model.shapeType == 2) {
                        // Draw Circle
                        createdCircle = new Circle();
                        createdCircle.setId("Circle");
                        createdCircle.setCenterX(startX);
                        createdCircle.setCenterY(startY);
                        createdCircle.setRadius(0);

                        //if(model.lineStyle != 0 && model.thickness != 0) {
                            createdCircle.setStroke(model.lineColour);
                            createdCircle.setFill(model.fillColour);
                            setThickness(createdCircle, model.thickness);
                            setStyle(createdCircle, model.lineStyle);
                            shapes.add(createdCircle);
                            getChildren().add(createdCircle);
                        //}
                    } else if(model.shapeType == 3) {
                        // Draw Rectangle
                        createdRec = new Rectangle();
                        createdRec.setId("Rectangle");
                        createdRec.setX(startX);
                        createdRec.setY(startY);
                        createdRec.setWidth(0);
                        createdRec.setHeight(0);

                        //if(model.lineStyle != 0 && model.thickness != 0) {
                            createdRec.setStroke(model.lineColour);
                            createdRec.setFill(model.fillColour);
                            setThickness(createdRec, model.thickness);
                            setStyle(createdRec, model.lineStyle);
                            shapes.add(createdRec);
                            getChildren().add(createdRec);
                        //}
                    }
                } else if(model.mode == 4) {
                    // Fill Mode
                    for(int i = shapes.size()-1; i >= 0; i--) {
                        if(shapes.get(i).contains(startX,startY)) {
                            shapes.get(i).setFill(model.fillColour);
                            break;
                        }
                    }
                }
            }
        });


        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isTriggered) {
                    if (model.mode == 1) {
                        double shiftX = 0;
                        double shiftY = 0;
                        double upMax = 0;
                        double dnMax = 0;
                        double leftMax = 0;
                        double rightMax = 0;
                        if(selectedShape.getId()=="Line") {
                            Line selectedLine = (Line) selectedShape;
                            leftMax = Math.max(minX-selectedLine.getEndX(),minX-selectedLine.getStartX());
                            rightMax = Math.min(maxX-selectedLine.getEndX(),maxX-selectedLine.getStartX());
                            upMax = -Math.min(Math.abs(minY-selectedLine.getEndY()),Math.abs(minY-selectedLine.getStartY()));
                            dnMax = Math.min(Math.abs(maxY-selectedLine.getEndY()),Math.abs(maxY-selectedLine.getStartY()));
                        } else if(selectedShape.getId()=="Circle") {
                            Circle selectedCircle = (Circle) selectedShape;
                            leftMax = minX-(selectedCircle.getCenterX()-selectedCircle.getRadius());
                            rightMax = maxX-(selectedCircle.getCenterX()+selectedCircle.getRadius());
                            upMax = minY-(selectedCircle.getCenterY()-selectedCircle.getRadius());
                            dnMax = maxY-(selectedCircle.getCenterY()+selectedCircle.getRadius());
                        } else if(selectedShape.getId()=="Rectangle") {
                            Rectangle selectedRec = (Rectangle) selectedShape;
                            leftMax = minX-selectedRec.getX();
                            rightMax = maxX-(selectedRec.getX()+selectedRec.getWidth());
                            upMax = minY-selectedRec.getY();
                            dnMax = maxY-(selectedRec.getY()+selectedRec.getWidth());
                        }

                        if(event.getX() < minX) {
                            shiftX = leftMax;
                        } else if(event.getX() > maxX) {
                            shiftX = rightMax;
                        } else {
                            shiftX = event.getX()-startX;
                            if(shiftX < 0) shiftX = Math.max(leftMax,shiftX);
                            else if(shiftX > 0) shiftX = Math.min(rightMax,shiftX);
                        }

                        if(event.getY() < minY) {
                            shiftY = upMax;
                        } else if(event.getY() > maxY) {
                            shiftY = dnMax;
                        } else {
                            shiftY = event.getY()-startY;
                            if(shiftY < 0) shiftY = Math.max(upMax,shiftY);
                            else if(shiftY > 0) shiftY = Math.min(dnMax,shiftY);
                        }

                        selectedShape.setTranslateX(shiftX);
                        selectedShape.setTranslateY(shiftY);

                    } else if (model.mode == 3) {
                        if (model.shapeType == 1) {
                            // Line
                            if(event.getX()<minX) {
                                createdLine.setEndX(minX);
                            } else if(event.getX()>maxX) {
                                createdLine.setEndX(maxX);
                            } else {
                                createdLine.setEndX(event.getX());
                            }
                            if(event.getY()<minY) {
                                createdLine.setEndY(minY);
                            } else if(event.getY()>maxY) {
                                createdLine.setEndY(maxY);
                            } else {
                                createdLine.setEndY(event.getY());
                            }
                        } else if (model.shapeType == 2) {
                            // Circle
                            Point2D des = new Point2D(event.getX(), event.getY());
                            double distance = des.distance(startX,startY);
                            distance = Math.min(distance,Math.abs(minX-startX));
                            distance = Math.min(distance,Math.abs(maxX-startX));
                            distance = Math.min(distance,Math.abs(minY-startY));
                            distance = Math.min(distance,Math.abs(maxY-startY));
                            createdCircle.setRadius(distance);
                        } else if(model.shapeType == 3) {
                            double width = event.getX() - startX;
                            if(width < 0) width = 0;
                            double height = event.getY() - startY;
                            if(height < 0) height = 0;
                            createdRec.setWidth(width);
                            createdRec.setHeight(height);
                        }
                    }
                }
            }
        });


        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(model.mode == 1) {
                    //selectedShape.setLayoutX(selectedShape.getLayoutX()+selectedShape.getTranslateX());
                    //selectedShape.setLayoutY(selectedShape.getLayoutY()+selectedShape.getTranslateY());
                    isTriggered = false;
                    /*Translate translation = new Translate(event.getX()-startX,event.getY()-startY);
                    selectedShape.getTransforms().add(translation);*/
                    double xChange = selectedShape.getTranslateX();
                    double yChange = selectedShape.getTranslateY();
                    if(xChange != 0 || yChange != 0) {
                        if (selectedShape.getId() == "Line") {
                            Line selectedLine = (Line) selectedShape;
                            Line relocatedLine = new Line(selectedLine.getStartX() + xChange, selectedLine.getStartY() + yChange,
                                    selectedLine.getEndX() + xChange, selectedLine.getEndY() + yChange);
                            relocatedLine.setId("Line");
                            relocatedLine.setFill(selectedLine.getFill());
                            relocatedLine.setStroke(selectedLine.getStroke());
                            relocatedLine.setStrokeWidth(selectedLine.getStrokeWidth());
                            relocatedLine.getStrokeDashArray().addAll(selectedLine.getStrokeDashArray());
                            // mark
                            relocatedLine.setEffect(new DropShadow(20, Color.BLACK));
                            shapes.set(selectedIndex, relocatedLine);
                            getChildren().set(selectedIndex, relocatedLine);
                            selectedShape = relocatedLine;
                        } else if (selectedShape.getId() == "Circle") {
                            Circle selectedCircle = (Circle) selectedShape;
                            Circle relocatedCircle = new Circle(selectedCircle.getCenterX() + xChange,
                                    selectedCircle.getCenterY() + yChange, selectedCircle.getRadius(), selectedCircle.getFill());
                            relocatedCircle.setId("Circle");
                            relocatedCircle.setStroke(selectedCircle.getStroke());
                            relocatedCircle.setStrokeWidth(selectedCircle.getStrokeWidth());
                            relocatedCircle.getStrokeDashArray().addAll(selectedCircle.getStrokeDashArray());
                            // mark
                            relocatedCircle.setEffect(new DropShadow(20, Color.BLACK));
                            shapes.set(selectedIndex, relocatedCircle);
                            getChildren().set(selectedIndex, relocatedCircle);
                            selectedShape = relocatedCircle;
                        } else if (selectedShape.getId() == "Rectangle") {
                            Rectangle selectedRec = (Rectangle) selectedShape;
                            Rectangle relocatedRec = new Rectangle(selectedRec.getX() + xChange, selectedRec.getY() + yChange, selectedRec.getWidth(), selectedRec.getHeight());
                            relocatedRec.setId("Rectangle");
                            relocatedRec.setFill(selectedRec.getFill());
                            relocatedRec.setStroke(selectedRec.getStroke());
                            relocatedRec.setStrokeWidth(selectedRec.getStrokeWidth());
                            relocatedRec.getStrokeDashArray().addAll(selectedRec.getStrokeDashArray());
                            // mark
                            relocatedRec.setEffect(new DropShadow(20, Color.BLACK));
                            shapes.set(selectedIndex, relocatedRec);
                            getChildren().set(selectedIndex, relocatedRec);
                            selectedShape = relocatedRec;
                        }
                    }
                } else if(model.mode == 3) {
                    isTriggered = false;
                }
            }
        });
    }

    private void setThickness(Shape shape, int thickVal) {
        if(thickVal == 1) {
            shape.setStrokeWidth(1);
        } else if(thickVal == 2) {
            shape.setStrokeWidth(3);
        } else if(thickVal == 3) {
            shape.setStrokeWidth(5);
        } else {
            shape.setStrokeWidth(0);
        }
    }

    private int getThickness(Shape shape) {
        if(shape.getStrokeWidth() == 1) {
            return 1;
        } else if(shape.getStrokeWidth() == 3) {
            return 2;
        } else if(shape.getStrokeWidth() == 5) {
            return 3;
        } else {
            return 0;
        }
    }

    private void setStyle(Shape shape, int styleVal) {
        while(!shape.getStrokeDashArray().isEmpty()) {
            shape.getStrokeDashArray().remove(0);
        }
        if(styleVal == 1) {
            shape.getStrokeDashArray().addAll(1d,0d);
        } else if(styleVal == 2) {
            shape.getStrokeDashArray().addAll(15d,10d);
        } else if(styleVal == 3) {
            shape.getStrokeDashArray().addAll(2d,15d);
        } else {
            shape.setStrokeWidth(0);
        }
    }

    private int getStyle(Shape shape) {
        ObservableList<Double> list = shape.getStrokeDashArray();
        if(list.contains(15d) && list.contains(10d)) {
            return 2;
        } else if(list.contains(15d) && list.contains(2d)) {
            return 3;
        } else if(list.contains(1d) && list.contains(0d)) {
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public void updateView() {
        if(model.mode == 1 && !isTriggered) {
            shapes.get(selectedIndex).setFill(model.fillColour);
            shapes.get(selectedIndex).setStroke(model.lineColour);
            setThickness(shapes.get(selectedIndex), model.thickness);
            setStyle(shapes.get(selectedIndex),model.lineStyle);
        }

    }
}
