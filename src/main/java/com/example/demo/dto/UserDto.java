package com.example.demo.dto;

import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@GraphQLType(name = "User")
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements IUserResult, IFindUserResult {

  private Long id;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotBlank
  private String email;
}
