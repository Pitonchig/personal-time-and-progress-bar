package net.thumbtack.ptpb.db.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final UserMapper userMapper;

    @Override
    public Optional<User> getUserById(long id) {
        return userMapper.findById(id);
    }

    @Override
    public Optional<User> getUserByName(String name) {
        List<User> users = userMapper.findByName(name);
        if (users.size() > 1) {
            throw new DuplicateKeyException(String.format("More than one user with name: %s", name));
        }
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
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
    public void deleteUser(long id) {
        userMapper.deleteById(id);
    }

    @Override
    public boolean isRegistered(String userName) {
        return userMapper.findByName(userName).size() > 0;
    }

    @Override
    public void deleteAllUsers() {
        userMapper.deleteAll();
    }
}
