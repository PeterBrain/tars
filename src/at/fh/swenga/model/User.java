package at.fh.swenga.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "user")
public class User implements java.io.Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	@Column(name = "firstname", nullable = false, length = 45)
	private String firstName;

	@Column(name = "lastname", nullable = false, length = 45)
	private String lastName;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	@NotNull(message = "Date of birth cannot be null")
	@Column(name = "dob", nullable = false)
	private Date dayOfBirth;

	@Column(name = "email", nullable = false, length = 45, unique = true)
	private String email;

	@Column(name = "username", nullable = false, length = 45, unique = true)
	private String userName;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@ManyToMany(fetch = FetchType.LAZY)//, cascade = CascadeType.PERSIST is not used because insert user would not work
	private Set<UserRole> userRoles;

	@OneToMany(mappedBy = "editor", fetch = FetchType.LAZY)
	private Set<Entry> entries;
	
	@OneToMany(mappedBy = "projectLeader", fetch = FetchType.LAZY)
	private Set<Project> projects;

	@Version
	private long version;

	// constructor
	public User() {
	} // default

	public User(String firstName, String lastName, @NotNull(message = "Date of birth cannot be null") Date dayOfBirth,
			String email, String userName, String password, boolean enabled) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dayOfBirth = dayOfBirth;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.enabled = enabled;
	}

	// getter & setter
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDayOfBirth() {
		return dayOfBirth;
	}

	public void setDayOfBirth(Date dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public void addUserRole(UserRole userRole) {
		if (userRoles == null) {
			userRoles = new HashSet<UserRole>();
		}
		userRoles.add(userRole);
	}

	public Set<Entry> getEntries() {
		return entries;
	}

	public void setEntries(Set<Entry> entries) {
		this.entries = entries;
	}

	public void addEntry(Entry entry) {
		if (entries == null)
			entries = new HashSet<Entry>();
		entries.add(entry);
	}

	public void encryptPassword() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		password = passwordEncoder.encode(password);
	}

	/**
	 * equals and hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userId;
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
		User other = (User) obj;
		if (userId != other.userId)
			return false;
		return true;
	}

	/**
	 * toString method
	 */
	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", dayOfBirth="
				+ dayOfBirth + ", email=" + email + ", userName=" + userName + ", password=" + password + ", enabled="
				+ enabled + ", userRoles=" + userRoles + ", entries=" + entries + "]";
	}

}
