package com.florenceconsulting.demorest.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.florenceconsulting.demorest.model.User;
import com.florenceconsulting.demorest.repository.UserRepository;
import com.florenceconsulting.demorest.utils.CSVHelper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<User> search(String keyword) {
		if (keyword != null) {
			return userRepository.search(keyword);
		}
		return userRepository.findAll();
	}

	@Override
	public User saveUser(User employee) {
		return userRepository.save(employee);
	}

	@Override
	public User getUserById(long id) {
		Optional<User> optional = userRepository.findById(id);
		User user = null;
		if (optional.isPresent()) {
			user = optional.get();
		} else {
			throw new RuntimeException(" User not found for id :: " + id);
		}
		return user;
	}

	@Override
	public void deleteUserById(long id) {
		this.userRepository.deleteById(id);
	}

	@Override
	public int importCSV(MultipartFile file) {
		int scarti = 0;
		try {
			List<User> users = CSVHelper.userConverter(file.getInputStream());
			for (User user : users) {
				if (userRepository.search(user.getCodiceFiscale()).isEmpty()) {
					userRepository.save(user);
				} else {
					scarti++;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
		return scarti;
	}

}
