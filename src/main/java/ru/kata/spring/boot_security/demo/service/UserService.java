package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(@Lazy PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        for (User user:users){
            Hibernate.initialize(user.getRoles());
        }
        return users;
    }

    public User findOne(int id) {
        Optional<User> foundUser = userRepository.findById(id);
        User user = null;
        if (foundUser.isPresent()) {
            user = foundUser.get();
            Hibernate.initialize(user.getRoles());
        }
        return user;
    }

    @Transactional
    public void save(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void update(int id, User updateUser) {
        updateUser.setId(id);
        updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        userRepository.save(updateUser);
    }

    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        Hibernate.initialize(user.getRoles());
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден!");
        }

        return  user;
    }

}
