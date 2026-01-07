package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "rules")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "conditionField")
    @NotBlank(message = "Hangi alanın kontrol edileceği (pulse, temp vb.) belirtilmeli")
    private String conditionField;

    @Column(name = "operator")
    @NotBlank
    @Pattern(regexp = "^(>|<|=|>=|<=)$", message = "Geçersiz operatör. Sadece >, <, =, >=, <= kullanılabilir")
    private String operator;

    @Column(name = "value")
    @NotBlank(message = "Değer boş olamaz")
    private String value;

    @Column(name = "scoreImpact")
    private int scoreImpact;

    @Column(name = "priority")
    @Min(value = 1)
    private int priority;

    public Rule(){}

    public Rule(String conditionField, String operator, String value, int scoreImpact, int priority) {
        this.conditionField = conditionField;
        this.operator = operator;
        this.value = value;
        this.scoreImpact = scoreImpact;
        this.priority = priority;
    }

    public String getConditionField() {
        return conditionField;
    }

    public void setConditionField(String conditionField) {
        this.conditionField = conditionField;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getScoreImpact() {
        return scoreImpact;
    }

    public void setScoreImpact(int scoreImpact) {
        this.scoreImpact = scoreImpact;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "conditionField='" + conditionField + '\'' +
                ", operator='" + operator + '\'' +
                ", value='" + value + '\'' +
                ", scoreImpact=" + scoreImpact +
                ", priority=" + priority +
                '}';
    }

    public Long getId() {
        return id;
    }
}
