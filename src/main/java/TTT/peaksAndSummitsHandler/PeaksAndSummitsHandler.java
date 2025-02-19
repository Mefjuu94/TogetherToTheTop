package TTT.peaksAndSummitsHandler;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class PeaksAndSummitsHandler {
    public static ArrayList<Top> polishSummits = new ArrayList<>();

    public PeaksAndSummitsHandler() {

    }

    public static void readAndCreateObjectsFromXLSX() {

        File file = new File("src/main/resources/peaksAndSummits/Wierzcholki-wzgorza-i-szczyty-gor-w-Polsce.xls");
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0; // No of columns
            int tmp = 0;

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for (int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if (tmp > cols) cols = tmp;
                }
            }

            for (int r = 1; r < rows; r++) { //  ! r=1 not 0! to avoid description of column
                row = sheet.getRow(r);
                if (row != null) {
                    cell = row.getCell((short) 0);
                    String heightOf = String.valueOf(row.getCell((short) 1));
                    if (!cell.getStringCellValue().isEmpty() && !heightOf.isEmpty()) { //Name or Height cannot be empty! -> avoid record if is empty!
                        String name = String.valueOf(cell);
                        String heightDouble = String.valueOf(row.getCell((short) 1));
                        String latitude = String.valueOf(row.getCell((short) 2));
                        String longitude = String.valueOf(row.getCell((short) 3));
                        String[] height = heightDouble.split("\\.");
                        latitude = latitude.replace(",", ".");
                        longitude = longitude.replace(",", ".");
                        polishSummits.add(new Top(name, Integer.parseInt(height[0]), Float.parseFloat(latitude), Float.parseFloat(longitude)));
                    }

                }
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
}


