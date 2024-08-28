import TTT.PeakAndSummitsHandler.Top;
import TTT.PeakAndSummitsHandler.TopDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class TopDaoTests {

    TopDAO testObject = new TopDAO();
    Top testTop = new Top("test",1,111,222);
    List<Top> testlist = new ArrayList<>();

    private List<Top> setList(){
        testObject.addTop(testTop);
        return setList();
    }

    @Test
    public void addTopTest(){
        Assertions.assertTrue(testObject.addTop(testTop));
    }

    @Test
    public void findSummitTest(){
        testObject.addTop(testTop);
        testlist.add(testTop);
        Assertions.assertEquals(testlist,testObject.findSummitByName("test"));
    }

    @Test
    public void getAllSummitRecordsNumberTest(){
        testObject.deleteTop("test"); //to avoid one more record from "test"
        Assertions.assertEquals(7912,testObject.getAllSummits().size()); //shoould return 7912 records
    }


    @Test
    public void findSummitTestFail(){
        testObject.addTop(testTop);
        testlist.add(testTop);
        Assertions.assertNotEquals(testlist,testObject.findSummitByName("tested"));
    }

    @Test
    public void deleteSummitTest(){
        testObject.addTop(testTop);
        testlist.add(testTop);
        Assertions.assertTrue(testObject.deleteTop("test"));
        Assertions.assertNotEquals(testlist,testObject.findSummitByName("test"));
    }

    @Test
    public void findSummitHeightBetween999_1000Test(){
        testlist.add(new Top("Podmokła",1000, 50.8766F, 15.353342F));
        testlist.add(new Top("Suhora",1000, 49.56942F, 20.06728F));
        testlist.add(new Top("Brija",1000, 49.324444F, 20.182388F));
        Assertions.assertEquals(testlist,testObject.findSummitsByHeightBetween(999,1000));
    }

    @Test
    public void findSummitHeightBetween999_1000TestFail(){
        testlist.add(new Top("Brija",1000, 49.324444F, 20.182388F));
        Assertions.assertNotEquals(testlist,testObject.findSummitsByHeightBetween(999,1000));
    }

}
