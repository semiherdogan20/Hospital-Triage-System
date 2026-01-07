package entity;

import enums.VisitStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    @NotNull(message = "Hasta boş olamaz")
    private Patient patient;

    @Column(name = "status")
    @NotNull(message = "Visit durumu boş olamaz")
    private VisitStatus status;

    @Column(name = "urgencyscore")
    @NotNull(message = "Aciliyet skoru boş olamaz")
    @Min(value = 1, message = "Aciliyet skoru minimum 1 olmalı")
    @Max(value = 100, message = "Aciliyet skoru maksimum 100 olmalı")
    private int urgencyScore;

    @Column(name = "createdtime")
    @NotNull(message = "Oluşturma tarihi boş olamaz")
    @PastOrPresent(message = "Oluşturma tarihi bugünden ileri tarihli olamaz")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Vitals vitals;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor")
    private Doctor doctor;

    @Version
    private Long version;

    // Getters and Setters
    public Vitals getVitals() {
        return vitals;
    }

    public void setVitals(Vitals vitals) {
        this.vitals = vitals;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public VisitStatus getStatus() {
        return status;
    }

    public void setStatus(VisitStatus status) {
        this.status = status;
    }

    public int getUrgencyScore() {
        return urgencyScore;
    }

    public void setUrgencyScore(int urgencyScore) {
        this.urgencyScore = urgencyScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    // Constructors
    public Visit(Patient patient, VisitStatus status, int urgencyScore, LocalDateTime createdAt) {
        this.patient = patient;
        this.status = status;
        this.urgencyScore = urgencyScore;
        this.createdAt = createdAt;
    }

    public Visit(VisitStatus status, int urgencyScore, LocalDateTime createdAt, Patient patient) {
        this.status = status;
        this.urgencyScore = urgencyScore;
        this.createdAt = createdAt;
        this.patient = patient;
    }

    public Visit() {
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", patient=" + patient +
                ", status=" + status +
                ", urgencyScore=" + urgencyScore +
                ", createdAt=" + createdAt +
                '}';
    }
}