package repository;

import entity.Doctor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Integer> {
    @Query("SELECT d " +
            "FROM Doctor d " +
            "LEFT JOIN Visit v ON v.doctor = d AND v.status = 'EXAMINATION' " +
            "WHERE d.specialty = :specialty " +
            "GROUP BY d " +
            "ORDER BY COUNT(v) ASC, d.skillScore DESC")
    List<Doctor> findAvailableDoctors(@Param("specialty") String specialty, Pageable pageable);

    @Query("SELECT d FROM Doctor d WHERE d.skillScore BETWEEN :skillScore - 5 AND :skillScore + 5")
    List<Doctor> findDoctorBySkillScore(@Param("skillScore") int skillScore);

    @Query("""
       SELECT d 
       FROM Doctor d 
       WHERE d.skillScore BETWEEN :skillScore - 5 AND :skillScore + 5
       """)
    Optional<Doctor> findOneDoctorBySkillScore(@Param("skillScore") int skillScore);

    @Modifying
    @Query("UPDATE Doctor d SET d.dailyPatientCount = 0")
    void resetAllDailyPatientCounts();

    Optional<Doctor> findById(Long id);

}
