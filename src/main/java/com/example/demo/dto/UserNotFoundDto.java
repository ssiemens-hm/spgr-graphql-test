package de.mymunch.code.dto;

import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@GraphQLType(name = "UserNotFound")
public class UserNotFoundDto implements IFindUserResult {
  private ErrorDto error;
}
