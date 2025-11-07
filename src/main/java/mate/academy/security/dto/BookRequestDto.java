package mate.academy.security.dto;

import jakarta.validation.constraints.NotNull;
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
}
