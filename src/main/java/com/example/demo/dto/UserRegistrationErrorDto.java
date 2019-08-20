package com.example.demo.dto;

import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@GraphQLType(name = "UserRegistrationError")
public class UserRegistrationErrorDto implements IUserResult {
  private List<ErrorDto> errors;
}
