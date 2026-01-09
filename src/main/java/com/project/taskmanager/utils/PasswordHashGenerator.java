package com.project.taskmanager.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("Alice: " + encoder.encode("password123"));
        System.out.println("Bob: " + encoder.encode("password123"));
        System.out.println("Admin: " + encoder.encode("admin123"));
    }
}
