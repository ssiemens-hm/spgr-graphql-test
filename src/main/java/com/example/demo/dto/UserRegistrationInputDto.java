package com.example.demo.dto;

import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@GraphQLType(name = "UserRegistration")
public class UserRegistrationInputDto {

    @NotBlank(message = "Name is mandatory")
    private String firstName;

    @Size(min = 3, message = "Name must be at least 3 characters long")
    @NotBlank(message = "Name is mandatory")
    private String lastName;

    @NotBlank(message = "Password is mandatory")
    private String password;
    private String matchingPassword;

    @NotBlank(message = "E-Mail is mandatory")
    private String email;
}
