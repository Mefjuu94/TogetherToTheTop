package TTT.PeakAndSummitsHandler;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Top {

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private int height;
    private float latitude;
    private float longitude;

    public Top(String name, int height, float latitude, float longitude) {
        this.name = name;
        this.height = height;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Top() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Top top = (Top) o;
        return height == top.height && latitude == top.latitude && longitude == top.longitude && Objects.equals(name, top.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, height, latitude, longitude);
    }

    @Override
    public String toString() {
        return "Top{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
