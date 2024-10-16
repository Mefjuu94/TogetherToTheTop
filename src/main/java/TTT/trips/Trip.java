package TTT.trips;

import TTT.users.CustomUser;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Trip {

    @Id
    @GeneratedValue
    private long id;

    public String tripDescription;
    public String destination;
    private String tripDuration;
    private boolean closedGroup;
    private int amountOfClosedGroup;
    private boolean peopleInTheCar;
    private int amountOfDriverPeople;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean ifTolerateAnimals;

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

    public CustomUser getOwnerOfTrip() {
        return owner;
    }

    public void setOwnerOfTrip(CustomUser customUser) {
        this.owner = customUser;
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

    public void setPeopleInTheCar(boolean driverPeople) {
        this.peopleInTheCar = driverPeople;
    }

    public int getAmountOfDriverPeople() {
        return amountOfDriverPeople;
    }

    public void setAmountOfDriverPeople(int amountOfDriverPeople) {
        this.amountOfDriverPeople = amountOfDriverPeople;
    }

    public List<CustomUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<CustomUser> participants) {
        this.participants = participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return id == trip.id && closedGroup == trip.closedGroup && amountOfClosedGroup == trip.amountOfClosedGroup && peopleInTheCar == trip.peopleInTheCar && amountOfDriverPeople == trip.amountOfDriverPeople && ifTolerateAnimals == trip.ifTolerateAnimals && Objects.equals(tripDescription, trip.tripDescription) && Objects.equals(destination, trip.destination) && Objects.equals(tripDuration, trip.tripDuration) && Objects.equals(owner, trip.owner) && Objects.equals(participants, trip.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tripDescription, destination, tripDuration, closedGroup, amountOfClosedGroup, peopleInTheCar, amountOfDriverPeople, ifTolerateAnimals, owner, participants);
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
                ", peopleInTheCar=" + peopleInTheCar +
                ", amountOfDriverPeople=" + amountOfDriverPeople +
                ", ifTolerateAnimals=" + ifTolerateAnimals +
                ", owner=" + owner +
                ", participants=" + participants +
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
    }
}
