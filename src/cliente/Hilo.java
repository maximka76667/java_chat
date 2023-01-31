package cliente;

import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import servidor.Mensaje;

public class Hilo extends Thread {

	private ObjectInputStream flujoentrada;
	private ventanaCliente ventana;

	public Hilo(ObjectInputStream flujoentrada, ventanaCliente ventana) {
		super();
		this.flujoentrada = flujoentrada;
		this.ventana = ventana;
	}

	public void showChat(boolean makeChatVisible) {
		ventana.getLabelservidor().setVisible(!makeChatVisible);
		ventana.getCampoServidor().setVisible(!makeChatVisible);
		ventana.getLabelPuerto().setVisible(!makeChatVisible);
		ventana.getCampoPuerto().setVisible(!makeChatVisible);
		ventana.getLabelNick().setVisible(!makeChatVisible);
		ventana.getCampoNick().setVisible(!makeChatVisible);
		ventana.getBotonConectar().setVisible(!makeChatVisible);

		ventana.getPanelChat().setVisible(makeChatVisible);
		ventana.getAreaChat().setVisible(makeChatVisible);
		ventana.getBotonDesconectar().setVisible(makeChatVisible);
		ventana.getLabelConectados().setVisible(makeChatVisible);
		ventana.getPanelConectados().setVisible(makeChatVisible);
		ventana.getAreaConectados().setVisible(makeChatVisible);
		ventana.getBotonEnviar().setVisible(makeChatVisible);
		ventana.getCampoEnviar().setVisible(makeChatVisible);

		ventana.setTitle("Cliente chat: nick " + ventana.getNick());
	}

	public void run() {
		messageReciever: while (true) {
			try {
				Mensaje recibido = (Mensaje) flujoentrada.readObject();
				switch (recibido.getTipomensaje()) {
				case CONNECTION_REJECTED:
					JOptionPane.showMessageDialog(null, recibido.getMensaje());
					break;

				case CONNECTION_ACCEPTED:
					JOptionPane.showMessageDialog(null, recibido.getMensaje());
					showChat(true);
					break;

				case NICKS:
					ventana.getAreaConectados().setText((String) recibido.getMensaje());
					break;

				case CONNECTION_DISCONNECT:
					showChat(false);
					break messageReciever;

				case MESSAGE:
					ventana.getAreaChat().append((String) recibido.getMensaje());
					break;

				default:
					break messageReciever;
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}

}
