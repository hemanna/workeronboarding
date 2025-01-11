package com.sbd.common.repository;

import com.sbd.common.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public User findById(int id){
        return find("id", id).firstResult();
    }
   public User findByEmail(String email){
        return find("email",email).firstResult();
   }
    public User findByPhoneNumber(String phoneNumber){
        return find("phoneNumber", phoneNumber).firstResult();  // Use 'phoneNumber' instead of 'phonenumber'
    }

}
