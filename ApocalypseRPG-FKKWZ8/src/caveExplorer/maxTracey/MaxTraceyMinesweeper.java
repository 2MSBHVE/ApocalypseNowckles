package caveExplorer.maxTracey;

import java.util.Iterator;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

import caveExplorer.*;

public class MaxTraceyMinesweeper implements Playable {

	public static boolean eventOccurred = false;
	
//	protected static final String cheatCode = "beatMinesweeper";
	protected static final String cheatCode = "LRLRS&D&U&TBBABS";
	protected static final String loseCode = "loseMinesweeper";
	
	private static final String[] instructionsBasic = {
			"Welcome to Minesweeper!",
			"Type a space such as \"A4\" or \"5B\" to reveal that space.",
			"Type \"mark C6\" to mark or unmark a space.",
			"A shield will protect you if you detonate a mine."
			};
	
	private static final String[] instructionsLPplusConsole = {
			"Unrevealed spaces display as #.",
			"They will not appear on the Launchpad until they are revealed or marked.\n",
			
			"Revealed spaces that are not mines will display the number of mines surrounding them.",
			"They will appear green on the Launchpad.\n",
			
			"Revealed or detonated mines will display as X.",
			"They will appear red on the Launchpad.\n",
			
			"Marked spaces will display as &.",
			"They will appear orange on the Launchpad."
			};
	
	private static final String[] instructionsConsole = {
			"Unrevealed spaces display as #.",
//			"They will not appear on the Launchpad until they are revealed or marked.",
			
			"Revealed spaces that are not mines will display the number of mines surrounding them.",
//			"They will appear green on the Launchpad",
			
			"Revealed or detonated mines will display as X.",
//			"They will appear red on the Launchpad.",
			
			"Marked spaces will display as &."
//			"They will appear orange on the Launchpad."
			};
	
	protected static boolean gameInProgress;
	
	
	static String[][] grid;
	
//	static Scanner in = new Scanner(System.in);
	static int numberMines;
	static boolean[][] mines;
	static boolean[][] revealed;
	static boolean[][] marked;
	static int SHIELDS_ORIG;
	static int shields;
	
//	fromSimon: {MineReveal,ExtraShield,OtherPowerup}
	
	public MaxTraceyMinesweeper(int numRows, int numCols, int numMines, int numShields) {
		SHIELDS_ORIG = numShields;
		numberMines = numMines;

		mines = new boolean[numRows][numCols];
		revealed = new boolean[numRows][numCols];
		
		//number of mines is given to the player
		//each time the player marks a mine, 
		//numOfMines minus 1 and
		//if the marked array is equal to the mines array then the player wins
		marked = new boolean[mines.length][mines[0].length];
		
	}
	
	public void play() throws InterruptedException, InvalidMidiDataException, MidiUnavailableException {
		int minesL = mines.length;
		int minesW = mines[0].length;
		mines = new boolean[minesL][minesW];
		revealed = new boolean[minesL][minesW];
		marked = new boolean[mines.length][mines[0].length];
		
		plantMines(mines, numberMines);
		
		shields = SHIELDS_ORIG;
		
		if (CaveExplorer.useLaunchpadInput) {
			new Thread() {
				public void run() {
					try {
						MaxLaunchpad.fillPads(MaxLaunchpad.launchpad, 13, "solid", 0, 50);
						MaxLaunchpad.clearPads(MaxLaunchpad.launchpad, 0, 50);
					} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Thread.yield();
				}
			}.start();               
		}

		this.eventOccurred = true;
//		Scanner inMS = new Scanner(System.in); 
		gameInProgress = true;

		
		printStrSeq(instructionsBasic);
		System.out.println("\n");
		if (CaveExplorer.useLaunchpadInput) {
			printStrSeq(instructionsLPplusConsole);
		}
		else {
			printStrSeq(instructionsConsole);
		}
		System.out.println("\n");
		
		if (caveExplorer.simon.SimonRoom.powerUps[0]) {
			int minesToReveal = 1;
			autoRevealMines(minesToReveal, mines, revealed);
			CaveExplorer.printDelay("From the card-matching game you discovered the location of " + minesToReveal + " mine", 30, false);
			if (minesToReveal != 1 && minesToReveal != -1) {
				CaveExplorer.printDelay("s", 30, false);
			}
			CaveExplorer.printDelay("!", 30, false);
			Thread.sleep(1000);
			System.out.println("\n\n");
		}
		
		if (caveExplorer.simon.SimonRoom.powerUps[1]) {
			shields++;
			CaveExplorer.printDelay("From the card-matching game you received an additional shield!", 30, false);
			Thread.sleep(500);
			System.out.println("\n");
		}
		System.out.println("You have " + shields + " shields.");
		Thread.sleep(1000);
		System.out.println("\n\n");
//		readSequence(SEQUENCE_1, 20);
		
		while(gameInProgress){
//			IF THE GAME IS WON
			if (checkArraysEqual(mines, marked)) {
				gameInProgress = false;
				endAnimationLP(MaxLaunchpad.launchpad, true);
				return;
			}
			
//			boolean[][] temp = new boolean[mines.length][mines[0].length];
//			for (int i = 0; i < mines.length; i++) {
//				for (int j = 0; j < mines[i].length; j++) {
//					temp[i][j] = true;
//				}
//			}
//			String[][] field = createField(mines, temp);
			
			String minePlural = "mine";
			if (countUnrevealedMines(mines, revealed) != 1) {
				minePlural += "s";
			}
			System.out.println(countUnrevealedMines(mines, revealed) + " " + minePlural + " remaining.\n");
			
			String[][] field = createField(mines, revealed);
			printField(field);
			
			if (CaveExplorer.useLaunchpadInput && mines.length <= 8 && mines[0].length <= 8) {
				new Thread() {
		            public void run() {
							try {
								sendToLaunchpad(mines, marked, revealed);
							} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		            	Thread.yield();
		            	}
		            }.start();               
			}
			
//			System.out.println("enter");
			String input = CaveExplorer.in.nextLine();
//			while (!isValidSpace(toGridSpace(input))) {
			while (!validateAndMarkInput(input, true)) {
				if (input.indexOf(cheatCode) < 0 && input.indexOf(loseCode) < 0) {
					System.out.println("Invalid input. Try again");
					input = CaveExplorer.in.nextLine();
				}
				else {
					System.out.println("Cheat code entered. Exiting.");
					if (input.indexOf(cheatCode) >= 0) {
						System.out.print("(Yes, that was the cheat code.)     ");
						Thread.sleep(2000);
						System.out.print("(I'm a shameless weeb.)");
						Thread.sleep(1000);
						System.out.print("\n");
					}
//					for (int i = 0; i < revealed.length; i++) {
//						for (int j = 0; j < field.length; j++) {
//							
//						}
//						revealSpace([, true);
//					}
					gameInProgress = false;
					if (input.indexOf(cheatCode) >= 0) {
						endAnimationLP(MaxLaunchpad.launchpad, true);
						return;
					}
					else {
						loseGame();
						endAnimationLP(MaxLaunchpad.launchpad, false);
//						input = "A1";
						break;
					}
				}
			}
			if (isValidSpace(toGridSpace(input))) {
				
				int[] enteredSpace = toGridSpace(input);
				int enteredR = toGridSpace(input)[0];
				int enteredC = toGridSpace(input)[1];
//				System.out.println(enteredR + " " + enteredC);
				
				revealSpace(enteredSpace, new int[] {-1,-1}, false);
			
			}
			
			if (shields < 0) {
				loseGame();
				break;
			}
			
			System.out.println("You have " + shields + " shields remaining.");
		}
		
		for(int r=0; r<mines.length; r++){
			for(int c=0; c<mines[r].length; c++){
				if(mines[r][c]){
//					revealed[r][c]=true;
					revealSpace(new int[] {r, c}, new int[] {-1,-1}, true);
				}
			}
		}
		
		String[][] field = createField(mines, revealed);
		printField(field);
		
	}

	private int countUnrevealedMines(boolean[][] minesArr, boolean[][] revealedArr) {
		int count = 0;
		for (int i = 0; i < minesArr.length; i++) {
			for (int j = 0; j < minesArr[i].length; j++) {
				if (minesArr[i][j] && !revealedArr[i][j]) {
					count++;
				}
			}
		}
		return count;
	}

	private void endAnimationLP(MidiDevice device, boolean win) {
		if (CaveExplorer.useLaunchpadInput) {
//			new Thread() {
//	            public void run() {
						int color = 5;
						if (win) {
							color = 21;
						}
						try {
							MaxLaunchpad.fillPads(device, color, "solid", 0, 25);
							MaxLaunchpad.clearPads(device, 0, 25);
							Thread.sleep(500);
						} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//	            	Thread.yield();
//	            	}
//	            }.start();               
		}
	}

	private void printStrSeq(String[] stringArr) {
		for (String string : stringArr) {
			System.out.println(string);
		}
	}

	private static boolean validateAndMarkInput(String input, boolean markOnRun){
		input = input.toLowerCase();
		String input2 = new String();
		input2 = input;
		if(input.indexOf("mark") >= 0 && markOnRun){
			input2 = input.replace("mark", "").trim();
//			System.out.println(input2);
			
//			if (isValidSpace(toGridSpace(input2))) {
//				int mR = toGridSpace(input2)[0];
//				int mC = toGridSpace(input2)[1];
//				marked[mR][mC] = !marked[mR][mC];
//				return true;
//			}
			
			if (isValidSpace(toGridSpace(input2))) {
				markSpace(toGridSpace(input2));
				return true;
			}
			else {
				return false;
			}
		}
		
		else {
			return isValidSpace(toGridSpace(input));
		}
			
		
//		return false;
	}
	
	private static void markSpace(int[] space) {
	
		if (isValidSpace(space)) {
			int mR = space[0];
			int mC = space[1];
			if (revealed[mR][mC]) {
				marked[mR][mC] = mines[mR][mC];
			}
			else {
				if (revealed[mR][mC] && !mines[mR][mC]) {
					marked[mR][mC] = false;
					System.out.println("Space " + toLtr(mR) + "" + (mC + 1) + " unmarked");
				}
				else {
					marked[mR][mC] = !marked[mR][mC];
					String toOut = "Space " + toLtr(mR) + "" + (mC + 1) + " ";
					if (!marked[mR][mC]) {
						toOut += "un";
					}
					toOut += "marked.";
					System.out.println(toOut);
				}
			}
		}
		
	}
	
	private static char toLtr(int mC) {
		return (char) (65+mC);
	}

	private static boolean checkArraysEqual(boolean[][] arr1, boolean[][] arr2){
		
		for (int r = 0; r < arr1.length; r++) {
			for (int c = 0; c < arr1[r].length; c++) {
				if (arr1[r][c] != arr2[r][c]) {
					return false;
				}
			}
		}
		return true;
	}

	private static void loseGame() throws InterruptedException, InvalidMidiDataException, MidiUnavailableException {
		gameInProgress = false;
		eventOccurred = false;
		CaveExplorer.msRoom.eventHappened = false;
//		MaxTraceyMinesweeper.eventOccurred = false;
		
//		throw user to previous room
//		String[] keys = {"w", "d", "s", "a"};
		caveExplorer.CaveExplorer.currentRoom.goToRoom(1);
	}

	private static boolean isValidSpace(int[] inArr) {
		int r = inArr[0];
		int c = inArr[1];
		
		if ((0 <= r) && (r < mines.length)) {
			if ((0 <= c) && (c < mines[0].length)) {
				return true;
			}
		}
		return false;
	}
	
	private static int[] toGridSpace(String input) {
		char rowLtr = 0;
		input = input.toUpperCase();
		int count = 0;
		for (int i = 0; i < input.length(); i++) {
			if (((int)(input.charAt(i)) >= 65) && ((int)(input.charAt(i)) <= 90)) {
				rowLtr = input.charAt(i);
				break;
			}
		}
		
//		int numStart;
		int numStartIdx = -1;
		
		for (int i = 0; i < input.length(); i++) {
			if (((int)(input.charAt(i)) >= 49) && ((int)(input.charAt(i)) <= 57)) {
//				numStart = (int)(input.charAt(i));
				numStartIdx = i;
				break;
			}
		}
//		int numEnd;
		int numEndIdx = numStartIdx;
		for (int i = numStartIdx + 1; i < input.length(); i++) {
			if (!(((int)(input.charAt(i)) >= 48) && ((int)(input.charAt(i)) <= 57))) {
				break;
			}
			else {
				numEndIdx++;
			}
		}
		

		int rowNum = 0;
		if (rowLtr != 0) {
			rowNum = (int)(rowLtr - 65);
		}
		else {
			rowNum = -1;
		}
		
		int colNum = 0;
		if (numStartIdx != -1) {
			String colNumStr = input.substring(numStartIdx, numEndIdx + 1);
			colNum = Integer.parseInt(colNumStr);
		}
		else {
			colNum = -1;
		}
		
		return new int[] {rowNum, colNum - 1};
		
//		return ((rowLtr != 0) && (numStartIdx != -1));
//		return true;
		
	}
	
	private static void plantMines(boolean[][] mines, int numMines) {
		while (numMines > 0) {
			int row = (int)(Math.random() * mines.length);
			int col = (int)(Math.random() * mines[0].length);
			
//			prevents selection of existing mine
			if (!mines[row][col]) {
				mines[row][col] = true;
				numMines--;
			}
		}
	}
	
	private static String[][] createField(boolean[][] mines, boolean[][] revealed) {
		String[][] field = 
				new String[mines.length][mines[0].length];
		for (int row = 0; row < field.length; row++) {
			for (int col = 0; col < field[row].length; col ++) {
				if (revealed[row][col]) {
					if (mines[row][col]) {
						field[row][col] = "X";
					}
					else {						
						field[row][col] = ""+countNearby(mines,row,col);
					}
				}
				else if (marked[row][col]) {
					field[row][col] = "&";
				}
				else {
					field[row][col] = "#";
				}
			}
		}
		
		
		return field;
	}
	
	private static void sendToLaunchpad(boolean[][] minesArr, boolean[][] markedArr, boolean[][] revealedArr) throws InterruptedException, InvalidMidiDataException, MidiUnavailableException {
		MidiDevice launchpadOut = MaxLaunchpad.launchpad;

//		Count revealed mines and revealed non-mines
		int countRevealedMines = 0;
		int countRevealedNonmines = 0;
		for (int i = 0; i < minesArr.length; i++) {
			for (int j = 0; j < minesArr[i].length; j++) {
				if (revealedArr[i][j]) {
					if (minesArr[i][j]) {
						countRevealedMines++;
					}
					else {
						countRevealedNonmines++;
					}
				}
			}
		}
		
//		Create arrays of revealed mines and revealed non-mines
		int[][] revealedMines = new int[countRevealedMines][2];
		int[][] revealedNonmines = new int[countRevealedNonmines][2];
		int rmCount = 0;
		int rnmCount = 0;
		for (int i = 0; i < minesArr.length; i++) {
			for (int j = 0; j < minesArr[i].length; j++) {
				if (revealedArr[i][j]) {
					if (minesArr[i][j]) {
						revealedMines[rmCount] = new int[] {i, j};
						rmCount++;
					}
					else {
						revealedNonmines[rnmCount] = new int[] {i, j};
						rnmCount++;
					}
				}
			}
		}
		
//		count marked non-revealed spaces
		int countMarkedNonrevealed = 0;
		for (int i = 0; i < minesArr.length; i++) {
			for (int j = 0; j < minesArr[i].length; j++) {
				if (markedArr[i][j] && !revealedArr[i][j]) {
					countMarkedNonrevealed++;
				}
			}
		}

//		create array of marked non-revealed spaces
		int[][] markedNonrevealed = new int[countMarkedNonrevealed][2];
		int mnrCount = 0;
		for (int i = 0; i < minesArr.length; i++) {
			for (int j = 0; j < minesArr[i].length; j++) {
				if (markedArr[i][j] && !revealedArr[i][j]) {
					markedNonrevealed[mnrCount] = new int[] {i, j};
					mnrCount++;
				}
			}
		}
		
		MaxLaunchpad.display(launchpadOut, revealedNonmines, 22, "solid");
		MaxLaunchpad.display(launchpadOut, revealedMines, 6, "solid");
		MaxLaunchpad.display(launchpadOut, markedNonrevealed, 9, "solid");

		
	}

	private static int countNearby(boolean[][] mines, int row, int col) {
		/* THIS METHOD ALLOWS YOU TO BE MOST SPECIFIC
		 * FOR EXAMPLE, IF YOU ONLY WANT NORTH AND EAST
		 */		
		
		
		int count = 0;
		
//		horizontal & vertical adjacents
		count += isValidAndTrue(mines, row-1, col);
		count += isValidAndTrue(mines, row+1, col);
		count += isValidAndTrue(mines, row, col-1);
		count += isValidAndTrue(mines, row, col+1);
		
//		diagonal adjacents
		count += isValidAndTrue(mines, row-1, col-1);
		count += isValidAndTrue(mines, row-1, col+1);
		count += isValidAndTrue(mines, row+1, col-1);
		count += isValidAndTrue(mines, row+1, col+1);
		
		
		return count;
	}
	
	private static int isValidAndTrue(boolean[][] mines, int i, int j) {
		if (i >= 0 && i < mines.length && j >= 0 && j < mines[0].length) {
			if (mines[i][j]) {
				return 1;
			}
		}
		return 0;
	}
	
	public static void printField(String[][] pic) {
		System.out.print("    ");
		for (int i = 0; i < pic[0].length; i++) {
			System.out.print(i+1 + " ");
			if (i<9) {
				System.out.print(" ");
			}
		}
		System.out.print("\n   ");
		for (int i = 0; i < pic[0].length; i++) {
			System.out.print("---");
		}
		System.out.print("\n");
		for (int i = 0; i < pic.length; i++) {
//		for (String[] row : pic) {
			System.out.print(((char)(65 + i) + " | ").toString());
			for (String col : pic[i]){
				System.out.print(col + "  ");
			}
			System.out.print("\n");
		}
	}
	
	private static void revealSpace(int[] space, int[] oldSpace, boolean safety) {
		int r = space[0];
		int c = space[1];
		if (revealed[r][c]) {
			return;
		}
		
		revealed[r][c] = true;
		if (mines[r][c] == true && !safety) {
			explodeMine(space);
			markSpace(space);
		}
		else {
			if (countNearby(mines, r, c) == 0) {
				int[][] newSpaces = new int[][] {
					{r-1, c-1},
					{r-1, c},
					{r-1, c+1},
					{r, c+1},
					{r+1, c+1},
					{r+1, c},
					{r+1, c-1},
					{r, c-1}
				};
				
				for (int i = 0; i < newSpaces.length; i++) {
					int[] newSpace = newSpaces[i];
					int nR = newSpace[0];
					int nC = newSpace[1];
					if (isValidSpace(newSpace)) {
						if (!revealed[nR][nC]) {
							revealSpace(newSpace, space, safety);
						}
					}
				}
				
			}
		}
		if (marked[r][c] && !mines[r][c]) {
			marked[r][c] = false;
			System.out.println("Space " + toLtr(r) + "" + (c + 1) + " unmarked");
		}
	}
	
	protected static void explodeMine(int[] mine){
		int mR = mine[0];
		int mC = mine[1];
		
		shields--;
//		if(shields>0) shields--;
//		else{
//			for(int r=0; r<mines.length; r++){
//				for(int c=0; c<mines[r].length; c++){
//					if(mines[c][r]){
//						revealed[c][r]=true;
//					}
//				}
//			}
//			System.out.println("You've lost.");
//		}
		
	}
	
	private static void autoRevealMines(int numToReveal, boolean[][] minesArr, boolean[][] revealedArr) {
		int revealedCount = 0;
		for (int i = 0; i < minesArr.length; i++) {
			for (int j = 0; j < minesArr[i].length; j++) {
				if (minesArr[i][j] && !revealedArr[i][j] && revealedCount < numToReveal) {
					revealSpace(new int[] {i,j}, new int[] {-1, -1}, true);
					revealedCount++;
				}
			}
		}
	}
	
	private int match2dArrs(boolean[][] inArr1, boolean[][] inArr2, boolean matchType) {
		int count = 0;
		if (inArr1.length == inArr2.length) {
			for (int i = 0; i < inArr1.length; i++) {
				for (int j = 0; j < inArr1.length; j++) {
					if ((inArr1[i][j] == inArr2[i][j]) == matchType) {
						count++;
					}
				}
			}
		}
		
		return count;
	}

}
