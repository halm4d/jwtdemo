package com.davidhalma.jwtdemo.user.service;

import com.davidhalma.jwtdemo.user.model.db.MDBUser;
import com.davidhalma.jwtdemo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public MDBUser findByUsername(String username){
        Optional<MDBUser> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            return user.get();
        }else {
            throw new UsernameNotFoundException("User not found. Username: " + username);
        }
    }

    public MDBUser save(MDBUser mdbUser) {
        return userRepository.save(mdbUser);
    }
}
