package TTT.trips;

import TTT.users.CustomUser;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.List;
import java.util.Objects;

@Entity
public class Trip {

    @Id
    @GeneratedValue
    private long id;

    public String tripDescription;

    public String destination;

    //private CustomUser user;

    private String tripDuration;

    private boolean closedGroup;
    private int amountOfClosedGroup;

    private boolean DriverPeople;
    private int amountOfDriverpeople;

    //private List<CustomUser> participants;

    public Trip(String tripDescription, String destination, CustomUser user, String tripDuration,
                boolean closedGroup, int amountOfClosedGroup, boolean driverPeople,
                int amountOfDriverpeople, List<CustomUser> participants) {
        this.tripDescription = tripDescription;
        this.destination = destination;
       // this.user = user;
        this.tripDuration = tripDuration;
        this.closedGroup = closedGroup;
        this.amountOfClosedGroup = amountOfClosedGroup;
        DriverPeople = driverPeople;
        this.amountOfDriverpeople = amountOfDriverpeople;
       // this.participants = participants;
    }

    public Trip() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTripDescription() {
        return tripDescription;
    }

    public void setTripDescription(String tripDescription) {
        this.tripDescription = tripDescription;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

//    public CustomUser getUser() {
//        return user;
//    }
//
//    public void setUser(CustomUser user) {
//        this.user = user;
//    }

    public String getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(String tripDuration) {
        this.tripDuration = tripDuration;
    }

    public boolean isClosedGroup() {
        return closedGroup;
    }

    public void setClosedGroup(boolean closedGroup) {
        this.closedGroup = closedGroup;
    }

    public int getAmountOfClosedGroup() {
        return amountOfClosedGroup;
    }

    public void setAmountOfClosedGroup(int amountOfClosedGroup) {
        this.amountOfClosedGroup = amountOfClosedGroup;
    }

    public boolean isDriverPeople() {
        return DriverPeople;
    }

    public void setDriverPeople(boolean driverPeople) {
        DriverPeople = driverPeople;
    }

    public int getAmountOfDriverpeople() {
        return amountOfDriverpeople;
    }

    public void setAmountOfDriverpeople(int amountOfDriverpeople) {
        this.amountOfDriverpeople = amountOfDriverpeople;
    }

//    public List<CustomUser> getParticipants() {
//        return participants;
//    }
//
//    public void setParticipants(List<CustomUser> participants) {
//        this.participants = participants;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Trip trip = (Trip) o;
//        return id == trip.id && closedGroup == trip.closedGroup && amountOfClosedGroup == trip.amountOfClosedGroup && DriverPeople == trip.DriverPeople && amountOfDriverpeople == trip.amountOfDriverpeople && Objects.equals(tripDescription, trip.tripDescription) && Objects.equals(destination, trip.destination) && Objects.equals(user, trip.user) && Objects.equals(tripDuration, trip.tripDuration) && Objects.equals(participants, trip.participants);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, tripDescription, destination, user, tripDuration, closedGroup, amountOfClosedGroup, DriverPeople, amountOfDriverpeople, participants);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return id == trip.id && closedGroup == trip.closedGroup && amountOfClosedGroup == trip.amountOfClosedGroup && DriverPeople == trip.DriverPeople && amountOfDriverpeople == trip.amountOfDriverpeople && Objects.equals(tripDescription, trip.tripDescription) && Objects.equals(destination, trip.destination) && Objects.equals(tripDuration, trip.tripDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tripDescription, destination, tripDuration, closedGroup, amountOfClosedGroup, DriverPeople, amountOfDriverpeople);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", tripDescription='" + tripDescription + '\'' +
                ", destination='" + destination + '\'' +
                ", tripDuration='" + tripDuration + '\'' +
                ", closedGroup=" + closedGroup +
                ", amountOfClosedGroup=" + amountOfClosedGroup +
                ", DriverPeople=" + DriverPeople +
                ", amountOfDriverpeople=" + amountOfDriverpeople +
                '}';
    }
}
