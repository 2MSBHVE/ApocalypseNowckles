package caveExplorer;

import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class CaveExplorer {
	
	public static boolean useLaunchpadInput;
	
	public static CaveRoomPd8[][] caves;
	public static Scanner in;
	public static CaveRoomPd8 currentRoom;
	public static InventoryNockles inventory;
	
	protected static boolean[][] cavesHidden;

	public static void main(String[] args) throws InterruptedException, MidiUnavailableException, InvalidMidiDataException {

		System.out.println("WARNING: \nThis program uses multithreading!");
		System.out.println("Quit now if the runtime environment does not support this!\n");
		
		in = new Scanner(System.in);
		
//		System.out.println("Play with Launchpad? (Y/N) ");
//		String ulpR = in.nextLine().toLowerCase();
//		while ((ulpR.indexOf("y") < 0 && ulpR.indexOf("n") < 0) || (ulpR.indexOf("y") >= 0 && ulpR.indexOf("n") >= 0)) {
//			System.out.println("Play with Launchpad? (Y/N) ");
//			ulpR = in.nextLine().toLowerCase();
//		}
		
//		useLaunchpadInput = (ulpR.indexOf("y") >= 0);
		
		useLaunchpadInput = Launchpad.main(null); 
		System.out.print("\n");
		
		
		if (useLaunchpadInput) {
//			Launchpad.main(null);
			Launchpad.clearPads(Launchpad.launchpad, 0, 0);
		}
		
		
		caves = new CaveRoomPd8[6][6];
		cavesHidden = new boolean[caves.length][caves[0].length];
		
//		for (int i = 0; i < cavesHidden.length; i++) {
//			for (int j = 0; j < cavesHidden[i].length; j++) {
//				cavesHidden[i][j] = false;
//			}
//		}
		

		for (int row = 0; row < caves.length; row++) {
			for (int col = 0; col < caves[row].length; col++) {
				caves[row][col] = new CaveRoomPd8("This room has coords " + row + ", " + col, cavesHidden[row][col]);
			}
		}
		
		
		caves[1][2] = new EventRoom("This is where you found the map.", true, new GameStartEvent());
		caves[1][0] = new EventRoom("You beat Minesweeper here!", true, new MaxTraceyMinesweeper());
		caves[1][5] = new EventRoom("You beat Simons game here!", true, new SimonRoom());
		
		caves[1][1].setConnection(CaveRoomPd8.WEST, caves[1][0], new Door());
		caves[1][1].setConnection(CaveRoomPd8.SOUTH, caves[2][1], new Door());
		caves[1][1].setConnection(CaveRoomPd8.EAST, caves[1][2], new Door());
		
		caves[1][2].setConnection(CaveRoomPd8.EAST, caves[1][3], new Door());
		
		caves[1][3].setConnection(CaveRoomPd8.NORTH, caves[0][3], new Door());
		
		caves[0][3].setConnection(CaveRoomPd8.EAST, caves[0][4], new Door());
		caves[0][3].setConnection(CaveRoomPd8.WEST, caves[0][2], new Door());
		caves[0][4].setConnection(CaveRoomPd8.EAST, caves[0][5], new Door());
		caves[0][5].setConnection(CaveRoomPd8.SOUTH, caves[1][5], new Door());
		

		cavesHidden[2][4] = true;

		currentRoom = caves[1][1];
		currentRoom.getAdjRooms();
		currentRoom.enter();
		
		inventory = new InventoryNockles();
		
//		HERE WE GO BOYS!
//		int[] pxl = {2,3};
//		Launchpad.display(Launchpad.launchpad, pxl, 21, "solid");
		startExploring();
	}

	private static void startExploring() throws InterruptedException, InvalidMidiDataException, MidiUnavailableException {
		int[] center = {1,1};
		while (true) {
			new Thread() {
				public void run() {
					if (useLaunchpadInput) {
//						if (!inventory.hasMap) {
							try {
								Launchpad.clearPads(Launchpad.launchpad, 0, 0);
//								InventoryNockles.printAdjLP(center, currentRoom.getAdjRooms());
//								if (inventory.hasMap) {
//									int[] searchCoords;
//									String searchDirStr;
//									int[] currentCoords = getCoords(currentRoom);
//									for (int i = 0; i < 4; i++) {
//										switch (i) {
//										case CaveRoomPd8.NORTH:
//											searchCoords = new int[] {currentCoords[0]-1,currentCoords[1]};
//											searchDirStr = "north";
//											break;
//
//										case CaveRoomPd8.EAST:
//											searchCoords = new int[] {currentCoords[0],currentCoords[1]+1};
//											searchDirStr = "east";
//											break;
//
//										case CaveRoomPd8.SOUTH:
//											searchCoords = new int[] {currentCoords[0]+1,currentCoords[1]};
//											searchDirStr = "south";
//											break;
//
//										case CaveRoomPd8.WEST:
//											searchCoords = new int[] {currentCoords[0],currentCoords[1]-1};
//											searchDirStr = "east";
//											break;
//
//										default:
//											searchCoords = null;
//											searchDirStr = null;
//										}
//
//
//										if (searchCoords != null) {
//											int searchY = searchCoords[0];
//											int searchX = searchCoords[1];
//											if ((searchY >= 0 && searchY < caves.length) && (searchX >= 0 && searchX < caves[searchY].length)) {
//
//												InventoryNockles.printLargeAdjLP(searchDirStr, caves[searchY][searchX].getAdjRooms());
//											}
//										}
//
//
//									}
//								}
								InventoryNockles.printLargeAdjLP("center", currentRoom.getAdjRooms());
							} catch (InterruptedException | InvalidMidiDataException | MidiUnavailableException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
							}
//						}
					}
				}
			}.start();
			
			print(inventory.getDescription());
			print(currentRoom.getDescription());
			
//			System.out.println(getCoords(currentRoom)[0] + " " + getCoords(currentRoom)[1]);
			printDelay("What would you like to do?", 30, false);
			
			
			
			String consoleInput = in.nextLine();
			System.out.print("\n");
			act(consoleInput);

		}
	}



	
	private static void act(String input) throws InterruptedException, InvalidMidiDataException, MidiUnavailableException {
		currentRoom.interpretAction(input);
	}
	
	public static void print(String text) {
		System.out.println(text);
	}
	
	public static void printDelay(String text, long delay, boolean lineBreak) throws InterruptedException{
		char[] charArr = text.toCharArray();
		for (char c : charArr) {
			System.out.print(c);
			Thread.sleep(delay);
		}
		if (lineBreak){
			System.out.print("\n");
		}
	}
	
	public static int[] getCoords(CaveRoomPd8 room) {
//		int[] coords = new int[2];
		
		for (int i = 0; i < caves.length; i++) {
			for (int j = 0; j < caves[i].length; j++) {
				if (caves[i][j] == room) {
					return new int[] {i, j};
				}
			}
		}
		
		return null;
		
	}
}
