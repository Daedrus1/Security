package mate.academy.security.mapper;

import java.util.List;
import mate.academy.security.dto.CategoryDto;
import mate.academy.security.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto dto);

    List<CategoryDto> toDto(List<Category> all);
}
