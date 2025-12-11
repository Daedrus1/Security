package mate.academy.security.mapper;

import mate.academy.security.dto.BookDto;
import mate.academy.security.dto.BookDtoWithoutCategoryIds;
import mate.academy.security.dto.BookRequestDto;
import mate.academy.security.model.Book;
import mate.academy.security.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;

import java.util.List;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    List<BookDto> toDtoList(List<Book> books);

    Book toEntity(BookRequestDto bookRequestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto out, Book book) {
        out.setCategoryIds(book.getCategories().stream().map(Category::getId).toList());
    }

}
