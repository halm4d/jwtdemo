package com.davidhalma.jwtdemo.user.model.db;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class MDBUser extends BaseModel{

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private Boolean isEnabled;
}
