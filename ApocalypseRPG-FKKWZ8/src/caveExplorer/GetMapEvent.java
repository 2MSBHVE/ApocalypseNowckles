package caveExplorer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class GetMapEvent implements Playable {

	public static boolean eventOccurred = false; 
	
	private static final String[] SEQUENCE_1 = {
			"[A little yellow mouse with brown stripes and a lightning shaped tail runs up to you.]",
			"Hi. I can see you're not from around here.",
			"Do you like puzzles?"
			};
	
	private static final String[] SEQUENCE_2 = {
			"You are going to have so much fun playing my 2D games.",
			"Take this map!"
			};
	
	public GetMapEvent() {
		
	}
	
	public void play() throws InterruptedException, InvalidMidiDataException, MidiUnavailableException {
		eventOccurred = true;
//		if (CaveExplorer.useLaunchpadInput) {
//			Launchpad.clearPads(Launchpad.launchpad, 15, 0);
//			new Thread() {
//	            public void run() {
//						try {
//							Launchpad.flashImg(Launchpad.launchpad, Launchpad.messageOutline, 3, 250, 250, 3, 0, 50, 0, 50, false);
//						} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//	            	Thread.yield();
//	            	}
//	            }.start();               
//		}
//		readSequence(SEQUENCE_1, 20);
//		if (CaveExplorer.useLaunchpadInput) {
//			Launchpad.clearPads(Launchpad.launchpad, 0, 0);
//			new Thread() {
//	            public void run() {
//						try {
//							Launchpad.flashImg(Launchpad.launchpad, Launchpad.questionMark, 3, 250, 250, 3, 0, 25, 0, 25, false);
//						} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//	            	Thread.yield();
//	            	}
//	            }.start();               
//		}
//		while (CaveExplorer.in.nextLine().toLowerCase().indexOf("yes") < 0) {
//			CaveExplorer.printDelay("C'mon! You know you like puzzles! Say yes!", 20, true);
//		}
//		readSequence(SEQUENCE_2, 10);
//		Thread.sleep(500);
		CaveExplorer.inventory.setHasMap(true);
//		if (CaveExplorer.useLaunchpadInput) {
//			Launchpad.clearPads(Launchpad.launchpad, 0, 0);
//			new Thread() {
//	            public void run() {
//	            	try {
//	            		Launchpad.flashImg(Launchpad.launchpad, Launchpad.plus6x6, 13, 67, 125, 5, 0, 0, 0, 0, false);
//	            	} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
//	            		// TODO Auto-generated catch block
//	            		e.printStackTrace();
//	            	}
//	            	Thread.yield();
//	            	}
//	            }.start();               
//		}
		CaveExplorer.printDelay("You obtained a map!", 20, true);
//		Thread.sleep(2000);
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

}