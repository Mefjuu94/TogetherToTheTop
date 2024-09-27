package TTT;

import TTT.databaseUtils.CustomUserDAO;
import TTT.peaksAndSummitsHandler.PeaksAndSummitsHandler;
import TTT.peaksAndSummitsHandler.Top;
import TTT.databaseUtils.TopDAO;
import TTT.users.CustomUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@SpringBootApplication
public class TogetherToTheTopApplication {

    static String city = "Praha";
    static String APIKEY = "zjEFy9NsTMuP_e3U9_B0sDu_axPSSl28smWg1PXW4i0";

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TogetherToTheTopApplication.class, args);

        TopDAO topDAO = new TopDAO();

        //fill database from file
//            PeaksAndSummitsHandler.readAndCreateObjectsFromXLSX();
//        for (int i = 0; i < PeaksAndSummitsHandler.polishSummits.size(); i++) {
//            topDAO.addTop(PeaksAndSummitsHandler.polishSummits.get(i));
//        }



//        HttpResponse<String> resp = getresp();
//        if (resp!=null) {
//            System.out.println(resp.statusCode());
//            System.out.println(resp.body());
//        }else {
//            System.out.println("pusta odpowiedz");
//        }
//

// https://api.mapy.cz/v1/suggest?lang=pl&apikey=eyJpIjoyNTcsImMiOjE2Njc0ODU2MjN9.c_UlvdpHGTI_Jb-TNMYlDYuIkCLJaUpi911RdlwPsAY&query=Katowice&limit=5






    }
//    final static HttpClient client = HttpClient.newHttpClient();
//
//    public static HttpResponse<String> getresp() {
//        try {
//            HttpRequest request = HttpRequest.newBuilder(
//                    new URI("https://api.mapy.cz/v1/suggest?lang=pl&apikey=" + APIKEY + "&query==" + city + "&limit=5")).GET().build();
//            //String urlString = "https://api.mapy.cz/geocode?query=" + encodedQuery + "&apikey=" + API_KEY;
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            if (response.statusCode() == 200) {
//                return response;
//            }
//            return null;
//        } catch (URISyntaxException | IOException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
