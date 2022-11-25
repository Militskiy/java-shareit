package ru.practicum.shareit.user.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import ru.practicum.shareit.user.annotations.UniqueEmail;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (userRepository == null) {
            return true;
        }
        ExampleMatcher emailMatcher = ExampleMatcher.matchingAny()
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id", "name");
        Example<User> example = Example.of(User.builder().email(email).build(), emailMatcher);
        return email != null && !userRepository.exists(example);
    }
}
