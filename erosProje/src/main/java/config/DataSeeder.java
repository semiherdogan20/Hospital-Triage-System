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
        // Eğer veritabanı boşsa (daha önce eklemediysek) kuralları ekle
        if (ruleRepository.count() == 0) {

            // 1. KURAL: Nabız 100'den büyükse 10 PUAN
            Rule rule1 = new Rule();
            rule1.setConditionField("pulse");
            rule1.setOperator(">");
            rule1.setValue("100");
            rule1.setScoreImpact(10);
            rule1.setPriority(1);
            ruleRepository.save(rule1);

            // 2. KURAL: Ateş 38.0'dan büyükse 20 PUAN
            Rule rule2 = new Rule();
            rule2.setConditionField("bodyTemp");
            rule2.setOperator(">");
            rule2.setValue("38.0");
            rule2.setScoreImpact(20);
            rule2.setPriority(2);
            ruleRepository.save(rule2);

            // 3. KURAL: Tansiyon 160'tan büyükse 15 PUAN
            Rule rule3 = new Rule();
            rule3.setConditionField("systolicBp");
            rule3.setOperator(">");
            rule3.setValue("160");
            rule3.setScoreImpact(15);
            rule3.setPriority(3);
            ruleRepository.save(rule3);

            System.out.println("----- KURALLAR VERİTABANINA YÜKLENDİ -----");
        }
    }
}
