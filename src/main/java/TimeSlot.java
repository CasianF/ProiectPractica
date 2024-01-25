import java.util.ArrayList;
import java.util.List;

public class TimeSlot {

	private int time;
    private String courseName;
    private char courseType;
    private String professorName;
    private List<String> groupNames;

    public TimeSlot(int time, String courseName, char courseType, String professorName) {
        this.time = time;
        this.courseName = courseName;
        this.courseType = courseType;
        this.professorName = professorName;
        this.groupNames = new ArrayList<String>();
    }

    public TimeSlot(int time, String courseName, char courseType, String professorName, List<String> groupNames) {
        this.time = time;
        this.courseName = courseName;
        this.courseType = courseType;
        this.professorName = professorName;
        this.groupNames = groupNames;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getGroupNames() {
        String result = "";

        for(int i = 0; i < groupNames.size(); i++)
        {
            if(i == groupNames.size() - 1)
            {
                result += groupNames.get(i);
            }
            else
            {
                result += groupNames.get(i) + ", ";
            }
        }

        return result;
    }

    public List<String> getGroups() {
        return this.groupNames;
    }

    public void setGroupNames(List<String> groupNames) {
        this.groupNames = groupNames;
    }

    public void addGroup(String groupName)
    {
        this.groupNames.add(groupName);
    }

    public char getCourseType() {
        return courseType;
    }

    public void setCourseType(char courseType) {
        this.courseType = courseType;
    }

    @Override
    public String toString() {
        return courseName + ", " + professorName + "\n" + groupNames;
    }

}