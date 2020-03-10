package net.thumbtack.ptpb.handler.users;

import net.thumbtack.ptpb.db.user.User;
import net.thumbtack.ptpb.db.user.UserDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.users.dto.requests.DeleteUserRequest;
import net.thumbtack.ptpb.handler.users.dto.requests.RegisterUserRequest;
import net.thumbtack.ptpb.handler.users.dto.responses.RegisterUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    private UserDao userDao;

    @Autowired
    public UsersService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public RegisterUserResponse registerUser(RegisterUserRequest request) {
        User user = User.builder()
                .name(request.getLogin())
                .password(request.getPassword())
                .build();
        //TODO: check user
        userDao.insert(user);
        return new RegisterUserResponse();
    }

    public EmptyResponse deleteUser(DeleteUserRequest request, int userId, String cookie) {
        //TODO: check login and password;
        userDao.deleteUser(userId);
        return new EmptyResponse();
    }
}
