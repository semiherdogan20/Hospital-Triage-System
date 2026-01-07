package service;

import entity.Doctor;
import entity.Visit;
import enums.VisitStatus;
import exception.DoctorNotFound;
import exception.VisitNotFoundException;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.DoctorRepository;
import repository.VisitRepository;

import java.util.*;

@Service
public class VisitTransitionService {
    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public VisitTransitionService(VisitRepository visitRepository, DoctorRepository doctorRepository) {
        this.visitRepository = visitRepository;
        this.doctorRepository = doctorRepository;
    }

    private static final Map<VisitStatus, Set<VisitStatus>> Transitions = new HashMap<>();

    static {
        Transitions.put(VisitStatus.WAITING, Set.of(VisitStatus.EXAMINATION));//Immutable
//      Transitions.put(VisitStatus.WAITING, new HashSet<>());              //Mutable
//      Transitions.get(VisitStatus.WAITING).add(VisitStatus.EXAMINATION);  // Mutable olması için Transitions ve iç sınıf static olmamalı

        Transitions.put(VisitStatus.EXAMINATION, Set.of(VisitStatus.DISCHARGED));

        Transitions.put(VisitStatus.MANUAL_REVIEW, Set.of(VisitStatus.WAITING));

        Transitions.put(VisitStatus.DISCHARGED, Set.of());
    }

    @Transactional
    public void changeStatus(long visitId, VisitStatus targetStatus) { // throws kısmını silebilirsin, RuntimeException olduğu için zorunlu değil
        // 1. Hastayı Bul
        Visit foundVisit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        // 2. BEKÇİ KONTROLÜ (Tek satır yeterli)
        // Eğer geçiş yasaksa, bu metot exception fırlatır ve kod burada durur.
        validateTransition(foundVisit.getStatus(), targetStatus);

        // 3. Buraya geldiyse izin çıkmıştır, güncelle ve kaydet
        foundVisit.setStatus(targetStatus);
        visitRepository.save(foundVisit);
    }

    private void validateTransition(VisitStatus currentStatus, VisitStatus targetStatus) {
        Set<VisitStatus> allowedTargets = Transitions.get(currentStatus);

        if (allowedTargets == null || !allowedTargets.contains(targetStatus)) {
            throw new IllegalStateException(
                    "HATA: " + currentStatus + " durumundan " + targetStatus + " durumuna geçiş yapılamaz!"
            );
        }
    }

    // resets the dailyPatientCount at 00:00
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyPatientCounts() {
        doctorRepository.resetAllDailyPatientCounts();
    }

    @Transactional
    public Visit saveVisit(Visit visitInRequest){
        Visit newVisit = new Visit();
        newVisit.setCreatedAt(visitInRequest.getCreatedAt());
        newVisit.setUrgencyScore(visitInRequest.getUrgencyScore());
        newVisit.setVitals(visitInRequest.getVitals());
        newVisit.setStatus(visitInRequest.getStatus());

        return visitRepository.save(newVisit);
    }

    public List<Visit> findAllByStatus(VisitStatus status) throws VisitNotFoundException{
        List<Visit> waitingVisits = visitRepository.findAllByStatus(status);
        if(waitingVisits.isEmpty())
            throw new VisitNotFoundException("No patients were found in the waiting status.");
        else
            return waitingVisits;
    }

    public Doctor findDoctorById(int id){
        Optional<Doctor> doctor =  doctorRepository.findById(id);
        if(doctor.isPresent())
            return doctor.get();
        else
            throw new DoctorNotFound("No doctor found with given id");
    }

    @Transactional
    public void saveDoctor(Doctor doctor){
        if(doctor == null)
            throw new DoctorNotFound("Doctor not found");
        else{
            Doctor newDoctor = doctor;
            doctorRepository.save(newDoctor);
        }
    }

    public Visit findVisitById(Long id){
        Visit tempVisit = visitRepository.findById(id).get();
        if(tempVisit != null){
            return tempVisit;
        }
        else
            throw new VisitNotFoundException("Visit not found with given id");
    }

    @Transactional
    public void deleteVisit(Visit visit){
        if(visit!=null)
            visitRepository.delete(visit);
        else
            throw new VisitNotFoundException("Visit not found");
    }

}
