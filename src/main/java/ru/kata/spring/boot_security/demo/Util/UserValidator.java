package ru.kata.spring.boot_security.demo.Util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserValidService;

@Component
public class UserValidator implements Validator {
    public final UserValidService userValidService;

    @Autowired
    public UserValidator(UserValidService userValidService) {
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
