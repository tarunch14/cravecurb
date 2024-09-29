package org.cravecurb.service;

import java.time.LocalDateTime;
import java.util.List;

import org.cravecurb.exception.ResourceNotFound;
import org.cravecurb.model.Customer;
import org.cravecurb.model.Restaurant;
import org.cravecurb.payload.CreateRestaurantRequest;
import org.cravecurb.payload.RestaurantDto;
import org.cravecurb.repository.AddressRepository;
import org.cravecurb.repository.CustomerRepository;
import org.cravecurb.repository.RestaurantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantService {

	private final RestaurantRepository restaurantRepo;
	private final AddressRepository addressRepo;
	private final CustomerRepository userRepo;
	private final UsersService userService;
	private final ModelMapper mapper;
	
	public Restaurant createRestaurant(CreateRestaurantRequest req, String jwtToken) {
		Customer owner = userService.findUserByJwtToken(jwtToken);
		
		addressRepo.save(req.getAddress());
		
		Restaurant rest = mapper.map(req, Restaurant.class);
		rest.setRegistrationDate(LocalDateTime.now());
		rest.setOwner(owner);
		rest.setOpenOrClose(false);
		return restaurantRepo.save(rest);
	}
	
	public Restaurant updateRestaurant(CreateRestaurantRequest req, Long restId, String jwtToken) {
		userService.findUserByJwtToken(jwtToken);
		Restaurant rest = restaurantRepo.findById(restId).orElseThrow(() -> new ResourceNotFound(String.format("restaurant not found with %L", restId) ));
		
		if(req.getCuisineType() != null) rest.setCuisineType(req.getCuisineType());
		if(req.getDescription() != null) rest.setDescription(req.getDescription());
		if(req.getName() != null) rest.setName(req.getName());
		if(req.getOpeningHours() != null) rest.setOpeningHours(req.getOpeningHours());
		
		return restaurantRepo.save(rest);
	}

	public String deleteRestaurant(Long restId, String jwtToken) {
		userService.findUserByJwtToken(jwtToken);
		restaurantRepo.findById(restId).orElseThrow(() -> new ResourceNotFound(String.format("restaurant not found with %L", restId)));
		restaurantRepo.deleteById(restId);
		return "Restaurant deleted successfully !";
	}
	
	public List<Restaurant> getAllRestaurants(String jwtToken){
		userService.findUserByJwtToken(jwtToken);
		return restaurantRepo.findAll();
	}
	
	public List<Restaurant> searchRestaurant(String keyWord, String jwtToken) {
		userService.findUserByJwtToken(jwtToken);
		return restaurantRepo.findBySearchQuery(keyWord);
	}
	
	public Restaurant findRestaurantById(Long restId, String jwtToken) {
		userService.findUserByJwtToken(jwtToken);
		return restaurantRepo.findById(restId).orElseThrow(() -> new ResourceNotFound(String.format("restaurant not found with %L", restId)));
	}
	
	public Restaurant getRestaurantByUserId(String jwtToken) {
		Customer user = userService.findUserByJwtToken(jwtToken);
		Restaurant rest = restaurantRepo.findByOwnerId(user.getId());
		if(rest == null) {
			throw new ResourceNotFound(String.format("restaurant not found with %L", user.getId()));
		}
		return rest;
	}
	
	public RestaurantDto addToFavourite(Long restId, String jwtToken) {
		Customer user = userService.findUserByJwtToken(jwtToken);
		Restaurant rest =  restaurantRepo.findById(restId).orElseThrow(() -> new ResourceNotFound("Restaurant not found !"));
		RestaurantDto dto = mapper.map(rest, RestaurantDto.class);
		
		boolean isFavourite = false;
		List<RestaurantDto> favourites = user.getFavourites();
		isFavourite = favourites.stream().anyMatch((fav) -> fav.getId().equals(restId));
		
		if(isFavourite) {
			favourites.removeIf(fav -> fav.getId().equals(restId));
		} else {
			favourites.add(dto);
		}
		user.setFavourites(favourites);
		userRepo.save(user);
		
		return dto;
	}
	
	public Restaurant updateRestaurantStatus(Long restId, String jwtToken) {
		userService.findUserByJwtToken(jwtToken);
		Restaurant rest =  restaurantRepo.findById(restId).orElseThrow(() -> new ResourceNotFound("Restaurant not found !"));
		rest.setOpenOrClose(! rest.getOpenOrClose());
		return restaurantRepo.save(rest);
	}
	
}
