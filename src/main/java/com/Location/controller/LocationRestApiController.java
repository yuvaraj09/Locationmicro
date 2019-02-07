package com.Location.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.Location.model.Location;
import com.Location.service.LocationService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class LocationRestApiController {

	public static final Logger logger = LoggerFactory.getLogger(LocationRestApiController.class);

	@Autowired
	LocationService userService;

	// -------------------Retrieve All Users---------------------------------------------

	@RequestMapping(value = "/location", method = RequestMethod.GET)
	@HystrixCommand(fallbackMethod = "getDataFallBack1",commandKey = "GET_REQ-LOCATIONSERVICES-LISTINGALLUSERS")
	public ResponseEntity<List<Location>> listAllUsers()  {
		List<Location> users = userService.findAllUsers();
		logger.info("get all location"+users);
		if (users.isEmpty()) {
			
			return new ResponseEntity(getDataFallBack1(),HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		System.out.println("in service end point");
		//RestTemplate template=new RestTemplate();
		
		/*ResponseEntity<Employee> list=restTemplate.getForEntity("https://localhost:8080/"+employee.getName(), Employee.class);
		location.setUserName(list.getBody());*/
		
		return new ResponseEntity<List<Location>>(users, HttpStatus.OK);
	}
	
	
	//----------------fallback method for retrieve all users------------------------------
	public ResponseEntity<List<Location>> getDataFallBack1(){
		List li =new ArrayList<Location>();
		Location user=new Location();
		user.setUserName("Default_name");
		user.setUserId(1L);
		user.setIpAddress("0.0.0.0");
		user.setAddress("Country-default");
		li.add(user);
		return new ResponseEntity<List<Location>>(li, HttpStatus.OK);
	}

	
	
	// -------------------Retrieve Single User------------------------------------------

	@RequestMapping(value = "/location/{id}", method = RequestMethod.GET)
	@HystrixCommand(fallbackMethod = "getDataFallBack2",commandKey = "GET_REQ-LOCATIONSERVICES-LISTINGUSER")
	public ResponseEntity<?> getUser(@PathVariable("id") long id) {
		logger.info("Fetching User with id {}", id);
		Location user = userService.findById(id);
		if (user == null) {
			logger.error("User with id {} not found.", id);
			return new ResponseEntity( getDataFallBack2(id),HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Location>(user, HttpStatus.OK);
	}

	// -------------------Fall back method Retrieve Single User------------------------------------------
	public ResponseEntity<?> getDataFallBack2(long id) {
		
		Location user=new Location();
		user.setUserId(0L);
		user.setAddress("Default_Country");
		user.setIpAddress("0.0.0.0");
		user.setUserName("Default");
		return new ResponseEntity<Location>(user, HttpStatus.OK);
	}
	
	
	// -------------------Create a User-------------------------------------------

	@RequestMapping(value = "/location", method = RequestMethod.POST)
	@HystrixCommand(fallbackMethod = "getDataFallBackCreateUser",commandKey = "POST_REQ-LOCATIONSERVICES-CREATEUSERS")
	public ResponseEntity<?> createUser(@RequestBody Location user, UriComponentsBuilder ucBuilder) {
		logger.info("Creating User : {}", user);

		if (userService.isUserExist(user)) {
			logger.error("Unable to create. A User with name {} already exist", user.getUserName());
			return new ResponseEntity(getDataFallBackCreateUser(user,ucBuilder),HttpStatus.CONFLICT);
		}
		userService.saveUser(user);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getUserId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	//--------------------fallback method for createuser-----------------------------------------
	public ResponseEntity<?> getDataFallBackCreateUser( Location user, UriComponentsBuilder ucBuilder) {
		Location user1=new Location();
		user1.setUserId(0L);
		user1.setAddress("Default_Country");
		user1.setIpAddress("0.0.0.0");
		user1.setUserName("Default");
		return new ResponseEntity<Location>(user1, HttpStatus.OK);
		
	}
	

	// ------------------- Update a User ------------------------------------------------

	@RequestMapping(value = "/location/{id}", method = RequestMethod.PUT)
	@HystrixCommand(fallbackMethod = "getDataFallBackupdateUser",commandKey = "PUT_REQ-LOCATIONSERVICES")
	public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody Location user) {
		logger.info("Updating User with id {}", id);

		Location currentUser = userService.findById(id);

		if (currentUser == null) {
			logger.error("Unable to update. User with id {} not found.", id);
			return new ResponseEntity(getDataFallBackupdateUser(id,user),
					HttpStatus.NOT_FOUND);
		}

		currentUser.setUserName(user.getUserName());
		currentUser.setUserId(user.getUserId());
		currentUser.setIpAddress(user.getIpAddress());
		currentUser.setAddress(user.getAddress());

		userService.updateUser(currentUser);
		return new ResponseEntity<Location>(currentUser, HttpStatus.OK);
	}
	
	//--------------------Fall back method for Updateuser-------------------------------------
	public ResponseEntity<?> getDataFallBackupdateUser( long id, @RequestBody Location user) {
		Location user1=new Location();
		user1.setUserId(0L);
		user1.setAddress("Default_Country");
		user1.setIpAddress("0.0.0.0");
		user1.setUserName("Default");
		return new ResponseEntity<Location>(user1, HttpStatus.OK);
		
	}
	

	// ------------------- Delete a User-----------------------------------------

	@RequestMapping(value = "/location/{id}", method = RequestMethod.DELETE)
	@HystrixCommand(fallbackMethod = "getDataFallBackdeleteUser",commandKey = "DELETE_REQ-LOCATIONSERVICES")
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting User with id {}", id);

		Location user = userService.findById(id);
		if (user == null) {
			logger.error("Unable to delete. User with id {} not found.", id);
			return new ResponseEntity(getDataFallBackdeleteUser(id),
					HttpStatus.NOT_FOUND);
		}
		userService.deleteUserById(id);
		 
		return new ResponseEntity<Location>(HttpStatus.NO_CONTENT);
	}
	
	//-------------fallback for delete user------------------------------------------------
	
	public ResponseEntity<?> getDataFallBackdeleteUser(long id) {
		Location user1=new Location();
		user1.setUserId(0L);
		user1.setAddress("Default_Country");
		user1.setIpAddress("0.0.0.0");
		user1.setUserName("Default");
		return new ResponseEntity<Location>(user1, HttpStatus.OK);
		
	}
	

	// ------------------- Delete All Users-----------------------------

	@RequestMapping(value = "/location", method = RequestMethod.DELETE)
	@HystrixCommand(fallbackMethod = "getDataFallBackdeleteAllUser",commandKey = "DElETE_ALL_REQ-LOCATIONSERVICES")
	public ResponseEntity<Location> deleteAllUsers() {
		logger.info("Deleting All Users");

		userService.deleteAllUsers();
		return new ResponseEntity<Location>( HttpStatus.OK);
	}
	
	//-----------------fall back for delete all users----------------------
	public ResponseEntity<Location> getDataFallBackdeleteAllUser() {
		Location user1=new Location();
		user1.setUserId(0L);
		user1.setAddress("Default_Country");
		user1.setIpAddress("0.0.0.0");
		user1.setUserName("Default");
		return new ResponseEntity<Location>( HttpStatus.OK);
		
	}
	

	

}