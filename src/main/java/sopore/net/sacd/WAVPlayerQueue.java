package sopore.net.sacd;

import java.io.File;
import java.util.ArrayList;

public class WAVPlayerQueue {

	
	private int threadCouter = 0;
	private ArrayList<WAVPlayer> queue;
	private static WAVPlayerQueue wpqInstance = null;
	private static final String ThreadName_Prefix = "SACD.WAVPlayer";
	
	
	private WAVPlayerQueue(){
		queue = new ArrayList<WAVPlayer>();
	}
	
	public static WAVPlayerQueue getInstance() {
		if(wpqInstance == null)
			wpqInstance = new WAVPlayerQueue();
		return wpqInstance;
	}
	
	
	//Currently there is no rate limiting or sequencing.
	public int addToQueue(String audioFilePath) {
		int returnVal = -1;
		WAVPlayer wavPlayer = null;
		try{
			File audioFile = new File(audioFilePath);
			if(audioFile.exists() && audioFile.canRead()) {
				wavPlayer = new WAVPlayer(generateNewThreadID(), audioFile);
				queue.add(wavPlayer);
				returnVal = queue.indexOf(wavPlayer);
				wavPlayer.start();	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return returnVal;
	}
	
	public boolean removeFromQueue(int index) {
		return queue.remove(index)!=null ? true :  false;
	}
	
	public void clearQueue() {
		queue.clear();
	}
	
	
	public void stopAllWork() {
		WAVPlayer.exitNow = true;
		clearQueue();
		for(Thread thread : queue) {
			thread.interrupt();
		}
	}
	
	public void resumeWork() {
		WAVPlayer.exitNow = false;
	}
	
	private String generateNewThreadID() {
		return  ThreadName_Prefix + ++threadCouter;
	}
}
