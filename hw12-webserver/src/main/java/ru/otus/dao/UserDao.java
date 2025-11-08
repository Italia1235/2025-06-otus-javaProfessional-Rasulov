package ru.otus.dao;

import java.util.Optional;
import ru.otus.crm.model.User;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);
}
