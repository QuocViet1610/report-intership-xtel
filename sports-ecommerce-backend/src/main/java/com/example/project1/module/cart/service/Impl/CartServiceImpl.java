package com.example.project1.module.cart.service.Impl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.cart.CartItemMapper;
import com.example.project1.mapper.cart.CartMapper;
import com.example.project1.mapper.product.ProductViewMapper;
import com.example.project1.model.dto.cart.CartItemCreateRequest;
import com.example.project1.model.dto.cart.CartItemDto;
import com.example.project1.model.dto.respone.CartItemResponse;
import com.example.project1.model.dto.respone.CartResponse;
import com.example.project1.model.dto.view.product.ProductView;
import com.example.project1.model.enity.cart.Cart;
import com.example.project1.model.enity.cart.CartItem;
import com.example.project1.model.enity.product.Product;
import com.example.project1.model.enity.product.ProductVariant;
import com.example.project1.module.cart.repository.CartItemRepository;
import com.example.project1.module.cart.repository.CartRepository;
import com.example.project1.module.cart.service.CartService;
import com.example.project1.module.product.repository.ProductRepository;
import com.example.project1.module.product.repository.ProductVariantRepository;
import com.example.project1.module.product.repository.ProductViewRepository;
import com.example.project1.utils.TokenUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    CartMapper cartMapper;
    CartItemMapper cartItemMapper;
    TokenUtil tokenUtil;
    ProductRepository productRepository;
    ProductVariantRepository productVariantRepository;
    ProductViewRepository productViewRepository;
    ProductViewMapper productViewMapper;

    private void validateLogic(CartItemCreateRequest request, boolean isCreated) {

    }

    @Override
    public CartItemDto addProductToCart(CartItemCreateRequest request) {
        Cart cart = cartRepository.findByUserId(tokenUtil.getCurrentUserId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(tokenUtil.getCurrentUserId());
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingCartItem;
        if (request.getProductVariantId() == null) {
            existingCartItem = cartItemRepository.findByProductId(request.getProductId());
        } else {
            existingCartItem = cartItemRepository.findByProductIdAndProductVariantId(request.getProductId(), request.getProductVariantId());
        }
        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setCartId(cart.getId());
            cartItem.setProductId(request.getProductId());
            cartItem.setProductVariantId(request.getProductVariantId());
            cartItem.setQuantity(request.getQuantity());
        }
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDto updateQuantity(Long cartItemId, Long quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ValidateException(Translator.toMessage("Sản phẩm trong giỏ hàng không tồn tại")));

        if (quantity <= 0) {
            throw new ValidateException(Translator.toMessage("Số lượng phải lớn hơn 0"));
        }
        cartItem.setQuantity(quantity);
        Long stock;
        if (cartItem.getProductVariantId() != null){
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new ValidateException(Translator.toMessage("Sản phẩm trong giỏ hàng không tồn tại")));
            ProductVariant productVariant = productVariantRepository.findByIdAndProductId(cartItem.getProductVariantId(), cartItem.getProductId())
                    .orElseThrow(() -> new ValidateException(Translator.toMessage("Biến thể trong giỏ hàng không tồn tại")));
            stock = productVariant.getQuantity();
        }else{
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new ValidateException(Translator.toMessage("Sản phẩm trong giỏ hàng không tồn tại")));
            stock = product.getStock();
        }

        if (quantity > stock) {
            throw new ValidateException(Translator.toMessage("Số lượng sản phẩm trong kho không đủ"));
        }

        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void removeProductFromCart(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new ValidateException("Sản phẩm trong giỏ hàng không tồn tại");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public CartResponse getCartByUserId() {
        // Lấy giỏ hàng của người dùng
        Long userId = tokenUtil.getCurrentUserId();
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ValidateException("Người dùng không tồn tại"));

        Set<CartItemResponse> cartItems = cart.getCartItems().stream()
                .sorted(Comparator.comparing(CartItem::getId)) // Sắp xếp theo ID
                .map(cartItem -> {

                    CartItemResponse cartItemResponse = cartItemMapper.toResponse(cartItem);

                    ProductView productView = productViewRepository.findById(cartItem.getProductId())
                            .orElseThrow(() -> new ValidateException(Translator.toMessage("Sản phẩm trong giỏ hàng không tồn tại")));
                    if (cartItem.getProductVariantId() != null) {
                        ProductVariant productVariant = productVariantRepository.findByIdAndProductId(cartItem.getProductVariantId(), cartItem.getProductId())
                                .orElseThrow(() -> new ValidateException(Translator.toMessage("Biến thể trong giỏ hàng không tồn tại")));
                        productView.setProductVariants(Set.of(productVariant));
                    } else {
                        productView.setProductVariants(Collections.emptySet());
                    }

                    // Gán ProductView vào CartItemResponse
                    cartItemResponse.setProductView(productViewMapper.toDto(productView));

                    return cartItemResponse;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));

        CartResponse cartResponse = cartMapper.toResponse(cart);
        cartResponse.setCartItems(cartItems);

        return cartResponse;
    }

}
