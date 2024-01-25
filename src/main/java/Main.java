import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
	//lista pentru sali
	public static List<String> sali = new ArrayList<>();

	
	// Stiu ca nu pare mult dar mi-a luat
	public static void main(String[] args) throws IOException {
		File file = new File("D:\\eclipse\\Workspace\\ExcelToCsvConvertor\\MI_34_OrSt33 2.xlsx");
		FileInputStream fis = new FileInputStream(file);
		processSchedule(fis);
	}

	private static void processSchedule(FileInputStream fis) throws IOException {
		Workbook workbook = new XSSFWorkbook(fis);
		try {
			Sheet sheet = workbook.getSheetAt(0);

			// Ignora primele 4 coloane(anul,spec,grupa,sgr)
			int startColumn = 4;
			// Incepe prelucrarea in functie de ziua in care esti 
			startColumn += (1 - 1) * 7;
			// Seteaza lungimea maxima pt ziua in care esti
			int maxColumns = Math.min(sheet.getRow(0).getPhysicalNumberOfCells(), startColumn + 7);
			// i = 5 pt ca sari peste primele 5 randuri
			for (int j = startColumn; j < maxColumns; j++) {

				System.out.println("Coloana" + (j + 1));
				//Prelucreaza celule de pe coloane
				for (int i = 7; i < sheet.getPhysicalNumberOfRows(); i++) {
					Row row = sheet.getRow(i);
					Cell cell = row.getCell(j);
					splitCell(cell);
				}
				System.out.print("Sali ocupate: ");
				for (String a : sali) {
					System.out.print(" " + a);
				}
				System.out.println();
				//goleste lista pt ora la care ai fost
				sali.clear();
			}

		} finally {
			workbook.close();
			fis.close();
		}

	}


	public static void splitCell(Cell cell) {
		String cellText = cell.toString();
		String[] arrOfStr = cellText.split(",");
		//Exclude celulele neimportante
		if (arrOfStr.length > 3) {
			sali.add(arrOfStr[2]);
		}

	}

	// Functie care converteste orice tip de date din excel in string
	private static String getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			return Double.toString(cell.getNumericCellValue());
		case Cell.CELL_TYPE_BOOLEAN:
			return Boolean.toString(cell.getBooleanCellValue());
		case Cell.CELL_TYPE_BLANK:
			return "";
		default:
			return "";
		}
	}
}
