package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

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

	public void run() {
		try {
			ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());

			messageReciever: while (true) {
				Mensaje recibido = (Mensaje) entrada.readObject();
				switch (recibido.getTipomensaje()) {
				case CONNECTION_REQUESTED:
					if (!compartida.nickExists(nick)) {
						nick = (String) recibido.getMensaje();
						compartida.connect(nick, salida);
						// Nick valido, cliente conectado
						Mensaje mensaje = new Mensaje("Nick correcto", TipoMensaje.CONNECTION_ACCEPTED);
						salida.writeObject(mensaje);
						salida.flush();
						ventana.getAreaServidor().append(nick + " ha conectado\n");
					} else {
						Mensaje mensaje = new Mensaje("Nick incorrecto", TipoMensaje.CONNECTION_DECLINED);
						salida.writeObject(mensaje);
						salida.flush();
						break messageReciever;
					}
					compartida.enviarnicks();
					break;

				case CONNECTION_DISCONNECT:
					compartida.disconnect(nick, salida);
					ventana.getAreaServidor().append(nick + " ha desconectado\n");
					salida.writeObject(new Mensaje("Cliente desconectado", TipoMensaje.CONNECTION_DISCONNECT));
					compartida.enviarnicks();
					break messageReciever;

				case MESSAGE:
					compartida.enviaratodos(recibido);
					break;

				default:
					break messageReciever;
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
