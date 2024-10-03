package TTT.trips;

import TTT.users.CustomUser;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    private boolean DriverPeople;
    private int amountOfDriverpeople;

    //private List<CustomUser> participants;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomUser customUser;


    public Trip(String tripDescription, String destination, CustomUser customUser, String tripDuration,
                boolean closedGroup, int amountOfClosedGroup, boolean driverPeople,
                int amountOfDriverpeople, List<CustomUser> participants) {
        this.tripDescription = tripDescription;
        this.destination = destination;
        this.customUser = customUser;
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
        return amountOfDriverpeople;
    }

    public void setAmountOfDriverpeople(int amountOfDriverpeople) {
        this.amountOfDriverpeople = amountOfDriverpeople;
    }

}
