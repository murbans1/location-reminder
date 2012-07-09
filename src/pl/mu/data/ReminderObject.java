package pl.mu.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "reminder")
public class ReminderObject {
	@DatabaseField(generatedId = true)
    public long id;
	@DatabaseField
	public String title;
	@DatabaseField
	public String endDateTimestamp;
	@DatabaseField
	public String lat;
	@DatabaseField
	public String lon;
	@DatabaseField
	public String description;
	@DatabaseField
	public int reminderAdded;
	
	public ReminderObject(long id, String title, String endDateTimestamp,
			String lat, String lon, String description, int reminderAdded) {
		super();
		this.id = id;
		this.title = title;
		this.endDateTimestamp = endDateTimestamp;
		this.lat = lat;
		this.lon = lon;
		this.description = description;
		this.reminderAdded = reminderAdded;
	}
	
	public ReminderObject() {
		super();
		this.id = -1;
		this.title = "";
		this.endDateTimestamp = "";
		this.lat = "";
		this.lon = "";
		this.description = "";
		this.reminderAdded = 0;
	}
	
	public double getLatDouble(){
		return Double.valueOf(this.lat);
	}
	
	public double getLonDouble(){
		return Double.valueOf(this.lon);
	}

	public int checkIfAdded(){
		return this.reminderAdded;
	}
	
	public void markAdded(int i) {
		this.reminderAdded = i;	
	}
	
	@Override
	public String toString() {
		return "ReminderObject [id=" + id + ", title=" + title
				+ ", endDateTimestamp=" + endDateTimestamp + ", lat=" + lat
				+ ", lon=" + lon + ", description=" + description + "]";
	}
}