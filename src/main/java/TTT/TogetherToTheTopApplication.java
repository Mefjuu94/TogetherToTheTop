package TTT;

import TTT.databaseUtils.CustomUserDAO;
import TTT.users.CustomUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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

        //work :D
//        List<Top> rysy = topDAO.findSummitByName("Rysy");
//        for (Top top : rysy) {
//            System.out.println(top);
//        }

//        List<Top> myHeight = topDAO.findSummitByHeight(1800);
//
//        for (Top top : myHeight) {
//            System.out.println(top);
//        }

        CustomUser cs = new CustomUser("costam@mail.com","123");
        CustomUserDAO dao = new CustomUserDAO();
        dao.saveUser(cs);

        System.out.println(dao.findCustomUser("costam@mail.com").getId());
        System.out.println(dao.deleteCustomUser("costam@mail.com"));


//        List<Top> summits = topDAO.findSummitByName("trzy");
//
//
//        for (Top summit : summits) {
//            System.out.println(summit);
//        }

       // System.out.println("Ilość szczytów: " + summits.size());





    }
}
