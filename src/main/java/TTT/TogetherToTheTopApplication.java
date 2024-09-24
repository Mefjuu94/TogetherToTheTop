package TTT;

import TTT.databaseUtils.CustomUserDAO;
import TTT.peaksAndSummitsHandler.PeaksAndSummitsHandler;
import TTT.peaksAndSummitsHandler.Top;
import TTT.databaseUtils.TopDAO;
import TTT.users.CustomUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class TogetherToTheTopApplication {

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

//        CustomUser cs = new CustomUser("costam@mail.com","123");
//        CustomUserDAO dao = new CustomUserDAO();
//        dao.saveUser(cs);
//
//        System.out.println(dao.findCustomUser("costam@mail.com").getId());
//        System.out.println(dao.deleteCustomUser("costam@mail.com"));


//        List<Top> summits = topDAO.findSummitByName("trzy");
//
//
//        for (Top summit : summits) {
//            System.out.println(summit);
//        }

       // System.out.println("Ilość szczytów: " + summits.size());





    }


}
