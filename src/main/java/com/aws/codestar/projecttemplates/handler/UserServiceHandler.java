package com.aws.codestar.projecttemplates.handler;

import com.aws.codestar.projecttemplates.dao.UserDAO;
import com.aws.codestar.projecttemplates.dto.User;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceHandler {

    @Autowired
    private UserDAO userDAO;

    public void saveUser(User user) {
        userDAO.saveUser(user);
    }
}
