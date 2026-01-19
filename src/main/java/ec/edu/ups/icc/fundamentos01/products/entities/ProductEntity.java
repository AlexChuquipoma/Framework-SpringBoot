package ec.edu.ups.icc.fundamentos01.products.entities;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.entities.BaseModel;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseModel {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(length = 500)
    private String description;

    // ================== RELACIÓN 1:N (se mantiene) ==================

    /**
     * Relación Many-to-One con User
     * Muchos productos pertenecen a un usuario (owner/creator)
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity owner;

    // ================== RELACIÓN N:N (evolución de 1:N) ==================

    /**
     * Relación Many-to-Many con Category
     * Un producto puede tener múltiples categorías
     * Una categoría puede estar en múltiples productos
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_categories",                    // Tabla intermedia
        joinColumns = @JoinColumn(name = "product_id"), // FK hacia products
        inverseJoinColumns = @JoinColumn(name = "category_id") // FK hacia categories
    )
    private Set<CategoryEntity> categories = new HashSet<>();

    public ProductEntity() {}

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public UserEntity getOwner() { return owner; }
    public void setOwner(UserEntity owner) { this.owner = owner; }

    public Set<CategoryEntity> getCategories() { return categories; }
    public void setCategories(Set<CategoryEntity> categories) {
        this.categories = categories != null ? categories : new HashSet<>();
    }

    // ============== MÉTODOS DE CONVENIENCIA ==============

    /**
     * Agrega una categoría al producto
     */
    public void addCategory(CategoryEntity category) {
        this.categories.add(category);
    }

    /**
     * Remueve una categoría del producto
     */
    public void removeCategory(CategoryEntity category) {
        this.categories.remove(category);
    }

    /**
     * Limpia todas las categorías
     */
    public void clearCategories() {
        this.categories.clear();
    }
}