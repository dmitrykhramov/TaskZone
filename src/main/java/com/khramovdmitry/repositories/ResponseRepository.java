package com.khramovdmitry.repositories;

import com.khramovdmitry.domain.Response;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Dmitry on 13.02.2017.
 */

public interface ResponseRepository extends CrudRepository<Response, Integer> {
}
