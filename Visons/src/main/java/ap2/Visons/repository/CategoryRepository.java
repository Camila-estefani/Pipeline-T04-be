package ap2.Visons.repository;

import ap2.Visons.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    @Query("SELECT c FROM Category c WHERE c.isActive = true")
    List<Category> findAllActive();
    
    @Query("SELECT c FROM Category c WHERE c.isActive = :state")
    List<Category> findByState(@Param("state") Boolean state);
}
