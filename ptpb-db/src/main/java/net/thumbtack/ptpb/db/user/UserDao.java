package net.thumbtack.ptpb.db.user;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    void insert(User user);

    void deleteUser(int user);
}
