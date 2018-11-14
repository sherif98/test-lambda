package com.aws.codestar.projecttemplates.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;
import com.aws.codestar.projecttemplates.dto.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO {

    private DynamoDBTableMapper<User, String, ?> userMapper;

    public UserDAOImpl() {
        DynamoDBMapper mapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
        userMapper = mapper.newTableMapper(User.class);
    }

    @Override
    public User getUser(String id) {
        return userMapper.load(id);
    }

    @Override
    public void saveUser(User user) {
        userMapper.save(user);
    }
}

