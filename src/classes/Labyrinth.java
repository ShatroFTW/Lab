package classes;

import java.awt.*;
import java.util.Random;

import enums.RoomEvent;
import game.Window;

import javax.swing.*;

public class Labyrinth {
	public static Labyrinth labyrinth = new Labyrinth();
	
	public Labyrinth() { }

	public void generateMap() {
		MapLayout.INSTANCE.clear();
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
		resetIcons();
	}

	private void resetIcons() {
		JLabel label;
		Room[][] rooms = MapLayout.INSTANCE.getRooms();
		Point charPosition = Character.INSTANCE.getCharacterPosition();
		for (int y = 0; y < 7; y++) {
			for (int x = 0; x < 7; x++) {
				if ((label = (JLabel) Window.getComponentMap().getOrDefault("lbl" + x + y, null)) != null) {
					if((x == charPosition.x && y == charPosition.y) || rooms[x][y].getEvent() == RoomEvent.STAIRS) {
						label.setIcon(rooms[x][y].getIcon());
						label.setBackground(Color.GRAY);
					}
					else {
						label.setIcon(new ImageIcon());
						label.setBackground(null);
					}
				}
			}
		}
	}
}