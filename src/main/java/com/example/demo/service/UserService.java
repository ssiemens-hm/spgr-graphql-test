package de.mymunch.code.services;

import de.mymunch.code.dto.ErrorDto;
import de.mymunch.code.dto.IFindUserResult;
import de.mymunch.code.dto.UserDto;
import de.mymunch.code.dto.UserNotFoundDto;
import de.mymunch.code.dto.UserRegistrationDto;
import de.mymunch.code.dto.UserRegistrationErrorDto;
import de.mymunch.code.dto.IUserResult;
import de.mymunch.code.entities.User;
import de.mymunch.code.entities.VerificationToken;
import de.mymunch.code.enums.UserRole;
import de.mymunch.code.registration.OnRegistrationCompleteEvent;
import de.mymunch.code.repositories.TokenRepository;
import de.mymunch.code.repositories.UserRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@GraphQLApi
public class UserService implements IUserService {

  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private static final ModelMapper modelMapper = new ModelMapper();
  Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private final ApplicationEventPublisher eventPublisher;

  @Autowired
  public UserService(UserRepository userRepository, ApplicationEventPublisher eventPublisher,
                     TokenRepository tokenRepository) {
    this.userRepository = userRepository;
    this.eventPublisher = eventPublisher;
    this.tokenRepository = tokenRepository;
  }


  @Override
  @GraphQLMutation
  public IUserResult registerUserAccount(
      @GraphQLArgument(name = "user") UserRegistrationDto user) {
    // validate input
    Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(user);
    List<ErrorDto> errors = new ArrayList<>();
    if (violations.size() > 0) {
      for (ConstraintViolation<UserRegistrationDto> violation : violations) {
        errors.add(new ErrorDto(violation.getPropertyPath().toString(), violation.getMessage()));
      }
      return new UserRegistrationErrorDto(errors);
    }

    // unify email address
    user.setEmail(user.getEmail().trim().toLowerCase());

    // check duplicate accounts
    if (emailExist(user.getEmail())) {
      errors.add(new ErrorDto("email", String.format("E-Mail '%s' is already registered",
          user.getEmail())));
      return new UserRegistrationErrorDto(errors);
    }

    try {
      User toRegister = modelMapper.map(user, User.class);
      toRegister.setRoles(Collections.singleton(UserRole.USER));
      User registered = userRepository.save(toRegister);
      // send registration email
      //try {
      eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered));
      //} catch (Exception me){
      //  throw new CouldNotSendRegistrationMail("Internal errror. Please contact the
      //  administrator "
      //      + "of this API.");
      //}

      System.out.println("Convert registred user to UserDTO");
      UserDto toReturn = modelMapper.map(registered, UserDto.class);
      System.out.println("Converted: "+ toReturn.toString());
      return toReturn;
    } catch (Exception ex) {
      errors.add(new ErrorDto("unknown", ex.toString()));
      return new UserRegistrationErrorDto(errors);
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
  public void createVerificationToken(User user, String token) {

    VerificationToken myToken = new VerificationToken();
    myToken.setToken(token);
    myToken.setUser(user);
    tokenRepository.save(myToken);
  }

  private boolean emailExist(String email) {
    Optional<User> user = userRepository.findByEmail(email);
    return user.isPresent();
  }
}
