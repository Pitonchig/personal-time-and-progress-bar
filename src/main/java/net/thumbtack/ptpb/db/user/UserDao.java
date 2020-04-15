package net.thumbtack.ptpb.db.user;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.common.ErrorCode;
import net.thumbtack.ptpb.common.PtpbException;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final UserMapper userMapper;

    public Optional<User> getUserById(long id) {
        return userMapper.findById(id);
    }

    public Optional<User> getUserByName(String name) throws PtpbException {
        List<User> users = userMapper.findByName(name);
        if (users.size() > 1) {
            throw new PtpbException(ErrorCode.WRAPPER_TIMEOUT);
        }
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }

    public List<User> getAllUsers() {
        List<User> users = new LinkedList<>();
        userMapper.findAll().forEach(users::add);
        return users;
    }

    public void insertUser(User user) {
        userMapper.save(user);
    }

    public void deleteUser(long id) {
        userMapper.deleteById(id);
    }

    public boolean isRegistered(String userName) {
        return userMapper.findByName(userName).size() > 0;
    }

    public void deleteAllUsers() {
        userMapper.deleteAll();
    }
}