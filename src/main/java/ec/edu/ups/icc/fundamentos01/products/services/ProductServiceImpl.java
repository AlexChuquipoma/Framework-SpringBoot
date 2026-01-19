package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;

    public ProductServiceImpl(
            ProductRepository productRepo,
            UserRepository userRepo,
            CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {

        // 1. VALIDAR USER
        UserEntity owner = userRepo.findById(dto.userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + dto.userId));

        // 2. VALIDAR Y OBTENER CATEGORÍAS
        Set<CategoryEntity> categories = validateAndGetCategories(dto.categoryIds);

        // Regla: nombre único
        if (productRepo.findByName(dto.name).isPresent()) {
            throw new IllegalStateException("El nombre del producto ya está registrado");
        }

        // 3. CREAR DOMINIO
        Product product = Product.fromDto(dto);

        // 4. CREAR ENTIDAD CON RELACIONES N:N
        ProductEntity entity = product.toEntity(owner, categories);

        // 5. PERSISTIR
        ProductEntity saved = productRepo.save(entity);

        return toResponseDto(saved);
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepo.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto findById(Long id) {
        return productRepo.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public List<ProductResponseDto> findByUserId(Long userId) {

        // Validar que el usuario existe
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("Usuario no encontrado con ID: " + userId);
        }

        return productRepo.findByOwnerId(userId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByCategoryId(Long categoryId) {

        // Validar que la categoría existe
        if (!categoryRepo.existsById(categoryId)) {
            throw new NotFoundException("Categoría no encontrada con ID: " + categoryId);
        }

        return productRepo.findByCategoriesId(categoryId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto) {

        // 1. BUSCAR PRODUCTO EXISTENTE
        ProductEntity existing = productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));

        // 2. VALIDAR NUEVAS CATEGORÍAS
        Set<CategoryEntity> newCategories = validateAndGetCategories(dto.categoryIds);

        // 3. ACTUALIZAR CAMPOS BÁSICOS DIRECTAMENTE EN LA ENTIDAD
        existing.setName(dto.name != null ? dto.name : existing.getName());
        existing.setPrice(dto.price != null ? dto.price : existing.getPrice());
        existing.setDescription(dto.description != null ? dto.description : existing.getDescription());

        // 4. ACTUALIZAR CATEGORÍAS (N:N)
        existing.clearCategories();
        existing.setCategories(newCategories);

        // 5. PERSISTIR Y RESPONDER
        ProductEntity saved = productRepo.save(existing);
        return toResponseDto(saved);
    }

    @Override
    public void delete(Long id) {

        ProductEntity product = productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));

        // Eliminación física
        productRepo.delete(product);
    }

    // ============== MÉTODOS HELPER ==============

    /**
     * Valida y obtiene las categorías por sus IDs
     */
    private Set<CategoryEntity> validateAndGetCategories(Set<Long> categoryIds) {
        Set<CategoryEntity> categories = new HashSet<>();

        for (Long categoryId : categoryIds) {
            CategoryEntity category = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + categoryId));
            categories.add(category);
        }

        return categories;
    }

    /**
     * Convierte ProductEntity a DTO incluyendo categorías (N:N)
     * Usa estructura anidada para mejor semántica
     */
    private ProductResponseDto toResponseDto(ProductEntity entity) {
        ProductResponseDto dto = new ProductResponseDto();

        // Campos básicos
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.price = entity.getPrice();
        dto.description = entity.getDescription();

        // Crear objeto User anidado
        ProductResponseDto.UserSummaryDto userDto = new ProductResponseDto.UserSummaryDto();
        userDto.id = entity.getOwner().getId();
        userDto.name = entity.getOwner().getName();
        userDto.email = entity.getOwner().getEmail();
        dto.user = userDto;

        // Convertir Set<CategoryEntity> a List<CategorySummaryDto>
        dto.categories = entity.getCategories().stream()
                .map(this::toCategorySummary)
                .sorted((c1, c2) -> c1.name.compareTo(c2.name)) // Ordenar por nombre
                .toList();

        // Populate legacy field for backward compatibility
        if (!dto.categories.isEmpty()) {
            dto.category = dto.categories.get(0);
        }

        dto.createdAt = entity.getCreatedAt();
        dto.updatedAt = entity.getUpdatedAt();

        return dto;
    }

    private ProductResponseDto.CategorySummaryDto toCategorySummary(CategoryEntity category) {
        ProductResponseDto.CategorySummaryDto summary = new ProductResponseDto.CategorySummaryDto();
        summary.id = category.getId();
        summary.name = category.getName();
        summary.description = category.getDescription();
        return summary;
    }

    @Override
    public List<ProductResponseDto> findByUserIdWithFilters(
            Long userId,
            String name,
            Double minPrice,
            Double maxPrice,
            Long categoryId
    ) {
        // Validar que el usuario existe
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("Usuario no encontrado con ID: " + userId);
        }

        // Consulta con filtros a nivel de base de datos
        return productRepo.findByUserIdWithFilters(userId, name, minPrice, maxPrice, categoryId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }
}
