package caveExplorer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import caveExplorer.maxTracey.MaxLaunchpad;

public class GetMapEvent implements Playable {

	public static boolean eventOccurred = false; 
	
	private static final String[] SEQUENCE_1 = {
			"You hear the crunch of paper under your foot.",
			"You look down and see what appears to be a map.",
			" - - - - press enter to pick up the map - - - - "
			};
	
	private static final String[] SEQUENCE_2 = {
			"Jackpot! You found a map of the mineshaft!"
			};
	
	public GetMapEvent() {
		
	}
	
	public void play() throws InterruptedException, InvalidMidiDataException, MidiUnavailableException {
		eventOccurred = true;
		if (CaveExplorer.useLaunchpadInput) {
			MaxLaunchpad.clearPads(MaxLaunchpad.launchpad, 15, 0);
			new Thread() {
	            public void run() {
						try {
							MaxLaunchpad.flashImg(MaxLaunchpad.launchpad, MaxLaunchpad.exclamationMark, 3, 250, 250, 3, 0, 50, 0, 50, false);
						} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	Thread.yield();
	            	}
	            }.start();               
		}
		readSequenceAuto(SEQUENCE_1, 20, 500);
		CaveExplorer.in.nextLine();
		readSequenceAuto(SEQUENCE_2, 20, 500);
		Thread.sleep(500);
		CaveExplorer.inventory.setHasMap(true);
		if (CaveExplorer.useLaunchpadInput) {
			MaxLaunchpad.clearPads(MaxLaunchpad.launchpad, 0, 0);
			new Thread() {
	            public void run() {
	            	try {
	            		MaxLaunchpad.flashImg(MaxLaunchpad.launchpad, MaxLaunchpad.plus6x6, 13, 67, 125, 5, 0, 0, 0, 0, false);
	            	} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
	            		// TODO Auto-generated catch block
	            		e.printStackTrace();
	            	}
	            	Thread.yield();
	            	}
	            }.start();               
		}
		CaveExplorer.printDelay("You obtained a map!", 20, true);
		Thread.sleep(2000);
	}
	
	public static void readSequence(String[] seq, long charDelay) throws InterruptedException, InvalidMidiDataException, MidiUnavailableException{
		for (int s = 0; s < seq.length - 1; s++) {
			CaveExplorer.printDelay(seq[s], charDelay, true);
			System.out.println("- - - press enter - - -");
			if(CaveExplorer.in.nextLine().toLowerCase().indexOf("skip") >= 0){
				System.out.println("Dialogue sequence skipped.");
				return;
			}
		}
		CaveExplorer.printDelay(seq[seq.length - 1], charDelay, true);
		System.out.print("\n");
	}
	
	public static void readSequenceAuto(String[] seq, long charDelay, long stringDelay) throws InterruptedException, InvalidMidiDataException, MidiUnavailableException{
		for (int s = 0; s < seq.length; s++) {
			CaveExplorer.printDelay(seq[s], charDelay, true);
			Thread.sleep(stringDelay);
		}
	}

}
