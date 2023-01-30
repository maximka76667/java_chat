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

	public void mostrar(boolean isChatVisible) {
		ventana.getLabelservidor().setVisible(!isChatVisible);
		ventana.getCampoServidor().setVisible(!isChatVisible);
		ventana.getLabelPuerto().setVisible(!isChatVisible);
		ventana.getCampoPuerto().setVisible(!isChatVisible);
		ventana.getLabelNick().setVisible(!isChatVisible);
		ventana.getCampoNick().setVisible(!isChatVisible);
		ventana.getBotonConectar().setVisible(!isChatVisible);

		ventana.getPanelChat().setVisible(isChatVisible);
		ventana.getAreaChat().setVisible(isChatVisible);
		ventana.getBotonDesconectar().setVisible(isChatVisible);
		ventana.getLabelConectados().setVisible(isChatVisible);
		ventana.getPanelConectados().setVisible(isChatVisible);
		ventana.getAreaConectados().setVisible(isChatVisible);
		ventana.getBotonEnviar().setVisible(isChatVisible);
		ventana.getCampoEnviar().setVisible(isChatVisible);

		ventana.setTitle("Cliente chat: nick " + ventana.getNick());
	}

	public void run() {
		messageReciever: while (true) {
			try {
				Mensaje recibido = (Mensaje) flujoentrada.readObject();
				switch (recibido.getTipomensaje()) {
				case CONNECTION_DECLINED:
					JOptionPane.showMessageDialog(null, recibido.getMensaje());
					break;
				case CONNECTION_ACCEPTED:
					JOptionPane.showMessageDialog(null, recibido.getMensaje());
					mostrar(true);
					break;
				case SEND_NICKS:
					ventana.getAreaConectados().setText("");
					ArrayList<String> connectedNicks = (ArrayList<String>) recibido.getMensaje();
					System.out.println(recibido);
					for (String nick : connectedNicks) {
						ventana.getAreaConectados().append(nick + "\n");
					}
					break;

				case CONNECTION_DISCONNECT:
					mostrar(false);
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
