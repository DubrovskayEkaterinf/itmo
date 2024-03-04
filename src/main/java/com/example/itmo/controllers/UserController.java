package com.example.itmo.controllers;

import com.example.itmo.service.UserService;
import com.example.itmo.model.dto.request.UserInfoRequest;
import com.example.itmo.model.dto.response.CarInfoResponse;
import com.example.itmo.model.dto.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Создание пользователя")
    public UserInfoResponse createUser(@RequestBody @Valid UserInfoRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя")
    public UserInfoResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактирование пользователя")
    public UserInfoResponse updateUser(@PathVariable Long id, @RequestBody @Valid UserInfoRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя")
    public void deletUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }


    @GetMapping("/all")
    @Operation(summary = "Создание всех пользователей")
    public Page<UserInfoResponse> getAllUsers(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer perPage,
                                              @RequestParam(defaultValue = "firsName") String sort,
                                              @RequestParam(defaultValue = "ASС") Sort.Direction order) {
        return userService.getAllUsers(page, perPage, sort, order);
    }


    @GetMapping("/allCar")
    @Operation(summary = "Получение всех машин c пользователями")
    public Page<CarInfoResponse> getAllCarsUser(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer perPage,
                                                @RequestParam(defaultValue = "firsName") String sort,
                                                @RequestParam(defaultValue = "ASС") Sort.Direction order) {
        return userService.getAllCarsUser(page, perPage, sort, order);
    }

}
