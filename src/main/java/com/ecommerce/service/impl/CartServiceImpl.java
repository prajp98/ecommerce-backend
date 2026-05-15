package com.ecommerce.service.impl;

import com.ecommerce.dto.request.AddToCartRequest;
import com.ecommerce.dto.request.UpdateCartItemRequest;
import com.ecommerce.dto.response.CartItemResponse;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ForbiddenOperationException;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.CartItemMapper;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    public CartServiceImpl(CartItemRepository cartItemRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    @Transactional
    public CartItemResponse addToCart(String userEmail, AddToCartRequest request) {
        User user = getUserByEmail(userEmail);
        Product product = getActiveProductById(request.getProductId());

        if (request.getQuantity() > product.getStock()) {
            throw new InsufficientStockException("Requested quantity exceeds available stock");
        }

        CartItem existingCartItem = cartItemRepository
                .findByUserIdAndProductId(user.getId(), product.getId())
                .orElse(null);

        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + request.getQuantity();

            if (newQuantity > product.getStock()) {
                throw new InsufficientStockException("Total quantity exceeds available stock");
            }

            existingCartItem.setQuantity(newQuantity);
            CartItem savedCartItem = cartItemRepository.save(existingCartItem);

            return buildResponse(savedCartItem, "Cart item updated successfully");
        }

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(request.getQuantity());

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return buildResponse(savedCartItem, "Product added to cart successfully");
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(String userEmail, Long cartItemId, UpdateCartItemRequest request) {
        User user = getUserByEmail(userEmail);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new ForbiddenOperationException("You cannot update another user's cart item");
        }

        Product product = cartItem.getProduct();

        if (request.getQuantity() > product.getStock()) {
            throw new InsufficientStockException("Requested quantity exceeds available stock");
        }

        cartItem.setQuantity(request.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        return buildResponse(savedCartItem, "Cart item updated successfully");
    }

    @Override
    @Transactional
    public CartItemResponse removeCartItem(String userEmail, Long cartItemId) {
        User user = getUserByEmail(userEmail);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new ForbiddenOperationException("You cannot remove another user's cart item");
        }

        CartItemResponse response = buildResponse(cartItem, "Cart item removed successfully");
        cartItemRepository.delete(cartItem);
        return response;
    }

    @Override
    public List<CartItemResponse> getMyCart(String userEmail) {
        User user = getUserByEmail(userEmail);

        return cartItemRepository.findByUserId(user.getId())
                .stream()
                .map(cartItem -> buildResponse(cartItem, "Cart item fetched successfully"))
                .toList();
    }

    private CartItemResponse buildResponse(CartItem cartItem, String message) {
        CartItemResponse response = cartItemMapper.toResponse(cartItem);
        response.setTotalPrice(calculateTotalPrice(response.getPrice(), response.getQuantity()));
        response.setMessage(message);
        return response;
    }

    private BigDecimal calculateTotalPrice(BigDecimal price, Integer quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private Product getActiveProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (!product.isActive()) {
            throw new ForbiddenOperationException("Product is not active");
        }

        return product;
    }
}