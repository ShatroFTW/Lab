package game;

import javax.swing.JTextArea;

public class GameLogger {
	public static GameLogger LOGGER;
	private JTextArea txtAreaLog;
	
	public GameLogger(JTextArea txtAreaLog) {
		this.txtAreaLog = txtAreaLog;
	}
	
	public void appendMessage(String message) {
		txtAreaLog.append(message + "\n");
	}
}