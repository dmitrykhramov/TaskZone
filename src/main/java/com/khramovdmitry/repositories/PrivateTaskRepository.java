package com.khramovdmitry.repositories;

import com.khramovdmitry.domain.PrivateTask;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Dmitry on 13.02.2017.
 */

public interface PrivateTaskRepository extends CrudRepository<PrivateTask, Integer> {

    List<PrivateTask> findAll(Sort sort);
}
