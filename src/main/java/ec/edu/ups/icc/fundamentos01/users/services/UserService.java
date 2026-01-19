package ec.edu.ups.icc.fundamentos01.users.services;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.*;
import java.util.List;

public interface UserService {
    List<UserResponseDto> findAll();
    UserResponseDto findOne(int id);
    UserResponseDto create(CreateUserDto dto);
    UserResponseDto update(int id, UpdateUserDto dto);
    UserResponseDto partialUpdate(int id, PartialUpdateUserDto dto);
    void delete(int id);

    // Obtener productos de un usuario específico
    List<ProductResponseDto> getProductsByUserId(Long userId);

    // Obtener productos de un usuario con filtros opcionales (v2)
    List<ProductResponseDto> getProductsByUserIdWithFilters(
            Long userId,
            String name,
            Double minPrice,
            Double maxPrice,
            Long categoryId
    );
}