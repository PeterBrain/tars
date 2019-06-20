package at.fh.swenga.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "security_message")
public class SecurityMessage implements java.io.Serializable {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int securityMessageId;
	
	@Column(name = "heading")
	private String heading;
	
	@Column(name = "message")
	private String message;
	
	@Version
	private long version;
	
	public SecurityMessage() {
		// TODO Auto-generated constructor stub
	}

	public SecurityMessage(String heading, String message) {
		super();
		this.heading = heading;
		this.message = message;
	}

	public int getSecurityMessageId() {
		return securityMessageId;
	}

	public void setSecurityMessageId(int securityMessageId) {
		this.securityMessageId = securityMessageId;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + securityMessageId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecurityMessage other = (SecurityMessage) obj;
		if (securityMessageId != other.securityMessageId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SecurityMessage [securityMessageId=" + securityMessageId + ", heading=" + heading + ", message="
				+ message + "]";
	}


	
	
}
