package caveExplorer.maxTracey;

public class TraceyMinesweeper {

	static void plantMines(boolean[][] mines, int numMines) {
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

	static boolean isValidSpace(boolean[][] arr2D, int[] inArr) {
		int r = inArr[0];
		int c = inArr[1];
		
		if ((0 <= r) && (r < arr2D.length)) {
			if ((0 <= c) && (c < arr2D[0].length)) {
				return true;
			}
		}
		return false;
	}

	static String[][] createField(boolean[][] mines, boolean[][] revealed) {
		String[][] field = 
				new String[mines.length][mines[0].length];
		for (int row = 0; row < field.length; row++) {
			for (int col = 0; col < field[row].length; col ++) {
				if (revealed[row][col]) {
					if (mines[row][col]) {
						field[row][col] = "X";
					}
					else {						
						field[row][col] = ""+MaxMinesweeper.countNearby(mines,row,col);
					}
				}
				else if (MaxMinesweeper.marked[row][col]) {
					field[row][col] = "&";
				}
				else {
					field[row][col] = "#";
				}
			}
		}
		
		
		return field;
	}

	static boolean checkArraysEqual(boolean[][] arr1, boolean[][] arr2){
		
		for (int r = 0; r < arr1.length; r++) {
			for (int c = 0; c < arr1[r].length; c++) {
				if (arr1[r][c] != arr2[r][c]) {
					return false;
				}
			}
		}
		return true;
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
//			System.out.print(((char)(65 + i) + " | ").toString());
			System.out.print((MaxMinesweeper.toLtr(i) + " | ").toString());
			for (String col : pic[i]){
				System.out.print(col + "  ");
			}
			System.out.print("\n");
		}
	}

	 static void markSpace(int[] space) {
	
		if (TraceyMinesweeper.isValidSpace(MaxMinesweeper.mines, space)) {
			int mR = space[0];
			int mC = space[1];
			if (MaxMinesweeper.revealed[mR][mC]) {
				MaxMinesweeper.marked[mR][mC] = MaxMinesweeper.mines[mR][mC];
			}
			else {
				if (MaxMinesweeper.revealed[mR][mC] && !MaxMinesweeper.mines[mR][mC]) {
					MaxMinesweeper.marked[mR][mC] = false;
					System.out.println("Space " + MaxMinesweeper.toLtr(mR) + "" + (mC + 1) + " unmarked");
				}
				else {
					MaxMinesweeper.marked[mR][mC] = !MaxMinesweeper.marked[mR][mC];
					String toOut = "Space " + MaxMinesweeper.toLtr(mR) + "" + (mC + 1) + " ";
					if (!MaxMinesweeper.marked[mR][mC]) {
						toOut += "un";
					}
					toOut += "marked.";
					System.out.println(toOut);
				}
			}
		}
		
	}

	
	
}
