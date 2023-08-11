package ru.kata.spring.boot_security.demo.Util;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;


@Component
public class UserValidator implements Validator {
    public final UserService userValidService;

    @Autowired
    public UserValidator(UserService userValidService) {
        this.userValidService = userValidService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
         return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if(userValidService.loadUserByUsername(user.getUsername()) != null){
               errors.rejectValue("email","", "Человек с таким email существует!");
        }
    }
}
