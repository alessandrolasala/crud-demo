package com.florenceconsulting.demorest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.florenceconsulting.demorest.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u"//
			+ " FROM User u"//
			+ " WHERE u.firstName LIKE %?1%" + " OR"//
			+ " u.lastName LIKE %?1%" + " OR"//
			+ " u.codiceFiscale LIKE %?1%" + " OR"//
			+ " u.email LIKE %?1%")
	public List<User> search(String keyword);

}