package repository;

import entity.Visit;
import enums.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface VisitRepository extends JpaRepository<Visit,Long> {

    List<Visit> findAllByStatus(VisitStatus status, Pageable pageable);

    List<Visit> findAllByStatus(VisitStatus status);

    void delete(Optional<Visit> tempVisit);
}
