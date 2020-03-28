package net.thumbtack.ptpb.db.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final UserMapper userMapper;

    @Override
    public Optional<User> getUserByName(String name) {
        return userMapper.findById(name);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new LinkedList<>();
        userMapper.findAll().forEach(users::add);
        return users;
    }

    @Override
    public void insertUser(User user) {
        userMapper.save(user);
    }

    @Override
    public void deleteUser(String userName) {
        userMapper.deleteById(userName);
    }

    @Override
    public boolean isRegistered(String userName) {
        return userMapper.existsById(userName);
    }

    @Override
    public void deleteAllUsers() {
        userMapper.deleteAll();
    }
}
