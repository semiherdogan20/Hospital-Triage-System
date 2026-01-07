package service;

import entity.Doctor;
import entity.Patient;
import entity.Visit;
import entity.Vitals;
import enums.VisitStatus;
import exception.TheBestMatchingDoctorNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import repository.DoctorRepository;
import repository.VisitRepository;
import repository.VitalsRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TriageService {
    private final VisitRepository visitRepository;
    private final VitalsRepository vitalsRepository;
    private final TriageRuleService triageRuleService;
    private  final DoctorRepository doctorRepository;
    private final double MULTIPLIER = 0.5;

    @Autowired
    public TriageService(VisitRepository visitRepository, VitalsRepository vitalsRepository, TriageRuleService triageRuleService, DoctorRepository doctorRepository) {
        this.visitRepository = visitRepository;
        this.vitalsRepository = vitalsRepository;
        this.triageRuleService = triageRuleService;
        this.doctorRepository = doctorRepository;
    }

    public Visit admitPatient(Patient patient, Vitals vital){
        Visit visit = new Visit();
        visit.setPatient(patient);
        visit.setStatus(VisitStatus.WAITING);

        int score = 0;
        try {
            score = triageRuleService.calculateScore(vital) ;
        } catch (Exception e) {
            visit.setStatus(VisitStatus.MANUAL_REVIEW);
            score = 50; //varsayılan değer
            System.out.println("Hesaplama hatası oldu ancak sisteme kaydedildi.");
        }

        visit.setUrgencyScore(score);
        visitRepository.save(visit);
        vital.setVisit(visit);
        vitalsRepository.save(vital);
        return visit;
    }
    /*
    public Visit matchDoctor(Doctor doctor, Visit visit){
        int gap = Math.abs(doctor.getSkillScore() - visit.getUrgencyScore());

        if(gap >= 20 && doctor.getSkillScore() >= 80){
            List<Doctor> doctors = doctorRepository.findDoctorBySkillScore(visit.getUrgencyScore());

            if(!doctors.isEmpty()){
                Doctor bestDoctor = null;
                int minGap = Integer.MAX_VALUE;

                for(Doctor tempDoctor : doctors){
                    int tempGap = Math.abs(tempDoctor.getSkillScore() - visit.getUrgencyScore());
                    if(tempGap < minGap){
                        minGap = tempGap;
                        bestDoctor = tempDoctor;
                    }
                }
                if(bestDoctor != null){
                    Visit newVisit = new Visit();
                    newVisit.setPatient(visit.getPatient());
                    newVisit.setDoctor(bestDoctor);
                    return newVisit;
                }
            }
            else {
                throw new TheBestMatchingDoctorNotFound("The patient matched with the previous doctor.");
            }
        }
        return visit;
    }
     */

    public Visit findNextPatientForDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı!"));

        List<Visit> waitingVisits = visitRepository.findAllByStatus(VisitStatus.WAITING);

        if (waitingVisits.isEmpty())
            return null;

        Visit bestVisit = null;
        double highestScore = -9999.0;

        for (Visit visit : waitingVisits) {

            long waitingMinutes = java.time.Duration.between(visit.getCreatedAt(), LocalDateTime.now()).toMinutes();
            double dynamicScore = visit.getUrgencyScore() + (waitingMinutes * 0.5);
            double skillGap = Math.abs(doctor.getSkillScore() - dynamicScore);
            double fatiguePenalty = doctor.getDailyPatientCount() * 0.2;
            double finalMatchScore = dynamicScore - skillGap - fatiguePenalty;

            if (finalMatchScore > highestScore) {
                highestScore = finalMatchScore;
                bestVisit = visit;
            }
        }
        if (bestVisit != null) {
            bestVisit.setStatus(VisitStatus.EXAMINATION);

            doctor.setDailyPatientCount(doctor.getDailyPatientCount() + 1);

            visitRepository.save(bestVisit);
            doctorRepository.save(doctor);
        }
        return bestVisit;
    }

}
