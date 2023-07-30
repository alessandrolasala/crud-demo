package com.florenceconsulting.demorest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.florenceconsulting.demorest.model.User;
import com.florenceconsulting.demorest.service.UserService;
import com.florenceconsulting.demorest.utils.CSVHelper;

@RestController
@RequestMapping("v1/api")
@ResponseBody
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping(
		value = "/users",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping(
		value = "/users/param={query}",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> search(@PathVariable String query) {
		if (query != null && !query.isEmpty()) {
			return userService.search(query);
		}
		return userService.getAllUsers();
	}

	@GetMapping(
		value = "/users/{id}",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		User user = null;
		try {
			user = userService.getUserById(id);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(user);
	}

	@PostMapping(
		value = "/users",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> saveUser(@RequestBody User user) {
		userService.saveUser(user);
		return ResponseEntity.ok(user);

	}

	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateStudent(@RequestBody User user, @PathVariable long id) {

		User oldUser = null;
		try {
			oldUser = userService.getUserById(id);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

		if (oldUser == null) {
			return ResponseEntity.notFound().build();
		}

		user.setId(id);
		userService.saveUser(user);
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@DeleteMapping(
		value = "/users/{id}",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteUserById(@PathVariable long id) {

		try {
			userService.deleteUserById(id);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok("Utente correttamente cancellato");
	}

	@DeleteMapping(
		value = "/users",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteAll() {
		List<User> list = userService.getAllUsers();
		for (User user : list) {
			userService.deleteUserById(user.getId());
		}
		return ResponseEntity.ok("Utenti correttamente cancellati");
	}

	@PostMapping("/users/upload")
	public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
		if (CSVHelper.hasCSVFormat(file)) {
			try {
				userService.importCSV(file);
				return ResponseEntity.ok(userService.getAllUsers());
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante l'import del csv");
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il file non ha il formato csv");
	}
}
