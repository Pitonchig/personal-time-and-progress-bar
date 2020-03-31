package net.thumbtack.ptpb.db.user;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> getUserById(long id);

    Optional<User> getUserByName(String name);

    List<User> getAllUsers();

    void insertUser(User user);

    void deleteUser(long id);

    boolean isRegistered(String userName);

    void deleteAllUsers();
}
