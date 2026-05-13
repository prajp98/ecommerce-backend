package com.ecommerce.service.impl;

import com.ecommerce.dto.request.AddToCartRequest;
import com.ecommerce.dto.request.UpdateCartItemRequest;
import com.ecommerce.dto.response.CartItemResponse;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.exception.DuplicateResourceException;
import com.ecommerce.exception.ForbiddenOperationException;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ResourceNotFoundException;
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

    public CartServiceImpl(CartItemRepository cartItemRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
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
            return toResponse(savedCartItem, "Cart item updated successfully");
        }

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(request.getQuantity());

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return toResponse(savedCartItem, "Product added to cart successfully");
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

        return toResponse(savedCartItem, "Cart item updated successfully");
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

        CartItemResponse response = toResponse(cartItem, "Cart item removed successfully");
        cartItemRepository.delete(cartItem);

        return response;
    }

    @Override
    public List<CartItemResponse> getMyCart(String userEmail) {
        User user = getUserByEmail(userEmail);

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());

        return cartItems.stream()
                .map(cartItem -> toResponse(cartItem, "Cart item fetched successfully"))
                .toList();
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

    private CartItemResponse toResponse(CartItem cartItem, String message) {
        Product product = cartItem.getProduct();

        BigDecimal price = product.getPrice();
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        CartItemResponse response = new CartItemResponse();
        response.setCartItemId(cartItem.getId());
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setPrice(price);
        response.setQuantity(cartItem.getQuantity());
        response.setTotalPrice(totalPrice);
        response.setMessage(message);

        return response;
    }
}