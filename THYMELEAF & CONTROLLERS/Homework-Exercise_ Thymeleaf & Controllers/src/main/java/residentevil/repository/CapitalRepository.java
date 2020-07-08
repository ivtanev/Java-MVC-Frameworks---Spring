package residentevil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import residentevil.domain.entities.Capital;

import java.util.List;
import java.util.Optional;

@Repository
public interface CapitalRepository extends JpaRepository<Capital, String> {

    @Query("" +
            "SELECT c " +
            "FROM Capital AS c " +
            "ORDER BY c.name ")
    List<Capital> findAllOrderByName();

    Optional<Capital> findById(String id);
}
