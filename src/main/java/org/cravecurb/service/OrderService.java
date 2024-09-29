package org.cravecurb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.cravecurb.common.ApplicationConstants;
import org.cravecurb.exception.ResourceNotFound;
import org.cravecurb.model.Address;
import org.cravecurb.model.Cart;
import org.cravecurb.model.CartItem;
import org.cravecurb.model.Customer;
import org.cravecurb.model.CustomerReview;
import org.cravecurb.model.Order;
import org.cravecurb.model.OrderItem;
import org.cravecurb.model.Restaurant;
import org.cravecurb.payload.OrderRequest;
import org.cravecurb.repository.AddressRepository;
import org.cravecurb.repository.CustomerRepository;
import org.cravecurb.repository.FeedbackRepository;
import org.cravecurb.repository.OrderItemRepository;
import org.cravecurb.repository.OrderRepository;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final ObjectMapper mapper;
	private final RuntimeService runtimeService;
	
	private final OrderRepository orderRepo;
	private final OrderItemRepository orderItemRepo;
	private final AddressRepository addressRepo;
	private final CustomerRepository userRepo;
	private final FeedbackRepository feedbackRepo;
	private final RestaurantService restaurantService;
	private final CartService cartService;
//	private final OrderCreationTaskListener orderCreationTaskListner;
//	private final RestaurantPreparationTaskListener restaurantPreparationTaskListener;
	private final TaskService taskService;
	
	public String createOrder(OrderRequest order, Customer user, String jwtToken) { 
		Address deliveryAddress = order.getDeliveryAddress();
		Address savedAddress = addressRepo.save(deliveryAddress);
		
		if(! user.getAddresses().contains(savedAddress)) {
			user.getAddresses().add(savedAddress);
			userRepo.save(user);
		}
		
		Restaurant restaurant = restaurantService.findRestaurantById(order.getRestaurantId(), jwtToken);
		
		Order createdOrder = new Order();
		createdOrder.setCustomer(user);
		createdOrder.setCreatedAt(LocalDateTime.now());
		createdOrder.setOrderStatus("PENDING");
		createdOrder.setDeliveryAddress(savedAddress);
		createdOrder.setRestaurant(restaurant);
		
		Cart cart = cartService.findCartByUserId(jwtToken);
		
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for(CartItem cartItem : cart.getItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setFood(cartItem.getFood());
			orderItem.setIngredients(cartItem.getIngredients());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setTotalPrice(cartItem.getTotalPrice());
			
			OrderItem savedOrderItem = orderItemRepo.save(orderItem);
			orderItems.add(savedOrderItem);;
		}
		Long totalPrice = cartService.calculateCartTotal(cart);
		
		createdOrder.setOrderItems(orderItems);
		createdOrder.setTotalAmount(totalPrice);
		createdOrder.setOrderStatus("Order Placed");
		
		Order savedOrder = orderRepo.save(createdOrder);
		
		Map<String, Object> map = mapper.convertValue(savedOrder, Map.class);
//		map.put("orderCreationTaskListener", orderCreationTaskListner);  //check this for error Unknown property used in expression: ${orderCreationTaskListener}	
		restaurant.getOrders().add(savedOrder);
		
		return createAndSubmitOrder(map);
		
	}
	
	private String createAndSubmitOrder(Map<String, Object> mp) {
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(ApplicationConstants.ORDER_PROCESS , mp);
		Task currentTask = taskService.createTaskQuery()
				                      .processInstanceId(instance.getId())
				                      .singleResult();
//		Map<String, Object> variables = new HashMap<String, Object>();
//		variables.put("actionPerformedAtRestaurantPhase", "ACCEPTED");
		taskService.complete(currentTask.getId());
		
		return instance.getId();
	}
	
	public Order updateOrder(Long orderId, String orderStatus) {
		Order order = findOrderById(orderId);
		if (orderStatus.equals("OUT_FOR_DELIVERY") || orderStatus.equals("OUT_FOR_DELIVERY")
				|| orderStatus.equals("COMPLETED") || orderStatus.equals("PENDING")) {
			order.setOrderStatus(orderStatus);
			return orderRepo.save(order);
		}
		
		throw new ResourceNotFound("please select a valid order status !");
	}
	
	public void cancelOrder(Long orderId) {
		Order order = findOrderById(orderId);
		orderRepo.deleteById(orderId);
	}
	
	public List<Order> getUsersOrder(Long userId) {
		return orderRepo.findByCustomerId(userId);
	}
	
	public List<Order> getRestautantsOrder(Long restId, String orderStatus) {
		List<Order> orders = orderRepo.findByRestaurantId(restId);
		if(orderStatus != null)
		    return orders.stream().filter(order -> order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
		return orders;
	}

	public Order findOrderById(Long orderId) {
		return orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFound("Order not found !"));
    }
	
	public String submitFeedback(CustomerReview feedback) {
		Customer user = userRepo.findById(feedback.getCustomerId()).orElseThrow(() -> new ResourceNotFound("Customer not found !"));
		Order order = findOrderById(feedback.getOrderId());
		
		if(user == null || order == null) return "";
		
		feedbackRepo.save(feedback);
		return "Thanks for submitting feedback";
	}

}
