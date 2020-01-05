package com.davidhalma.jwtdemo.user.repository;

import com.davidhalma.jwtdemo.user.model.db.MDBUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<MDBUser, String> {

    Optional<MDBUser> findByUsername(String username);

}
