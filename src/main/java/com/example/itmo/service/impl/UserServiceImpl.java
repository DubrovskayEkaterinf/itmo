package com.example.itmo.service.impl;

import com.example.itmo.service.UserService;
import com.example.itmo.exceptions.CustomException;
import com.example.itmo.model.db.entity.User;
import com.example.itmo.model.db.repository.CarRepo;
import com.example.itmo.model.db.repository.UserRepo;
import com.example.itmo.model.dto.request.UserInfoRequest;
import com.example.itmo.model.dto.response.CarInfoResponse;
import com.example.itmo.model.dto.response.UserInfoResponse;
import com.example.itmo.model.enums.UserStatus;
import com.example.itmo.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {



    private static UserRepo userRepo;
    private final ObjectMapper mapper;
    public static final String ERR_MSG = "User not found";
  private final CarRepo carRepo;


    @Override
    public UserInfoResponse createUser(UserInfoRequest request) {
        String email = request.getEmail();

        if(!EmailValidator.getInstance().isValid(email)) {
            throw new CustomException("Invalid email", HttpStatus.BAD_REQUEST);
        }

        userRepo.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new CustomException("Email already exists", HttpStatus.CONFLICT);
                });


        User user = mapper.convertValue(request, User.class);
        user.setStatus(UserStatus.CREATED);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepo.save(user);

        return mapper.convertValue(user, UserInfoResponse.class);
    }

    @Override
    public UserInfoResponse getUser(Long id) {   //Для поиска пользователя и/или добавления нового

        return mapper.convertValue(getUserDb(id), UserInfoResponse.class);
    }

    @Override
    public User getUserDb(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new CustomException(ERR_MSG, HttpStatus.NOT_FOUND));

    }


    @Override
    public UserInfoResponse updateUser(Long id, UserInfoRequest request) {

        User user = getUserDb(id);
        user.setEmail(request.getEmail() == null ? user.getEmail() : request.getEmail());
        user.setPassword(request.getPassword() == null ? user.getPassword() : request.getPassword());
        user.setFirstName(request.getFirstName() == null ? user.getFirstName() : request.getFirstName());
        user.setLastName(request.getLastName() == null ? user.getLastName() : request.getLastName());
        user.setMiddleName(request.getMiddleName() == null ? user.getMiddleName() : request.getMiddleName());
        user.setAge(request.getAge() == null ? user.getAge() : request.getAge());
        user.setGender(request.getGender() == null ? user.getGender() : request.getGender());
        user.setStatus(UserStatus.UPDATE);
        user.setUpdateAt(LocalDateTime.now());
        user = userRepo.save(user);



        return mapper.convertValue(user, UserInfoResponse.class);
    }



    @Override
    public Page<UserInfoResponse> getAllUsers(Integer page, Integer perPage, String sort, Sort.Direction order) {
        Pageable request = PaginationUtil.getPageRequest(page, perPage, sort, order);

        List<UserInfoResponse> all = userRepo.findAll(request)
                .getContent()
                .stream()
                .map(user -> mapper.convertValue(user, UserInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(all);
   }

        @Override
    public Page<CarInfoResponse> getAllCarsUser(Integer page, Integer perPage, String sort, Sort.Direction order) {
           Pageable request = PaginationUtil.getPageRequest(page, perPage, sort, order);
           List<CarInfoResponse> all = carRepo.findAll(request)
                   .getContent()
                   .stream()
                   .map(car -> mapper.convertValue(car, CarInfoResponse.class))
                   .collect(Collectors.toList());

        return new PageImpl<>(all);
    }



    @Override
    public void deleteUser(Long id) {
        User user = getUserDb(id);
        user.setStatus(UserStatus.DELETED);
        user.setUpdateAt(LocalDateTime.now());
        userRepo.save(user);

    }

    @Override
    public User updateCarList(User user) {
        return userRepo.save(user);
    }



}
