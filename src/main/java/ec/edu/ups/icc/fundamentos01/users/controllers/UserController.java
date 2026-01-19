package ec.edu.ups.icc.fundamentos01.users.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.*;
import ec.edu.ups.icc.fundamentos01.users.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto findOne(@PathVariable int id) {
        return service.findOne(id);
    }

    @PostMapping
    public UserResponseDto create(@RequestBody CreateUserDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable int id, @RequestBody UpdateUserDto dto) {
        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public UserResponseDto partialUpdate(@PathVariable int id, @RequestBody PartialUpdateUserDto dto) {
        return service.partialUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    // GET /api/users/{id}/products - Obtener productos de un usuario
    @GetMapping("/{id}/products")
    public List<ProductResponseDto> getProductsByUserId(@PathVariable Long id) {
        return service.getProductsByUserId(id);
    }

    // GET /api/users/{id}/products-v2 - Obtener productos con filtros opcionales
    @GetMapping("/{id}/products-v2")
    public List<ProductResponseDto> getProductsByUserIdWithFilters(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Long categoryId
    ) {
        return service.getProductsByUserIdWithFilters(id, name, minPrice, maxPrice, categoryId);
    }
}