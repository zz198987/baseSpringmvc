package com.zzh.component.service.impl;

import com.zzh.component.dao.UserDao;
import com.zzh.component.service.UserService;
import com.zzh.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao dao;
    @Override
    public User getUser(User user) {
        return dao.getUser(user);
    }
}
