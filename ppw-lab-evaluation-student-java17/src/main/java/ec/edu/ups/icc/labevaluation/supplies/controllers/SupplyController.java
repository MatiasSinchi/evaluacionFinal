package ec.edu.ups.icc.labevaluation.supplies.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import ec.edu.ups.icc.labevaluation.supplies.dtos.CreateSupplyDto;
import ec.edu.ups.icc.labevaluation.supplies.dtos.SupplyResponseDto;
import ec.edu.ups.icc.labevaluation.supplies.services.SupplyService;

@RestController
@RequestMapping("/supplies")
public class SupplyController {
    private final SupplyService service;

    public SupplyController(SupplyService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SupplyResponseDto> create(@Valid @RequestBody CreateSupplyDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }
}
