package sopore.net.sacd;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Rectangle2D;

public class Utils {
	
	private static String _DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static String _OPENING_STR = "<";
	private static String _CLOSING_STR = ">";
	public static volatile boolean START_MONITORING = false; 
	
	public static String getCurrentDateAndTime() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the desired date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(_DATE_FORMAT);

        // Format the date and time using the formatter
        String formattedDateTime = now.format(formatter);

        return formattedDateTime;
    }
    
    public static String getCurrentDateAndTimeEnclosed() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(_OPENING_STR);
    	sb.append(getCurrentDateAndTime());
    	sb.append(_CLOSING_STR);
        return  sb.toString();
    }
    
    
    public static String getDisplayRectangle2DInStr(Rectangle2D bounds) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("X:" + bounds.getMinX() + " ");
    	sb.append("Y:" + bounds.getMinY() + " ");
    	sb.append("W:" + bounds.getWidth() + " ");
    	sb.append("H:" + bounds.getHeight());
    	return sb.toString();
    	
    }
    
    public static String getTextStatusScreenMonitored() {
    	return new String(MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.MONITOR_SCREEN_NUM.getKey()) + " (" + MainAppProps.SCREEN_AREA_SELECTION_VALUES.valueOf(MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.MONITOR_MODE.getKey())).getDisplayValue() + " " +  getDisplayRectangle2DInStr(MainAppProps.getInstance().getMonitoredBounds()) + ")");
    	
    }

	public static String getTextStatusActiveNotificationMethods() {
		return new String(MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.NOTIFICATION_ACTIVE_METHODS.getKey()));
	}

	public static double roundToNearestInt(Double val) {		
		double roundedValue = (int) Math.round(val);
		return roundedValue;
	}
}


