package com.example.project1.module.Order.service.Impl;
import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.order.OrderDetailMapper;
import com.example.project1.mapper.order.OrderMapper;
import com.example.project1.mapper.product.ProductViewMapper;
import com.example.project1.model.dto.Order.OrderDto;
import com.example.project1.model.dto.request.Order.OrderDetailCreateRequest;
import com.example.project1.model.dto.request.Order.OrderRequest;
import com.example.project1.model.dto.request.Order.OrderSearchRequest;
import com.example.project1.model.dto.respone.OrderDetailResponse;
import com.example.project1.model.dto.respone.OrderResponse;
import com.example.project1.model.dto.view.product.ProductView;
import com.example.project1.model.enity.order.Order;
import com.example.project1.model.enity.order.OrderDetail;
import com.example.project1.model.enity.order.OrderView;
import com.example.project1.model.enity.product.Product;
import com.example.project1.model.enity.product.ProductImage;
import com.example.project1.model.enity.product.ProductVariant;
import com.example.project1.module.Order.repository.OrderDetailRepository;
import com.example.project1.module.Order.repository.OrderRepository;
import com.example.project1.module.Order.repository.OrderStatusRepository;
import com.example.project1.module.Order.repository.OrderViewRepository;
import com.example.project1.module.Order.service.OrderService;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.User.repository.UserRepository;
import com.example.project1.module.product.repository.ProductImageRepository;
import com.example.project1.module.product.repository.ProductRepository;
import com.example.project1.module.product.repository.ProductVariantRepository;
import com.example.project1.module.product.repository.ProductViewRepository;
import com.example.project1.utils.DataUtils;
import com.example.project1.utils.SearchSpecificationUtil;
import com.example.project1.utils.TokenUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderStatusRepository orderStatusRepository;
    OrderMapper orderMapper;
    TokenUtil tokenUtil;
    ProductRepository productRepository;
    ProductVariantRepository productVariantRepository;
    OrderDetailMapper orderDetailMapper;
    OrderDetailRepository orderDetailRepository;
    ProductViewRepository productViewRepository;
    ProductViewMapper productViewMapper;
    OrderViewRepository orderViewRepository;
    UserRepository userRepository;
    ProductImageRepository productImageRepository;

    private void validateLogic(OrderRequest request, boolean isCreated) {
        Long userId = tokenUtil.getCurrentUserId();
        request.setUserId(userId);
    }

    private void validateOrderDetail(OrderDetailCreateRequest request, boolean isCreated) {
        OrderDetailCreateRequest.validate(request);
        if (!productRepository.findById(request.getProductId()).isPresent()) {
            throw new ValidateException(Translator.toMessage("Sản phẩm trong đơn hàng không tồn tại"));
        }
        if (!productVariantRepository.findById(request.getProductVariantId()).isPresent()) {
            throw new ValidateException(Translator.toMessage("Biến thể sản phẩm trong đơn hàng không tồn tại "));
        }

    }

    @Override
    public OrderDto create(OrderRequest orderCreateRequest) {
        this.validateLogic(orderCreateRequest, true);
        Order order = orderRepository.save(orderMapper.toCreate(orderCreateRequest));
        order.setOrderCode(String.format("DH%06d", order.getId()));
        order.setStatusId(1L);
        orderRepository.save(order);
        List<OrderDetail> orderDetails = orderCreateRequest.getOrderDetails().stream().map(orderDetailCreateRequest -> {
            validateOrderDetail(orderDetailCreateRequest, true);
            orderDetailCreateRequest.setOrderId(order.getId());
            return orderDetailMapper.toCreate(orderDetailCreateRequest);
        }).collect(Collectors.toList());

        orderDetailRepository.saveAll(orderDetails);
        order.setOrderDetails(orderDetails.stream().collect(Collectors.toSet()));
        return orderMapper.toDto(order);
    }

    @Override
    public OrderResponse getOrderDetail(Long id) {
        Long userId = tokenUtil.getCurrentUserId();
        Order order = orderRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new ValidateException("Đơn hàng không tồn tại"));
        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderId(order.getId());
        Set<OrderDetailResponse> orderDetails = orderDetailList.stream()
                .sorted(Comparator.comparing(OrderDetail::getOrderId)) // Sắp xếp theo ID
                .map(orderDetail -> {

                    OrderDetailResponse orderDetailResponse = orderDetailMapper.toResponse(orderDetail);

                    ProductView productView = productViewRepository.findById(orderDetail.getProductId())
                            .orElseThrow(() -> new ValidateException(Translator.toMessage("Sản phẩm trong giỏ hàng không tồn tại")));
                    if (orderDetail.getProductVariantId() != null) {
                        ProductVariant productVariant = productVariantRepository.findByIdAndProductId(orderDetail.getProductVariantId(), orderDetail.getProductId())
                                .orElseThrow(() -> new ValidateException(Translator.toMessage("Biến thể trong giỏ hàng không tồn tại")));
                        productView.setProductVariants(Set.of(productVariant));
                    } else {
                        productView.setProductVariants(Collections.emptySet());
                    }
                    // Gán ProductView vào orderItemResponse
                    orderDetailResponse.setProductView(productViewMapper.toDto(productView));

                    return orderDetailResponse;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));

        OrderResponse orderResponse = orderMapper.toResponse(order);
        orderResponse.setOrderDetails(orderDetails);
        return orderResponse;
    }

    @Override
    public List<OrderDto> getAllOrder() {
        Long userId = tokenUtil.getCurrentUserId();
        List<Order> orders = orderRepository.findByUserId(userId, Sort.by(Sort.Order.desc("id")));
        return orderMapper.toDto(orders);
    }

    @Override
    public Object search(OrderSearchRequest searchRequest, PageableCustom pageable) {
        Map<String, String> mapCondition = new HashMap<>();
        if (!DataUtils.isNullOrEmpty(searchRequest.getSearchText())) {
            mapCondition.put("orderCode", searchRequest.getSearchText());
            mapCondition.put("userFullName", searchRequest.getSearchText());
            mapCondition.put("userEmail", searchRequest.getSearchText());
            mapCondition.put("phone", searchRequest.getSearchText());

        }
        Specification<OrderView> conditions = Specification.where(SearchSpecificationUtil.<OrderView>alwaysTrue())
                .and(SearchSpecificationUtil.equal("statusId", searchRequest.getStatusId()))
                .and(SearchSpecificationUtil.or(mapCondition));
        if (!DataUtils.isNullOrEmpty(pageable) && !pageable.isFindAll()) {
            Page<OrderView> page = orderViewRepository.findAll(conditions, pageable);
            return new PageImpl<>(page.getContent(), pageable, page.getTotalElements());
        }
        return orderViewRepository.findAll(conditions, pageable.getSort());
    }

    @Override
    public OrderDto UpdateProcess(Long id) {
        Long userId = tokenUtil.getCurrentUserId();
        Order order = orderRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new ValidateException("Đơn hàng không tồn tại"));
        order.setStatusId(order.getStatusId() + 1);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto CancelOrder(Long id) {
        Long userId = tokenUtil.getCurrentUserId();
        Order order = orderRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new ValidateException("Đơn hàng không tồn tại"));
        if (order.getStatusId() >= 3) {
            throw new ValidateException(Translator.toMessage("Đơn hàng đã vận chuyển không thể huỷ"));
        }
        order.setStatusId(5L);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public Object Dashboard() {
        Long totalOrders = orderRepository.getTotalOrders();
        Double totalRevenue = orderRepository.getTotalRevenue();
        Long totalProduct = productRepository.count();
        Long totalProductVariant = productVariantRepository.count();
        Long countProduct = totalProduct + totalProductVariant;
        Long totalUser = userRepository.count();
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("totalOrders", totalOrders);
        dashboardData.put("totalRevenue", totalRevenue);
        dashboardData.put("totalProduct", countProduct);
        dashboardData.put("totalUser", totalUser);
        return dashboardData;
    }

    @Override
    public Object doanhThu() {
        int currentYear = LocalDate.now().getYear();
        List<Object[]> results = orderRepository.getMonthlyRevenue(currentYear);

        double[] monthlyRevenue = new double[12];

        for (Object[] row : results) {
            int month = ((Integer) row[0]) - 1; // Trừ 1 vì mảng bắt đầu từ 0
            BigDecimal total = (BigDecimal) row[1]; // Sửa tại đây: dùng BigDecimal
            monthlyRevenue[month] = total != null ? total.doubleValue() : 0.0;
        }

        return monthlyRevenue;
    }

    @Override
    public Object loiNhuan() {
        List<Double> profits = new ArrayList<>(Collections.nCopies(12, 0.0));
        List<Object[]> result = orderRepository.getMonthlyProfitData();

        for (Object[] row : result) {
            int month = ((Number) row[0]).intValue(); // tháng (1-12)
            double profit = ((Number) row[3]).doubleValue(); // lợi nhuận
            profits.set(month - 1, profit);
        }

        return profits;
    }

    @Override
    public Object topProduct() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("totalOrders")));
        List<Object[]> result = orderDetailRepository.findTop5ProductsByTotalOrders(pageable);


        List<Long> productIds = result.stream()
                .map(row -> (Long) row[0])  // Chuyển đổi từ Object[] lấy product_id
                .collect(Collectors.toList());


        List<ProductView> products = productViewRepository.findAllById(productIds);


        List<ProductImage> productImages = new ArrayList<>();


        for (Long productId : productIds) {

            List<ProductImage> images = productImageRepository.findAllByProductId(productId);

            productImages.addAll(images);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("products", products);            // Thêm danh sách sản phẩm vào Map
        resultMap.put("productImages", productImages);
        return resultMap;
    }

    @Override
    public Object topOder() {
        Pageable pageable = PageRequest.of(0, 5);  // Lấy 5 đơn hàng có số tiền cao nhất
        return orderRepository.findTop5ByOrderByTotalAmountDesc(pageable);
    }


}
