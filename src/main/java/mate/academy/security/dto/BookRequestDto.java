package mate.academy.security.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookRequestDto {
    private String title;
    private String author;
    private String description;
    @NotNull
    private BigDecimal price;
    private String isbn;
    @NotEmpty
    private List<Long> categoryIds;
}
