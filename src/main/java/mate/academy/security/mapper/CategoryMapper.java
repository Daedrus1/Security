package mate.academy.security.mapper;

import java.util.List;
import mate.academy.security.dto.CategoryDto;
import mate.academy.security.dto.CategoryRequestDto;
import mate.academy.security.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryRequestDto);

    List<CategoryDto> toDto(List<Category> all);

    void updateCategoryFromDto(CategoryRequestDto dto, @MappingTarget Category category);

}
