package ec.edu.ups.icc.fundamentos01.categories.entities;

import ec.edu.ups.icc.fundamentos01.entities.BaseModel;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseModel {

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(length = 500)
    private String description;

    // ============== RELACIÓN BIDIRECCIONAL N:N ==============

    /**
     * Relación inversa con Product
     * mappedBy = "categories" hace referencia al atributo en ProductEntity
     */
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<ProductEntity> products = new HashSet<>();

    public CategoryEntity() {
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductEntity> products) {
        this.products = products != null ? products : new HashSet<>();
    }

    // ============== MÉTODOS DE CONVENIENCIA ==============

    public void addProduct(ProductEntity product) {
        this.products.add(product);
    }

    public void removeProduct(ProductEntity product) {
        this.products.remove(product);
    }
}
