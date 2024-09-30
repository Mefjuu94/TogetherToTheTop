package TTT;

import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.peaksAndSummitsHandler.PeaksAndSummitsHandler;
import TTT.peaksAndSummitsHandler.Top;
import TTT.databaseUtils.TopDAO;
import TTT.trips.Trip;
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


        Trip trip = new Trip("coś tam","Rysy",null,"2h 44m",true,5,
                false,0,null);

        TripDAO tripDAO = new TripDAO();

        tripDAO.addAnnouncement(trip);




    }
}
