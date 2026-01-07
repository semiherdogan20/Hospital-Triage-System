package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "specialty")
    @NotBlank
    private String specialty;

    @Column(name = "skillscore")
    @Min(value = 0, message = "Kıdem puanı 0'dan küçük olamaz")
    @Max(value = 100, message = "Kıdem paunı 100'den büyük olamaz")
    private int skillScore;

    @Column(name = "dailypatientcount")
    private int dailyPatientCount = 0;

    @Column(name = "isavailable")
    private boolean isAvailable;

    public Doctor(String name,String specialty, int skillScore) {
        this.specialty = specialty;
        this.name = name;
        this.skillScore = skillScore;
    }

    public Doctor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDailyPatientCount() {
        return dailyPatientCount;
    }

    public void setDailyPatientCount(int dailyPatientCount) {
        this.dailyPatientCount = dailyPatientCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getSkillScore() {
        return skillScore;
    }

    public void setSkillScore(int skillScore) {
        this.skillScore = skillScore;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", specialty='" + specialty + '\'' +
                ", skillScore=" + skillScore +
                '}';
    }
}

