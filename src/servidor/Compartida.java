package servidor;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JTextArea;

import common.Utils;
import servidor.Mensaje.TipoMensaje;

public class Compartida {

	private ArrayList<User> connectedUsers;

	public Compartida(JTextArea aServidor) {
		connectedUsers = new ArrayList<>();
	}

	public synchronized boolean nickExists(String nick) {
		return connectedUsers.contains(new User(nick, null));
	}

	public synchronized void connect(String nick, ObjectOutputStream salida) {
		connectedUsers.add(new User(nick, salida));
	}

	public synchronized void disconnect(String nick) {
		connectedUsers.remove(new User(nick, null));
	}

	public synchronized void sendMessageToChat(Mensaje mensaje) {
		for (User user : connectedUsers) {
			Utils.respondMessage(user.getOutput(), mensaje);
		}
	}

	public synchronized void sendNicks() {
		for (User user : connectedUsers) {
			Utils.respondGeneratedMessage(user.getOutput(), Utils.convertNicksToString(connectedUsers),
					TipoMensaje.NICKS);
		}
	}
}
