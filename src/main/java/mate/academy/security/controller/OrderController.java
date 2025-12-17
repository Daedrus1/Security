package mate.academy.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.CreateOrderRequestDto;
import mate.academy.security.dto.OrderDto;
import mate.academy.security.dto.UpdateOrderStatusRequestDto;
import mate.academy.security.service.OrderService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order management endpoints")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place an order")
    public OrderDto placeOrder(@Valid @RequestBody CreateOrderRequestDto requestDto){
        return orderService.placeOrder(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get current user's order history")
    public Page<OrderDto> getMyOrders(@ParameterObject Pageable pageable){
        return orderService.getMyOrders(pageable);
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status (ADMIN)")
    public OrderDto updateStatus(@PathVariable Long id,
                                 @Valid @RequestBody UpdateOrderStatusRequestDto requestDto) {
        return orderService.updateStatus(id, requestDto);
    }
}
