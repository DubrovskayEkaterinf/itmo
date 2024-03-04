package com.example.itmo.service;

import com.example.itmo.model.db.entity.User;
import com.example.itmo.model.dto.request.UserInfoRequest;
import com.example.itmo.model.dto.response.CarInfoResponse;
import com.example.itmo.model.dto.response.UserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface UserService {
    UserInfoResponse createUser(UserInfoRequest request);

    UserInfoResponse getUser(Long id);

    User getUserDb(Long id);

    UserInfoResponse updateUser(Long id, UserInfoRequest request);

    void deleteUser(Long id);

    User updateCarList(User user);



    Page<UserInfoResponse> getAllUsers(Integer page, Integer perPage, String sort, Sort.Direction order);

    Page<CarInfoResponse> getAllCarsUser(Integer page, Integer perPage, String sort, Sort.Direction order);
}

