package org.example.accountservice.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class User {

    @NotNull
    @Length(min = 3, max = 20)
    private String userName;

    @NotNull
    @Length(min = 6, max = 20)
    private String password;


}
