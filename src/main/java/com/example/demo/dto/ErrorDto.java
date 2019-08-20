package de.mymunch.code.dto;

import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@GraphQLType
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ErrorDto {
  private final String field;
  private final String reason;
}
