package game;

import classes.Labyrinth;
import classes.Character;
import classes.MapLayout;
import classes.Room;
import com.formdev.flatlaf.FlatDarculaLaf;
import enums.Direction;
import enums.RoomEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Window {
    private JPanel contentPane;
    private JPanel labyrinthPane;
    private JButton pickUpButton;
    private JButton southButton;
    private JButton westButton;
    private JButton eastButton;
    private JButton northButton;
    private JTextArea logTextArea;
    private JLabel currentPointsLabel;
    private JLabel lifePointsLabel;
    private JLabel holdingAxeLabel;
    private static HashMap<String, Component> componentMap = new HashMap<>();
    private Properties properties;

    public Window() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("/.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        northButton.addActionListener(e -> Character.INSTANCE.moveDirection(Direction.NORTH));
        southButton.addActionListener(e -> Character.INSTANCE.moveDirection(Direction.SOUTH));
        westButton.addActionListener(e -> Character.INSTANCE.moveDirection(Direction.WEST));
        eastButton.addActionListener(e -> Character.INSTANCE.moveDirection(Direction.EAST));
        pickUpButton.addActionListener(e -> {
            Room currentRoom = MapLayout.INSTANCE.getRoom(Character.INSTANCE.getCharacterPosition());
            if (currentRoom.getEvent() == RoomEvent.ITEM_AND_CHARACTER && !Character.INSTANCE.hasItem()) {
                Character.INSTANCE.pickUpItem();
                currentRoom.setEvent(RoomEvent.CHARACTER);
                GameLogger.LOGGER.appendMessage("You picked up an axe.");
            }
        });

        init();

        Room[][] rooms = MapLayout.INSTANCE.getRooms();

        //for testing purposes
        //Icons will be hidden and revealed only when you move
        Component c;
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                if((c = componentMap.getOrDefault("lbl" + x + y, null)) != null)
                    ((JLabel) c).setIcon(rooms[x][y].getIcon());
            }
        }
    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        JFrame frame = new JFrame("Lab");
        frame.setContentPane(new Window().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(960, 540));
        frame.setPreferredSize(new Dimension(1280, 720));
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        labyrinthPane = new JPanel();
        labyrinthPane.setLayout(new GridLayout(7, 7, -1, -1));
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setName("lbl" + j + i);
                label.setBorder(new LineBorder(Color.BLACK));
                labyrinthPane.add(label);
            }
        }
    }

    private void init() {
        GameLogger.LOGGER = new GameLogger(logTextArea);
        Labyrinth.labyrinth.generateMap();
        componentMap = createComponentMap(labyrinthPane);
        currentPointsLabel.setText(properties.getProperty("current.points") + GameState.INSTANCE.getPoints());
        holdingAxeLabel.setText(properties.getProperty("holding.an.axe") + Character.INSTANCE.hasItem());
    }

    public static HashMap<String, Component> createComponentMap(JPanel pane) {
        HashMap<String, Component> map = new HashMap<>();
        for (Component c : pane.getComponents()) {
                if (!(c instanceof JLabel label))
                    continue;
                map.put(label.getName(), label);

        }
        return map;
    }

    public static Component getComponentByName(String name) {
        return componentMap.getOrDefault(name, null);
    }
}