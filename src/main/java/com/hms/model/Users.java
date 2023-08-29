package com.hms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Users {
    @Id
    String username;
    String password;
    String enabled;
}
