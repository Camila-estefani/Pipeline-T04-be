package ap2.Visons.service;

import ap2.Visons.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();

    List<Category> findAllActive();

    List<Category> findByState(Boolean state);

    Optional<Category> findById(Integer id);

    Category save(Category category);

    Category update(Integer id, Category category);

    Category delete(Integer id);

    Category restore(Integer id);
    
}
