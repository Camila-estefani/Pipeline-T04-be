package vallegrande.edu.pe.visons.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import vallegrande.edu.pe.visons.model.CurrentInventory;

@Repository
public interface CurrentInventoryRepository extends JpaRepository<CurrentInventory, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CurrentInventory> findByProductId(Integer productId);
}