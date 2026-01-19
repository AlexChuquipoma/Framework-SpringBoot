package ec.edu.ups.icc.fundamentos01.categories.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCategoryDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 120, message = "El nombre debe tener entre 3 y 120 caracteres")
    public String name;

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    public String description;
}
