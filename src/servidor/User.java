package servidor;

import java.io.ObjectOutputStream;

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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;

		return this.nick.equals(other.getNick());
	}

	@Override
	public String toString() {
		return "User [nick=" + nick + ", output=" + output + "]";
	}

}
