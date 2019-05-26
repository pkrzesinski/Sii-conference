package com.project.siiproject.feature.user.dao;

import com.project.siiproject.feature.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
}
