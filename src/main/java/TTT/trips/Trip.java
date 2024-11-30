package TTT.trips;

import TTT.users.CustomUser;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
public class Trip {

    @Id
    @GeneratedValue
    private long id;

    @Column(length = 1000)
    public String tripDescription;
    public String destination;
    private String tripDuration;
    private boolean closedGroup;
    private int amountOfClosedGroup;
    private boolean peopleInTheCar;
    private int amountOfDriverPeople;
    private String distanceOfTrip;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean ifTolerateAnimals;
    private LocalDateTime tripDateTime;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean tripVisible = true;

    // Właściciel wycieczki (ManyToOne)
    @ManyToOne
    private CustomUser owner; // Właściciel wycieczki

    // Uczestnicy wycieczki (ManyToMany)
    @ManyToMany
    @JoinTable(
            name = "trip_participants", // Nazwa tabeli pośredniej
            joinColumns = @JoinColumn(name = "trip_id"), // Kolumna dla Trip
            inverseJoinColumns = @JoinColumn(name = "user_id") // Kolumna dla CustomUser
    )

    private List<CustomUser> participants; // Lista uczestników
    @Column(length = 10000)
    private String waypoints;

    @Lob
    private byte[] gpxFile;

    // Gettery i Settery dla gpxFile
    public byte[] getGpxFile() {
        return gpxFile;
    }

    public void setGpxFile(byte[] gpxFile) {
        this.gpxFile = gpxFile;
    }

    // Prywatny konstruktor, używający buildera
    private Trip(TripBuilder builder) {
        this.tripDescription = builder.tripDescription;
        this.destination = builder.destination;
        this.owner = builder.owner;
        this.tripDuration = builder.tripDuration;
        this.closedGroup = builder.closedGroup;
        this.amountOfClosedGroup = builder.amountOfClosedGroup;
        this.peopleInTheCar = builder.driverPeople;
        this.amountOfDriverPeople = builder.peopleInTheCar;
        this.ifTolerateAnimals = builder.tolerateAnimals;
        this.waypoints = builder.waypoints;
        this.tripDateTime = builder.tripDateTime;
        this.distanceOfTrip = builder.distanceOfTrip;
        this.gpxFile = builder.gpxFile;
    }

    public Trip() {}

    // Gettery i Settery


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

    public boolean isPeopleInTheCar() {
        return peopleInTheCar;
    }

    public void setPeopleInTheCar(boolean peopleInTheCar) {
        this.peopleInTheCar = peopleInTheCar;
    }

    public int getAmountOfDriverPeople() {
        return amountOfDriverPeople;
    }

    public void setAmountOfDriverPeople(int amountOfDriverPeople) {
        this.amountOfDriverPeople = amountOfDriverPeople;
    }

    public boolean isIfTolerateAnimals() {
        return ifTolerateAnimals;
    }

    public void setIfTolerateAnimals(boolean ifTolerateAnimals) {
        this.ifTolerateAnimals = ifTolerateAnimals;
    }

    public CustomUser getOwner() {
        return owner;
    }

    public void setOwner(CustomUser owner) {
        this.owner = owner;
    }

    public List<CustomUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<CustomUser> participants) {
        this.participants = participants;
    }



    public String getWaypoints() {return waypoints;}

    public void setWaypoints(String waypoints) {this.waypoints = waypoints;}

    public LocalDateTime getTripDateTime() {return tripDateTime;}

    public void setTripDateTime(LocalDateTime tripDateTime) {this.tripDateTime = tripDateTime;}

    public boolean isTripVisible() {
        return tripVisible;
    }

    public void setTripVisible(boolean tripVisible) {
        this.tripVisible = tripVisible;
    }

    public String getDistanceOfTrip() {
        return distanceOfTrip;
    }

    public void setDistanceOfTrip(String distanceOfTrip) {
        this.distanceOfTrip = distanceOfTrip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return id == trip.id && closedGroup == trip.closedGroup && amountOfClosedGroup == trip.amountOfClosedGroup && peopleInTheCar == trip.peopleInTheCar && amountOfDriverPeople == trip.amountOfDriverPeople && ifTolerateAnimals == trip.ifTolerateAnimals && tripVisible == trip.tripVisible && Objects.equals(tripDescription, trip.tripDescription) && Objects.equals(destination, trip.destination) && Objects.equals(tripDuration, trip.tripDuration) && Objects.equals(distanceOfTrip, trip.distanceOfTrip) && Objects.equals(tripDateTime, trip.tripDateTime) && Objects.equals(owner, trip.owner) && Objects.equals(participants, trip.participants) && Objects.equals(waypoints, trip.waypoints) && Objects.deepEquals(gpxFile, trip.gpxFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tripDescription, destination, tripDuration, closedGroup, amountOfClosedGroup, peopleInTheCar, amountOfDriverPeople, distanceOfTrip, ifTolerateAnimals, tripDateTime, tripVisible, owner, participants, waypoints, Arrays.hashCode(gpxFile));
    }

    @Override
    public String toString() {
        return "Trip{" +
                "waypoints='" + waypoints + '\'' +
                ", id=" + id +
                ", tripDescription='" + tripDescription + '\'' +
                ", destination='" + destination + '\'' +
                ", tripDuration='" + tripDuration + '\'' +
                ", closedGroup=" + closedGroup +
                ", amountOfClosedGroup=" + amountOfClosedGroup +
                ", peopleInTheCar=" + peopleInTheCar +
                ", amountOfDriverPeople=" + amountOfDriverPeople +
                ", distanceOfTrip=" + distanceOfTrip +
                ", ifTolerateAnimals=" + ifTolerateAnimals +
                ", tripDateTime=" + tripDateTime +
                ", tripVisible=" + tripVisible +
                '}';
    }

    // TripBuilder
    public static class TripBuilder {
        private String tripDescription;
        private String destination;
        private String tripDuration;
        private boolean closedGroup;
        private int amountOfClosedGroup;
        private boolean driverPeople; // Poprawiona nazwa
        private int peopleInTheCar;
        private CustomUser owner;
        private boolean tolerateAnimals;
        private String waypoints;
        private LocalDateTime tripDateTime;
        private String distanceOfTrip;
        private byte[] gpxFile;

        public Trip build() {
            return new Trip(this); // Zwraca obiekt korzystający z danych buildera
        }

        public TripBuilder withTripDescription(String tripDescription) {
            this.tripDescription = tripDescription;
            return this;
        }

        public TripBuilder withDestination(String destination) {
            this.destination = destination;
            return this;
        }

        public TripBuilder withClosedGroup(boolean closedGroup) {
            this.closedGroup = closedGroup;
            return this;
        }

        public TripBuilder withAmountOfClosedGroup(int amountOfClosedGroup) {
            this.amountOfClosedGroup = amountOfClosedGroup;
            return this;
        }

        public TripBuilder withTripDuration(String tripDuration) {
            this.tripDuration = tripDuration;
            return this;
        }

        public TripBuilder withDriverPeople(boolean driverPeople) {
            this.driverPeople = driverPeople;
            return this;
        }

        public TripBuilder withAmountOfDriverPeople(int peopleInTheCar) {
            this.peopleInTheCar = peopleInTheCar;
            return this;
        }

        public TripBuilder withOwner(CustomUser owner) {
            this.owner = owner;
            return this;
        }

        public TripBuilder withAnimals(boolean tolerateAnimals) {
            this.tolerateAnimals = tolerateAnimals;
            return this;
        }

        public TripBuilder withWaypoints(String waypoints) {
            this.waypoints = waypoints;
            return this;
        }

        public TripBuilder withTripDataTime(LocalDateTime tripDateTime) {
            this.tripDateTime = tripDateTime;
            return this;
        }

        public TripBuilder withDistanceOfTrip(String distanceOfTrip) {
            this.distanceOfTrip = distanceOfTrip;
            return this;
        }

        public TripBuilder withGpxFile(byte[] gpxFile) {
            this.gpxFile = gpxFile;
            return this;
        }

    }
}
