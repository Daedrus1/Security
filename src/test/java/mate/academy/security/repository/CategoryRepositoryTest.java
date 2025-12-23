package mate.academy.security.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import mate.academy.security.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void saveAndFindById_shouldWork() {

        Category category = new Category();
        category.setName("test");

        Category saved = categoryRepository.save(category);

        Optional<Category> found = categoryRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("test");
        assertThat(found.get().isDeleted()).isFalse();
    }

    @Test
    void deleteById_shouldSoftDelete_andNotBeFound() {
        Category category = new Category();
        category.setName("test-delete"); // другое имя!

        Category saved = categoryRepository.save(category);
        Long id = saved.getId();

        categoryRepository.deleteById(id);

        Optional<Category> found = categoryRepository.findById(id);
        assertThat(found).isEmpty();
    }

}
