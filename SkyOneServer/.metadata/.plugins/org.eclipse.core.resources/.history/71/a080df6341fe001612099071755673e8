package com.amaker.dao;

import java.util.ArrayList;

import com.amaker.bean.User;

public interface UserDao {

	//Add a new user
	public void add(User user);
	
	//delete a user
	public void delete(int id);
	
	//Modify a user , the id will never changed ,so find the old-target by id
	public void modify(int id, User newUser);
	
	//Show all the users
	public ArrayList<User> List();
	
	//Search user
	public User searchUser(String phone, String password);
}
