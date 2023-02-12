package cliente;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import common.Utils;
import servidor.Mensaje.TipoMensaje;

public class ventanaCliente extends JFrame {

	private JPanel contentPane;
	private JTextField campoServidor;
	private JTextField campoPuerto;
	private JTextField campoNick;
	private JTextField campoEnviar;
	private JLabel labelservidor;
	private JLabel labelPuerto;
	private JLabel labelNick;
	private JLabel labelConectados;
	private JTextArea areaChat;
	private JTextArea areaConectados;
	private JButton botonEnviar;
	private JButton botonDesconectar;
	private JButton botonConectar;
	private int port;
	private String hostName;
	private static String nick;
	private Socket socket;
	private static ObjectOutputStream flujosalida;
	private ObjectInputStream flujoentrada;
	private JScrollPane panelChat;
	private JScrollPane panelConectados;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ventanaCliente frame = new ventanaCliente();
					frame.setVisible(true);
					frame.addWindowListener(new WindowListener() {

						@Override
						public void windowOpened(WindowEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowIconified(WindowEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowDeiconified(WindowEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowDeactivated(WindowEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowClosing(WindowEvent e) {
							disconnect();
						}

						@Override
						public void windowClosed(WindowEvent e) {
						}

						@Override
						public void windowActivated(WindowEvent e) {
							// TODO Auto-generated method stub

						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void disconnect() {
		Utils.respondGeneratedMessage(flujosalida, nick, TipoMensaje.CONNECTION_DISCONNECT);
		nick = null;
	}

	/**
	 * Create the frame.
	 */
	public ventanaCliente() {
		setTitle("Cliente chat: Conexion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 869, 544);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		labelservidor = new JLabel("Servidor:");
		labelservidor.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelservidor.setBounds(10, 24, 59, 20);
		contentPane.add(labelservidor);

		labelPuerto = new JLabel("Puerto:");
		labelPuerto.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelPuerto.setBounds(257, 27, 80, 20);
		contentPane.add(labelPuerto);

		labelNick = new JLabel("Nick:");
		labelNick.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelNick.setBounds(460, 24, 80, 20);
		contentPane.add(labelNick);

		campoServidor = new JTextField();
		campoServidor.setBounds(79, 24, 159, 20);
		contentPane.add(campoServidor);
		campoServidor.setColumns(10);

		campoPuerto = new JTextField();
		campoPuerto.setBounds(324, 24, 86, 20);
		contentPane.add(campoPuerto);
		campoPuerto.setColumns(10);

		campoNick = new JTextField();
		campoNick.setBounds(510, 24, 159, 20);
		contentPane.add(campoNick);
		campoNick.setColumns(10);

		botonConectar = new JButton("Conectar");
		botonConectar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Take fields' values
				hostName = campoServidor.getText();
				port = Integer.valueOf(campoPuerto.getText());
				nick = campoNick.getText();

				// Create socket and get objectStream
				try {
					socket = new Socket(hostName, port);
					flujosalida = new ObjectOutputStream(socket.getOutputStream());
					flujoentrada = new ObjectInputStream(socket.getInputStream());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}

				// Request connection by nick
				Utils.respondGeneratedMessage(flujosalida, nick, TipoMensaje.CONNECTION_REQUESTED);

				// Send message that user connected
				Utils.respondGeneratedMessage(flujosalida, nick + " ha conectado\n", TipoMensaje.MESSAGE);

				areaChat.setText("");

				Hilo hilo = new Hilo(flujoentrada, getthis());
				hilo.start();
			}

		});
		botonConectar.setBounds(696, 23, 118, 23);
		contentPane.add(botonConectar);

		botonDesconectar = new JButton("Desconectar");
		botonDesconectar.setVisible(false);
		botonDesconectar.setBounds(696, 66, 118, 23);
		contentPane.add(botonDesconectar);

		botonDesconectar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				disconnect();
			}
		});

		labelConectados = new JLabel("Conectados");
		labelConectados.setVisible(false);
		labelConectados.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelConectados.setBounds(717, 113, 80, 20);
		contentPane.add(labelConectados);

		campoEnviar = new JTextField();
		campoEnviar.setVisible(false);
		campoEnviar.setBounds(10, 440, 671, 20);
		contentPane.add(campoEnviar);
		campoEnviar.setColumns(10);

		campoEnviar.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					onSendMessage();
				}
			}
		});

		botonEnviar = new JButton("Enviar");
		botonEnviar.setVisible(false);
		botonEnviar.setBounds(696, 439, 89, 23);
		contentPane.add(botonEnviar);

		botonEnviar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onSendMessage();
			}
		});

		panelChat = new JScrollPane();
		panelChat.setVisible(false);
		panelChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panelChat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panelChat.setBounds(20, 11, 648, 421);
		contentPane.add(panelChat);

		areaChat = new JTextArea();
		areaChat.setEditable(false);
		panelChat.setViewportView(areaChat);

		panelConectados = new JScrollPane();
		panelConectados.setVisible(false);
		panelConectados.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panelConectados.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panelConectados.setBounds(696, 144, 132, 284);
		contentPane.add(panelConectados);

		areaConectados = new JTextArea();
		areaConectados.setEditable(false);
		panelConectados.setViewportView(areaConectados);

	}

	public void onSendMessage() {
		String message = nick + ": " + campoEnviar.getText() + "\n";
		Utils.respondGeneratedMessage(flujosalida, message, TipoMensaje.MESSAGE);
		campoEnviar.setText("");
	}

	private ventanaCliente getthis() {
		return this;
	}

	public JTextField getCampoServidor() {
		return campoServidor;
	}

	public JTextField getCampoPuerto() {
		return campoPuerto;
	}

	public JTextField getCampoNick() {
		return campoNick;
	}

	public JTextField getCampoEnviar() {
		return campoEnviar;
	}

	public JLabel getLabelservidor() {
		return labelservidor;
	}

	public JLabel getLabelPuerto() {
		return labelPuerto;
	}

	public JLabel getLabelNick() {
		return labelNick;
	}

	public JLabel getLabelConectados() {
		return labelConectados;
	}

	public JTextArea getAreaChat() {
		return areaChat;
	}

	public JTextArea getAreaConectados() {
		return areaConectados;
	}

	public JButton getBotonEnviar() {
		return botonEnviar;
	}

	public JButton getBotonDesconectar() {
		return botonDesconectar;
	}

	public JButton getBotonConectar() {
		return botonConectar;
	}

	public int getPUERTO() {
		return port;
	}

	public String getServidor() {
		return hostName;
	}

	public String getNick() {
		return nick;
	}

	public Socket getSocket() {
		return socket;
	}

	public ObjectOutputStream getFlujosalida() {
		return flujosalida;
	}

	public ObjectInputStream getFlujoentrada() {
		return flujoentrada;
	}

	public JScrollPane getPanelChat() {
		return panelChat;
	}

	public JScrollPane getPanelConectados() {
		return panelConectados;
	}

}
