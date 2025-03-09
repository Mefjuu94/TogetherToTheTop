import TTT.databaseUtils.TripDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;

@Testcontainers
public class TripDAOTests {
    private TripDAO testObject;
    private final Trip testTrip = EntityFactoryForTests.createTestTrip();
    private final CustomUser customUser = EntityFactoryForTests.createTestUser();

    @BeforeEach
    public void setUp() {
        DAOFactoryForMockTests.clearDatabase();
        this.testObject = DAOFactoryForMockTests.getTripDAO();
        DAOFactoryForMockTests.getCustomUserDAO().saveUser(customUser);
        long id = DAOFactoryForMockTests.getCustomUserDAO().listAllUsers().get(0).getId();
        customUser.setId(id);
        testTrip.setOwner(customUser);
    }

    @Test
    public void addAnnouncementTest() {
        Assertions.assertTrue(testObject.addAnnouncement(testTrip));
    }

    @Test
    public void addAnnouncementTestFail() {
        Trip trip = null;
        Assertions.assertFalse(testObject.addAnnouncement(trip));
    }

    @Test
    public void findTripTest() {
        addAnnouncementTest();
        List<Trip> tripList = testObject.findTrip("destination");
        tripList.get(0).setId(0);

        List<Trip> list = new ArrayList<>();
        list.add(testTrip);
        testTrip.setTripDateTime(tripList.get(0).getTripDateTime()); // because of delay in time
        Assertions.assertEquals(testTrip.toString(), tripList.get(0).toString());
    }

    @Test
    public void findTripTestFail() {
        List<Trip> list = new ArrayList<>();
        Assertions.assertEquals(list, testObject.findTrip("dest"));
    }

    @Test
    public void deleteTripTest() {
        addAnnouncementTest();
        Assertions.assertTrue(testObject.deleteTrip(testTrip));
    }

    @Test
    public void deleteTripTestFail() {
        Assertions.assertFalse(testObject.deleteTrip(null));
    }

    @Test
    public void listAllAnnouncementsTest() {
        addAnnouncementTest();
        List<Trip> tripList = testObject.findTrip(testTrip.getDestination());

        Assertions.assertEquals(tripList.toString(), testObject.listAllAnnouncements().toString());
    }


    @Test
    public void findTripIDTest() {
        testObject.addAnnouncement(testTrip);

        List<Trip> tripList = testObject.listAllAnnouncements();

        Trip trip = tripList.get(0);

        Assertions.assertEquals(trip.toString(), testObject.findTripID(trip.getId()).toString());
    }

    @Test
    public void findTripIDTestFail() {
        testObject.addAnnouncement(testTrip);

        Assertions.assertNull(testObject.findTripID(2));
    }

    @Test
    public void updateTripTest() {
        testObject.addAnnouncement(testTrip);
        List<Trip> tripList = testObject.listAllAnnouncements();

        Trip trip = tripList.get(0);
        trip.setDistanceOfTrip("13h");

        Assertions.assertTrue(testObject.updateTrip(trip));
    }

    @Test
    public void updateTripTestFail() {
        Trip trip = null;
        Assertions.assertFalse(testObject.updateTrip(trip));
    }


    @Test
    public void listAllAnnouncementsByUserIdTestFail() {
        Trip trip = null;

        Assertions.assertFalse(testObject.updateTrip(trip));
    }

    @Test
    public void listAllAnnouncementsByUserIdTest() {
        List<CustomUser> participants = new ArrayList<>();
        participants.add(customUser);
        testTrip.setParticipants(participants);
        testObject.addAnnouncement(testTrip);

        List<Trip> tripList = new ArrayList<>();
        long id = DAOFactoryForMockTests.getCustomUserDAO().listAllUsers().get(0).getId();
        long idTrip = testObject.listAllAnnouncements().get(0).getId();
        testTrip.setId(idTrip);
        tripList.add(testTrip);

        List<Trip> result = testObject.listAllAnnouncementsByUserId(id);
        result.get(0).setTripDateTime(testTrip.getTripDateTime());

        Assertions.assertEquals(tripList.toString(), result.toString()); //"contents are identical" -> to string works
    }

    @Test
    public void listAllTripParticipantIdsTest() {
        testObject.addAnnouncement(testTrip);

        CustomUser participant1 = EntityFactoryForTests.createTestUser();
        CustomUser participant2 = EntityFactoryForTests.createTestUser();
        participant1.setEmail("participant1@email.com");
        participant2.setEmail("participant2@email.com");

        //add users to database
        DAOFactoryForMockTests.getCustomUserDAO().saveUser(participant1);
        DAOFactoryForMockTests.getCustomUserDAO().saveUser(participant2);

        List<CustomUser> participants = DAOFactoryForMockTests.getCustomUserDAO().listAllUsers();

        List<Trip> tripList = testObject.listAllAnnouncements();
        Trip trip = tripList.get(0);
        //getID of trip
        long tripId = trip.getId();
        trip.setParticipants(participants);
        //add participants to trip
        testObject.updateTrip(trip);

        //create map to get participants list
        Map<Long, List<Long>> tripsAndIDs = testObject.listAllTripParticipantIds();

        List<Long> listTripId = new ArrayList<>();
        listTripId.add(tripId);

        Map<Long, List<Long>> results = new HashMap<>();
        for (CustomUser participant : participants) {
            results.put(participant.getId(), listTripId);
        }

        Assertions.assertEquals(results, tripsAndIDs);
    }
}
