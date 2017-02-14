package com.khramovdmitry.repositories;

import com.khramovdmitry.domain.PublicTask;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Dmitry on 12.02.2017.
 */

public interface PublicTaskRepository extends CrudRepository<PublicTask, Integer> {

    List<PublicTask> findAll();
}
