package ap2.Visons.service.impl;

import ap2.Visons.model.Category;
import ap2.Visons.repository.CategoryRepository;
import ap2.Visons.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        log.info("Listando todas las categorías");
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findAllActive() {
        log.info("Listando categorías activas");
        return categoryRepository.findAllActive();
    }

    @Override
    public List<Category> findByState(Boolean state) {
        log.info("Listando categorías por estado: {}", state);
        return categoryRepository.findByState(state);
    }

    @Override
    public Optional<Category> findById(Integer id) {
        log.info("Buscando categoría por ID: {}", id);
        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category category) {
        log.info("Registrando nueva categoría: {}", category.getName());
        category.setCategoryId(null);
        category.setIsActive(true);
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Integer id, Category categoryDetails) {
        log.info("Actualizando categoría con ID: {}", id);
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();

            if (categoryDetails.getName() != null) {
                category.setName(categoryDetails.getName());
            }
            if (categoryDetails.getDescription() != null) {
                category.setDescription(categoryDetails.getDescription());
            }

            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("Categoría no encontrada");
        }
    }

    @Override
    public Category delete(Integer id) {
        log.info("Eliminando categoría con ID: {}", id);
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            Category cat = category.get();
            cat.setIsActive(false);
            return categoryRepository.save(cat);
        } else {
            throw new RuntimeException("Categoría no encontrada");
        }
    }

    @Override
    public Category restore(Integer id) {
        log.info("Restaurando categoría con ID: {}", id);
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            Category cat = category.get();
            cat.setIsActive(true);
            return categoryRepository.save(cat);
        } else {
            throw new RuntimeException("Categoría no encontrada");
        }
    }
}
