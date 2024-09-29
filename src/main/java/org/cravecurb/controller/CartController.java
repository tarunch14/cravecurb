package org.cravecurb.controller;

import org.cravecurb.model.Cart;
import org.cravecurb.model.CartItem;
import org.cravecurb.payload.AddCartItemRequest;
import org.cravecurb.payload.UpdateCartItemRequest;
import org.cravecurb.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CartController {
	
	private @Autowired CartService cartService;
	
	@PutMapping("/cart/add")
	@ResponseStatus(HttpStatus.OK)
	public CartItem addItemToCart(@RequestBody AddCartItemRequest req, @RequestHeader("Authorization") String jwtToken) {
		return cartService.addItemTocart(req, jwtToken);
	}
	
	@PutMapping("/cart-item/update")
	@ResponseStatus(HttpStatus.OK)
	public CartItem updateCartItemQuantity(@RequestBody UpdateCartItemRequest req, @RequestHeader("Authorization") String jwtToken) {
		return cartService.updateCartItemQuantity(req.getCartItemId(), req.getQuantity());
	}
	
	@DeleteMapping("/cart-item/remove/{_id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Cart removeCartItem(@PathVariable(value = "_id") Long id, @RequestHeader("Authorization") String jwtToken) {
		return cartService.removeItemFromcart(id, jwtToken);
	}
	
	@PutMapping("/cart/clear")
	@ResponseStatus(HttpStatus.OK)
	public Cart updateCartItemQuantity(@RequestHeader("Authorization") String jwtToken) {
		return cartService.clearCart(jwtToken);
	}
	
	@GetMapping("/cart")
	@ResponseStatus(HttpStatus.OK)
	public Cart getUserCart(@RequestHeader("Authorization") String jwtToken) {
		return cartService.findCartByUserId(jwtToken);
	}

}
