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
    private int numbersOfAnnoucements;

    @OneToMany(mappedBy = "customUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> trips;

    public CustomUser() {
    }

    public CustomUser(long id, String email, String password, String customUserName, int age, int numbersOfTrips, int numbersOfAnnoucements, List<Trip> trips) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.customUserName = customUserName;
        this.age = age;
        this.numbersOfTrips = numbersOfTrips;
        this.numbersOfAnnoucements = numbersOfAnnoucements;
        this.trips = trips;
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

    public void setCustomUserName(String name) {
        this.customUserName = name;
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

    public int getNumbersOfAnnoucements() {
        return numbersOfAnnoucements;
    }

    public void setNumbersOfAnnoucements(int numbersOfAnnoucements) {
        this.numbersOfAnnoucements = numbersOfAnnoucements;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUser that = (CustomUser) o;
        return id == that.id && customUserName == that.customUserName && age == that.age && numbersOfTrips == that.numbersOfTrips && numbersOfAnnoucements == that.numbersOfAnnoucements && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(trips, that.trips);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, customUserName, age, numbersOfTrips, numbersOfAnnoucements, trips);
    }

    @Override
    public String toString() {
        return "CustomUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name=" + customUserName +
                ", age=" + age +
                ", numbersOfTrips=" + numbersOfTrips +
                ", numbersOfAnnoucements=" + numbersOfAnnoucements +
                ", trips=" + trips +
                '}';
    }
}
