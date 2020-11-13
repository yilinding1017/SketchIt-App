import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;


public class ToolbarView extends VBox implements IView {
    private Model model;

    ToggleGroup tools;
    static StandardButton selectTool;
    static StandardButton eraseTool;
    // Shape tool
    StandardButton lineTool;
    StandardButton circleTool;
    StandardButton rectangleTool;
    StandardButton fillTool;

    static ColorPicker lineColourPicker;
    static ColorPicker fillColourPicker;

    ToggleGroup lineThicknessToggles;
    static StandardButton thickness1;
    static StandardButton thickness2;
    static StandardButton thickness3;

    ToggleGroup lineStyleToggles;
    static StandardButton style1;
    static StandardButton style2;
    static StandardButton style3;

    ToolbarView(Model model) {
        this.model = model;
        // Add layout
        this.setMaxWidth(300);
        this.setMaxHeight(800);
        this.setMinHeight(500);
        this.setMinWidth(200);

        this.layoutView();
        this.registerControllers();

        model.addView(this);
    }

    private void registerControllers() {
        selectTool.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            // if 'Select' is selected
            if(newValue == true) {
                lineColourPicker.setDisable(true);
                fillColourPicker.setDisable(true);
                thickness1.setDisable(true);
                thickness2.setDisable(true);
                thickness3.setDisable(true);
                style1.setDisable(true);
                style2.setDisable(true);
                style3.setDisable(true);

                model.changeMode(1);
            } else {
                if(tools.getSelectedToggle() == null) {
                    model.changeMode(0);
                    lineColourPicker.setDisable(false);
                    fillColourPicker.setDisable(false);
                    thickness1.setDisable(false);
                    thickness2.setDisable(false);
                    thickness3.setDisable(false);
                    style1.setDisable(false);
                    style2.setDisable(false);
                    style3.setDisable(false);
                }
                if(CanvasView.selectedShape != null) {
                    CanvasView.selectedShape.setEffect(null);
                    CanvasView.selectedShape = null;
                }
            }
        }));

        eraseTool.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            // if 'Erase' is selected
            if(newValue == true) {
                lineColourPicker.setDisable(true);
                fillColourPicker.setDisable(true);
                thickness1.setDisable(true);
                thickness2.setDisable(true);
                thickness3.setDisable(true);
                style1.setDisable(true);
                style2.setDisable(true);
                style3.setDisable(true);

                model.changeMode(2);
            } else {
                if(tools.getSelectedToggle() == null) {
                    model.changeMode(0);
                    lineColourPicker.setDisable(false);
                    fillColourPicker.setDisable(false);
                    thickness1.setDisable(false);
                    thickness2.setDisable(false);
                    thickness3.setDisable(false);
                    style1.setDisable(false);
                    style2.setDisable(false);
                    style3.setDisable(false);
                }
            }
        }));

        lineTool.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            // if 'Line' is selected
            if(newValue == true) {
                lineColourPicker.setDisable(false);
                fillColourPicker.setDisable(false);
                thickness1.setDisable(false);
                thickness2.setDisable(false);
                thickness3.setDisable(false);
                style1.setDisable(false);
                style2.setDisable(false);
                style3.setDisable(false);

                model.changeMode(3);
                model.changeShapetoDraw(1);
            } else {
                if(tools.getSelectedToggle() == null) {
                    model.changeMode(0);
                }
            }
        }));

        circleTool.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            // if 'Circle' is selected
            if(newValue == true) {
                lineColourPicker.setDisable(false);
                fillColourPicker.setDisable(false);
                thickness1.setDisable(false);
                thickness2.setDisable(false);
                thickness3.setDisable(false);
                style1.setDisable(false);
                style2.setDisable(false);
                style3.setDisable(false);

                model.changeMode(3);
                model.changeShapetoDraw(2);
            } else {
                if(tools.getSelectedToggle() == null) model.changeMode(0);
            }
        }));

        rectangleTool.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            // if 'Rectangle' is selected
            if(newValue == true) {
                lineColourPicker.setDisable(false);
                fillColourPicker.setDisable(false);
                thickness1.setDisable(false);
                thickness2.setDisable(false);
                thickness3.setDisable(false);
                style1.setDisable(false);
                style2.setDisable(false);
                style3.setDisable(false);

                model.changeMode(3);
                model.changeShapetoDraw(3);
            } else {
                if(tools.getSelectedToggle() == null) model.changeMode(0);
            }
        }));

        fillTool.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            // if 'Fill' is selected
            if(newValue == true) {
                lineColourPicker.setDisable(false);
                fillColourPicker.setDisable(false);
                thickness1.setDisable(true);
                thickness2.setDisable(true);
                thickness3.setDisable(true);
                style1.setDisable(true);
                style2.setDisable(true);
                style3.setDisable(true);

                model.changeMode(4);
            } else {
                if(tools.getSelectedToggle() == null) {
                    model.changeMode(0);
                    lineColourPicker.setDisable(false);
                    fillColourPicker.setDisable(false);
                    thickness1.setDisable(false);
                    thickness2.setDisable(false);
                    thickness3.setDisable(false);
                    style1.setDisable(false);
                    style2.setDisable(false);
                    style3.setDisable(false);
                }
            }
        }));

        // -------------------------------------------------------------
        lineColourPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.changeLineColour(lineColourPicker.getValue());
            }
        });

        fillColourPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.changeFillColour(fillColourPicker.getValue());
            }
        });

        thickness1.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            // if 'Thickness 1' is selected
            if(newValue == true) {
                model.changeThickness(1);
            } else {
                if(lineThicknessToggles.getSelectedToggle() == null) model.changeThickness(0);
            }
        }));

        thickness2.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            // if 'Thickness 2' is selected
            if(newValue == true) {
                model.changeThickness(2);
            } else {
                if(lineThicknessToggles.getSelectedToggle() == null) model.changeThickness(0);
            }
        }));

        thickness3.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            // if 'Thickness 3' is selected
            if(newValue == true) {
                model.changeThickness(3);
            } else {
                if(lineThicknessToggles.getSelectedToggle() == null) model.changeThickness(0);
            }
        }));

        style1.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            // if 'Style 1' is selected
            if(newValue == true) {
                model.changeLineStyle(1);
            } else {
                if(lineStyleToggles.getSelectedToggle() == null) model.changeLineStyle(0);
            }
        }));

        style2.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            // if 'Style 2' is selected
            if(newValue == true) {
                model.changeLineStyle(2);
            } else {
                if(lineStyleToggles.getSelectedToggle() == null) model.changeLineStyle(0);
            }
        }));

        style3.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            // if 'Style 3' is selected
            if(newValue == true) {
                model.changeLineStyle(3);
            } else {
                if(lineStyleToggles.getSelectedToggle() == null) model.changeLineStyle(0);
            }
        }));
    }

    // Customized button
    // Used to set default values for all buttons
    // Lets us manipulate MIN, MAX, PREFERRED sizes in one place for all demos
    static public class StandardButton extends ToggleButton {
        StandardButton() {
            this("");
        }

        StandardButton(String caption) {
            super(caption);
            // setText(caption); // call to super class already does this
            setVisible(true);
            setMinSize(100,50);
            setPrefWidth(150);
            setMaxSize(300,100);
            /*setMinWidth(55);
            setPrefWidth(150);
            setMaxWidth(200);*/
        }

        StandardButton(String caption, Node graphic) {
            super(caption, graphic);
        }
    }

    private void layoutView() {
        // Add Tools
        this.getChildren().add(new Label("Tools"));
        GridPane toolsBox = new GridPane();
        tools = new ToggleGroup();
        Image select = new Image("click.png");
        ImageView selectNode = new ImageView(select);
        selectNode.setFitHeight(30);
        selectNode.setFitWidth(30);
        selectTool = new StandardButton("Select");
        /*selectNode.fitWidthProperty().bind(selectTool.widthProperty());
        selectNode.fitHeightProperty().bind(selectTool.heightProperty());*/
        selectTool.setGraphic(selectNode);
        /*selectTool.setBackground(new Background(new BackgroundImage(select,BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO,
                BackgroundSize.AUTO, true, true, true, true))));*/
        double iconW = 30;
        double iconH = 30;

        Image erase = new Image("eraser.png");
        ImageView eraseNode = new ImageView(erase);
        eraseNode.setFitHeight(iconH);
        eraseNode.setFitWidth(iconW);
        eraseTool = new StandardButton("Erase");
        eraseTool.setGraphic(eraseNode);

        Image line = new Image("line.png");
        ImageView lineNode = new ImageView(line);
        lineNode.setFitHeight(iconH);
        lineNode.setFitWidth(iconW);
        lineTool = new StandardButton("  Line");
        lineTool.setGraphic(lineNode);

        Image circle = new Image("circle.png");
        ImageView circleNode = new ImageView(circle);
        circleNode.setFitHeight(iconH);
        circleNode.setFitWidth(iconW);
        circleTool = new StandardButton("Circle");
        circleTool.setGraphic(circleNode);

        Image rectangle = new Image("rectangle.png");
        ImageView rectangleNode = new ImageView(rectangle);
        rectangleNode.setFitHeight(iconH);
        rectangleNode.setFitWidth(iconW+6);
        rectangleTool = new StandardButton("Box");
        rectangleTool.setGraphic(rectangleNode);

        Image fill = new Image("fill.png");
        ImageView fillNode = new ImageView(fill);
        fillNode.setFitHeight(iconH);
        fillNode.setFitWidth(iconW);
        fillTool = new StandardButton(" Fill");
        fillTool.setGraphic(fillNode);

        toolsBox.add(selectTool,0,0);
        toolsBox.add(eraseTool,1,0);
        toolsBox.add(lineTool,0,1);
        toolsBox.add(circleTool,1,1);
        toolsBox.add(rectangleTool,0,2);
        toolsBox.add(fillTool,1,2);
        tools.getToggles().addAll(selectTool,eraseTool,lineTool,circleTool,rectangleTool,fillTool);
        toolsBox.setHgap(0);
        toolsBox.setVgap(0);
        this.getChildren().add(toolsBox);

        // Add Properties
        // Colours
        //GridPane propertyBox = new GridPane();
        //propertyBox.add(new Label("Line Colour"),0,3);
        lineColourPicker = new ColorPicker();
        lineColourPicker.setMinSize(55,20);
        lineColourPicker.setPrefWidth(150);
        lineColourPicker.setMaxSize(300,40);
        VBox lineColourBox = new VBox(new Label("Line Colour"),lineColourPicker);
        //propertyBox.add(lineColourPicker,0,4);

        //propertyBox.add(new Label("Fill Colour"),1,3);
        fillColourPicker = new ColorPicker();
        fillColourPicker.setMinSize(55,20);
        fillColourPicker.setPrefWidth(150);
        fillColourPicker.setMaxSize(300,40);
        VBox fillColourBox = new VBox(new Label("Fill Colour"),fillColourPicker);
        //propertyBox.add(fillColourPicker,1,4);
        TilePane coloursBox = new TilePane(lineColourBox,fillColourBox);
        this.getChildren().add(coloursBox);

        // Line Thickness
        //propertyBox.add(new Label("Line Thickness"),0,5);
        lineThicknessToggles = new ToggleGroup();
        Line line1 = new Line();
        line1.setStrokeWidth(1);
        thickness1 = new StandardButton("",line1);
        line1.setStartX(thickness1.getTranslateX());
        line1.setStartY(thickness1.getTranslateY());
        line1.setEndX(thickness1.getTranslateX()+62);
        line1.setEndY(thickness1.getTranslateY()+40);

        Line line2 = new Line();
        line2.setStrokeWidth(3);
        thickness2 = new StandardButton("",line2);
        line2.setStartX(thickness2.getTranslateX()-1);
        line2.setStartY(thickness2.getTranslateY());
        line2.setEndX(thickness2.getTranslateX()+60);
        line2.setEndY(thickness2.getTranslateY()+37);

        Line line3 = new Line();
        line3.setStrokeWidth(5);
        thickness3 = new StandardButton("", line3);
        line3.setStartX(thickness3.getTranslateX()+4);
        line3.setStartY(thickness3.getTranslateY()+2);
        line3.setEndX(thickness3.getTranslateX()+60);
        line3.setEndY(thickness3.getTranslateY()+35);
        /*thickness1.setMinSize(100,50);
        thickness1.setMaxSize(100,50);
        thickness2.setMinSize(100,50);
        thickness2.setMaxSize(100,50);
        thickness3.setMinSize(100,50);
        thickness3.setMaxSize(100,50);*/
        lineThicknessToggles.getToggles().addAll(thickness1,thickness2,thickness3);
        this.getChildren().add(new Label("Line Thickness"));
        TilePane thicknessBox = new TilePane(thickness1,thickness2,thickness3);
        this.getChildren().add(thicknessBox);
        /*propertyBox.add(thickness1,0,6);
        propertyBox.add(thickness2,1,6);
        propertyBox.add(thickness3,2,6);*/

        //propertyBox.add(new Label("Line Styles"),0,7);
        lineStyleToggles = new ToggleGroup();
        Line solidLine = new Line();
        solidLine.setStrokeWidth(2);
        solidLine.getStrokeDashArray().addAll(1d,0d);
        style1 = new StandardButton("", solidLine);
        solidLine.setStartX(style1.getTranslateX()+3);
        solidLine.setStartY(style1.getTranslateY());
        solidLine.setEndX(style1.getTranslateX()+65);
        solidLine.setEndY(style1.getTranslateY()+40);

        Line dashedLine = new Line();
        dashedLine.setStrokeWidth(2);
        dashedLine.getStrokeDashArray().addAll(15d,10d);
        style2 = new StandardButton("",dashedLine);
        dashedLine.setStartX(style2.getTranslateX());
        dashedLine.setStartY(style2.getTranslateY());
        dashedLine.setEndX(style2.getTranslateX()+60);
        dashedLine.setEndY(style2.getTranslateY()+40);

        Line dotedLine = new Line();
        dotedLine.setStrokeWidth(2);
        dotedLine.getStrokeDashArray().addAll(2d,15d);
        style3 = new StandardButton("",dotedLine);
        dotedLine.setStartX(style3.getTranslateX()-3);
        dotedLine.setStartY(style3.getTranslateY());
        dotedLine.setEndX(style3.getTranslateX()+60);
        dotedLine.setEndY(style3.getTranslateY()+40);

        lineStyleToggles.getToggles().addAll(style1,style2,style3);
        /*propertyBox.add(style1,0,8);
        propertyBox.add(style2,1,8);
        propertyBox.add(style3,2,8);
        propertyBox.setHgap(1);
        propertyBox.setVgap(3);
        this.getChildren().add(propertyBox);*/
        this.getChildren().add(new Label("Line Style"));
        TilePane styleBox = new TilePane(style1,style2,style3);
        this.getChildren().add(styleBox);
    }

    @Override
    public void updateView() {
        // update line colour, fill colour, line thickness, line style
        lineColourPicker.setValue(model.lineColour);
        fillColourPicker.setValue(model.fillColour);

        if(model.thickness == 1) {
            thickness1.setSelected(true);
        } else if(model.thickness == 2) {
            thickness2.setSelected(true);
        } else if(model.thickness == 3) {
            thickness3.setSelected(true);
        }

        if(model.lineStyle == 1) {
            style1.setSelected(true);
        } else if(model.lineStyle == 2) {
            style2.setSelected(true);
        } else if(model.lineStyle == 3) {
            style3.setSelected(true);
        }
    }
}
