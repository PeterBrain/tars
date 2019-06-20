package at.fh.swenga.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "entry_history")
public class EntryHistory implements java.io.Serializable {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int EntryHistoryId;
	
	@Column(name = "timestamp")
	private Date timestamp;
	
	@Column(name = "note")
	private String note;
	
	@Column(name = "activity")
	private String activity;
	
	@Column(name = "start")
	private Date timestampStart;
	
	@Column(name = "end")
	private Date timestampEnd;
	
	@Column(name = "minutes")
	private float minutes;
	
	@Column(name = "entry")
	private int entryId;
	
	@Column(name = "project")
	private String project;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "username")
	private String username;
	
	@Version
	private long version;
	
	public EntryHistory() {
		// TODO Auto-generated constructor stub
	}

	public EntryHistory(Date timestamp, String note, String activity, Date timestampStart, Date timestampEnd,
			float minutes, int entryId, String project, String category, String username) {
		super();
		this.timestamp = timestamp;
		this.note = note;
		this.activity = activity;
		this.timestampStart = timestampStart;
		this.timestampEnd = timestampEnd;
		this.minutes = minutes;
		this.entryId = entryId;
		this.project = project;
		this.category = category;
		this.username = username;
	}

	public int getEntryHistoryId() {
		return EntryHistoryId;
	}

	public void setEntryHistoryId(int entryHistoryId) {
		EntryHistoryId = entryHistoryId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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

	public float getMinutes() {
		return minutes;
	}

	public void setMinutes(float minutes) {
		this.minutes = minutes;
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
		result = prime * result + EntryHistoryId;
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
		EntryHistory other = (EntryHistory) obj;
		if (EntryHistoryId != other.EntryHistoryId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EntryHistory [EntryHistoryId=" + EntryHistoryId + ", timestamp=" + timestamp + ", note=" + note
				+ ", activity=" + activity + ", timestampStart=" + timestampStart + ", timestampEnd=" + timestampEnd
				+ ", minutes=" + minutes + ", entryId=" + entryId + ", project=" + project + ", category=" + category
				+ ", username=" + username + ", version=" + version + "]";
	}
	
	
}
