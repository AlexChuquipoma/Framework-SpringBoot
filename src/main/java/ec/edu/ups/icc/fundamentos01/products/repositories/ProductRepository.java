package ec.edu.ups.icc.fundamentos01.products.repositories;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByName(String name);

    /**
     * Encuentra todos los productos de un usuario específico
     * Spring Data JPA genera: SELECT * FROM products WHERE user_id = ?
     */
    List<ProductEntity> findByOwnerId(Long userId);

    /**
     * Encuentra productos que tienen UNA categoría específica (N:N)
     * Útil para filtros de categoría
     */
    List<ProductEntity> findByCategoriesId(Long categoryId);

    /**
     * Encuentra productos que tienen una categoría con nombre específico (N:N)
     */
    List<ProductEntity> findByCategoriesName(String categoryName);

    /**
     * Encuentra productos por nombre de usuario
     * Genera JOIN automáticamente:
     * SELECT p.* FROM products p JOIN users u ON p.user_id = u.id WHERE u.name = ?
     */
    List<ProductEntity> findByOwnerName(String ownerName);

    /**
     * Busca productos de un usuario con filtros opcionales.
     * Los filtros se aplican a nivel de base de datos (no en memoria).
     * Si un parámetro es null, ese filtro se ignora.
     */
    @Query("""
        SELECT DISTINCT p FROM ProductEntity p
        LEFT JOIN p.categories c
        WHERE p.owner.id = :userId
        AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        AND (:categoryId IS NULL OR c.id = :categoryId)
        """)
    List<ProductEntity> findByUserIdWithFilters(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("categoryId") Long categoryId
    );
}