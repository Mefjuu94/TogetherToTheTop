import TTT.trips.Comments;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EntityFactoryForTests {

    protected static CustomUser createTestUser() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "testUser", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city", 0);
        return customUser;
    }

    protected static Trip createTestTrip() {
        return new Trip.TripBuilder()
                .withTripDescription("description")
                .withDestination("destination")
                .withOwner(createTestUser())
                .withTripDuration("5h")
                .withClosedGroup(false)
                .withAmountOfClosedGroup(1)
                .withDriverPeople(false)
                .withAmountOfDriverPeople(12)
                .withAnimals(true)
                .withWaypoints("")
                .withTripDataTime(LocalDateTime.now())
                .withDistanceOfTrip("5km")
                .withGpxFile(null)
                .build();
    }

    public static Comments createTestComment() {
        return new Comments("new comment", 1, 1, "Alex");
    }

    protected static UserRating createRate() {
        CustomUser reviewer = new CustomUser(2, "reviewer@mail.com", "testUser123!",
                "reviewerTestUser", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "reviewerCity", 0);
        UserRating rate = new UserRating(5, createTestTrip(), false, "comment", createTestUser(), reviewer);
        return rate;
    }
}
