package classes;

import java.awt.Point;
import java.util.Random;

import enums.RoomEvent;

public class Labyrinth {
	public static Labyrinth labyrinth = new Labyrinth();
	
	public Labyrinth() {
		
	}

	public void generateMap() {
		int x, y, enemiesPlaced = 0, itemsPlaced = 0;
		boolean stairsPlaced = false, characterPlaced = false;
		Random rand = new Random();
		//TODO: potential endless loop, needs refactoring
		while(enemiesPlaced < 5 || itemsPlaced < 4 || !stairsPlaced || !characterPlaced) {
			x = rand.nextInt(0, 7);
			y = rand.nextInt(0, 7);
			RoomEvent event = RoomEvent.EMPTY;
			
			if(enemiesPlaced < 5) {
				event = RoomEvent.ENEMY;
				enemiesPlaced++;
			} else if(itemsPlaced < 4) {
				event = RoomEvent.ITEM;
				itemsPlaced++;
			} else if(!stairsPlaced) {
				event = RoomEvent.STAIRS;
				stairsPlaced = true;
			} else if(!characterPlaced) {
				event = RoomEvent.CHARACTER;
				characterPlaced = true;
				Character.INSTANCE.setCharacterPosition(new Point(x,y));
			}
			
			MapLayout.INSTANCE.createRoom(new Room(event), x, y);
		}
	}
}