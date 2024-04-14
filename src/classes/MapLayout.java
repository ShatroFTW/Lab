package classes;

import java.awt.Point;

import enums.RoomEvent;

public class MapLayout {
	public static final MapLayout INSTANCE = new MapLayout();
	private Room[][] rooms;
	
	public Room[][] getRooms() { return rooms; }
	
	protected MapLayout() {
		rooms = new Room[7][7];
		for(int y = 0; y<7; y++) {
			for(int x = 0; x<7;x++) {
				rooms[x][y] = new Room(RoomEvent.EMPTY);
			}
		}
	}
	
	public Room getRoom(Point p) {
		return rooms[p.x][p.y];
	}
	
	public void createRoom(Room newRoom, int x, int y) {
		rooms[x][y] = newRoom;
	}

	public void clear() {
		INSTANCE.rooms = new Room[7][7];
		for(int y = 0; y<7; y++) {
			for(int x = 0; x<7;x++) {
				rooms[x][y] = new Room(RoomEvent.EMPTY);
			}
		}
	}

	public void discoverRoom(Point prevRoomPoint, Point newRoomPoint) {
		Room newRoom = rooms[newRoomPoint.x][newRoomPoint.y];
		Room prevRoom = rooms[prevRoomPoint.x][prevRoomPoint.y];
		newRoom.enterRoom(prevRoom, prevRoomPoint);
	}
}