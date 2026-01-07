package repository;

import entity.Doctor;
import entity.Vitals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VitalsRepository extends JpaRepository<Vitals,Long> {
}
