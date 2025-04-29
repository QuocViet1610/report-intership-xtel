package com.example.project1.module.controller.order;
import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.dto.Order.OrderDto;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.request.Order.OrderRequest;
import com.example.project1.model.dto.request.Order.OrderSearchRequest;
import com.example.project1.model.dto.respone.OrderResponse;
import com.example.project1.module.Order.service.OrderService;
import com.example.project1.module.PageableCustom;
import com.example.project1.utils.DataUtils;
import jakarta.websocket.server.PathParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("")
    public ResponseResult<OrderDto> create(@RequestBody @TrimAndValid OrderRequest orderCreateRequest) {
        return ResponseResult.ofSuccess(orderService.create(orderCreateRequest));
    }

    @GetMapping("/{id}")
    public ResponseResult<OrderResponse> getOrderByUserId(@PathVariable Long id) {
        return ResponseResult.ofSuccess(orderService.getOrderDetail(id));
    }

    @GetMapping("")
    public ResponseResult<List<OrderDto>> getAllOrder() {
        return ResponseResult.ofSuccess(orderService.getAllOrder());
    }

    @PostMapping("/search")
    public ResponseResult<Object> search(@RequestBody @TrimAndValid OrderSearchRequest searchRequest,
                                         @PathParam("page") int page,
                                         @PathParam("size") int size,
                                         @RequestParam(name="sort", required=false) List<String> sort) {
        if (!DataUtils.isNullOrEmpty(page) && page >= 0) {
            return ResponseResult.ofSuccess(orderService.search(searchRequest, PageableCustom.setPageableCustom(page, size, sort)));
        } else {
            return ResponseResult.ofSuccess(orderService.search(searchRequest, PageableCustom.setPageableCustom(0, size, sort,true)));
        }
    }

    @PutMapping("/process/{id}")
    public ResponseResult<OrderDto> processOrder(@PathVariable Long id) {
        return ResponseResult.ofSuccess(orderService.UpdateProcess(id));
    }

    @PutMapping("/cancel/{id}")
    public ResponseResult<OrderDto> cancelOrder(@PathVariable Long id) {
        return ResponseResult.ofSuccess(orderService.CancelOrder(id));
    }
}

