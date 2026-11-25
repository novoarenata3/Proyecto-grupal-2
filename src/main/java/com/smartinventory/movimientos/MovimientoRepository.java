package com.smartinventory.movimientos;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long>, JpaSpecificationExecutor<Movimiento> {
    default List<Movimiento> buscar(Long productoId, LocalDate desde, LocalDate hasta) {
        Instant inicio = desde == null ? null : desde.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant finExclusivo = hasta == null ? null
                : hasta.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        Specification<Movimiento> filtros = (root, query, criteria) -> criteria.conjunction();
        if (productoId != null) {
            filtros = filtros.and((root, query, criteria) ->
                    criteria.equal(root.get("productoId"), productoId));
        }
        if (inicio != null) {
            filtros = filtros.and((root, query, criteria) ->
                    criteria.greaterThanOrEqualTo(root.get("fecha"), inicio));
        }
        if (finExclusivo != null) {
            filtros = filtros.and((root, query, criteria) ->
                    criteria.lessThan(root.get("fecha"), finExclusivo));
        }

        Sort orden = Sort.by(Sort.Direction.DESC, "fecha")
                .and(Sort.by(Sort.Direction.DESC, "id"));
        return findAll(filtros, orden);
    }
}
