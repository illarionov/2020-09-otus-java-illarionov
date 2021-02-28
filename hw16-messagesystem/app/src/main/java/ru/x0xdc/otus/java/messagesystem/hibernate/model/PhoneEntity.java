package ru.x0xdc.otus.java.messagesystem.hibernate.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="phone")
public class PhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="number", nullable = false/* , unique = true */)
    private String number;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

    public PhoneEntity() {
    }

    public PhoneEntity(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public PhoneEntity(long id, String number, UserEntity user) {
        this.id = id;
        this.number = number;
        this.user = user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneEntity phone = (PhoneEntity) o;
        return id == phone.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
