package com.sbd.common.repository;

import com.sbd.common.entity.UserCredentials;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserCredentialsRepository implements PanacheRepository<UserCredentials> {

    public UserCredentials findByUsername(String username) {
        return find("username", username).firstResult();
    }
    public UserCredentials findByEmployeeId(Integer employeeId) {
        return find("employee.id", employeeId).firstResult();
    }

}
