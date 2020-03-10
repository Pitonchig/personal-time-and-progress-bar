package net.thumbtack.ptpb.db.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private UserMapper userMapper;

    @Autowired
    public UserDaoImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new LinkedList<>();
        userMapper.findAll().forEach(users::add);
        return users;
    }

    @Override
    public void insert(User user) {
        userMapper.save(user);
    }

    @Override
    public void deleteUser(int userId) {
        userMapper.deleteById(userId);
    }
}
