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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "entry")
public class Entry implements java.io.Serializable {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int entryId;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	@NotNull(message = "timestamp cannot be null")
	@Column(name = "timestamp", nullable = false)
	private Date timestamp;

	@Column(name = "note", nullable = false, length = 30)
	private String note;

	@Column(name = "activity", nullable = false, length = 30)
	private String activity;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	@NotNull(message = "create time cannot be null")
	@Column(name = "created", nullable = false)
	private Date timestampCreated;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	@NotNull(message = "modify time cannot be null")
	@Column(name = "modified", nullable = false)
	private Date timestampModified;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private User editor;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@Version
	private long version;

	// constructor
	public Entry() {
	}

	// getter & setter
}