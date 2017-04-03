package caveExplorer.maxTracey;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import caveExplorer.*;
import caveExplorer.maxTracey.MaxLaunchpad;

public class MaxGameStart implements Playable {

	public static boolean eventOccurred = false; 
	
	private static final String[] SEQUENCE_1 = {
			"The war started with a tweet...",
			"All hope of diplomacy between nations fell apart.",
			"With no other options, humanity took to the underground to survive.",
			"It is a difficult life, with many perils.",
			"One such peril is falling into abandoned mines.",
			"Legend tells that the mineshafts were enchanted to ward off looters.",
			"To find anything valuable, trespassers must pass a plethora of grueling games.",
			"Every year, dozens of people fall into the mines, never to be seen again.",
			"This is the story of a survivor."
			};
	
	private static final String[] SEQUENCE_2 = {
			"Above you, you can see the hole you fell through.",
			"Your neighbor shouts down from above.",
			"\"Stay calm! It doesn't appear that deep!\"",
			"\"You should be able to build a ladder from what's down there!\"",
			"\"Just find some wood, nails, and a hammer!\"",
			"\"They've gotta be down there. It IS a mineshaft, after all!\"",
			"\"Good luck!\""
			};
	
	public MaxGameStart() {
		
	}
	
	public void play() throws InterruptedException, InvalidMidiDataException, MidiUnavailableException {
		eventOccurred = true;
		readSequenceAuto(SEQUENCE_1, 10, 500);
		System.out.println("- - - press enter - - -");
		CaveExplorer.in.nextLine();
//		readSequenceAuto(SEQUENCE_1, 10, 500);
//		System.out.println("- - - press enter - - -");
//		CaveExplorer.in.nextLine();
		readSequenceAuto(SEQUENCE_2, 10, 500);
		if (CaveExplorer.useLaunchpadInput) {
//			Launchpad.clearPads(Launchpad.launchpad, 15, 0);
//			new Thread() {
//	            public void run() {
						try {
							for (int i = 0; i < 4; i++) {
								
								MaxLaunchpad.chase(MaxLaunchpad.launchpad, MaxLaunchpad.SQUARES_OUTWARD, i, "solid", 100, 0, 0, true);
								MaxLaunchpad.clearPads(MaxLaunchpad.launchpad, 0, 0);
							}
						} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//	            	Thread.yield();
//	            	}
//	            }.start();               
		}
		else {
			Thread.sleep(1000);
		}
		System.out.print("\n\n\n");
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

