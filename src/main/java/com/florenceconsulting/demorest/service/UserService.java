package com.florenceconsulting.demorest.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.florenceconsulting.demorest.model.User;

public interface UserService {

	List<User> getAllUsers();

	User saveUser(User user);

	User getUserById(long id);

	void deleteUserById(long id);

	List<User> search(String keyword);

	int importCSV(MultipartFile file);
}
