package service;

import entity.Visit;
import enums.VisitStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import repository.VisitRepository;

import java.util.List;

@Service
public class AgingService {
    VisitRepository visitRepository;

    @Autowired
    public AgingService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void incrementUrgencyScores(){
        int page = 0;
        int size = 100;
        while(true){
            Pageable pageRequest = PageRequest.of(page,100, Sort.by("id"));
            List<Visit> waitingVisits = visitRepository.findAllByStatus(VisitStatus.WAITING,pageRequest);

            if(waitingVisits.isEmpty())
                break;
            else{
                for(Visit visited : waitingVisits){
                    visited.setUrgencyScore(visited.getUrgencyScore() + 1);
                }
                visitRepository.saveAll(waitingVisits);
            }
            page++;
        }

    }
}
