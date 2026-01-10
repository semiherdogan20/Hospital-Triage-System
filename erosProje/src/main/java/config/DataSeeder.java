package config;

import entity.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import repository.RuleRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RuleRepository ruleRepository;
    @Autowired
    public DataSeeder(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (ruleRepository.count() == 0) {

            Rule rule1 = new Rule();
            rule1.setConditionField("pulse");
            rule1.setOperator(">");
            rule1.setValue("100");
            rule1.setScoreImpact(10);
            rule1.setPriority(1);
            ruleRepository.save(rule1);

            Rule rule2 = new Rule();
            rule2.setConditionField("bodyTemp");
            rule2.setOperator(">");
            rule2.setValue("38.0");
            rule2.setScoreImpact(20);
            rule2.setPriority(2);
            ruleRepository.save(rule2);

            Rule rule3 = new Rule();
            rule3.setConditionField("systolicBp");
            rule3.setOperator(">");
            rule3.setValue("160");
            rule3.setScoreImpact(15);
            rule3.setPriority(3);
            ruleRepository.save(rule3);
        }
    }
}
