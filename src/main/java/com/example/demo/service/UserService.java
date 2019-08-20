package com.example.demo.service;

import com.example.demo.dto.ErrorDto;
import com.example.demo.dto.IFindUserResult;
import com.example.demo.dto.IUserResult;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserNotFoundDto;
import com.example.demo.dto.UserRegistrationInputDto;
import com.example.demo.dto.UserRegistrationErrorDto;
import com.example.demo.entities.User;
import com.example.demo.respositories.UserRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@GraphQLApi
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @GraphQLMutation
    public IUserResult registerUserAccount(
            @GraphQLArgument(name = "user") UserRegistrationInputDto user) {

        // validation omitted

        List<ErrorDto> errors = new ArrayList<>();
        //check duplicate email addresses
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            errors.add(new ErrorDto("email", "user already registered with this email address"));
            return new UserRegistrationErrorDto(errors);
        }

        User toRegister = modelMapper.map(user, User.class);
        User registered = userRepository.save(toRegister);
        return modelMapper.map(registered, UserDto.class);
    }


    @Override
    @GraphQLQuery(name = "getUserByEmail")
    public IFindUserResult getUserByEmail(@GraphQLArgument(name = "email") String email) {
        Optional<User> maybeuser = userRepository.findByEmail(email);
        if (maybeuser.isPresent()) {
            UserDto user = modelMapper.map(maybeuser.get(), UserDto.class);
            return user;
        } else {
            return new UserNotFoundDto(new ErrorDto("email", "user does not exist"));
        }
    }

    @Override
    @GraphQLQuery(name = "allUsers", description = "Returns all users")
    public List<UserDto> getUsers() {
        List<User> allusers = userRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();
        return allusers.stream()
                .map(source -> modelMapper.map(source, UserDto.class))
                .collect(Collectors.toList());
    }
}
