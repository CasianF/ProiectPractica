import java.util.List;

public class Room {
	   private int id;
	    private String name;

	    public TimeSlot timeSlots[];

	    public Room(String name) {
	        this.name = name;
	        this.timeSlots = new TimeSlot[7];
	    }

	    public Room(String name, List<TimeSlot> timeSlots)
	    {
	        this.name = name;
	        this.timeSlots = new TimeSlot[7];
	        for (TimeSlot timeSlot : timeSlots) {
	            this.timeSlots[timeSlot.getTime()] = timeSlot;
	        }
	    }

	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public TimeSlot getTimeSlot(int i)
	    {
	        return timeSlots[i];
	    }

	    public void addTimeSlot(int i, TimeSlot timeSlot)
	    {
	        this.timeSlots[i] = timeSlot;
	    }

	    public void setTimeSlots(List<TimeSlot> timeSlots)
	    {
	        this.timeSlots = new TimeSlot[7];
	        for (TimeSlot timeSlot : timeSlots) {
	            this.timeSlots[timeSlot.getTime()] = timeSlot;
	        }
	    }
}
