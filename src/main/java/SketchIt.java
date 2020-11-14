import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;


public class SketchIt extends Application {
    Shape copiedShape;
    int count = 1;
    static MenuBar menubar;
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create menu items
        menubar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newfile = new MenuItem("New");
        MenuItem load = new MenuItem("Load");
        MenuItem save = new MenuItem("Save");
        MenuItem quit = new MenuItem("Quit");
        FileChooser fileChooser = new FileChooser();

        Menu editMenu = new Menu("Edit");
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");

        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("About");

        // Put menus together
        fileMenu.getItems().addAll(newfile,load,save,quit);
        editMenu.getItems().addAll(cut,copy,paste);
        helpMenu.getItems().add(about);
        menubar.getMenus().addAll(fileMenu,editMenu,helpMenu);

        // Map accelerator keys to menu items
        newfile.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        load.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
        save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        quit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        cut.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        copy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        paste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));


        // create and initialize the Model to hold our counter
        Model model = new Model();

        // create each view, and tell them about the model
        // the views will register themselves with the model
        CanvasView canvas = new CanvasView(model);
        ToolbarView toolbar = new ToolbarView(model);

        // put together the canvas and the toolbar
        HBox box = new HBox();
        box.getChildren().addAll(toolbar,canvas);
        box.setPadding(new Insets(0,0,0,5));

        // add the menubar into the above
        VBox root = new VBox();
        root.getChildren().addAll(menubar,box);

        // Attach the scene to the stage and show it

        primaryStage.setResizable(true);
        primaryStage.setMinWidth(450);
        primaryStage.setMinHeight(450);
        primaryStage.setMaxWidth(1250);
        primaryStage.setMaxHeight(860);

        Scene scene = new Scene(root , 1100, 800);

        primaryStage.setScene(scene);
        primaryStage.setTitle("SketchIt");
        primaryStage.show();

        //canvas.setPrefSize(primaryStage.getWidth(), primaryStage.getHeight());
        /*canvas.maxX = canvas.getWidth();
        canvas.maxY = primaryStage.getHeight()-29;*/
        //canvas.maxY = canvas.getHeight()+29;
        System.out.println("Stage: "+primaryStage.getWidth() + " " + primaryStage.getHeight());
        System.out.println("Canvas: "+canvas.getWidth() + " "+ canvas.getHeight());
        System.out.println("Toolbar "+toolbar.getWidth() + " "+ toolbar.getHeight());
        System.out.println("Menubar "+menubar.getWidth() + " "+ menubar.getHeight());

        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(model.mode == 1 && CanvasView.selectedShape != null) {
                    KeyCode keycode = event.getCode();
                    System.out.println(event.getCode());
                    if (keycode == KeyCode.ESCAPE) {
                        canvas.isTriggered = false;
                        toolbar.selectTool.setSelected(false);
                    } else if (keycode == KeyCode.BACK_SPACE) {
                        canvas.getChildren().remove(CanvasView.shapes.get(CanvasView.selectedIndex));
                        canvas.shapes.remove(CanvasView.selectedIndex);
                        canvas.selectedShape = null;
                    }
                }
            }
        });

        // Set up handlers
        newfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!canvas.shapes.isEmpty()) {
                    Alert newAlert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                    newAlert.setTitle("New File");
                    newAlert.setHeaderText("There might be new changes made to this file. " +
                            "\nDo you want to save before proceeding?");
                    newAlert.showAndWait();
                    if (newAlert.getResult() == ButtonType.YES) {
                        // Save
                        save();
                        // New
                        while (!canvas.shapes.isEmpty()) {
                            canvas.shapes.remove(0);
                            canvas.getChildren().remove(0);
                        }
                        newAlert.close();
                    } else if (newAlert.getResult() == ButtonType.NO) {
                        // New
                        while (!canvas.shapes.isEmpty()) {
                            canvas.shapes.remove(0);
                            canvas.getChildren().remove(0);
                        }
                        newAlert.close();
                    } else if (newAlert.getResult() == ButtonType.CANCEL) {
                        newAlert.close();
                    }

                }
            }
        });

        load.setOnAction(actionEvent -> {
            if(canvas.shapes.isEmpty()) {
                File loadFile = fileChooser.showOpenDialog(primaryStage);
                load(loadFile.getName());
                System.out.println(canvas.shapes.size());
                canvas.getChildren().addAll(canvas.shapes);
            } else {
                Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.setTitle("Load File");
                alert.setHeaderText("The current drawing will be discarded." +
                        "\nDo you want to save before proceeding?");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    // Save
                    save();
                    // New
                    while (!canvas.shapes.isEmpty()) {
                        canvas.shapes.remove(0);
                        canvas.getChildren().remove(0);
                    }
                    // Load
                    File loadFile = fileChooser.showOpenDialog(primaryStage);
                    load(loadFile.getName());
                    canvas.getChildren().addAll(canvas.shapes);
                    alert.close();
                } else if (alert.getResult() == ButtonType.NO) {
                    // New
                    while (!canvas.shapes.isEmpty()) {
                        canvas.shapes.remove(0);
                        canvas.getChildren().remove(0);
                    }
                    // Load
                    File loadFile = fileChooser.showOpenDialog(primaryStage);
                    load(loadFile.getName());
                    System.out.println(canvas.shapes.size());
                    canvas.getChildren().addAll(canvas.shapes);
                    alert.close();
                } else if (alert.getResult() == ButtonType.CANCEL) {
                    alert.close();
                }
            }
        });

        save.setOnAction(actionEvent -> {
            save();
        });

        quit.setOnAction(actionEvent -> {
            if(canvas.shapes.isEmpty()) System.exit(0);
            else {
                Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL, ButtonType.NO);
                alert.setTitle("Exit Confirmation");
                alert.setHeaderText("Quitting will discard your drawing.\n Do you want to save before proceeding?");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    save();
                    System.exit(0);
                } else if (alert.getResult() == ButtonType.NO) {
                    System.exit(0);
                } else if (alert.getResult() == ButtonType.CANCEL) {
                    alert.close();
                }
            }
        });

        about.setOnAction(actionEvent -> {
            Alert dialogue = new Alert(AlertType.INFORMATION,
                    "Created by\n" + "Name: Yilin Ding\n" + "WatId: 20765311",
                    ButtonType.CLOSE);
            dialogue.setTitle("About Info");
            dialogue.setHeaderText("SketchIt");
            dialogue.show();
            if(dialogue.getResult() == ButtonType.CLOSE) {
                dialogue.close();
            }
        });

        cut.setOnAction(actionEvent -> {
            if(model.mode == 1 && canvas.selectedShape != null) {
                copyShape(canvas.selectedShape);
                canvas.shapes.remove(canvas.selectedShape);
                canvas.getChildren().remove(canvas.selectedIndex);
                toolbar.selectTool.setSelected(false);
                toolbar.selectTool.setSelected(true);
            }
        });

        copy.setOnAction(actionEvent -> {
            if(model.mode == 1 && canvas.selectedShape != null) {
                copyShape(canvas.selectedShape);
            }
        });

        paste.setOnAction(actionEvent -> {
            // paste into the selected widget
            System.out.println(model.mode+","+copiedShape);
            if(model.mode == 1 && copiedShape != null) {
                canvas.shapes.add(copiedShape);
                canvas.getChildren().add(copiedShape);
                copiedShape = null;
            }
        });
    }

    // copy the given shape to the global shape holder
    void copyShape(Shape selectedShape) {
        if (selectedShape.getId() == "Line") {
            Line selectedLine = (Line) selectedShape;
            copiedShape = new Line(selectedLine.getStartX() , selectedLine.getStartY(),
                    selectedLine.getEndX() , selectedLine.getEndY());
            copiedShape.setId("Line");
            copiedShape.setFill(selectedLine.getFill());
            copiedShape.setStroke(selectedLine.getStroke());
            copiedShape.setStrokeWidth(selectedLine.getStrokeWidth());
            copiedShape.getStrokeDashArray().addAll(selectedLine.getStrokeDashArray());
        } else if (selectedShape.getId() == "Circle") {
            Circle selectedCircle = (Circle) selectedShape;
            copiedShape = new Circle(selectedCircle.getCenterX() ,
                    selectedCircle.getCenterY(), selectedCircle.getRadius(), selectedCircle.getFill());
            copiedShape.setId("Circle");
            copiedShape.setStroke(selectedCircle.getStroke());
            copiedShape.setStrokeWidth(selectedCircle.getStrokeWidth());
            copiedShape.getStrokeDashArray().addAll(selectedCircle.getStrokeDashArray());
        } else if (selectedShape.getId() == "Rectangle") {
            Rectangle selectedRec = (Rectangle) selectedShape;
            copiedShape = new Rectangle(selectedRec.getX(), selectedRec.getY(), selectedRec.getWidth(), selectedRec.getHeight());
            copiedShape.setId("Rectangle");
            copiedShape.setFill(selectedRec.getFill());
            copiedShape.setStroke(selectedRec.getStroke());
            copiedShape.setStrokeWidth(selectedRec.getStrokeWidth());
            copiedShape.getStrokeDashArray().addAll(selectedRec.getStrokeDashArray());
        }
    }

    private void save() {
        // Save dialog
        TextInputDialog dialog = new TextInputDialog("Untitled" + count);
        dialog.setTitle("Save File");
        dialog.setHeaderText("Please enter the filename where want to save your file");
        dialog.setContentText("Save this file as:");
        //dialog.getDialogPane().lookupButton(ButtonType.OK);
        Optional<String> result = dialog.showAndWait();
        String fileName = "";
        fileName = result.get();
        if(fileName == "") return;


        FileWriter file = null;
        BufferedWriter writer = null;

        try {
            file = new FileWriter(fileName);
            writer = new BufferedWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ++count;
            for(int i = 0; i < CanvasView.shapes.size(); ++i) {
                Shape currShape = CanvasView.shapes.get(i);
                String type = currShape.getId();
                String data = "";

                if(type == "Line") {
                    Line currLine = (Line)currShape;
                    data = currLine.getStartX() + " " + currLine.getStartY() + " " + currLine.getEndX() + " " + currLine.getEndY() + " " +
                            currLine.getFill() + " " + currLine.getStroke() + " " + currLine.getStrokeWidth() + " ";
                    for(int j = 0; j < currLine.getStrokeDashArray().size();++j) {
                        data += currLine.getStrokeDashArray().get(j);
                        if(j != currLine.getStrokeDashArray().size()-1) data += ",";
                    }
                } else if(type == "Circle") {
                    Circle currCircle = (Circle)currShape;
                    data = currCircle.getCenterX() + " " + currCircle.getCenterY() + " " + currCircle.getRadius() + " " + currCircle.getFill() + " " +
                            currCircle.getStroke() + " " + currCircle.getStrokeWidth() + " ";
                    for(int j = 0; j < currCircle.getStrokeDashArray().size();++j) {
                        data += currCircle.getStrokeDashArray().get(j);
                        if(j != currCircle.getStrokeDashArray().size()-1) data += ",";
                    }
                } else if(type == "Rectangle") {
                    Rectangle currRec = (Rectangle)currShape;
                    data = currRec.getX() + " " + currRec.getY() + " " + currRec.getWidth() + " " + currRec.getHeight() + " " + currRec.getFill() + " " +
                            currRec.getStroke() + " " + currRec.getStrokeWidth() + " ";
                    for(int j = 0; j < currRec.getStrokeDashArray().size();++j) {
                        data += currRec.getStrokeDashArray().get(j);
                        if(j != currRec.getStrokeDashArray().size()-1) data += ",";
                    }
                }
                writer.write(type+" "+data+"\n");
            }

            writer.close();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void load(String fileName) {
        FileReader file = null;
        BufferedReader reader = null;
        String[] values;

        // open input file
        try {
            file = new FileReader(fileName);
            reader = new BufferedReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("Load throw exception");
            e.printStackTrace();
        }

        // read and process lines one at a time
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                values = line.split(" ");
                if(values[0].equals("Line")) {
                    Line currLine = new Line(Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Double.parseDouble(values[4]));
                    currLine.setId("Line");
                    currLine.setFill(Color.valueOf(values[5]));
                    currLine.setStroke(Color.valueOf(values[6]));
                    currLine.setStrokeWidth(Double.parseDouble(values[7]));
                    for(String value : values[8].split(",")) {
                        currLine.getStrokeDashArray().add(Double.parseDouble(value));
                    }
                    CanvasView.shapes.add(currLine);
                } else if(values[0].equals("Circle")) {
                    Circle currCircle = new Circle(Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Color.valueOf(values[4]));
                    currCircle.setId("Circle");
                    currCircle.setStroke(Color.valueOf(values[5]));
                    currCircle.setStrokeWidth(Double.parseDouble(values[6]));
                    System.out.println("Circle before add array");
                    for(String value : values[7].split(",")) {
                        currCircle.getStrokeDashArray().add(Double.parseDouble(value));
                    }
                    CanvasView.shapes.add(currCircle);
                } else if(values[0].equals("Rectangle")) {
                    Rectangle currRec = new Rectangle(Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Double.parseDouble(values[4]));
                    currRec.setId("Rectangle");
                    currRec.setFill(Color.valueOf(values[5]));
                    currRec.setStroke(Color.valueOf(values[6]));
                    currRec.setStrokeWidth(Double.parseDouble(values[7]));
                    for(String value : values[8].split(",")) {
                        currRec.getStrokeDashArray().add(Double.parseDouble(value));
                    }
                    CanvasView.shapes.add(currRec);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
