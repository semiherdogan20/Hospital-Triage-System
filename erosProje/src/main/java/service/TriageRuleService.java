package service;

import entity.Rule;
import entity.Vitals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.RuleRepository;

import java.util.List;
import java.util.Set;

@Service
public class TriageRuleService {
    private final RuleRepository ruleRepository;

    @Autowired
    public TriageRuleService(RuleRepository ruleRepository){
        this.ruleRepository = ruleRepository;
    }

    public int calculateScore(Vitals vitals){
        int urgencyScore = 0;
        List<Rule> allRules = ruleRepository.findAll();
        for(Rule rule : allRules){

           boolean kuralUyuyorMu = evaluate(rule,vitals);
           if(kuralUyuyorMu){
               urgencyScore = urgencyScore + rule.getScoreImpact() ;
               System.out.println("Kural eşleşti! Eklenen Puan: "+ rule.getScoreImpact());
           }
        }
        return urgencyScore;
    }

    private boolean evaluate(Rule rule, Vitals vitals){
        double ruleLimit = Double.parseDouble(rule.getValue());
        double patientValue = 0.0;

        switch(rule.getConditionField().toLowerCase()){
            case "pulse": patientValue = vitals.getPulse(); break;
            case "systolicbp": patientValue = vitals.getSystolicBp(); break;
            case "diastolicbp":patientValue = vitals.getDiastolicBp(); break;
            case "bodytemp": patientValue = vitals.getBodyTemp(); break;
            default: return false;
        }
        switch (rule.getOperator()) {
            case ">": return patientValue > ruleLimit;
            case "<": return patientValue < ruleLimit;
            case ">=": return patientValue >= ruleLimit;
            case "<=": return patientValue <= ruleLimit;
            case "=": return patientValue == ruleLimit;
            default: return false;
        }
    }

    public void validateRule(Rule rule) {

        if (rule == null)
            throw new IllegalArgumentException("Kural verisi boş olamaz!");

        if (rule.getConditionField() == null || rule.getConditionField().trim().isEmpty())
            throw new IllegalArgumentException("Hata: 'conditionField' (Alan Adı) boş bırakılamaz.");

        if (rule.getOperator() == null || rule.getOperator().trim().isEmpty())
            throw new IllegalArgumentException("Hata: 'operator' boş bırakılamaz.");

        if (rule.getValue() == null || rule.getValue().trim().isEmpty())
            throw new IllegalArgumentException("Hata: 'value' (Değer) boş bırakılamaz.");


        String field = rule.getConditionField().toLowerCase().trim();
        Set<String> allowedFields = Set.of("pulse", "bodytemp", "systolicbp", "diastolicbp");

        if (!allowedFields.contains(field))
            throw new IllegalArgumentException("Geçersiz Alan Adı: '" + field + "'. Sadece şunlar kullanılabilir: pulse, bodytemp, systolicbp, diastolicbp");

        String op = rule.getOperator().trim();
        Set<String> allowedOperators = Set.of(">", "<", ">=", "<=", "=");

        if (!allowedOperators.contains(op))
            throw new IllegalArgumentException("Geçersiz Operatör: '" + op + "'. Sadece >, <, >=, <=, = kullanılabilir.");

        try {
            Double.parseDouble(rule.getValue());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Hata: Girilen değer ('" + rule.getValue() + "') sayısal değil! Lütfen bir sayı girin.");
        }

        if (rule.getScoreImpact() <= 0)
            throw new IllegalArgumentException("Puan Etkisi (scoreImpact) 0'dan büyük pozitif bir sayı olmalıdır.");

    }

}
