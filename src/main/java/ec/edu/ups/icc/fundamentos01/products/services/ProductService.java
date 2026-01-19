package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.List;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;

public interface ProductService {
    List<ProductResponseDto> findAll();
    ProductResponseDto findById(Long id);
    ProductResponseDto create(CreateProductDto dto);
    ProductResponseDto update(Long id, UpdateProductDto dto);
    void delete(Long id);

    // Métodos de consulta relacionales
    List<ProductResponseDto> findByUserId(Long userId);
    List<ProductResponseDto> findByCategoryId(Long categoryId);

    // Búsqueda con filtros opcionales (v2)
    List<ProductResponseDto> findByUserIdWithFilters(
            Long userId,
            String name,
            Double minPrice,
            Double maxPrice,
            Long categoryId
    );
}