package com.teerenzo.socialMedia.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
	
	private static List<User> users = new ArrayList<>();
	private static int count= 0;
	
	static {
		users.add(new User(++count,"renzo",LocalDate.now().minusYears(20)));
		users.add(new User(++count,"tee",LocalDate.now().minusYears(20)));
		users.add(new User(++count,"emmanuel",LocalDate.now().minusYears(20)));
	}
	
	public List<User> findAll(){
		return users;
	}
	
	public User findById(int id){
		Predicate<? super User> predicate= user -> user.getId()==id;
		return users.stream().filter(predicate).findFirst().orElse(null);
	}
	
	public User save(User user) {
		user.setId(++count);
		users.add(user);
		return user;
	}
	
	public void deleteById(int id){
		Predicate<? super User> predicate= user -> user.getId()==id;
		users.removeIf(predicate);
	}
	
	
	
	
	

}
