package mate.academy.security.mapper;

import mate.academy.security.dto.BookDto;
import mate.academy.security.dto.BookRequestDto;
import mate.academy.security.model.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    List<BookDto> toDtoList(List<Book> books);

    Book toEntity(BookRequestDto bookRequestDto);

}
