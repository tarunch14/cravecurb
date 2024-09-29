package org.cravecurb.service;

import org.cravecurb.exception.ResourceNotFound;
import org.cravecurb.model.Cart;
import org.cravecurb.model.CartItem;
import org.cravecurb.model.Customer;
import org.cravecurb.model.Food;
import org.cravecurb.payload.AddCartItemRequest;
import org.cravecurb.repository.CartItemRepository;
import org.cravecurb.repository.CartRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
	
	private final CartRepository cartRepo;
	private final CartItemRepository cartItemRepo;
	private final FoodService foodService;
	private final UsersService userService;
	
	
	public CartItem addItemTocart(AddCartItemRequest req, String jwtToken) {
		Customer user = userService.findUserByJwtToken(jwtToken);
		Food food = foodService.findFoodById(req.getFoodId());
		Cart cart = cartRepo.findByCustomerId(user.getId());
		
		cart.getItems().stream().filter(cartItem -> cartItem.getFood().equals(food))
	                            .findFirst()
	                            .ifPresent(cartItem -> {
	                                     int newQuantity = cartItem.getQuantity() + req.getQuantity();
	                                     updateCartItemQuantity(cartItem.getId(), newQuantity);
	                             });
		
		CartItem newCartItem = new CartItem();
		newCartItem.setFood(food);
		newCartItem.setCart(cart);
		newCartItem.setQuantity(req.getQuantity());
		newCartItem.setIngredients(req.getIngredients());
		newCartItem.setTotalPrice(req.getQuantity() * food.getPrice());
		CartItem savedCart = cartItemRepo.save(newCartItem);
		cart.getItems().add(savedCart);
		
		return savedCart;
	}
	
	public CartItem updateCartItemQuantity(long cartItemId, int quantity) {
		CartItem cartItem = cartItemRepo.findById(cartItemId).orElseThrow(() -> new ResourceNotFound("cart item not found !"));
		cartItem.setQuantity(quantity);
		cartItem.setTotalPrice(cartItem.getFood().getPrice() * quantity);

		return cartItemRepo.save(cartItem);
	}
	
	public Cart removeItemFromcart(Long cartItemId, String jwtToken) {
		Customer user = userService.findUserByJwtToken(jwtToken);
		Cart cart = cartRepo.findByCustomerId(user.getId());
		CartItem cartItem = cartItemRepo.findById(cartItemId).orElseThrow(() -> new ResourceNotFound("cart item not found !"));
		cart.getItems().remove(cartItem);
		
		return cartRepo.save(cart);
	}
	
	public Long calculateCartTotal(Cart cart) {
	    return cart.getItems().stream()
	        .mapToLong(item -> item.getFood().getPrice() * item.getQuantity())
	        .sum();
	}
	
	public Cart findCartById(Long cartId) {
		return cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFound("cart not found !"));
	}
	
	public Cart findCartByUserId(String jwtToken) {
		Customer user = userService.findUserByJwtToken(jwtToken);
		Cart cart = cartRepo.findByCustomerId(user.getId());
		cart.setTotal(calculateCartTotal(cart));
		return cart;
	}
	
	public Cart clearCart(String jwtToken) {
		Cart cart = findCartByUserId(jwtToken);
		cart.getItems().clear();
		
		return cartRepo.save(cart);
	}

}
