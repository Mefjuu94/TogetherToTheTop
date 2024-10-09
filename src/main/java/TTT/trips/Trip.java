package TTT.trips;

import TTT.users.CustomUser;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    private boolean DriverPeople;
    private int amountOfDriverPeople;

    //private List<CustomUser> participants;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomUser customUser;


    private Trip(TripBuilder builder) {
        this.tripDescription = tripDescription;
        this.destination = destination;
        this.customUser = customUser;
        this.tripDuration = tripDuration;
        this.closedGroup = closedGroup;
        this.amountOfClosedGroup = amountOfClosedGroup;
        this.DriverPeople = DriverPeople;
        this.amountOfDriverPeople = amountOfDriverPeople;
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

    public CustomUser getCustomUser() {
        return customUser;
    }

    public void setCustomUser(CustomUser customUser) {
        this.customUser = customUser;
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

    public boolean isDriverPeople() {
        return DriverPeople;
    }

    public void setDriverPeople(boolean driverPeople) {
        DriverPeople = driverPeople;
    }

    public int getAmountOfDriverpeople() {
        return amountOfDriverPeople;
    }

    public void setAmountOfDriverpeople(int amountOfDriverpeople) {
        this.amountOfDriverPeople = amountOfDriverpeople;
    }



    public static class TripBuilder{

        private String tripDescription;
        private String destination;
        private String tripDuration;
        private boolean closedGroup;
        private int amountOfClosedGroup;
        private boolean DriverPeople;
        private int amountOfDriverPeople;
        private CustomUser customUser;

        public Trip build(){
            return new Trip();
        }

        public TripBuilder withTripDescription(String tripDescription){
            this.tripDescription = tripDescription;
            return this;
        }

        public TripBuilder withDestination(String destination){
            this.destination = destination;
            return this;
        }
        public TripBuilder withClosedGroup(boolean closedGroup){
            this.closedGroup = closedGroup;
            return this;
        }
        public TripBuilder withAmountOfClosedGroup(int amountOfClosedGroup){
            this.amountOfClosedGroup = amountOfClosedGroup;
            return this;
        }
        public TripBuilder withTripDuration(String tripDuration){
            this.tripDuration = tripDuration;
            return this;
        }

        public TripBuilder withDriverPeople(boolean DriverPeople){
            this.DriverPeople = DriverPeople;
            return this;
        }

        public TripBuilder withAmountOfDriverPeople(int amountOfDriverPeople){
            this.amountOfDriverPeople = amountOfDriverPeople;
            return this;
        }

        public TripBuilder withCustomUser(CustomUser customUser){
            this.customUser = customUser;
            return this;
        }

    }

}
