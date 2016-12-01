package caveExplorer.jimmyIvan;
//by Jimmy
import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import caveExplorer.CaveExplorer;
import caveExplorer.maxTracey.MaxLaunchpad;

public class FifteenPuzzle {
	public static Scanner in = new Scanner(System.in);
	static String[][] puzzle;
	static int starti;
	static int startj;
	static int currenti;
	static int currentj;
	private static final String cheatCode = "beat15";

	
	public static void startGame() {
		puzzle = new String[4][4];
		int x = 1;
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle[i].length; j++) {
				puzzle[i][j] = ""+x;
				x++;
			}
		}
	
		Randomize.solution(puzzle);
		createNull(puzzle);
		playGame();
	}
	
	private static void createNull(String[][] puzzle) {
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle[i].length; j++) {
				if (puzzle[i][j].equals ("16")) {
					puzzle[i][j] = "";
					starti = i;
					startj = j;
				}
				
			}
		}
	}
	
	private static void playGame() {
		currenti = starti;
		currentj = startj;
		boolean stayInGame = true;
		while (stayInGame) {
			winGame(puzzle);
			printPuzzle(puzzle);
			if (CaveExplorer.useLaunchpadInput) {
				try {
					MaxLaunchpad.clearPads(MaxLaunchpad.launchpad, 0, 0);
					sendToLaunchpad(puzzle);
				} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Enter a direction to slide the tile.");
			String input = in.nextLine();
			if (input.equals(cheatCode)) {
				stayInGame = false;
				
				System.out.println("Cheat code entered.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Exiting game.");
				if (CaveExplorer.useLaunchpadInput) {
//					new Thread() {
//						public void run() {
							try {
								MaxLaunchpad.displayDelay(MaxLaunchpad.launchpad, MaxLaunchpad.SQUARE4X4SOLID, 21, "solid", 25, 0);
								Thread.sleep(1000);
								MaxLaunchpad.clearPads(MaxLaunchpad.launchpad, 0, 50);
							} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							Thread.yield();
//						}
//					}.start();               
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				return;
			}
			else {
				int[] newCoordinates = interpretInput(input);
				starti = newCoordinates[0];
				startj = newCoordinates[1];
				puzzle[starti][startj] = "";
			}
		}
	}
	private static int[] interpretInput(String input) {
		//verify input is valid
		
		while(!isValid(input)){
			System.out.println("Please enter a valid direction to slide: w = up, s = down, a  = left, d = right.");
			input = in.nextLine();
		}
		currenti = starti;
		currentj = startj;
		input = input.toLowerCase();
		checkSpace(input);
		
		int[] newCoordinates = {starti,startj};
		
		if(currenti>=0 && currenti < puzzle.length &&
				currentj >=0 && currentj < puzzle[0].length){
			newCoordinates[0]=currenti;
			newCoordinates[1]=currentj;
		}else{
			System.out.println("Sorry, you cannot slide the tile in this direction anymore.");
		}
		return newCoordinates;
	}
	


	private static void checkSpace(String input) {
		if(input.equals("s")) {
//		if(input.equals("w")) {
			currenti++;
			if (currenti < 4) {
				puzzle[starti][startj] = puzzle[starti+1][startj];
			}
		}
		if(input.equals("w")) {
//		if(input.equals("s")) {
			currenti--;	
			if (currenti > -1) {
				puzzle[starti][startj] = puzzle[starti-1][startj];
			}	
		}
		if(input.equals("d")) {
//		if(input.equals("a")) {
			currentj++;
			if (currentj < 4) {
				puzzle[starti][startj] = puzzle[starti][startj+1];
			}	
		}
		if(input.equals("a")) {
//		if(input.equals("d")) {
			currentj--;
			if (currentj > -1) {
				puzzle[starti][startj] = puzzle[starti][startj-1];
			}	
		}
	}
	
	private static void winGame(String[][] puzzle) {
		
		if (puzzle[0][0].equals("1") && puzzle[0][1].equals("2") && puzzle[0][2].equals("3") && puzzle[0][3].equals("4")
				 && puzzle[1][0].equals("5") && puzzle[1][1].equals("6") && puzzle[1][2].equals("7")
				 && puzzle[1][3].equals("8") && puzzle[2][0].equals("9") && puzzle[2][1].equals("10")
				 && puzzle[2][2].equals("11") && puzzle[2][3].equals("12") && puzzle[3][0].equals("13")
				 && puzzle[3][1].equals("14") && puzzle[3][2].equals("15")) {
			if (CaveExplorer.useLaunchpadInput) {
				new Thread() {
					public void run() {
						try {
							MaxLaunchpad.displayDelay(MaxLaunchpad.launchpad, MaxLaunchpad.SQUARE4X4SOLID, 21, "solid", 25, 0);
							MaxLaunchpad.clearPads(MaxLaunchpad.launchpad, 0, 50);
						} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Thread.yield();
					}
				}.start();               
			}
			System.out.println("You have completed the game! You may escape.");
			System.out.println("The door opens and you leave the room.");
			
		}
	}
	//
	private static boolean isValid(String in) {
		String[] validKeys = {"w","a","s","d"};
		for(String key: validKeys){
			if(in.toLowerCase().equals(key)){
				return true;
			}
		}
		return false;
	}
	
	
	
	private static void printPuzzle(String[][] puzzle) {
		for(String[] row : puzzle) {
			for (String i : row) {
	            System.out.print(i);
	            System.out.print("\t");
	        }
	        System.out.println();

        }
	}
	private static void sendToLaunchpad(String[][] puzzleStringArrArr) throws InterruptedException, InvalidMidiDataException, MidiUnavailableException {
		for (int i = 0; i < puzzleStringArrArr.length; i++) {
			for (int j = 0; j < puzzleStringArrArr[i].length; j++) {
				MaxLaunchpad.display(MaxLaunchpad.launchpad, new int[] {i+2, j+2}, 5, "solid");
			}
		}
		int dispY = currenti;
		if (currenti >= puzzleStringArrArr.length) {
			dispY = puzzleStringArrArr.length - 1;
		}
		else if (currenti < 0) {
			dispY = 0;
		}
		
		int dispX = currentj;
		if (currentj >= puzzleStringArrArr[dispY].length) {
			dispX = puzzleStringArrArr[dispY].length - 1;
		}
		else if (currentj < 0) {
			dispX = 0;
		}
		
		dispY += 2;
		dispX += 2;
		if (0 <= dispY && dispY <= 7 && 0 <= dispX && dispX <= 7) {
			MaxLaunchpad.display(MaxLaunchpad.launchpad, new int[] {dispY, dispX}, 3, "solid");
		}
		
	}
}
