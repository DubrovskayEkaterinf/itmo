package com.example.itmo.controllers;

import com.example.itmo.service.CarService;
import com.example.itmo.model.dto.request.CarInfoRequest;
import com.example.itmo.model.dto.response.CarInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor

public class CarController {
    private final CarService carService;

    @PostMapping
   @Operation(summary = "Создание автомобиля")
    public CarInfoResponse createCar(@RequestBody CarInfoRequest request){
        return carService.createCar(request);    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение автомобиля")
    public CarInfoResponse getCar(@PathVariable Long id) {
        return carService.getCar(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактирование автомобиля")
    public CarInfoResponse updateCar(@PathVariable Long id, @RequestBody CarInfoRequest request) {
        return carService.updateCar(id, request);}

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление автомобиля")
    public void deletCar(@PathVariable Long id) {
        carService.deletCar(id);
    }


    @PostMapping("/lincCarDriver/{userId}/{carId}")
    @Operation(summary = "Создание вывода автомобилей пользователя")
    public CarInfoResponse lincCarDriver(@PathVariable Long userId, @PathVariable Long carId) {
        return carService.lincCarDriver(userId, carId);
    }


    @PostMapping("/allCars")
    @Operation(summary = "Создание всех автомобилей")
    public Page<CarInfoResponse> getAllCars(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer perPage,
                                            @RequestParam(defaultValue = "brand") String sort,
                                            @RequestParam(defaultValue = "ASС") Sort.Direction order) {
        return carService.getAllCars(page, perPage, sort, order);
    }

    @GetMapping("/allCarsUserId/{userId}")
    @Operation(summary = "Получение всех машин пользователя")
    public Page<CarInfoResponse> getCarsForUserId(Long id,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer perPage,
                                                  @RequestParam(defaultValue = "brand") String sort,
                                                  @RequestParam(defaultValue = "ASС") Sort.Direction order) {


        return carService.getCarsForUserId(id, page, perPage, sort, order);
    }

}
