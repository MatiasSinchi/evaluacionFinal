package ec.edu.ups.icc.labevaluation.supplies.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.ups.icc.labevaluation.supplies.dtos.CreateSupplyDto;
import ec.edu.ups.icc.labevaluation.supplies.dtos.SupplyResponseDto;
import ec.edu.ups.icc.labevaluation.supplies.entities.SupplyEntity;
import ec.edu.ups.icc.labevaluation.supplies.exceptions.SupplyConflictException;
import ec.edu.ups.icc.labevaluation.supplies.mappers.SupplyMapper;
import ec.edu.ups.icc.labevaluation.supplies.repositories.SupplyRepository;

@Service
public class SupplyServiceImpl implements SupplyService {
    private final SupplyRepository repository;

    public SupplyServiceImpl(SupplyRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public SupplyResponseDto create(CreateSupplyDto dto) {
        if (repository.existsByNameIgnoreCaseAndDeletedFalse(dto.name())) {
            throw new SupplyConflictException("Supply name already exists");
        }
        SupplyEntity entity = new SupplyEntity();
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setQuantity(dto.quantity());
        entity.setMinimumStock(dto.minimumStock());
        entity.setUnitPrice(dto.unitPrice());
        entity.setActive(true);
        return SupplyMapper.toResponse(repository.save(entity));
    }
}
