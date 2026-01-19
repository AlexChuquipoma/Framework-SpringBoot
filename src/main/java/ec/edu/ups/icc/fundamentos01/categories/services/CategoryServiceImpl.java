package ec.edu.ups.icc.fundamentos01.categories.services;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.dtos.CreateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categories.dtos.UpdateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo, ProductRepository productRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    @Override
    public List<CategoryResponseDto> findAll() {
        return categoryRepo.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public CategoryResponseDto findById(Long id) {
        return categoryRepo.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + id));
    }

    @Override
    public CategoryResponseDto create(CreateCategoryDto dto) {
        // Validar que no exista una categoría con el mismo nombre
        if (categoryRepo.existsByName(dto.name)) {
            throw new IllegalStateException("Ya existe una categoría con el nombre: " + dto.name);
        }

        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.name);
        entity.setDescription(dto.description);

        CategoryEntity saved = categoryRepo.save(entity);
        return toResponseDto(saved);
    }

    @Override
    public CategoryResponseDto update(Long id, UpdateCategoryDto dto) {
        CategoryEntity existing = categoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + id));

        // Validar que no exista otra categoría con el mismo nombre
        categoryRepo.findByNameIgnoreCase(dto.name).ifPresent(found -> {
            if (!found.getId().equals(id)) {
                throw new IllegalStateException("Ya existe otra categoría con el nombre: " + dto.name);
            }
        });

        existing.setName(dto.name);
        existing.setDescription(dto.description);

        CategoryEntity saved = categoryRepo.save(existing);
        return toResponseDto(saved);
    }

    @Override
    public void delete(Long id) {
        CategoryEntity category = categoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + id));

        // Verificar si hay productos asociados (N:N)
        long productCount = productRepo.findByCategoriesId(id).size();
        if (productCount > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar la categoría porque tiene " + productCount + " producto(s) asociado(s)"
            );
        }

        categoryRepo.delete(category);
    }

    @Override
    public long countProductsByCategoryId(Long categoryId) {
        // Validar que la categoría existe
        if (!categoryRepo.existsById(categoryId)) {
            throw new NotFoundException("Categoría no encontrada con ID: " + categoryId);
        }

        return productRepo.findByCategoriesId(categoryId).size();
    }

    private CategoryResponseDto toResponseDto(CategoryEntity entity) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.description = entity.getDescription();
        dto.createdAt = entity.getCreatedAt();
        dto.updatedAt = entity.getUpdatedAt();
        return dto;
    }
}
