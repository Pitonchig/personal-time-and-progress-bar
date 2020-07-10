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

    public Optional<User> getUserById(String id) {
        return userMapper.findById(id);
    }

    public Optional<User> getUserByName(String name) throws PtpbException {
        List<User> users = userMapper.findByName(name);
        if (users.size() > 1) {
            throw new PtpbException(ErrorCode.UNKNOWN_ERROR);
        }
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }


    public Optional<User> getUserByNameAndPassword(String name, String password) throws PtpbException {
        //List<User> users = userMapper.findByNameAndPassword(name, password);  //TODO: try to use this method
        List<User> users = userMapper.findByName(name);
        if (users.size() > 1) {
            throw new PtpbException(ErrorCode.UNKNOWN_ERROR);
        }
        boolean isPasswordValid = !users.isEmpty() && users.get(0).getPassword().equals(password); //TODO: remove when findByNameAndPassword method will be used
        return users.isEmpty() || !isPasswordValid ? Optional.empty() : Optional.ofNullable(users.get(0));
    }

    public List<User> getAllUsers() {
        List<User> users = new LinkedList<>();
        userMapper.findAll().forEach(users::add);
        return users;
    }

    public void insertUser(User user) {
        userMapper.save(user);
    }

    public void deleteUser(String id) {
        userMapper.deleteById(id);
    }

    public boolean isRegistered(String userName) {
        return userMapper.findByName(userName).size() > 0;
    }

    public void deleteAllUsers() {
        userMapper.deleteAll();
    }
}
