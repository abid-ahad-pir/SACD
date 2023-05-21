package sopore.net.sacd;


import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.robot.Robot;

public class ScreenAreaMonitor 	 {

    private Image lastImage;
    private MainAppController uiController;


    public void init(MainAppController uiController, Stage primaryStage) {
        this.uiController = uiController;
    	Long pollInterval = Long.parseLong(MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.MONITOR_POLLING_INTERVAL_SECONDS.getKey()));
        
        Thread monitorThread = new Thread(() -> {
            while (Utils.START_MONITORING) {
                Platform.runLater(this::captureScreenAndCheckChanges);

                try {
                	Thread.sleep(pollInterval*1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    private void captureScreenAndCheckChanges() {
    	Image currentImage = captureScreenArea(MainAppProps.getInstance().getMonitoredBounds());
    	if (lastImage == null || !imagesEqual(lastImage, currentImage)) {
            lastImage = currentImage;

            // Screen area has changed, perform desired actions here
            sentAlertNotification();
            
        }
    }

    private void sentAlertNotification() {
    	System.out.println("Change detected on screen.");
    	uiController.fillEventHistory("Change detected on screen");
        String audioFilePath = MainAppProps.getInstance().getPersistenPropertyValue(MainAppProps.PERSISTENT_PROP_KEYS.NOTIFICATION_SOUND_WAV_FILE.getKey());
        uiController.incrementTriggeredEvents();
        WAVPlayerQueue.getInstance().addToQueue(audioFilePath);
	}

	private Image captureScreenArea(Rectangle2D area) {
    	Robot robot = new Robot();
        Image imgReturn = robot.getScreenCapture(null, area);
        return imgReturn;
    }

    private boolean imagesEqual(Image img1, Image img2) {
    	if (img1 == null || img2 == null) {
            return false;
        }

        int width = (int) img1.getWidth();
        int height = (int) img1.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color1 = img1.getPixelReader().getColor(x, y);
                Color color2 = img2.getPixelReader().getColor(x, y);
                if (!color1.equals(color2)) {
                    return false;
                }
            }
        }

        return true;
    }
}
