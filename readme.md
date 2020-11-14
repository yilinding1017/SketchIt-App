Yilin Ding<br>*20765311 y264ding*<br>*openjdk version "11.0.8" 2020-07-14*<br>*macOS 11.0.1 (MacBook Pro 2019)*<br>sources of images used: https://www.flaticon.com/<br>

 ### SketchIt:

The SketchIt application allows user to draw shapes by using provided tools and set their properties. This application has the following layout and their corresponding features in detail:

1. ##### Tool Palette

   - **Selection tool**: clicking on this tool allows user to click on a drawn shape to select, during which the properties will immediately reflect this selected shape’s properties. A selected item can be dragged in the bound of current canvas and user can change its properties by changing the these on the tool bar. When a item is selected, clicking on **key ESC** or clicking on an empty party of the canvas will deselect this item; clicking **delete key**, in the other case, will delete this selected item. Also user can cut and copy an selected shape by using the edit menubar.
   - **Erase tool**: clicking on this tool allows user to erase an drawn shape by then simply clicking on that shape.
   - **Line drawing tool**: clicking on this tool allows the user draw a line with the selected properties (all properties must be selected to draw a line), where the first mouse press sets the start point and then the mouse release point after dragging sets the end point of the line.
   - **Circle drawing tool**: clicking on this tool allows the user to draw a circle with the selected properties, where first mouse press the circle center and the mouse release after dragging sets the distance between press and release as the radius of the circle.
   - **Rectangle drawing tool**: clicking on this tool allows the user to draw a rectangle with the selected properties, where first mouse press sets the upper-left corner starting position and the mouse release after dragging sets its lower-right ending position. (Note that a rectangle is set to only to be drawn from upper left to lower right).
   - **Fill tool**: clicking on this tool allows user to change an drawn shape's fill colour to the one on the property bar by simply clicking on the shape to be filled

   - 

2. ##### Properties

   - **Line Colour**: the colour will be used as the border colour for any shapes that are drawn/selected (or the ONLY colour in the case of a line). In default, it is set to BLACK.

   - **Fill Colour**: the colour will be used as the fill colour for any shapes that are drawn/selected (it will NOT be used if a line is being drawn/selected). In default, it is set to GOLD.

   - **Line Thickness**: this sets the line thickness or border thickness for any shapes drawn/selected (it must selected for a line or the border of the shape to be drawn). When a shape is selected and this property is unselected, the border will be set to none (in the case of a line, the line will disappear) and reselect one of this property will set the border/line back. In default, it is set to THICK.

   - **Line Style**: this sets the line style or border style for any shapes that are drawn/ selected (it must selected for a line or the border of the shape to be drawn).When a shape is selected and this property is unselect, the border will be set to none (in the case of a line, the line will disappear) and reselect any property will set the border/line back. In default, it is set to SOLID.

     

3. ##### Menubar

   - File

     - **New**: create a new blank drawing with prompt to save before opening a new file
     - **Load**: load a drawing that is previously saved by using file chooser, with prompt to save before loading a file
     - **Save**: save the current drawing with prompt for file name
     - **Quit**: exit the application with prompt to save before exiting

   - Edit

     - **Copy**: copy the currently selected shape onto clipboard (must be used in selection tool mode with one shape being selected)
     - **Cut**: cut the currently selected shape onto clipboard (must be used in selection tool mode with one shape being selected)
     - **Paste**: paste the shape on the clipboard (if there is one) on to the canvas at its originally position when it is copied/cut (must be used in selection tool mode

   - Help

     - **About**: display a dialogbox with my program name, my name and WatID.

     

4. Canvas

   The Canvas is resizable with dotted line outlined it as border. The window is also resizable with minimum and maximum sizes of (450, 450) and (1250, 860).

   