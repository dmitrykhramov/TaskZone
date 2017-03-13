package com.khramovdmitry.services;

import java.util.List;

/**
 * Created by Dmitry on 12.02.2017.
 */

public interface BaseService<T> {

    List<T> listAll();

    T getById(int id);

    T saveOrUpdate(T object);

    void delete(int id);
}
