package com.aws.codestar.projecttemplates.dao;

import com.aws.codestar.projecttemplates.dto.User;

public interface UserDAO {

    User getUser(String id);

    void saveUser(User user);
}
