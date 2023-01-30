package servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import servidor.Mensaje.TipoMensaje;

public class Compartida {

	private ArrayList<String> connectedNicks;
	private ArrayList<ObjectOutputStream> listastreamssalida;
	private JTextArea AServidor;

	public Compartida(JTextArea aServidor) {
		super();
		AServidor = aServidor;
		connectedNicks = new ArrayList<String>();
		listastreamssalida = new ArrayList<ObjectOutputStream>();
	}

	// Comprueba si el nick existe en la lista de nicks
	// Si el nick ya existe devuelve false, si no devuelve true y actualiza las
	// listas
	public synchronized boolean nickExists(String nick) {
		return connectedNicks.contains(nick);
	}

	public synchronized void connect(String nick, ObjectOutputStream salida) {
		connectedNicks.add(nick);
		listastreamssalida.add(salida);
	}

	public synchronized void disconnect(String nick, ObjectOutputStream salida) {
		connectedNicks.remove(nick);
		listastreamssalida.remove(salida);
	}

	public synchronized void enviaratodos(Mensaje mensaje) {
		try {
			for (ObjectOutputStream salida : listastreamssalida) {
				salida.writeObject(mensaje);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public synchronized void enviarnicks() {
		System.out.println("SEND NEW NICKS");
		try {
			for (ObjectOutputStream salida : listastreamssalida) {
				System.out.println(connectedNicks);
				salida.writeObject(new Mensaje(connectedNicks, TipoMensaje.SEND_NICKS));
				salida.flush();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

}
