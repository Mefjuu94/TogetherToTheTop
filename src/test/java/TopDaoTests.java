import TTT.databaseUtils.TopDAO;
import TTT.peaksAndSummitsHandler.Top;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class TopDaoTests {

    TopDAO testObject = new TopDAO();
    Top testTop = new Top("test", 1, 111, 222);
    List<Top> testlist = new ArrayList<>();

    private List<Top> setList() {
        testObject.addTop(testTop);
        return setList();
    }

    @Test
    public void addTopTest() {
        Assertions.assertTrue(testObject.addTop(testTop));
    }

    @Test
    public void addBigTopTestFail() {
        Assertions.assertFalse(testObject.addTop(new Top("biggest top ever", 9999, 123123, 213232)));
        testObject.deleteTop("biggest top ever");
    }

    @Test
    public void addSmallTopTestFail() {
        Assertions.assertFalse(testObject.addTop(new Top("smalles top ever", -2, 123123, 213232)));
        testObject.deleteTop("smalles top ever");
    }

    @Test
    public void findSummitTest() {
        testObject.addTop(testTop);
        testlist.add(testTop);
        Assertions.assertEquals(testlist, testObject.findSummitByName("test"));
    }

    @Test
    public void findSummitTestFail() {
        testObject.addTop(testTop);
        testlist.add(testTop);
        Assertions.assertNotEquals(testlist, testObject.findSummitByName("tested"));
    }


    @Test
    public void getAllSummitRecordsNumberTest() {
        testObject.deleteTop("test"); //to avoid one more record from "test"
        Assertions.assertEquals(7912, testObject.getAllSummits().size()); //should return 7912 records
    }


    @Test
    public void deleteSummitTest() {
        testObject.addTop(testTop);
        testlist.add(testTop);
        Assertions.assertTrue(testObject.deleteTop("test"));
        Assertions.assertNotEquals(testlist, testObject.findSummitByName("test"));
    }

    @Test
    public void deleteSummitTestFail() {
        Assertions.assertFalse(testObject.deleteTop("testing"));
    }

    @Test
    public void findSummitHeightBetween999_1000Test() {
        testlist.add(new Top("Podmokła", 1000, 50.8766F, 15.353342F));
        testlist.add(new Top("Suhora", 1000, 49.56942F, 20.06728F));
        testlist.add(new Top("Brija", 1000, 49.324444F, 20.182388F));
        Assertions.assertEquals(testlist, testObject.findSummitsByHeightBetween(999, 1000));
    }

    @Test
    public void findSummitHeightBetween0_2TestFail() {
        testObject.deleteTop("test");
        Assertions.assertEquals(testlist, testObject.findSummitsByHeightBetween(1, 2));
    }

    @Test
    public void findSummitHeightBetween999_1000TestFail() {
        testlist.add(new Top("Brija", 1000, 49.324444F, 20.182388F));
        Assertions.assertNotEquals(testlist, testObject.findSummitsByHeightBetween(999, 1000));
    }

    @Test
    public void findSummitsByHeightGreaterThan() {
        testlist.add(new Top("Ťažký štít", 2500, 49.173706F, 20.091536F));
        testlist.add(new Top("Rysy", 2503, 49.179317F, 20.088434F));
        testlist.add(new Top("Rysy", 2499, 49.179585F, 20.088047F));
        Assertions.assertEquals(testlist, testObject.findSummitsByHeightGreaterThan(2450));
    }

    @Test
    public void findSummitsByHeightGreaterThanFail() {
        testlist.add(new Top("Rysy", 2503, 49.179317F, 20.088434F));
        Assertions.assertNotEquals(testlist, testObject.findSummitsByHeightGreaterThan(2450));
    }

    @Test
    public void findSummitsByHeightGreaterThan8000isEmpty() {
        Assertions.assertEquals(testlist, testObject.findSummitsByHeightGreaterThan(8000));
    }

    @Test
    public void findSummitsByHeightLessThan() {
        testObject.deleteTop("test"); // to avoid test object
        testlist.add(new Top("Jarcowa Skałka", 3, 49.275032F, 19.869242F));
        Assertions.assertEquals(testlist, testObject.findSummitsByHeightLessThan(5));
    }

    @Test
    public void findSummitsByHeightLessThan2isEmpty() {
        testObject.deleteTop("test"); // to avoid test object
        Assertions.assertEquals(testlist, testObject.findSummitsByHeightLessThan(2));
    }

    @Test
    public void findSummitsByHeightLessThanFail() {
        testObject.deleteTop("test"); // to avoid test object
        testlist.add(new Top("Wzgórze Cmentarne", 6, 54.66309F, 17.060831F));
        Assertions.assertNotEquals(testlist, testObject.findSummitsByHeightLessThan(5));
    }


    @Test
    public void findSummitsByHeight() {
        testObject.deleteTop("test"); // to avoid test object
        testlist.add(new Top("Wzgórze Cmentarne", 6, 54.66309F, 17.060831F));
        Assertions.assertEquals(testlist, testObject.findSummitByHeight(6));
    }

    @Test
    public void findSummitsByHeightNoResult() {
        Assertions.assertEquals(testlist, testObject.findSummitByHeight(1800));
    }

}
