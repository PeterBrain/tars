package at.fh.swenga.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Version;

public class UserRole {
	@Id
	@Column(name = "roleId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roleId;

	@Column(nullable = false, length = 30)
	private String name;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	@OrderBy("lastname, firstname")
	private Set<User> users;

	@Version
	long version;

	public UserRole() {
	}

	public UserRole(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public void addUser(User user) {
		if (users == null) {
			users = new HashSet<User>();
		}
		users.add(user);
	}
}
