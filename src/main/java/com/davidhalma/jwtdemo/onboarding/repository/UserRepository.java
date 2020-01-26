package com.davidhalma.jwtdemo.onboarding.repository;

import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<MDBUser, String> {

    Optional<MDBUser> findByUsername(String username);

}
