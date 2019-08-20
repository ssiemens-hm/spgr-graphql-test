package com.example.demo.service;

import com.example.demo.dto.IFindUserResult;
import com.example.demo.dto.IUserResult;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserRegistrationInputDto;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;

import java.util.List;

public interface IUserService {
    @GraphQLMutation
    IUserResult registerUserAccount(
            @GraphQLArgument(name = "user") UserRegistrationInputDto user);

    @GraphQLQuery(name = "getUserByEmail")
    IFindUserResult getUserByEmail(@GraphQLArgument(name = "email") String email);

    @GraphQLQuery(name = "getAllUsers")
    public List<UserDto> getUsers();

}
