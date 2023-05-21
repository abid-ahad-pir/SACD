package sopore.net.sacd;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class MainAppController implements Initializable{
    
	private ScreenAreaSelector sac;
	@FXML private ToggleButton tbActivate;	
	@FXML private Text textStatusScreenMonitored;	
	@FXML private Text textStatusActiveNotificationMethods;	
	@FXML private Text textStatusMonitoringSince;
	@FXML private Text textStatusMonitoringEvents;	
    @FXML private TitledPane tpMain;    
    @FXML private TitledPane tpEmail;    
    @FXML private TitledPane tpSound;
    @FXML private ChoiceBox choiceboxScreenSelector;
    @FXML private ChoiceBox choiceboxScreenAreaSelector;
    @FXML private ChoiceBox choiceboxSoundNotificationSelector;
    @FXML private Button buttonSaveTPMain;
    @FXML private Button buttonPlaySelected;
    @FXML private Button buttonSaveTPSound;
    @FXML private TextArea textAreaEventHistory;
    @FXML private Button buttonClearEventHistory;
    @FXML private MenuItem menuItemAbout;
    @FXML private MenuItem menuItemExit;
    @FXML private TextField textfieldPollingIntervalSeconds;
    private DialogPane dialogPaneAbout;
    
    private int numberOfEvents = 0;
    private ScreenAreaMonitor sam = null;
        
    public void initialize() {
       //
    }    
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	sac = new ScreenAreaSelector();
    	sam =  new 	ScreenAreaMonitor();
    	
    	textStatusScreenMonitored.setText(Utils.getTextStatusScreenMonitored());
    	textStatusActiveNotificationMethods.setText(Utils.getTextStatusActiveNotificationMethods());
    	textStatusMonitoringSince.setText(String.valueOf("--"));
    	textStatusMonitoringEvents.setText(String.valueOf(numberOfEvents));
		textfieldPollingIntervalSeconds.setText(MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.MONITOR_POLLING_INTERVAL_SECONDS.getKey()));
		
		
		
		tpMain.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                tpEmail.setExpanded(false);
                tpSound.setExpanded(false);
            }
        });
    	
    	tpEmail.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                tpMain.setExpanded(false);
                tpSound.setExpanded(false);
            }
        });
    	
    	tpSound.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                tpMain.setExpanded(false);
                tpEmail.setExpanded(false);
            }
        });

    	
    	// Monitor Selection Choice Box Related
    	ArrayList displays = sac.getAttachedDisplays();
    	
    	choiceboxScreenSelector.getItems().addAll(displays);
    	choiceboxScreenSelector.getSelectionModel().clearAndSelect(displays.indexOf(MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.MONITOR_SCREEN_NUM.getKey())));
    	
    	choiceboxScreenSelector.setOnAction((event) -> {
    	    String selectedItem = choiceboxScreenSelector.getSelectionModel().getSelectedItem().toString();
    	    MainAppProps.getInstance().setPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.MONITOR_SCREEN_NUM.getKey(), selectedItem);
    	});
    	
    	//Screen Area Choice Box Related
    	choiceboxScreenAreaSelector.getItems().add(MainAppProps.SCREEN_AREA_SELECTION_VALUES.FULL_SCREEN.getDisplayValue());
    	choiceboxScreenAreaSelector.getItems().add(MainAppProps.SCREEN_AREA_SELECTION_VALUES.PARTIAL_SCREEN.getDisplayValue());
    	
    	int selectionIndex;
    	if(MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.MONITOR_MODE.getKey()).contentEquals(MainAppProps.SCREEN_AREA_SELECTION_VALUES.FULL_SCREEN.toString())) 
				selectionIndex = 0 ;				
			else 
				selectionIndex = 1;
				
			
    	choiceboxScreenAreaSelector.getSelectionModel().clearAndSelect(selectionIndex);
    	
    	choiceboxScreenAreaSelector.setOnAction((event) -> {
    		int selectedItemIndex = choiceboxScreenAreaSelector.getSelectionModel().getSelectedIndex();
    	    String selectedItemDisplayStr = choiceboxScreenAreaSelector.getSelectionModel().getSelectedItem().toString();    	    
    	    String selectedItemEnumStr = MainAppProps.SCREEN_AREA_SELECTION_VALUES.getEnumConstant(selectedItemDisplayStr).toString();
    	    if(selectedItemIndex==0) {
    	    	MainAppProps.getInstance().setMonitoredBounds(sac.getEntireScreenArea(MainApp.getPrimaryStage()));
    	    	
    	    }else {
    	    	sac.selectScreenArea(MainApp.getPrimaryStage());
    	    }
    	    
    	    MainAppProps.getInstance().setPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.MONITOR_MODE.getKey(), selectedItemEnumStr);
    	    //textStatusScreenMonitored.setText(Utils.getTextStatusScreenMonitored());
			savePersistentProps();

    	});
    	
    	
    	// Sound WAV File Choice Box Related    	
    	choiceboxSoundNotificationSelector.getItems().add(MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.NOTIFICATION_SOUND_WAV_FILE.getKey()));
    	
    }
    
    @FXML
    public void fillEventHistory(String str) {    	
    	//textarea1EventHistory.appendText(Utils.getCurrentDateAndTimeEnclosed() + str + "\n");
    	textAreaEventHistory.appendText(Utils.getCurrentDateAndTimeEnclosed() + str + "\n");
    }
    
    @FXML
    public void fillApplicationLog(String str) {    	
    	//textarea1ApplicationLog.appendText(Utils.getCurrentDateAndTimeEnclosed() + str + "\n");
    }
    
    
    @FXML
    void onButtonSaveTPMain(ActionEvent event) {
    	savePersistentProps();
    	textStatusScreenMonitored.setText(Utils.getTextStatusScreenMonitored());
    	
    }
    
    @FXML
    void onButtonPlaySelected(ActionEvent event) {
        String audioFilePath = MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.NOTIFICATION_SOUND_WAV_FILE.getKey());
        WAVPlayerQueue.getInstance().addToQueue(audioFilePath);
    }
    
    @FXML
    void onButtonSaveTPSound(ActionEvent event) {
    	//savePersistentProps();
    	//textStatusScreenMonitored.setText(Utils.getTextStatusScreenMonitored());
    	
    }

    @FXML
    void actionTextfieldPollingIntervalSeconds() {
    	MainAppProps.getInstance().setPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.MONITOR_POLLING_INTERVAL_SECONDS.getKey(), textfieldPollingIntervalSeconds.getText());
    }

   
    public void savePersistentProps() {
    	try {
			MainAppProps.getInstance().savePersistentProps();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    @FXML
    void actionTBActivate(ActionEvent event) {
    	Boolean armed = tbActivate.isSelected() ? true:false;
    	Utils.START_MONITORING=armed;
    	if(armed) {
    		sam.init(this, null);
    		WAVPlayerQueue.getInstance().resumeWork();
    		tbActivate.setText(MainAppProps.ToggleButtonActiveLabel);
    		textStatusMonitoringSince.setText(Utils.getCurrentDateAndTime());
    		
    	}
    	else { 
    		WAVPlayerQueue.getInstance().stopAllWork();
    		tbActivate.setText(MainAppProps.ToggleButtonInActiveLabel);
    		textStatusMonitoringSince.setText("");
    	}
    }
    
    @FXML
    void actionClearEventHistory(ActionEvent event){
    	textAreaEventHistory.clear();
    }
    
    @FXML
    void actionExit(ActionEvent event) {
    	Platform.exit();
    }
    
    @FXML
    void actionAbout(ActionEvent event) {
    	
    	Alert aboutDialog = new Alert(AlertType.INFORMATION, "Content here", ButtonType.OK);
    	aboutDialog.setTitle(MainAppProps.ApplicationTitle);
        aboutDialog.setHeaderText(MainAppProps.AboutHeader3);
        aboutDialog.setContentText(MainAppProps.LicenseContent1 + MainAppProps.LicenseContent2 + MainAppProps.LicenseContent3);
        aboutDialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        
        aboutDialog.showAndWait();
    }
    
	public int triggeredEvents() {
		return numberOfEvents;
	}
	
	public int resetTriggeredEvents() {
		numberOfEvents = 0;
		textStatusMonitoringEvents.setText(String.valueOf(numberOfEvents));
		return 0;
	}
	
	public int incrementTriggeredEvents() {
		textStatusMonitoringEvents.setText(String.valueOf(numberOfEvents));
		return ++numberOfEvents;
	}

}
