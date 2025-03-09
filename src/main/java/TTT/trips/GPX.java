package TTT.trips;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GPX {


    public void makeGPX(String geoJsonString, int numberOfWaypoints, String filePath) {

        String jsonString = "{\"geometry\": {\"type\": \"LineString\", \"coordinates\": [" + geoJsonString + "]}}";
        JSONObject geoJson = new JSONObject(jsonString);
        JSONArray coordinates = geoJson.getJSONObject("geometry").getJSONArray("coordinates");

        StringBuilder gpxContent = new StringBuilder();
        gpxContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        gpxContent.append("<gpx version=\"1.1\" creator=\"GPXCreator\">\n");
        gpxContent.append("  <rte>\n");

        double start1 = 0;
        double start2 = 0;

        int counterOfRepeat = 0;


        for (int i = 0; i < coordinates.length(); i++) {
            JSONArray coord = coordinates.getJSONArray(i);
            if (i == 0) {
                start1 = coord.getDouble(0);
                start2 = coord.getDouble(1);
            }

            double lon = coord.getDouble(0);
            double lat = coord.getDouble(1);


            if (!(start1 == lon) && !(start2 == lat)) {
                counterOfRepeat += 1;

                if (counterOfRepeat == numberOfWaypoints-1) {
                    gpxContent = new StringBuilder();
                    gpxContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                    gpxContent.append("<gpx version=\"1.1\" creator=\"GPXCreator\">\n");
                    gpxContent.append("<rte>\n");
                }
            }
            gpxContent.append("<rtept lon=\"").append(lon).append("\" lat=\"").append(lat).append("\">\n");
            gpxContent.append("</rtept>\n");
        }

        gpxContent.append("  </rte>\n");
        gpxContent.append("</gpx>");

        String folderPath = "src/main/resources/routes";

        File folder = new File(folderPath);

        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("The folder has been created: " + folderPath);
            } else {
                System.out.println("Failed to create folder.");
            }
        } else {
            System.out.println("The folder already exists.");
        }

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(gpxContent.toString());
            fileWriter.close();
            System.out.println("GPX file written to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error when trying to save: " + e.getMessage());
        }
    }
}
