package de.mymunch.code.services;

import de.mymunch.code.dto.IFindUserResult;
import de.mymunch.code.dto.UserDto;
import de.mymunch.code.dto.UserRegistrationDto;
import de.mymunch.code.dto.IUserResult;
import de.mymunch.code.entities.User;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import java.util.List;

public interface IUserService {
  @GraphQLMutation
  IUserResult registerUserAccount(
      @GraphQLArgument(name = "user") UserRegistrationDto user);

  @GraphQLQuery(name = "allUsers", description = "Returns all users")
  List<UserDto> getUsers();

  @GraphQLQuery(name = "getUserByEmail")
  IFindUserResult getUserByEmail(@GraphQLArgument(name = "email") String email);

  void createVerificationToken(User user, String token);
}
