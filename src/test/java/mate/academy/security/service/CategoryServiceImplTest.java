package mate.academy.security.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import mate.academy.security.dto.CategoryDto;
import mate.academy.security.dto.CategoryRequestDto;
import mate.academy.security.mapper.BookMapper;
import mate.academy.security.mapper.CategoryMapper;
import mate.academy.security.model.Category;
import mate.academy.security.repository.BookRepository;
import mate.academy.security.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("""
        CategoryServiceImpl tests
        """)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("""
            Verify that getById throws EntityNotFoundException
            when category with given id does not exist
            """)
    void getById_whenNotFound_shouldThrowException() {
        Long categoryId = 999L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(categoryId));

        verify(categoryRepository).findById(categoryId);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    @DisplayName("""
        Verify that deleteById throws EntityNotFoundException
        when category with given id does not exist
        """)
    void deleteById_whenNotFound_shouldThrowException(){
        Long categoryId = 123L;
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteById(categoryId));

        verify(categoryRepository).existsById(categoryId);
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("""
        Verify that save returns CategoryDto
        when valid CategoryRequestDto is provided
        """)
    void save_shouldReturnCategoryDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Fiction");
        requestDto.setDescription("Books category");

        Category entity = new Category();
        entity.setName("Fiction");
        entity.setDescription("Books category");

        Category savedEntity = new Category();
        savedEntity.setId(1L);
        savedEntity.setName("Fiction");

        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(1L);
        expectedDto.setName("Fiction");

        when(categoryMapper.toEntity(requestDto)).thenReturn(entity);
        when(categoryRepository.save(entity)).thenReturn(savedEntity);
        when(categoryMapper.toDto(savedEntity)).thenReturn(expectedDto);

        CategoryDto actual = categoryService.save(requestDto);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo("Fiction");

        verify(categoryMapper).toEntity(requestDto);
        verify(categoryRepository).save(entity);
        verify(categoryMapper).toDto(savedEntity);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("""
        Verify that update updates category
        and returns updated CategoryDto
        """)
    void update_shouldUpdateAndReturnCategoryDto() {
        Long categoryId = 1L;

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Updated name");
        requestDto.setDescription("Updated description");

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old name");

        Category savedCategory = new Category();
        savedCategory.setId(categoryId);
        savedCategory.setName("Updated name");

        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(categoryId);
        expectedDto.setName("Updated name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(expectedDto);

        CategoryDto actual = categoryService.update(categoryId, requestDto);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(categoryId);
        assertThat(actual.getName()).isEqualTo("Updated name");

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateCategoryFromDto(requestDto, existingCategory);
        verify(categoryRepository).save(existingCategory);
        verify(categoryMapper).toDto(savedCategory);
    }


}
