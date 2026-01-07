package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "vitals")
public class Vitals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "pulse")
    @NotNull(message = "Bu alan boş olamaz")
    @Min(value = 0, message = "Nabız negatif olamaz")
    @Max(value = 300, message = "Nabız değeri fizyolojik sınırların dışında")
    private int pulse;

    @Column(name = "systolicbp")
    @Min(0) @Max(300)
    private int systolicBp; // Büyük Tansiyon

    @Column(name = "diasttolicbp")
    @Min(0) @Max(300)
    private int diastolicBp; // Küçük Tansiyon

    @Column(name = "temp")
    @DecimalMin(value = "30.0", message = "Vücut ısısı 30 derecenin altında olamaz")
    @DecimalMax(value = "45.0", message = "Vücut ısısı 45 derecenin üzerinde olamaz")
    private int bodyTemp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id")
    private Visit visit;

    public Vitals(){}

    public Vitals(int pulse, Integer systolicBp, Integer diastolicBp, int bodyTemp) {
        this.pulse = pulse;
        this.systolicBp = systolicBp;
        this.diastolicBp = diastolicBp;
        this.bodyTemp = bodyTemp;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public Integer getSystolicBp() {
        return systolicBp;
    }

    public void setSystolicBp(Integer systolicBp) {
        this.systolicBp = systolicBp;
    }

    public Integer getDiastolicBp() {
        return diastolicBp;
    }

    public void setDiastolicBp(Integer diastolicBp) {
        this.diastolicBp = diastolicBp;
    }

    public int getBodyTemp() {
        return bodyTemp;
    }

    public void setBodyTemp(int bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    @Override
    public String toString() {
        return "Vitals{" +
                "pulse=" + pulse +
                ", systolicBp=" + systolicBp +
                ", diastolicBp=" + diastolicBp +
                ", bodyTemp=" + bodyTemp +
                ", visit=" + visit +
                '}';
    }
}
