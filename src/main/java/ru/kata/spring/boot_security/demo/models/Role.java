package ru.kata.spring.boot_security.demo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;


    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
