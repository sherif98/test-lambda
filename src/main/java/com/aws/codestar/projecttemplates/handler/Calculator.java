package com.aws.codestar.projecttemplates.handler;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;

public class Calculator {
    private DynamoDBTableMapper<User, String, ?> userMapper;


    public Calculator() {
        DynamoDBMapper mapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
        userMapper = mapper.newTableMapper(User.class);
    }
    public int add(Pair pair) {
        User user = new User();
        user.setName("sherif");
        userMapper.save(user);
        return pair.getX() + pair.getY();
    }
}

class Pair {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}