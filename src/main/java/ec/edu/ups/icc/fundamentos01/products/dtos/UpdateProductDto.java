package ec.edu.ups.icc.fundamentos01.products.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UpdateProductDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150)
    public String name;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false)
    public Double price;

    @Size(max = 500)
    public String description;

    // ============== ACTUALIZACIÓN DE RELACIONES ==============

    @NotNull(message = "Debe especificar al menos una categoría")
    @Size(min = 1, message = "El producto debe tener al menos una categoría")
    public Set<Long> categoryIds; // Múltiples categorías

    // Nota: No se permite cambiar el owner de un producto una vez creado
}