package com.khramovdmitry.repositories;

import com.khramovdmitry.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Dmitry on 12.02.2017.
 */

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findAll();

    User findByEmail(String email);
}
