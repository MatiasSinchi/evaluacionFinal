package ec.edu.ups.icc.labevaluation.supplies.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.ups.icc.labevaluation.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.labevaluation.supplies.dtos.CreateSupplyDto;
import ec.edu.ups.icc.labevaluation.supplies.dtos.SupplyResponseDto;
import ec.edu.ups.icc.labevaluation.supplies.dtos.UpdateSupplyQuantityDto;
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
            throw new SupplyConflictException("Supply name already registered");
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

    @Override
    @Transactional(readOnly = true)
    public List<SupplyResponseDto> findLowStock(Integer maxQuantity) {
        return repository.findByActiveTrueAndDeletedFalseAndQuantityLessThanOrderByQuantityAsc(maxQuantity)
            .stream().map(SupplyMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public SupplyResponseDto updateQuantity(Long id, UpdateSupplyQuantityDto dto) {
        SupplyEntity entity = repository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new NotFoundException("SUPPLY_NOT_FOUND", "Supply not found"));
        entity.setQuantity(dto.quantity());
        return SupplyMapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        SupplyEntity entity = repository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new NotFoundException("SUPPLY_NOT_FOUND", "Supply not found"));
        if (entity.getQuantity() != 0) {
            throw new SupplyConflictException("Supply cannot be deleted while quantity is greater than zero");
        }
        entity.setDeleted(true);
        entity.setActive(false);
        repository.save(entity);
    }
}
