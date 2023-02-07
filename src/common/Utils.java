package common;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import servidor.Mensaje;
import servidor.Mensaje.TipoMensaje;
import servidor.User;

public class Utils {

	public synchronized static void respondMessage(ObjectOutputStream output, Mensaje message) {
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized static void respondGeneratedMessage(ObjectOutputStream output, Object message,
			TipoMensaje messageType) {
		try {
			output.writeObject(new Mensaje(message, messageType));
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String convertNicksToString(ArrayList<User> users) {
		String nicksString = "";
		for (User user : users) {
			nicksString += user.getNick() + "\n";
		}
		return nicksString;
	}
}
