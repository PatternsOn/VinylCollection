package main;

public class Record {
	// Properties
	private String track;
	private String artist;
	private String remix;
	private String label;
	private String recordID;
	private String owner;
	private String forSale;
	
	public Record(String track, String artist, String remix, String label, String recordID, String owner, String forSale) {
		this.track = track;
		this.artist = artist;
		this.remix = remix;
		this.label = label;
		this.recordID = recordID;
		this.owner = owner;
		this.forSale = forSale;	
	}

	public String getTrack() {
		return track;
	}

	public String getArtist() {
		return artist;
	}

	public String getRemix() {
		return remix;
	}

	public String getLabel() {
		return label;
	}

	public String getRecordID() {
		return recordID;
	}

	public String getOwner() {
		return owner;
	}

	public String isForSale() {
		return forSale;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setRemix(String remix) {
		this.remix = remix;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setForSale(String forSale) {
		this.forSale = forSale;
	}
}
