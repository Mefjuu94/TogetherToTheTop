package TTT.trips;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
public class Comments {

    @Id
    @GeneratedValue
    private Long ID;

    private String comment;
    private long userID;
    private long tripID;
    private String nameOfComentator;

    private LocalDateTime createdDate;

    public Comments() {
    }

    public Comments(String comment, long userID, long tripID,String nameOfComentator) {
        this.comment = comment;
        this.userID = userID;
        this.tripID = tripID;
        this.createdDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        this.nameOfComentator = nameOfComentator;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getTripID() {
        return tripID;
    }

    public void setTripID(long tripID) {
        this.tripID = tripID;
    }

    public LocalDateTime getDateOfComment() {
        return createdDate;
    }

    public void setDateOfComment(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getNameOfComentator() {
        return nameOfComentator;
    }

    public void setNameOfComentator(String nameOfComentator) {
        this.nameOfComentator = nameOfComentator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comments comments = (Comments) o;
        return userID == comments.userID && tripID == comments.tripID && Objects.equals(ID, comments.ID) && Objects.equals(comment, comments.comment) && Objects.equals(nameOfComentator, comments.nameOfComentator) && Objects.equals(createdDate, comments.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, comment, userID, tripID, nameOfComentator, createdDate);
    }

    @Override
    public String toString() {
        return "Comments{" +
                "ID=" + ID +
                ", comment='" + comment + '\'' +
                ", userID=" + userID +
                ", tripID=" + tripID +
                ", nameOfComentator='" + nameOfComentator + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
