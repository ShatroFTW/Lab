package classes;

import java.awt.Point;

import enums.Direction;

public class Character {
    public static final Character INSTANCE = new Character("");
    private final String name;
    private int lifePoints;
    private boolean hasItem;
    private Point characterPosition;


    public String getName() { return name; }

    public int getLifePoints() { return lifePoints; }

    public Point getCharacterPosition() { return characterPosition; }
    public void setCharacterPosition(Point characterPosition) { this.characterPosition = characterPosition; }

    public boolean hasItem() { return hasItem; }

    public Character(String name) {
        this.name = name;
        this.lifePoints = 3;
        this.hasItem = false;
    }

    public void attack() {
        hasItem = false;
    }

    public int takeDamage() {
        return --lifePoints;
    }

    public void pickUpItem() {
        hasItem = true;
    }

    public void moveDirection(Direction direction) {
        Point prevPosition = (Point) characterPosition.clone();

        if (direction == Direction.SOUTH && characterPosition.y < 6) {
            characterPosition.y++;
        } else if (direction == Direction.NORTH && characterPosition.y > 0) {
            characterPosition.y--;
        } else if (direction == Direction.WEST && characterPosition.x > 0) {
            characterPosition.x--;
        } else if(direction == Direction.EAST && characterPosition.x < 6){
            characterPosition.x++;
        } else return;

        MapLayout.INSTANCE.discoverRoom(prevPosition, characterPosition);
    }
}