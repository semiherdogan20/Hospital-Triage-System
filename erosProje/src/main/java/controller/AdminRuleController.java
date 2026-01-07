package controller;

import entity.Rule;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.RuleRepository;
import service.TriageRuleService;

import java.util.Map;
import java.util.Optional;

@RestController // 1. DÜZELTME: Controller'ın JSON dönebilmesi için şart
@RequestMapping("/api/v1/rules")
public class AdminRuleController {

    private final RuleRepository ruleRepository;
    private final TriageRuleService triageRuleService;

    @Autowired
    public AdminRuleController(RuleRepository ruleRepository, TriageRuleService triageRuleService) {
        this.ruleRepository = ruleRepository;
        this.triageRuleService = triageRuleService;
    }

    @PostMapping
    public ResponseEntity<?> addRule(@RequestBody Rule rule) { // 2. DÜZELTME: @RequestBody ekledim
        try {
            triageRuleService.validateRule(rule);

            Rule savedRule = ruleRepository.save(rule);
            return ResponseEntity.ok(Map.of("message", "Kural başarıyla eklendi.", "id", savedRule.getId()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Kayıt sırasında hata oluştu: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRule(@PathVariable("id") Long id) {
        if (ruleRepository.existsById(id)) {
            ruleRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Kural silindi."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Silinecek kural bulunamadı."));
        }
    }


}