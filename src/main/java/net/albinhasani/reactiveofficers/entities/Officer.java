package net.albinhasani.reactiveofficers.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class Officer {

    @Id
    private String id;
    private Rank rank;
    private String first;
    private String last;

    public Officer() {
    }

    public Officer(Rank rank, String first, String last) {
        this.rank = rank;
        this.first = first;
        this.last = last;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    @Override
    public String toString() {
        return "Officer{" +
                "id='" + id + '\'' +
                ", rank=" + rank +
                ", first='" + first + '\'' +
                ", last='" + last + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Officer officer = (Officer) o;
        return Objects.equals(id, officer.id) && Objects.equals(rank, officer.rank) && Objects.equals(first, officer.first) && Objects.equals(last, officer.last);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rank, first, last);
    }
}
