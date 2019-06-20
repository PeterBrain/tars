package at.fh.swenga.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "project")
public class Project implements java.io.Serializable {

	@Id
	@Column(name = "projectid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int projectId;

	@Column(name = "name", length = 45)
	private String name;

	@Column(name = "description", length = 45)
	private String description;

	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
	private Set<Entry> entries;

	//https://howtodoinjava.com/hibernate/hibernate-one-to-many-mapping-using-annotations/
	@ManyToOne
	private User projectLeader;

	@Version
	private long version;

	public Project() {
		// TODO Auto-generated constructor stub
	}

	public Project(String name, String description, User projectLeader) {
		super();
		this.name = name;
		this.description = description;
		this.projectLeader = projectLeader;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Entry> getEntries() {
		return entries;
	}

	public void setEntries(Set<Entry> entries) {
		this.entries = entries;
	}

	public User getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(User projectLeader) {
		this.projectLeader = projectLeader;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + projectId;
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
		Project other = (Project) obj;
		if (projectId != other.projectId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Project [projectId=" + projectId + ", name=" + name + ", description=" + description + ", entries="
				+ entries + ", projectLeader=" + projectLeader + "]";
	}

}
