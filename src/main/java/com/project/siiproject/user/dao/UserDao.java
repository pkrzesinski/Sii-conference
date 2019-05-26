package com.project.siiproject.user.dao;

import com.project.siiproject.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
}
