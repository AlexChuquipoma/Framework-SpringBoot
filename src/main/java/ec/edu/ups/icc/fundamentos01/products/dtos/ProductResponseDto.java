package ec.edu.ups.icc.fundamentos01.products.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class ProductResponseDto {

    public Long id;
    public String name;
    public Double price;
    public String description;

    // ============== OBJETOS ANIDADOS ==============

    public UserSummaryDto user;

    // ============== CATEGORÍAS (N:N) - Lista de objetos ==============
    public List<CategorySummaryDto> categories;

    // Legacy support (1:N) - Primera categoría para compatibilidad con tests
    public CategorySummaryDto category;

    // ============== AUDITORÍA ==============

    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    // ============== DTOs INTERNOS ==============

    public static class UserSummaryDto {
        public Long id;
        public String name;
        public String email;
    }

    public static class CategorySummaryDto {
        public Long id;
        public String name;
        public String description;
    }
}