package com.example.Recipe;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @Column(name = "role_id",nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }
}