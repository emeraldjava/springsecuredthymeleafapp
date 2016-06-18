package com.learning.securedapp.domain;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class User {

    @Id
    private String id;

    @Indexed
    @Size(min=2, max=30)
    private String userName;

    @Size(min=2, max=30)
    private String password;
    
    @NotNull
    private String email;
    
    private String passwordResetToken;

    private boolean enabled = false;

    @DBRef(lazy= true)
    private List<Role> roleList;
}
