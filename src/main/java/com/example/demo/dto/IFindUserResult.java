package de.mymunch.code.dto;

import io.leangen.graphql.annotations.types.GraphQLUnion;

@GraphQLUnion(name = "FindUserResult", description = "Result object for searched users",
    possibleTypes = {UserDto.class, UserNotFoundDto.class})
public interface IFindUserResult {
}
