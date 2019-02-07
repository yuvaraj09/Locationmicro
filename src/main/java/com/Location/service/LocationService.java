package com.Location.service;


import java.util.List;

import com.Location.model.Location;

public interface LocationService {
	
	Location findById(long id);
	
	
	Location findByName(String name);
	
	void saveUser(Location user);
	
	void updateUser(Location user);
	
	void deleteUserById(long id);

	
	List<Location> findAllUsers();
	
	void deleteAllUsers();
	
	boolean isUserExist(Location user);
	
	
	
}
