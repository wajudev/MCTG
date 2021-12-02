package com.example.mctg.user;

import java.util.List;

public interface UserServiceInterface {
    UserInterface addUser(UserInterface user);

    UserInterface getUser(int id);

    UserInterface updateUser(int id, UserInterface user);

    UserInterface getUserWithoutSensibleData(int id);

    UserInterface getUserByUsername(String username);

    UserInterface getUserByUsernameWithoutSensibleData(String username);

    List<UserInterface> getUsers();

    boolean deleteUser(int id);
}
