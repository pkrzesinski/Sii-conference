package com.project.siiproject.feature.user.dao;

import com.project.siiproject.feature.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(final String email);

    public User findByLogin(final String login);

    public User findByLoginAndEmail(final String login, final String email);
}
