package com.khramovdmitry.services.servicesImpl;

import com.khramovdmitry.domain.User;
import com.khramovdmitry.repositories.UserRepository;
import com.khramovdmitry.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dmitry on 12.02.2017.
 */

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<?> listAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(int id) {
        return userRepository.findOne(id);
    }

    @Override
    public User saveOrUpdate(User object) {
        return userRepository.save(object);
    }

    @Override
    public void delete(int id) {
        userRepository.delete(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
