package com.sbd.common.repository;

import com.sbd.common.entity.UserCredentials;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class UserRepository implements PanacheRepository<UserCredentials> {
    public UserCredentials findById(int id){
        return find("id", id).firstResult();
    }
    public UserCredentials findByUsername(String username) {
        return find("username", username).firstResult();
    }
}
