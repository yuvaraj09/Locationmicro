package com.Location.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.Location.model.Location;



@Service("userService")
public class LocationServiceImpl implements LocationService{
	
	private static final AtomicLong counter = new AtomicLong();
	
	private static List<Location> users;
	
	static{
		users= populateDummyUsers();
	}

	public List<Location> findAllUsers() {
		return users;
	}
	
	public Location findById(long id) {
		for(Location user : users){
			if(user.getUserId() == id){
				return user;
			}
		}
		return null;
	}
	
	public Location findByName(String name) {
		for(Location user : users){
			if(user.getUserName().equalsIgnoreCase(name)){
				return user;
			}
		}
		return null;
	}
	
	public void saveUser(Location user) {
		user.setUserId(counter.incrementAndGet());
		users.add(user);
	}

	public void updateUser(Location user) {
		int index = users.indexOf(user);
		users.set(index, user);
	}

	public void deleteUserById(long id) {
		
		for (Iterator<Location> iterator = users.iterator(); iterator.hasNext(); ) {
		    Location user = iterator.next();
		    if (user.getUserId() == id) {
		        iterator.remove();
		    }
		}
	}

	public boolean isUserExist(Location user) {
		return findByName(user.getUserName())!=null;
	}
	
	public void deleteAllUsers(){
		users.clear();
	}

	private static List<Location> populateDummyUsers(){
		List<Location> users = new ArrayList<Location>();
		users.add(new Location("vignesh",counter.incrementAndGet(),"10.21.19.256","U.s"));
		users.add(new Location("kumar",counter.incrementAndGet(),"10.22.19.256","India"));
		/*users.add(new User("lenin",counter.incrementAndGet(),"10.23.19.256","England"));
		users.add(new User("arvind",counter.incrementAndGet(),"10.24.19.256","europe"));*/
		return users;
	}

	

}
