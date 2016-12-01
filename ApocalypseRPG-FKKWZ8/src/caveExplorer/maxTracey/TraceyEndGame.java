package caveExplorer.maxTracey;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import caveExplorer.*;
import caveExplorer.maxTracey.MaxLaunchpad;

public class TraceyEndGame implements Playable {

	public static boolean eventOccurred = false; 
	
	private static final String[] SEQUENCE_1 = {
			"You return to the hole you fell through.",
			"The room is just big enough for you to construct your ladder and raise it.",
			"You escape the mineshaft.",
			};
	
	public TraceyEndGame() {
		
	}
	
	public void play() throws InterruptedException, InvalidMidiDataException, MidiUnavailableException {
		eventOccurred = true;
		readSequenceAuto(SEQUENCE_1, 30, 1000);
		if (CaveExplorer.useLaunchpadInput) {
			MaxLaunchpad.clearPads(MaxLaunchpad.launchpad, 15, 0);
//			new Thread() {
//	            public void run() {
	            	try {
	            		MaxLaunchpad.chase(MaxLaunchpad.launchpad, MaxLaunchpad.SQUARES_OUTWARD, 21, "solid", 100, 0, 0, false);
	            	} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
	            		// TODO Auto-generated catch block
	            		e.printStackTrace();
	            	}
//	            	Thread.yield();
//	            }
//			}.start();               
		}
//		System.out.println("you win");
		CaveExplorer.printDelay("\n\n\n\nYou win.", 333, false);
		System.exit(0);
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
			if (s < seq.length - 1) {
				Thread.sleep(stringDelay);
			}
		}
	}

}

