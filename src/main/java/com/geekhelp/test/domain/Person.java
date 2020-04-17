package com.geekhelp.test.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @NotNull
    @Size(min = 2)
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "hotel")
    private String hotel;

    @Column(name = "datein")
    private LocalDate datein;

    @Column(name = "dateout")
    private LocalDate dateout;

    @Column(name = "comments")
    private String comments;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public Person firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Person lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public Person dob(LocalDate dob) {
        this.dob = dob;
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public Person phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public Person address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public Person email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHotel() {
        return hotel;
    }

    public Person hotel(String hotel) {
        this.hotel = hotel;
        return this;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public LocalDate getDatein() {
        return datein;
    }

    public Person datein(LocalDate datein) {
        this.datein = datein;
        return this;
    }

    public void setDatein(LocalDate datein) {
        this.datein = datein;
    }

    public LocalDate getDateout() {
        return dateout;
    }

    public Person dateout(LocalDate dateout) {
        this.dateout = dateout;
        return this;
    }

    public void setDateout(LocalDate dateout) {
        this.dateout = dateout;
    }

    public String getComments() {
        return comments;
    }

    public Person comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", dob='" + getDob() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", email='" + getEmail() + "'" +
            ", hotel='" + getHotel() + "'" +
            ", datein='" + getDatein() + "'" +
            ", dateout='" + getDateout() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
