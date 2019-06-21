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
	private String title;

	@Column(name = "message")
	private String message;

	@Version
	private long version;

	public SecurityMessage() {
	}

	public int getSecurityMessageId() {
		return securityMessageId;
	}

	public void setSecurityMessageId(int securityMessageId) {
		this.securityMessageId = securityMessageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public SecurityMessage(String title, String message) {
		super();
		this.title = title;
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
		return "SecurityMessage [securityMessageId=" + securityMessageId + ", title=" + title + ", message=" + message
				+ "]";
	}

}
