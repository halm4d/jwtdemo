package com.davidhalma.jwtdemo.onboarding.service;

import com.davidhalma.jwtdemo.onboarding.exception.UserAlreadyExistsException;
import com.davidhalma.jwtdemo.onboarding.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.onboarding.model.business.User;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import com.davidhalma.jwtdemo.onboarding.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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


    public MDBUser register(AuthenticationRequest authenticationRequest) {
        checkUserExists(authenticationRequest);
        MDBUser mdbUser = getMdbUser(authenticationRequest);
        return save(mdbUser);
    }

    private MDBUser getMdbUser(AuthenticationRequest authenticationRequest) {
        MDBUser mdbUser = new MDBUser();

        mdbUser.setUsername(authenticationRequest.getUsername());
        mdbUser.setPassword(authenticationRequest.getPassword());

        mdbUser.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(User.RoleEnum.USER.toString())));
        mdbUser.setIsCredentialsNonExpired(true);
        mdbUser.setIsAccountNonExpired(true);
        mdbUser.setIsAccountNonLocked(true);
        mdbUser.setIsEnabled(true);
        return mdbUser;
    }

    private void checkUserExists(AuthenticationRequest authenticationRequest) {
        userRepository.findByUsername(authenticationRequest.getUsername()).ifPresent(mdbUser1 -> {
            throw new UserAlreadyExistsException("User already exists. Username: " + mdbUser1.getUsername());
        });
    }
}
