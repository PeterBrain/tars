package at.fh.swenga.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "entry")
@NamedQuery(name = "Entry.findByEditor", query = "SELECT e FROM Entry e WHERE e.editor = :user")
@NamedQuery(name = "Entry.getAllEntriesFromUser", query = "SELECT e.timestampStart, e.timestampEnd FROM Entry e, IN (e.editor) AS ed WHERE ed.id = :id")
public class Entry implements java.io.Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int entryId;

	@Column(name = "note", nullable = false, length = 30)
	private String note;

	@Column(name = "activity", nullable = false, length = 30)
	private String activity;

	// @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	@NotNull(message = "create time cannot be null")
	@Column(name = "start", nullable = false)
	private Date timestampStart;

	// @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	@Column(name = "end", nullable = true)
	private Date timestampEnd;

	@Column(name = "minutes", nullable = true)
	private float minutes;

	// @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	@NotNull(message = "create time cannot be null")
	@Column(name = "created", nullable = false)
	private Date timestampCreated;

	// @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	@NotNull(message = "modify time cannot be null")
	@Column(name = "modified", nullable = false)
	private Date timestampModified;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private User editor;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private Category category;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private Project project;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@Version
	private long version;

	// constructor
	public Entry() {
	}

	public Entry(String note, String activity, @NotNull(message = "create time cannot be null") Date timestampStart,
			@NotNull(message = "create time cannot be null") Date timestampEnd,
			@NotNull(message = "create time cannot be null") Date timestampCreated,
			@NotNull(message = "modify time cannot be null") Date timestampModified, boolean enabled) {
		super();
		this.note = note;
		this.activity = activity;
		this.timestampStart = timestampStart;
		this.timestampEnd = timestampEnd;
		this.timestampCreated = timestampCreated;
		this.timestampModified = timestampModified;
		this.enabled = enabled;
	}

	// getter & setter
	public int getEntryId() {
		return entryId;
	}

	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Date getTimestampCreated() {
		return timestampCreated;
	}

	public void setTimestampCreated(Date timestampCreated) {
		this.timestampCreated = timestampCreated;
	}

	public Date getTimestampModified() {
		return timestampModified;
	}

	public void setTimestampModified(Date timestampModified) {
		this.timestampModified = timestampModified;
	}

	public User getEditor() {
		return editor;
	}

	public void setEditor(User editor) {
		this.editor = editor;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getTimestampStart() {
		return timestampStart;
	}

	public void setTimestampStart(Date timestampStart) {
		this.timestampStart = timestampStart;
	}

	public Date getTimestampEnd() {
		return timestampEnd;
	}

	public void setTimestampEnd(Date timestampEnd) {
		this.timestampEnd = timestampEnd;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	// equals & hashcode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + entryId;
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
		Entry other = (Entry) obj;
		if (entryId != other.entryId)
			return false;
		return true;
	}

	public float getMinutes() {
		return minutes;
	}

	public void setMinutes(float minutes) {
		this.minutes = minutes;
	}

	// toString
	@Override
	public String toString() {
		return "Entry [entryId=" + entryId + ", note=" + note + ", activity=" + activity + ", timestampStart="
				+ timestampStart + ", timestampEnd=" + timestampEnd + ", timestampCreated=" + timestampCreated
				+ ", timestampModified=" + timestampModified + ", editor=" + editor + ", category=" + category
				+ ", project=" + project + ", enabled=" + enabled + ", version=" + version + "]";
	}

}