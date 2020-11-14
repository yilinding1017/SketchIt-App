import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;

public class Model {
    /* A list of the model's views. */
    private ArrayList<IView> views;

    // Tool mode: (1) Select (2) Erase (3) ShapeSketch (4) Fill
    public static int mode = 0;
    // Shape Drawing Tool: (1) Line (2) Circle (3) Rectangle
    public static int shapeType = 0;

    // Properties: line colour, fill colour, line thickness, line style
    public static Color lineColour = Color.BLACK;
    public static  Color fillColour = Color.GOLD;
    public static  int thickness = 3;
    public static  int lineStyle = 1;

    Model() {
        views = new ArrayList<>();
    }

    // method that the views can use to register themselves with the Model
    // once added, they are told to update and get state from the Model
    public void addView(IView view) {
        views.add(view);
        view.updateView();
    }

    public void changeMode(int modeNum) {
        mode = modeNum;
    }

    public void changeShapetoDraw(int shapeNum) {
        shapeType = shapeNum;
    }

    // properties change
    public void changeLineColour(Color colour) {
        lineColour = colour;
        if(mode == 1) {
            // Select Mode

        }
        notifyObservers();
    }

    public void changeFillColour(Color colour) {
        fillColour = colour;
        notifyObservers();
    }

    public void changeThickness(int thickVal) {
        thickness = thickVal;
        notifyObservers();
    }

    public void changeLineStyle(int style) {
        lineStyle = style;
        notifyObservers();
    }

    private void notifyObservers() {
        for (IView view : this.views) {
            view.updateView();
        }
    }
}
