//package com.example.authservice.entity;
//
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.security.core.CredentialsContainer;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//@Document(collection = "users")
//public class MyUser implements UserDetails, CredentialsContainer {
//
//    @Id
//    private String username;
//    private String password;
//    private boolean enabled;
//
//    public MyUser(User user) {
//        this.username = user.getUsername();
//        this.password = user.getPassword();
//        this.enabled = user.isEnabled();
//    }
//
//
//    @Override
//    public void eraseCredentials() {
//        this.password = null;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.emptyList();
//    }
//
//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    public String getUsername() {
//        return this.username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return this.enabled;
//    }
//}
