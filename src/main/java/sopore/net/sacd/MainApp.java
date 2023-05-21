package sopore.net.sacd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    private static Stage _PRIMARY_STAGE;
    private static Scene _MAIN_SCENE;

    protected static Stage getPrimaryStage() {
		return _PRIMARY_STAGE;
	}
    
    protected static Scene getMainScene() {
		return _MAIN_SCENE;
	}
    
    public static void loadFXML(ClassLoader classLoader, Stage stage, String fxmlFile, Boolean startUp) throws Exception{

        // Load the FXML file
    	Parent root = FXMLLoader.load(classLoader.getResource(fxmlFile));
        
    	// Create a new Scene with the loaded FXML file
    	_MAIN_SCENE = new Scene(root);
        //scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
        
        if(startUp) {
            //Misc
        	stage.setTitle(MainAppProps.ApplicationTitle);
            stage.setResizable(false);
        }
        
        // Set the new Scene on the Stage
        stage.setScene(_MAIN_SCENE);
    }
    
    public static void loadFXML(ClassLoader classLoader, String fxmlFile, Boolean startUp) throws Exception{
		loadFXML(classLoader, _PRIMARY_STAGE, fxmlFile, startUp);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
    	 // Save the Stage object for later use
    	_PRIMARY_STAGE = stage;
    	
    	//Initialize application configuration 
    	MainAppProps.getInstance().loadPersistentProps();
        
    	// Load the first FXML file
    	loadFXML(getClass().getClassLoader(), stage, MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.UI_FXML_FILE.getKey()), true);
    	
    	// Show the Stage
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}