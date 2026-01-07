package controller;
import entity.Doctor;
import entity.Patient;
import entity.Visit;
import entity.Vitals;
import enums.VisitStatus;
import exception.VisitNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.TriageRuleService;
import service.TriageService;
import service.VisitTransitionService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TriageController {
    private final TriageService triageService;
    private final TriageRuleService triageRuleService;
    private final VisitTransitionService visitTransitionService;

    @Autowired
    public TriageController(TriageService triageService, TriageRuleService triageRuleService, VisitTransitionService visitTransitionService) {
        this.triageService = triageService;
        this.triageRuleService = triageRuleService;
        this.visitTransitionService = visitTransitionService;
    }

    @PostMapping("/triage")
    public ResponseEntity<?> entryPatient(@RequestBody @Validated Vitals vitals){
        int scoreOfPatient = triageRuleService.calculateScore(vitals);
        Visit visit = new Visit();
        visit.setVitals(vitals);
        visit.setStatus(VisitStatus.WAITING);
        visit.setUrgencyScore(scoreOfPatient);

        LocalDateTime entryTime = LocalDateTime.now();
        visit.setCreatedAt(entryTime);
        visitTransitionService.saveVisit(visit);
        return ResponseEntity.status(HttpStatus.CREATED).body(visit);
    }

    @GetMapping("/queue/next/{doctorId}")
    public ResponseEntity<Visit> getNextPatient(@PathVariable("doctorId") Long doctorId) {
        Visit nextPatient = triageService.findNextPatientForDoctor(doctorId);
        if (nextPatient == null)
            return ResponseEntity.ok().body(nextPatient);

        return ResponseEntity.ok().body(nextPatient);
    }

    @PostMapping("/visit/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Long id, @RequestParam String newStatus) {
        try {
            VisitStatus targetStatus = VisitStatus.valueOf(newStatus.toUpperCase());
            visitTransitionService.changeStatus(id, targetStatus);
            return ResponseEntity.ok("Durum başarıyla " + targetStatus + " olarak güncellendi.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Hata: Geçersiz durum bilgisi! Lütfen geçerli bir durum girin.");

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/visit/{id}")
    public ResponseEntity<?> deleteVisit(@PathVariable("id") Long id){
        Visit deletedVisit = visitTransitionService.findVisitById(id);

        try {
            visitTransitionService.deleteVisit(deletedVisit);
        } catch (VisitNotFoundException e) {
            e.getMessage();
        }

        return ResponseEntity.ok().body("Başarıyla silindi");
    }

}
