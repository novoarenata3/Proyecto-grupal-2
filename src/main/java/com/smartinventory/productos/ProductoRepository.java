package com.smartinventory.productos;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select p from Producto p where p.id = ?1")
    java.util.Optional<Producto> bloquear(long id);

    @org.springframework.data.jpa.repository.Query("select p from Producto p where p.activo = true and p.stock <= p.stockMinimo order by p.nombre, p.id")
    java.util.List<Producto> conAlerta();
}
