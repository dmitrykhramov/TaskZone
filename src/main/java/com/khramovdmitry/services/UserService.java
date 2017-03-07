package com.khramovdmitry.services;

import com.khramovdmitry.domain.User;

import java.util.List;

/**
 * Created by Dmitry on 12.02.2017.
 */

public interface UserService extends BaseService<User> {

    User findByEmail(String email);

    User findByUsername(String username);

    User createUser(User user);

}
