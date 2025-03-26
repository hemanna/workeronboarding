package com.sbd.common.repository;

import com.sbd.common.entity.UserCredentials;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@ApplicationScoped
public class UserCredentialsRepository implements PanacheRepository<UserCredentials> {

    public UserCredentials findByUsername(String username) {
        return find("username", username).firstResult();
    }
    public UserCredentials findByEmployeeId(Integer employeeId) {
        return find("employee.id", employeeId).firstResult();
    }
    // Find User by Username (Email) or Phone Number
    public Optional<UserCredentials> findByUsernameOrPhoneNumber(String identifier) {
        return find(QueryEnum.QUERY_FIND_BY_USERNAME_OR_PHONE.getValue(),
                Parameters.with(QueryEnum.USERNAME.getValue(), identifier)
                        .and(QueryEnum.PHONE_NUMBER.getValue(), identifier))
                .firstResultOptional();
    }



    // Enum for Queries
    @Getter
    @AllArgsConstructor
    private enum QueryEnum {
        QUERY_FIND_BY_USERNAME_OR_PHONE(
                "SELECT u FROM UserCredentials u WHERE u.username = :username OR u.employee.phoneNumber = :phoneNumber"
        ),

        USERNAME("username"),
        PHONE_NUMBER("phoneNumber");

        private final String value;
    }

}
