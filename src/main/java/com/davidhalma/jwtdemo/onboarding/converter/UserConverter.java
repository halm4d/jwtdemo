package com.davidhalma.jwtdemo.onboarding.converter;

import com.davidhalma.jwtdemo.onboarding.model.business.User;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements BaseConverter<User, MDBUser> {

    @Override
    public User to(MDBUser mdbUser) {
        User user = new User();
        user.setId(mdbUser.getId());
        user.setUsername(mdbUser.getUsername());
        user.setPassword(mdbUser.getPassword());
        user.setAuthorities(mdbUser.getAuthorities());
        user.setAccountNonExpired(mdbUser.getIsAccountNonExpired());
        user.setAccountNonLocked(mdbUser.getIsAccountNonLocked());
        user.setCredentialsNonExpired(mdbUser.getIsCredentialsNonExpired());
        user.setEnabled(mdbUser.getIsEnabled());
        return user;
    }

    @Override
    public MDBUser from(User user) {
        MDBUser mdbUser = new MDBUser();
        mdbUser.setId(user.getId());
        mdbUser.setUsername(user.getUsername());
        mdbUser.setPassword(user.getPassword());
        mdbUser.setAuthorities(user.getAuthorities());
        mdbUser.setIsAccountNonExpired(user.isAccountNonExpired());
        mdbUser.setIsAccountNonLocked(user.isAccountNonLocked());
        mdbUser.setIsCredentialsNonExpired(user.isCredentialsNonExpired());
        mdbUser.setIsEnabled(user.isEnabled());
        return mdbUser;
    }

}
