package sopore.net.sacd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javafx.geometry.Rectangle2D;

public class MainAppProps {
	
	static final String ApplicationTitle = "Screen Area Change Detector - SACD";
	static final String AboutHeader1 = "\nAbout";
	static final String AboutHeader2 = "\nLicense Terms\n\n\n";
	static final String AboutHeader3 = "\nDeveloped by abid.ahad.pir@gmail.com  - 2023 \n\nSACD v1.1 (Beta) ";
	
	static final String LicenseContent1 = "This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.\r\n\n";
	
	static final String LicenseContent2 = "This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.\r\n\n";	
	
	static final String LicenseContent3 = "You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.\r\n\n";


	
	
	
	static final String ToggleButtonActiveLabel = "Active";
	static final String ToggleButtonInActiveLabel = "In-Active";	
	private Rectangle2D monitoredBounds  = null;	
	private static MainAppProps mainAppProps = null;
	public Properties  persistentProps = null;
	private Properties runtimeProps = null;
	
	public static enum PERSISTENT_PROP_KEYS {
		APPLICATION_PROPERTY_FILE("APPLICATION_PROPERTY_FILE"),
		UI_FXML_FILE ("UI_FXML_FILE"),
		MONITOR_POLLING_INTERVAL_SECONDS ("MONITOR_POLLING_INTERVAL_SECONDS"),
		MONITOR_SCREEN_NUM ("MONITOR_SCREEN_NUM"),
		MONITOR_MODE("MONITOR_MODE"),
		MONITOR_RECT_STARTX("MONITOR_RECT_STARTX"),
		MONITOR_RECT_STARTY("MONITOR_RECT_STARTY"),
		MONITOR_RECT_WIDTH("MONITOR_RECT_WIDTH"),
		MONITOR_RECT_HEIGHT("MONITOR_RECT_HEIGHT"),
		NOTIFICATION_ACTIVE_METHODS("NOTIFICATION_ACTIVE_METHODS"),
		NOTIFICATION_SOUND_WAV_FILE("NOTIFICATION_SOUND_WAV_FILE"),
		NOTIFICATION_EMAIL_SMTP_HOST("NOTIFICATION_EMAIL_SMTP_HOST"),
		NOTIFICATION_EMAIL_SMTP_PORT("NOTIFICATION_EMAIL_SMTP_PORT"),
		NOTIFICATION_EMAIL_RECEPIENTS("NOTIFICATION_EMAIL_RECEPIENTS");
		
	    private final String key;

	    PERSISTENT_PROP_KEYS(String key) {
	        this.key = key;
	    }

	    public String getKey() {
	        return key;
	    }
	}
	
	public static enum NONPERSISTENT_PROP_KEYS {
		STATUS_MONITORING_SINCE("STATUS_MONITORING_SINCE"),
		STATUS_NOTIFICATION_EVENTS("STATUS_NOTIFICATION_EVENTS");

	    private final String key;

	    NONPERSISTENT_PROP_KEYS(String key) {
	        this.key = key;
	    }

	    public String getKey() {
	        return key;
	    }
	}
	
	public static enum SCREEN_AREA_SELECTION_VALUES {
		FULL_SCREEN("Full Screen"),
		PARTIAL_SCREEN("Screen Area");

	    private final String value;

	    SCREEN_AREA_SELECTION_VALUES(String value) {
	        this.value = value;
	    }
	    
	    public static SCREEN_AREA_SELECTION_VALUES getEnumConstant(String value) {
	        for (SCREEN_AREA_SELECTION_VALUES e : values()) {
	            if (e.value.equals(value)) {
	                return e;
	            }
	        }
	        return null;
	    }

	    public String getDisplayValue() {
	    	return value;
	    }
	}
	
	//Constructor
	private MainAppProps(){
		this.persistentProps = new Properties();
		this.runtimeProps = new Properties();
	}
	
	//Make this Singleton Class
	static MainAppProps getInstance() {
		if(mainAppProps == null) {
			mainAppProps =  new MainAppProps();	
			mainAppProps.loadHardcodedPersistentProps();			
		}
		return mainAppProps;
	}
	
	private void loadHardcodedPersistentProps() {			
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.APPLICATION_PROPERTY_FILE.getKey(), "application.properties");
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.UI_FXML_FILE.getKey(), "sacd-main.fxml");
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.MONITOR_POLLING_INTERVAL_SECONDS.getKey(), "5");
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.MONITOR_SCREEN_NUM.getKey(), "1");
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.MONITOR_MODE.getKey(), SCREEN_AREA_SELECTION_VALUES.PARTIAL_SCREEN.toString());
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_STARTX.getKey(), "0");
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_STARTY.getKey(), "0");
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_WIDTH.getKey(), "0");
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_HEIGHT.getKey(), "0");
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.NOTIFICATION_ACTIVE_METHODS.getKey(), "SOUND");
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.NOTIFICATION_SOUND_WAV_FILE.getKey(), "sacd.wav");
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.NOTIFICATION_EMAIL_SMTP_HOST.getKey(), "DUMMY-HOST");
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.NOTIFICATION_EMAIL_SMTP_PORT.getKey(), "12345");
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.NOTIFICATION_EMAIL_RECEPIENTS.getKey(), "dumm1@dummy.com,dummy2@dummy.com");
		monitoredBounds = new Rectangle2D(Double.parseDouble(persistentProps.getProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_STARTX.getKey())),Double.parseDouble(persistentProps.getProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_STARTY.getKey())),Double.parseDouble(persistentProps.getProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_WIDTH.getKey())),Double.parseDouble(persistentProps.getProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_HEIGHT.getKey())));
	}
	
	public void loadPersistentProps() throws IOException {   	
		loadPersistentePropsFromFile();    	
		recalculateInMemoryProps();    	
	}
	
	private void loadPersistentePropsFromFile() throws IOException{
		// Load the properties from the file
        Properties properties = new Properties();
        File file = new File(persistentProps.getProperty(PERSISTENT_PROP_KEYS.APPLICATION_PROPERTY_FILE.getKey()));        
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() +"----------->" + file.getAbsolutePath());
        FileInputStream inputStream = new FileInputStream(file);
        properties.load(inputStream);
        inputStream.close();
        // Set the value of a property
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + ">" + key +":" + value);
            persistentProps.setProperty(key, value);
        }
	}	
	
	public void savePersistentProps() throws IOException {
		recalculateInMemoryProps();
		savePersistentPropsToFile();
	}
	
	private void savePersistentPropsToFile() throws IOException {
		 	// Save the properties to the file
			File file = new File(persistentProps.getProperty(PERSISTENT_PROP_KEYS.APPLICATION_PROPERTY_FILE.getKey()));        	
            FileOutputStream outputStream = new FileOutputStream(file);
            System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() +" ----------->" + file.getAbsolutePath());
            persistentProps.store(outputStream, "Updated properties on " + Utils.getCurrentDateAndTime());;
            outputStream.close();
	}
	
	public void recalculateInMemoryProps() {
		monitoredBounds = new Rectangle2D(Double.parseDouble(persistentProps.getProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_STARTX.getKey())),Double.parseDouble(persistentProps.getProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_STARTY.getKey())),Double.parseDouble(persistentProps.getProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_WIDTH.getKey())),Double.parseDouble(persistentProps.getProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_HEIGHT.getKey())));
	}
	
	public String getPersistenPropertyValue(String key) {
		return persistentProps.getProperty(key);
	}
	
	public void setPersistenPropertyValue(String key, String val) {
		persistentProps.setProperty(key, val);
	}

	public String getRuntimePropertyValue(String key) {
		return runtimeProps.getProperty(key);
	}
	
	public void setRuntimePropertyValue(String key, String val) {
		runtimeProps.setProperty(key, val);
	}
	
	public Rectangle2D getMonitoredBounds() {
		return monitoredBounds;
	}
	
	public void setMonitoredBounds(Rectangle2D bounds) {
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_STARTX.getKey(), String.valueOf(bounds.getMinX()));
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_STARTY.getKey(), String.valueOf(bounds.getMinY()));
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_WIDTH.getKey(), String.valueOf(bounds.getWidth()));
		persistentProps.setProperty(PERSISTENT_PROP_KEYS.MONITOR_RECT_HEIGHT.getKey(), String.valueOf(bounds.getHeight()));
		monitoredBounds = bounds;
	}
}
