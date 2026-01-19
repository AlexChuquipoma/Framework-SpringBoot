package ec.edu.ups.icc.fundamentos01.products.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class CreateProductDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    public String name;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    public Double price;

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    public String description;

    // ============== RELACIONES ==============

    @NotNull(message = "El ID del usuario es obligatorio")
    public Long userId;

    @NotNull(message = "Debe especificar al menos una categoría")
    @Size(min = 1, message = "El producto debe tener al menos una categoría")
    public Set<Long> categoryIds; // Múltiples categorías
}