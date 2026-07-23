package ec.edu.ups.icc.labevaluation.supplies.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.ups.icc.labevaluation.supplies.entities.SupplyEntity;

public interface SupplyRepository extends JpaRepository<SupplyEntity, Long> {
    boolean existsByNameIgnoreCaseAndDeletedFalse(String name);
}
