package org.cravecurb.payload;

import org.cravecurb.model.Address;

import lombok.Data;

@Data
public class OrderRequest {
	
	private Long restaurantId;
	
	private Address deliveryAddress;

}
