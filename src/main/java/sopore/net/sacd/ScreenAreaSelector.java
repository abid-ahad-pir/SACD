package sopore.net.sacd;

import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;

public class ScreenAreaSelector {
    

	private StackPane selectorStackPane;
	private Rectangle2D selectedBounds;
	private boolean selectionMade = false;
	private double startX;
	private double startY;
	private double endX;
	private double endY;
	
    public ScreenAreaSelector() {
    	
    }
    
    public Rectangle2D getSelectedBounds() throws Exception {
    		if(selectionMade && selectedBounds != null )
    			return selectedBounds;
    		else
    			throw new Exception("Selection not made yet.");    		
    }
    
    public void selectScreenArea(Stage stage) {
    	// Get the primary screen
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();

        // Create a canvas with the same size as the screen
        Canvas canvas = new Canvas(bounds.getWidth(), bounds.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());

        // Add a mouse event handler to the canvas
        canvas.setOnMousePressed(event -> {
        	startX = event.getX();
        	startY = event.getY();
            gc.clearRect(0, 0, bounds.getWidth(), bounds.getHeight());
            gc.setFill(Color.rgb(0, 0, 0, 0.5));
            gc.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());
           
        });

        canvas.setOnMouseDragged(event -> {
        	endX = event.getX();
        	endY = event.getY();
            gc.clearRect(0, 0, bounds.getWidth(), bounds.getHeight());
            gc.setFill(Color.rgb(0, 0, 0, 0.5));
            gc.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());
            gc.setFill(Color.rgb(255, 255, 255, 0.5));
            gc.fillRect(startX, startY, endX - startX, endY - startY);
        });

        canvas.setOnMouseReleased(event -> {
          	
            endX = event.getX();
            endY = event.getY();
            gc.clearRect(0, 0, bounds.getWidth(), bounds.getHeight());
            gc.setFill(Color.rgb(0, 0, 0, 0.5));
            gc.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());
            gc.setFill(Color.rgb(255, 255, 255, 0.5));
                        
            gc.fillRect(startX, startY, endX, endY);
            
            stage.setScene(MainApp.getMainScene());
            stage.show();    
           
            //selectedBounds = new Rectangle2D(startX, startY, endX - startX, endY - startY);
            selectedBounds = new Rectangle2D(startX, startY, Utils.roundToNearestInt(endX - startX), Utils.roundToNearestInt( endY - startY));
            selectionMade = true;
            System.out.println("----------111111" + selectedBounds);
        	try {
    			MainAppProps.getInstance().setMonitoredBounds(selectedBounds);
    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        });


        // Add the canvas to a StackPane and create a scene
        selectorStackPane = new StackPane(canvas);
        Scene scene = new Scene(selectorStackPane);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        
    }

    public Rectangle2D getEntireScreenArea(Stage stage) {
    	Rectangle2D screenBounds = null;
    	// Get the bounds of the primary screen
        Screen primaryScreen = Screen.getPrimary();
        screenBounds = primaryScreen.getBounds();
        return screenBounds;
    }
    
    public ArrayList<String> getAttachedDisplays() {
    	ArrayList<String> displays = new ArrayList<String>();
    	displays.add("1");
    	return displays;
    }
    
    public void setSelectedDisplayOnTop(ArrayList<String> displays) {
    	displays.remove(MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.MONITOR_SCREEN_NUM.getKey()));
    	displays.add(0, MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.MONITOR_SCREEN_NUM.getKey()));
    }
}
