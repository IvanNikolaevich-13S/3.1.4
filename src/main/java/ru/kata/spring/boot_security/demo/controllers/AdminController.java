package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Util.UserValidator;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;
    
    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;


    }

    @GetMapping
    public String adminHomePage(Model model) {
        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("admin", admin);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findALL());

        if (!model.containsAttribute("newUser")) {
            model.addAttribute("newUser", new User());
        }
        return "admin";
    }

    @PostMapping
    public String addUser(Model model,@ModelAttribute("newUser") @Valid User user,
                          BindingResult bindingResult,
                          @RequestParam(value = "roles2", required = false) List<Integer> roles){


        userValidator.validate(user,bindingResult);


        if(bindingResult.hasErrors()) {
            User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            model.addAttribute("admin", admin);
            model.addAttribute("users", userService.findAll());
            model.addAttribute("roles", roleService.findALL());
            model.addAttribute("newUser", user);
            return "admin";
        }

        user.setRoles(roles.stream().map(roleService::findOne).collect(Collectors.toList()));


        userService.save(user);
        return  "redirect:/admin";
    }

    @PatchMapping("/{id}")
    public String editUser(Model model,@PathVariable("id") int id, @ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult,
                           @RequestParam(value = "role1", required = false) List<Integer> roles) {
        if(bindingResult.hasErrors()) {
            return adminHomePage(model);
        }

        user.setRoles(roles.stream().map(roleService::findOne).collect(Collectors.toList()));
        userService.update(id,user);

        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id){
        userService.delete(id);
        return "redirect:/admin";
    }

}
