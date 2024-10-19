package TTT.users;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class UserRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;  // Ocena od 1 do 10

    @Column(nullable = true, length = 1000)  // Opcjonalny komentarz, z limitem długości
    private String comment;

    // Relacja do użytkownika, który jest oceniany
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser user;

    // Nowe pole: Relacja do użytkownika, który wystawił ocenę
    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private CustomUser reviewer;  // Użytkownik, który wystawił ocenę

    public UserRating() {
    }

    public UserRating(int rating, String comment, CustomUser user, CustomUser reviewer) {
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.reviewer = reviewer;
    }

    // Gettery i Settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CustomUser getUser() {
        return user;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }

    public CustomUser getReviewer() {
        return reviewer;
    }

    public void setReviewer(CustomUser reviewer) {
        this.reviewer = reviewer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRating that = (UserRating) o;
        return rating == that.rating &&
                Objects.equals(id, that.id) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(user, that.user) &&
                Objects.equals(reviewer, that.reviewer);  // Porównanie nowego pola
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, comment, user, reviewer);  // Dodano reviewer
    }

    @Override
    public String toString() {
        return "UserRating{" +
                "id=" + id +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", user=" + user +
                ", reviewer=" + reviewer +  // Wyświetlanie nowego pola
                '}';
    }
}
