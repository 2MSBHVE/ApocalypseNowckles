package caveExplorer.jimmyIvan;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import caveExplorer.CaveExplorer;
import caveExplorer.Playable;

public class JimmyIvanFifteenPuzzleEvent implements Playable {

	private static final String[] SEQUENCE_1 = {"This room contains the Fifteen Puzzle!", "In order to leave this room, you must complete the puzzle.", "Would you like to play?"};
	private static final String[] SEQUENCE_2 = {"No turning back now."};
	
	public static void main(String[] args) {
		new JimmyIvanFifteenPuzzleEvent().play();
	}
	public void play(){
		try {
			readSequenceAuto(SEQUENCE_1, 20, 100);
		} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("Come on. Say yes!");
		while(CaveExplorer.in.nextLine().
				toLowerCase().indexOf("yes") < 0 ){
			CaveExplorer.print("You must say yes!. If you don't, you can't leave.");
		}
		try {
			readSequenceAuto(SEQUENCE_2, 20, 100);
		} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JimmyFifteenPuzzle.startGame();
	}
	
	public static void readSequence(String[] seq){
		for(String s : seq){
			CaveExplorer.print(s);
			CaveExplorer.print("- - - press enter - - -");
			CaveExplorer.in.nextLine();
		}
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
