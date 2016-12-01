package caveExplorer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import caveExplorer.maxTracey.MaxLaunchpad;

public class InventoryNockles {
	
	protected boolean hasMap;
	protected static boolean hasWood;
	protected static boolean hasHammer;
	protected static boolean hasNails;
	private String map;
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;

	public InventoryNockles() {
		hasMap = false;
		if (hasMap) {
			updateMap();
		}
	}

	public void updateMap() {
		CaveRoomPd8[][] caves = CaveExplorer.caves;
		
		map = " ";
		for (int i = 0; i < caves.length - 1; i++) {
			map += "____";
		}
		map += "___\n";
		
//		each room
		for (CaveRoomPd8[] row : caves) {
//			3 rows of text
			for (int r = 0; r < 3; r++) {
				for (CaveRoomPd8 cr : row) {
					String str = "|   ";
					String contents = cr.getContents();
					if (r == 1) {
						if(cr.getDoor(CaveRoomPd8.WEST) != null && cr.getDoor(CaveRoomPd8.WEST).isOpen()) {
							str = "  " + contents + " ";
						}
						else {
							str = "| " + contents + " ";
						}
					}
					else if (r == 2){
						if (cr.getDoor(CaveRoomPd8.SOUTH) != null && cr.getDoor(CaveRoomPd8.SOUTH).isOpen()) {
							str = "|_ _";
						}
						else {
							str = "|___";
						}
					}
					map += str;
				} //end of row of caverooms
				map += "|\n";
			}
//			map += "|";
		}
		
//		System.out.print(map);
	}
	
	public void updateMapLP() {
		
	}
	
	public static void printAdjLP(int[] center, boolean[][] dirs) throws InterruptedException, InvalidMidiDataException, MidiUnavailableException {
		MaxLaunchpad.display(MaxLaunchpad.launchpad, center, 21, "pulse");
		
		int[][] modPxls = {
				{center[0]-1, center[1]}, //NORTH
				{center[0], center[1]+1}, //EAST
				{center[0]+1, center[1]}, //SOUTH
				{center[0], center[1]-1}, //WEST
		};
		
		for (int i = 0; i < dirs[0].length; i++) {
			if (dirs[0][i]) {
				MaxLaunchpad.display(MaxLaunchpad.launchpad, modPxls[i], 3, "solid");
			}
			else if (dirs[1][i]) {
				MaxLaunchpad.display(MaxLaunchpad.launchpad, modPxls[i], 5, "solid");
			}
		}
	}
	
	public static void printLargeAdjLP(String position, boolean[][] dirs) throws InterruptedException, InvalidMidiDataException, MidiUnavailableException {
//		int[] center = {3,3};
		int[] center;
		switch (position.toLowerCase()) {
		case "north":
			center = new int[] {1,3};
			break;
			
		case "east":
			center = new int[] {3,5};
			break;
			
		case "south":
			center = new int[] {5,3};
			break;
			
		case "west":
			center = new int[] {3,1};
			break;

		default:
			center = new int[] {3,3};
			break;
		}
		
		
		int[][] modPxls = {
				{center[0]-2, center[1]}, //NORTH
				{center[0], center[1]+2}, //EAST
				{center[0]+2, center[1]}, //SOUTH
				{center[0], center[1]-2}, //WEST
		};
		
		for (int i = 0; i < dirs[0].length; i++) {
			if (dirs[0][i]) { //if door is open
				if(MaxLaunchpad.make2x2Square(modPxls[i]) != null) {
				


					MaxLaunchpad.display(MaxLaunchpad.launchpad, MaxLaunchpad.make2x2Square(modPxls[i]), 2, "solid");
				}
			}
			else if (dirs[1][i]) { //if door is locked
				MaxLaunchpad.display(MaxLaunchpad.launchpad, MaxLaunchpad.make2x2Square(modPxls[i]), 5, "solid");
			}
		}
		MaxLaunchpad.display(MaxLaunchpad.launchpad, MaxLaunchpad.make2x2Square(center), 21, "solid");
	}

	public String getDescription() {
		if (hasMap) {
			return map;
		}
		else {
			return "You have no inventory.";
		}
	}

	public void setHasMap(boolean b) {
		hasMap = b;
	}

	public void setHasNails(boolean b) {
		hasNails = b;
	}
	
	public void setHasHammer(boolean b) {
		hasHammer = b;
	}
	
	public void setHasWood(boolean b) {
		hasWood = b;
	}
	

}
