package servidor;

import java.io.Serializable;

public class Mensaje implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object mensaje;
	private TipoMensaje tipomensaje;

	public Mensaje(Object mensaje, TipoMensaje tipomensaje) {
		super();
		this.mensaje = mensaje;
		this.tipomensaje = tipomensaje;
	}

	public Object getMensaje() {
		return mensaje;
	}

	public TipoMensaje getTipomensaje() {
		return tipomensaje;
	}

	public enum TipoMensaje {
		CONNECTION_ACCEPTED, CONNECTION_DECLINED, SEND_NICKS, CONNECTION_REQUESTED, CONNECTION_DISCONNECT, MESSAGE;
	}
}
