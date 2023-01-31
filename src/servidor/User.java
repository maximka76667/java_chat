package servidor;

import java.io.ObjectOutputStream;
import java.util.Objects;

public class User {

	private String nick;
	private ObjectOutputStream output;

	public User(String nick, ObjectOutputStream output) {
		super();
		this.nick = nick;
		this.output = output;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public void setOutput(ObjectOutputStream output) {
		this.output = output;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nick);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(nick, other.nick);
	}

}
