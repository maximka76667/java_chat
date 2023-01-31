package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import common.Utils;
import servidor.Mensaje.TipoMensaje;

public class Hilo extends Thread {

	private Socket socket;
	private Compartida compartida;
	private String nick;
	private ventanaServidor ventana;

	public Hilo(Socket socket, Compartida compartida, ventanaServidor ventana) {
		super();
		this.socket = socket;
		this.compartida = compartida;
		this.ventana = ventana;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());

			messageReceiver: while (true) {
				Mensaje receivedMessage = (Mensaje) entrada.readObject();
				switch (receivedMessage.getTipomensaje()) {
				case CONNECTION_REQUESTED:
					if (!compartida.nickExists(nick)) {
						// Receive nick and connect new user
						nick = (String) receivedMessage.getMensaje();
						compartida.connect(nick, salida);

						// Respond with accepted connection
						Utils.respondGeneratedMessage(salida, "Nick correcto", TipoMensaje.CONNECTION_ACCEPTED);
						ventana.getAreaServidor().append(nick + " ha conectado\n");

						// Send list with updated users
						compartida.sendNicks();
					} else {
						// If user already exists - reject connection
						Utils.respondGeneratedMessage(salida, "Nick incorrecto", TipoMensaje.CONNECTION_REJECTED);

						// And break cycle
						break messageReceiver;
					}
					break;

				case CONNECTION_DISCONNECT:
					compartida.disconnect(nick);
					ventana.getAreaServidor().append(nick + " ha desconectado\n");
					Utils.respondGeneratedMessage(salida, "Cliente desconectado", TipoMensaje.CONNECTION_DISCONNECT);
					compartida.sendNicks();
					break messageReceiver;

				case MESSAGE:
					compartida.sendMessageToChat(receivedMessage);
					break;

				default:
					break messageReceiver;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
