package com.aws.codestar.projecttemplates.handler;

import com.aws.codestar.projecttemplates.dao.UserDAO;
import com.aws.codestar.projecttemplates.dao.UserDAOImpl;
import com.aws.codestar.projecttemplates.dto.User;

public class UserServiceHandler {

    private UserDAO userDAO;

    public UserServiceHandler() {
        userDAO = new UserDAOImpl();
    }

    public void saveUser(User user) {
        userDAO.saveUser(user);
    }
}
