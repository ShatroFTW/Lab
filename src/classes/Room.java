package classes;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import enums.RoomEvent;
import game.GameLogger;
import game.GameState;
import game.Window;

public class Room {
    private RoomEvent event;
    private boolean hasItem;
    private boolean discovered;
    private ImageIcon icon;

    public RoomEvent getEvent() {
        return event;
    }

    public void setEvent(RoomEvent event) {
        this.event = event;
    }

    public boolean hasItem() {
        return hasItem;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public Room(RoomEvent event) {
        this.event = event;

        updateIcon();

        if (event == RoomEvent.ITEM)
            hasItem = true;

        discovered = false;
    }

    public void enterRoom(Room prevRoom, Point prevPosition) {
        //changing the event state of the new room
        if (event == RoomEvent.ENEMY && Character.INSTANCE.hasItem()) {
            Character.INSTANCE.attack();
            event = RoomEvent.CHARACTER;

            GameLogger.LOGGER.appendMessage("A monster attacked you but you defeated it with your axe. It broke in the process.");
        } else if (event == RoomEvent.ENEMY && !Character.INSTANCE.hasItem()) {
            Character.INSTANCE.takeDamage();
            event = RoomEvent.ENEMY_AND_CHARACTER;

            GameLogger.LOGGER.appendMessage("A monster attacked you and you took some damage.");
        } else if (event == RoomEvent.ITEM) {
            event = RoomEvent.ITEM_AND_CHARACTER;

            if (!Character.INSTANCE.hasItem())
                GameLogger.LOGGER.appendMessage("You entered the room and found an axe. You might want to pick it up.");
            else
                GameLogger.LOGGER.appendMessage("You entered the room and found an axe. You can't carry more than one.");
        } else if (event == RoomEvent.STAIRS) {
            Labyrinth.labyrinth.generateMap();

            GameLogger.LOGGER.appendMessage("You found the stairs and went up one floor.");
        } else if (event == RoomEvent.EMPTY) {
            event = RoomEvent.CHARACTER;

//            GameLogger.LOGGER.appendMessage("You entered the room and found nothing.");
        }

        if (!discovered) GameState.INSTANCE.increaseScore();
        discovered = true;

        this.updateIcon();
        this.updateLabelIcon(Character.INSTANCE.getCharacterPosition());


        //setting the event state of the room you just left
        if (prevRoom.event == RoomEvent.CHARACTER) {
            prevRoom.event = RoomEvent.EMPTY;
        } else if (prevRoom.event == RoomEvent.ENEMY_AND_CHARACTER) {
            prevRoom.event = RoomEvent.ENEMY;
        } else if (prevRoom.event == RoomEvent.ITEM_AND_CHARACTER) {
            prevRoom.event = RoomEvent.ITEM;
        }

        prevRoom.updateIcon();
        prevRoom.updateLabelIcon(prevPosition);
    }

    public void updateIcon() {

        if (event == RoomEvent.ITEM) {
            icon = new ImageIcon(this.getClass().getResource("/axe.png"));
        } else if (event == RoomEvent.ENEMY) {
            icon = new ImageIcon(this.getClass().getResource("/monster.png"));
        } else if (event == RoomEvent.STAIRS) {
            icon = new ImageIcon(this.getClass().getResource("/stairs.png"));
        } else if (event == RoomEvent.CHARACTER || event == RoomEvent.ITEM_AND_CHARACTER ||
                event == RoomEvent.ENEMY_AND_CHARACTER) {
            icon = new ImageIcon(this.getClass().getResource("/character.png"));
        } else if (event == RoomEvent.EMPTY) {
            icon = new ImageIcon(this.getClass().getResource("/empty.png"));
        }


        if (icon != null) {
            Image image = icon.getImage();
            Image newImg = image.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
        }
    }

    private void updateLabelIcon(Point point) {
        JLabel label = ((JLabel) Window.getComponentByName("lbl" + point.x + point.y));
        assert label != null;
        label.setIcon(icon);
        if(discovered) label.setBackground(Color.GRAY);
    }
}
