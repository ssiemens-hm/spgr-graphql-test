package de.mymunch.code.dto;

import io.leangen.graphql.annotations.types.GraphQLUnion;

@GraphQLUnion(name = "UserResult", description = "Result object for users",
    possibleTypes = {UserDto.class, UserRegistrationErrorDto.class})
public interface IUserResult {
}
