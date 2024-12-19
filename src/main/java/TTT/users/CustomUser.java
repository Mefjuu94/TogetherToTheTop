package TTT.users;

import TTT.trips.Trip;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.List;
import java.util.Objects;

@Entity
public class CustomUser {

    @Id
    @GeneratedValue
    private long id;

    @Email
    private String email;
    private String password;

    private String customUserName;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int age;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int numbersOfTrips;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int numbersOfAnnouncements;
    private double distanceTraveled = 0.00;
    private String city;

    // Lista wycieczek, których użytkownik jest właścicielem
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> tripsOwned; // Wycieczki, których użytkownik jest właścicielem

    // Lista wycieczek, w których użytkownik bierze udział
    @ManyToMany(mappedBy = "participants")
    private List<Trip> tripsParticipated; // Wycieczki, w których użytkownik uczestniczy

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRating> ratings;

    public CustomUser() {
    }

    public CustomUser(long id, String email, String password, String customUserName, int age, int numbersOfTrips, int numbersOfAnnouncements, List<Trip> tripsOwned, List<Trip> tripsParticipated, String city, double distanceTraveled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.customUserName = customUserName;
        this.age = age;
        this.numbersOfTrips = numbersOfTrips;
        this.numbersOfAnnouncements = numbersOfAnnouncements;
        this.tripsOwned = tripsOwned;
        this.tripsParticipated = tripsParticipated;
        this.city = city;
        this.distanceTraveled = distanceTraveled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCustomUserName() {
        return customUserName;
    }

    public void setCustomUserName(String customUserName) {
        this.customUserName = customUserName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNumbersOfTrips() {
        return numbersOfTrips;
    }

    public void setNumbersOfTrips(int numbersOfTrips) {
        this.numbersOfTrips = numbersOfTrips;
    }

    public int getNumbersOfAnnouncements() {
        return numbersOfAnnouncements;
    }

    public void setNumbersOfAnnouncements(int numbersOfAnnoucements) {
        this.numbersOfAnnouncements = numbersOfAnnoucements;
    }

    public List<Trip> getTripsOwned() {
        return tripsOwned;
    }

    public void setTripsOwned(List<Trip> tripsOwned) {
        this.tripsOwned = tripsOwned;
    }

    public List<Trip> getTripsParticipated() {
        return tripsParticipated;
    }

    public void setTripsParticipated(List<Trip> tripsParticipated) {
        this.tripsParticipated = tripsParticipated;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public List<UserRating> getRatings() {
        return ratings;
    }

    public void setRatings(List<UserRating> ratings) {
        this.ratings = ratings;
    }

    // Dodaj metodę pomocniczą do dodawania oceny
    public void addRating(UserRating rating) {
        ratings.add(rating);
        rating.setUser(this);
    }

    public void removeRating(UserRating rating) {
        ratings.remove(rating);
        rating.setUser(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUser that = (CustomUser) o;
        return id == that.id && age == that.age && numbersOfTrips == that.numbersOfTrips && numbersOfAnnouncements == that.numbersOfAnnouncements && distanceTraveled == that.distanceTraveled && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(customUserName, that.customUserName) && Objects.equals(city, that.city) && Objects.equals(tripsOwned, that.tripsOwned) && Objects.equals(tripsParticipated, that.tripsParticipated) && Objects.equals(ratings, that.ratings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, customUserName, age, numbersOfTrips, numbersOfAnnouncements, distanceTraveled, city, tripsOwned, tripsParticipated, ratings);
    }

    @Override
    public String toString() {
        return "CustomUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", customUserName='" + customUserName + '\'' +
                ", age=" + age +
                ", numbersOfTrips=" + numbersOfTrips +
                ", numbersOfAnnouncements=" + numbersOfAnnouncements +
                ", distanceTraveled=" + distanceTraveled +
                ", city='" + city + '\'' +
                ", tripsOwned=" + tripsOwned +
                ", ratings=" + ratings +
                '}';
    }
}

