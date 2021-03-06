package at.fh.swenga.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
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
@NamedQuery(name = "User.getAllWithRole", query = "SELECT u FROM User u, IN (u.userRoles) AS ur WHERE ur.id = :id")
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
	private Date dateOfBirth;

	@Column(name = "email", nullable = false, length = 45, unique = true)
	private String email;

	@Column(name = "username", nullable = false, length = 45, unique = true)
	private String userName;

	@Column(name = "password", nullable = false)
	private String password;

	@NotNull(message = "Total Holiday cannot be null")
	@Column(name = "holidaytotal", nullable = false)
	private int holidayTotal;

	@NotNull(message = "Consumed Holiday cannot be null")
	@Column(name = "holidayconsumed", nullable = false)
	private int holidayConsumed;

	@NotNull(message = "Working hours per week cannot be null")
	@Column(name = "workinghoursweek", nullable = false)
	private int workingHoursWeek;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@ManyToMany(fetch = FetchType.EAGER) // cascade = CascadeType.MERGE would work for user insert
	private Set<UserRole> userRoles;

	@OneToMany(mappedBy = "editor", fetch = FetchType.EAGER)
	private Set<Entry> entries;

	// https://howtodoinjava.com/hibernate/hibernate-one-to-many-mapping-using-annotations/
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	private Set<Project> projects;

	@Version
	private long version;

	// constructor
	public User() {
	}

	public User(String firstName, String lastName, @NotNull(message = "Date of birth cannot be null") Date dateOfBirth,
			String email, String userName, String password, int workingHoursWeek, int holidayConsumed,
			boolean enabled) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.enabled = enabled;
		this.workingHoursWeek = workingHoursWeek;
		this.holidayConsumed = holidayConsumed;
		this.holidayTotal = workingHoursWeek * 5;
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
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

	public int getHolidayTotal() {
		return holidayTotal;
	}

	public void setHolidayTotal(int holidayTotal) {
		this.holidayTotal = holidayTotal;
	}

	public int getHolidayConsumed() {
		return holidayConsumed;
	}

	public void setHolidayConsumed(int holidayConsumed) {
		this.holidayConsumed = holidayConsumed;
	}

	public int getWorkingHoursWeek() {
		return workingHoursWeek;
	}

	public void setWorkingHoursWeek(int workingHoursWeek) {
		this.workingHoursWeek = workingHoursWeek;
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

	public void removeUserRole(UserRole userRole) {
		if (userRoles != null) {
			userRoles.remove(userRole);
		}
	}

	public void removeAllUserRoles() {
		userRoles = new HashSet<UserRole>();
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

	public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(oldPassword, user.getPassword());
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
		return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth="
				+ dateOfBirth + ", email=" + email + ", userName=" + userName + ", password=" + password + ", enabled="
				+ enabled + ", userRoles=" + userRoles + ", entries=" + entries + ", projects=" + projects
				+ ", version=" + version + "]";
	}
}
