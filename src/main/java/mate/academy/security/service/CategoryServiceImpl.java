package mate.academy.security.service;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.BookDtoWithoutCategoryIds;
import mate.academy.security.dto.CategoryDto;
import mate.academy.security.dto.CategoryRequestDto;
import mate.academy.security.mapper.BookMapper;
import mate.academy.security.mapper.CategoryMapper;

import mate.academy.security.repository.BookRepository;
import mate.academy.security.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found, id=" + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CategoryRequestDto categoryRequestDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(categoryRequestDto)));
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto categoryRequestDto) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found, id=" + id));

        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable) {
        return bookRepository
                .findAllByCategories_Id(id, pageable)
                .map(bookMapper::toDtoWithoutCategories);
    }
}
