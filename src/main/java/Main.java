import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
	// lista pentru sali
	public static List<Room> roomsList = new ArrayList<Room>();

	public static void processData() throws IOException {
		File inputFile = new File("MI_34_OrSt33 2.xlsx");
		FileInputStream fis = new FileInputStream(inputFile);
		Calendar calendar = Calendar.getInstance();
		int day = calendar.WEDNESDAY;

		Workbook workbook = new XSSFWorkbook(fis);
		try {
			Sheet sheet = workbook.getSheetAt(0);

			// Ignora primele 4 coloane (anul, spec, grupa, sgr)
			int startColumn = 4;
			startColumn = getCurrentDay(startColumn, day);
			System.out.println(startColumn);
			// Lungimea maxima pentru o zi
			int maxColumns = 6 + startColumn;

			// Parcurgem tabelul pe coloane si apoi pe randuri
			for (int j = startColumn; j < maxColumns; j++) {
				for (int i = 7; i < sheet.getPhysicalNumberOfRows(); i++) {
					Row row = sheet.getRow(i);
					Cell cell = row.getCell(j);

					processCell(cell, j - startColumn);
				}
			}
		} finally {
			workbook.close();
			fis.close();
		}
	}

	public static int getCurrentDay(int startColumn, int day) {
		switch (day) {
		case Calendar.MONDAY:
			return startColumn + 0;
		case Calendar.TUESDAY:
			return startColumn + 7;
		case Calendar.WEDNESDAY:
			return startColumn + 14;
		case Calendar.THURSDAY:
			return startColumn + 21;
		case Calendar.FRIDAY:
			return startColumn + 28;
		case Calendar.SATURDAY:
			return startColumn + 35;
		default:
			return startColumn;
		}
	}

	public static void processCell(Cell cell, int time) {
		// Get the text of the cell as a String
		String cellText = cell.toString();
		if (cellText.isEmpty()) {
			return;
		}

		// Split the String into "tokens"
		String[] tokens = cellText.split(",");

		if (tokens.length < 4) {
			return;
		}

		System.out.println("[cellText] " + cellText);
		// Assign elements of the cell to their respective variables
		String courseName = tokens[0];
		char courseType = tokens[1].charAt(0);
		String roomName = tokens[2];
		String professorName = tokens[3];

		// Check to see if the room with "roomName" exists already
		Room roomFound = null;
		for (Room room : roomsList) {
			if (room.getName().equals(roomName))
				roomFound = room;
		}
		// Room exists
		if (roomFound != null) {
			// Get the group name that corresponds to this cell
			String groupName = getGroup(cell);

			// Check to see if the room already has a TimeSlot at the time specified
			TimeSlot timeSlotFound = roomFound.getTimeSlot(time);
			// If it does, don't replace it
			if (timeSlotFound != null) {
				// Check to see if the group is not already added to the TimeSlot
				if (!timeSlotFound.getGroups().contains(groupName))
					// If not, add the group to the TimeSlot
					timeSlotFound.addGroup(groupName);
			}
			// If the TimeSlot doesn't exist, create it with the correct attributes and add
			// it to the room object
			else {
				TimeSlot timeSlot = new TimeSlot(time, courseName, courseType, professorName);
				timeSlot.addGroup(groupName);
				roomFound.addTimeSlot(time, timeSlot);
			}

		}
		// Else, create the room object then add it to the list
		else {
			Room roomNew = new Room(roomName);
			String groupName = getGroup(cell);
			TimeSlot timeSlot = new TimeSlot(time, courseName, courseType, professorName);
			timeSlot.addGroup(groupName);
			roomNew.addTimeSlot(time, timeSlot);
			roomsList.add(roomNew);
		}
	}

	// Function that gets the group of a cell in the table
	public static String getGroup(Cell cell) {
		// Get the sheet and the row number of the cell
		Sheet sheet = cell.getSheet();
		int rowNumber = cell.getRowIndex();

		String result = "";

		// Since merged cells are un-merged when imported with Apache POI, we have to
		// "search" for the value they hold,
		// which is in the first cell of the merged cell (the top-left most cell)
		// In this case, we can only ever be at or below the group text, so every
		// iteration we check if the cell is empty,
		// and if it is, we go up a row until we find text (The first text we find
		// should be the group name)
		for (int i = rowNumber; i > rowNumber - 4; i--) {
			Row row = sheet.getRow(i);
			Cell cellGroup = row.getCell(2);
			if (!cellGroup.toString().isEmpty()) {
				result = cellGroup.toString();
				break;
			}
		}

		return result;
	}

	public static void main(String[] args) throws IOException {
		processData();
	}
}
