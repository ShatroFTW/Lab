package game;

import classes.Labyrinth;
import classes.Character;
import classes.MapLayout;
import classes.Room;
import com.formdev.flatlaf.FlatDarkLaf;
import enums.Direction;
import enums.RoomEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

public class Window {
    private static Window instance;
    private JPanel contentPane;
    private JPanel labyrinthPane;
    private JButton pickUpButton;
    private JButton southButton;
    private JButton westButton;
    private JButton eastButton;
    private JButton northButton;
    private JTextArea logTextArea;
    public  JLabel currentPointsLabel;
    private JLabel lifePointsLabel;
    private JLabel holdingAxeLabel;
    private static HashMap<String, Component> componentMap = new HashMap<>();
    private final Properties properties;

    public static Window getInstance() {
        return instance;
    }

    public static HashMap<String, Component> getComponentMap() {
        return componentMap;
    }
    public Window() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("src/strings.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        northButton.addActionListener(e -> {
            Character.INSTANCE.moveDirection(Direction.NORTH);
            updateLabels();
        });
        southButton.addActionListener(e -> {
            Character.INSTANCE.moveDirection(Direction.SOUTH);
            updateLabels();
        });
        westButton.addActionListener(e -> {
            Character.INSTANCE.moveDirection(Direction.WEST);
            updateLabels();
        });
        eastButton.addActionListener(e -> {
            Character.INSTANCE.moveDirection(Direction.EAST);
            updateLabels();
        });
        pickUpButton.addActionListener(e -> {
            Room currentRoom = MapLayout.INSTANCE.getRoom(Character.INSTANCE.getCharacterPosition());
            if (currentRoom.getEvent() == RoomEvent.ITEM_AND_CHARACTER && !Character.INSTANCE.hasItem()) {
                Character.INSTANCE.pickUpItem();
                currentRoom.setEvent(RoomEvent.CHARACTER);
                GameLogger.LOGGER.appendMessage("You picked up an axe.");
                updateLabels();
            }
        });

        init();
    }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        JFrame frame = new JFrame("Lab");
        instance = new Window();
        frame.setContentPane(instance.contentPane);
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
                label.setIcon(new ImageIcon());
                label.setBorder(new LineBorder(Color.BLACK));
                labyrinthPane.add(label);
            }
        }
    }

    public void init() {
        GameLogger.LOGGER = new GameLogger(logTextArea);
        componentMap = createComponentMap(labyrinthPane);
        Labyrinth.labyrinth.generateMap();
        updateLabels();
    }

    public void updateLabels() {
        currentPointsLabel.setText(properties.getProperty("current.points") + GameState.INSTANCE.getPoints());
        lifePointsLabel.setText(properties.getProperty("life.points") + Character.INSTANCE.getLifePoints());
        holdingAxeLabel.setText(properties.getProperty("holding.an.axe") +
                (Character.INSTANCE.hasItem() ? "Yes" : "No"));
    }

    public static HashMap<String, Component> createComponentMap(JPanel pane) {
        HashMap<String, Component> map = new HashMap<>();
        for (Component c : pane.getComponents()) {
                if (!(c instanceof JLabel label))
                    continue;

                label.setOpaque(true);
                map.put(label.getName(), label);

        }
        return map;
    }

    public static Component getComponentByName(String name) {
        return componentMap.getOrDefault(name, null);
    }


}