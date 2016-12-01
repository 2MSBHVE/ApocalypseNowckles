package caveExplorer.maxTracey;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import caveExplorer.CaveExplorer;
import caveExplorer.Playable;
import caveExplorer.maxTracey.MaxLaunchpad;

public class MaxGetNails implements Playable {

	public static boolean eventOccurred = false; 
	
	private static final String[] SEQUENCE_1 = {
			"A shimmer of light comes from a small metal box across the room.",
			"The box is labeled \"NAILS.\"",
			"You open the box to find it is indeed full of nails.",
			" - - - - press enter to pick up the nails - - - - "
			};
	
	public MaxGetNails() {
		
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
		CaveExplorer.inventory.setHasNails(true);
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
		CaveExplorer.printDelay("You obtained a box of nails!", 20, true);
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
