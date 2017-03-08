package com.khramovdmitry.services.servicesImpl;

import com.khramovdmitry.domain.Response;
import com.khramovdmitry.repositories.ResponseRepository;
import com.khramovdmitry.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dmitry on 06.03.2017.
 */

@Service
public class ResponceServiceImpl implements ResponseService{

    @Autowired
    private ResponseRepository responseRepository;

    @Override
    public List<Response> listAll() {
        return null;
    }

    @Override
    public Response getById(int id) {
        return responseRepository.findOne(id);
    }

    @Override
    public Response saveOrUpdate(Response object) {
        return responseRepository.save(object);
    }

    @Override
    public void delete(int id) {

    }
}
