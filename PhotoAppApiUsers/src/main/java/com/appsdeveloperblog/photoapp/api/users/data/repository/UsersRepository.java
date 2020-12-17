package com.appsdeveloperblog.photoapp.api.users.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.appsdeveloperblog.photoapp.api.users.data.UserEntity;

public interface UsersRepository extends CrudRepository<UserEntity, Long> {

	// findBy... - jpa will create a SELECT query
	// findByEmail - Email has to be a COLUMN
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
}
