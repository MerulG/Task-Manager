package com.project.taskmanager.repository;

import com.project.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//Spring generates SQL and persistence logic automatically.
public interface UserRepository extends JpaRepository<User, Integer> {
}