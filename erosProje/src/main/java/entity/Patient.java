package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name="patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name = "name")
    @NotBlank(message = "Bu alan boş bırakılamaz")
    @Size(min = 2, max = 50, message = "İsim 2 ile 50 karakter arasında olmalıdır")
    private String name;

    @Column(name = "surname")
    @NotBlank(message = "Bu alan boş bırakılamaz")
    private String surname;

    @Column(name = "tckno",unique = true)
    @Size(min = 11, max = 11, message = "TC Kimlik No 11 haneli olmalıdır.")
    @NotBlank(message = "Bu alan boş bırakılamaz")
    @Pattern(regexp = "^[1-9]{1}[0-9]{9}[02468]{1}$", message = "Geçerli bir TC Kimlik No formatı değil")
    private String tckNo;

    @Column(name = "birthdate")
    @NotBlank(message = "Bu alan boş bırakılamaz")
    @Past(message = "Doğum tarihi bugünden ileri bir tarih olamaz")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "patient")
    private Visit visit;

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTckNo() {
        return tckNo;
    }

    public void setTckNo(String tckNo) {
        this.tckNo = tckNo;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Patient() {
    }

    public Patient(Long id, String name, String surname, String tckNo, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.tckNo = tckNo;
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", tckNo='" + tckNo + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
