package ec.edu.ups.icc.fundamentos01.products.models;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;

import java.util.Set;

public class Product {
    private Long id;
    private String name;
    private Double price;
    private String description;

    public Product() {
    }

    public Product(String name, Double price, String description) {
        this.validateBusinessRules(name, price, description);
        this.name = name;
        this.price = price;
        this.description = description;
    }

    private void validateBusinessRules(String name, Double price, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("La descripción no puede superar 500 caracteres");
        }
    }

    // ==================== FACTORY METHODS ====================

    /**
     * Crea un Product desde un DTO de creación
     */
    public static Product fromDto(CreateProductDto dto) {
        return new Product(dto.name, dto.price, dto.description);
    }

    /**
     * Crea un Product desde una entidad persistente
     */
    public static Product fromEntity(ProductEntity entity) {
        Product product = new Product(
                entity.getName(),
                entity.getPrice(),
                entity.getDescription()
        );
        product.id = entity.getId();
        return product;
    }

    // ==================== CONVERSION METHODS ====================

    /**
     * Convierte este Product a una entidad persistente
     * REQUIERE las entidades relacionadas como parámetros (N:N con categorías)
     */
    public ProductEntity toEntity(UserEntity owner, Set<CategoryEntity> categories) {
        ProductEntity entity = new ProductEntity();

        if (this.id != null && this.id > 0) {
            entity.setId(this.id);
        }

        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setDescription(this.description);

        // Asignar relaciones
        entity.setOwner(owner);
        entity.setCategories(categories);

        return entity;
    }

    /**
     * Actualiza los campos modificables de este Product
     */
    public Product update(UpdateProductDto dto) {
        this.validateBusinessRules(dto.name, dto.price, dto.description);
        this.name = dto.name;
        this.price = dto.price;
        this.description = dto.description;
        return this;
    }

    // Getters y setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public Double getPrice() { return price; }
    public String getDescription() { return description; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(Double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
}