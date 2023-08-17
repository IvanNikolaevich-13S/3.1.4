package ru.kata.spring.boot_security.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> {



    @Query("select distinct u from User u join fetch u.roles where u.email=:email")
    User findByEmail(@Param("email")String email);

    @Query("select distinct u from User u join fetch u.roles")
    List<User> findAll();
}
