package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminHomePage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin";
    }


    //Добавление
    @GetMapping("/new")
    public String newUser(Model model,@ModelAttribute("newUser") User user){
        model.addAttribute("roles",roleService.findALL());
        return "new";
    }

    @PostMapping
    public String addUser(@ModelAttribute("newUser") @Valid User user,
                          @RequestParam(value = "roles", required = false) List<Integer> roles,
                          BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return "new";
        }
        List<Role> roles2 = new ArrayList<>();
        for (int id: roles){
            roles2.add(roleService.findOne(id));
        }
        user.setRoles(roles2);
        userService.save(user);
        return  "redirect:/admin";
    }


    //Обновление
    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model){
        model.addAttribute("editUser", userService.findOne(id));
        model.addAttribute("roles", roleService.findALL());
        return "edit";
    }

    @PatchMapping("/{id}")
    public String editUser(@PathVariable("id") int id, @ModelAttribute @Valid User user,
                           @RequestParam(value = "roles", required = false) List<Integer> roles,
                           BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "edit";
        }
        List<Role> roles2 = new ArrayList<>();
        for (int id2: roles){
            roles2.add(roleService.findOne(id2));
        }
        user.setRoles(roles2);
        userService.update(id,user);
        return "redirect:/admin";
    }


    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id){
        userService.delete(id);
        return "redirect:/admin";
    }

}
