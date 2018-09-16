package com.zzh.component.dao;

import com.zzh.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    public User getUser(User user);
}
