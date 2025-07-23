package org.example.accountservice.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class User {

    @NotNull(message ="username must not null")
    @Length(min = 3, max = 20,message = "username length >3 and <20")
    private String username;

    @NotNull(message ="password must not null")
    @Length(min = 6, max = 20  , message = "password length >6 and <20")
    private String password;


}
