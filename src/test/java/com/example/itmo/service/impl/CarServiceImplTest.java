package com.example.itmo.service.impl;


import com.example.itmo.service.UserService;
import com.example.itmo.exceptions.CustomException;
import com.example.itmo.model.db.entity.Car;
import com.example.itmo.model.db.entity.User;
import com.example.itmo.model.db.repository.CarRepo;
import com.example.itmo.model.dto.request.CarInfoRequest;
import com.example.itmo.model.dto.response.CarInfoResponse;
import com.example.itmo.model.enums.CarStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {
    @InjectMocks
    private CarServiceImpl carService;
    @Mock
    private CarRepo carRepo;
    @Mock
    private UserService userService;
    @Spy
    private ObjectMapper mapper;
    private Object page;


    @Test
    public void createCar() {
        CarInfoRequest request = new CarInfoRequest();
        request.setBrand("Brand");

        Car car = new Car();
        car.setId(1L);

        when(carRepo.save(any(Car.class))).thenReturn(car);
        CarInfoResponse result = carService.createCar(request);
        assertEquals(Long.valueOf(1L), result.getId());

    }
    @Test(expected = CustomException.class)
    public void createCarExists() {
        CarInfoRequest request = new CarInfoRequest();
        request.setBrand("Brand");

        Car car = new Car();
        car.setId(1L);

        when(carRepo.findByBrand(anyString())).thenReturn(Optional.of(car));

        carService.createCar(request);

    }


    @Test
    public void getCar() {
    }

    @Test
    public void getCarDb() {
    }

    @Test
    public void updateCar() {
        CarInfoRequest request = new CarInfoRequest();
        request.setYear(2012);

        Car car = new Car();
        car.setId(1L);
        car.setYear(2010);
        car.setBrand("BMW");

        when(carRepo.findById(car.getId())).thenReturn(Optional.of(car));
        when(carRepo.save(any(Car.class))).thenReturn(car);

        CarInfoResponse result = carService.updateCar(car.getId(), request);
        assertEquals(car.getBrand(), result.getBrand());
        assertEquals(request.getYear(), result.getYear());
    }

    @Test
    public void deletCar() {
        Car car = new Car();
        car.setId(1L);

        when(carRepo.findById(car.getId())).thenReturn(Optional.of(car));
        carService.deletCar(car.getId());
        verify(carRepo, times(1)).save(any(Car.class));
        assertEquals(CarStatus.DELETED, car.getStatus());


    }

    @Test
    public void lincCarDriver() {
    }

    @Test
    public void updateUserList() {
    }

    @Test
    public void getAllCars() {
    }

    @Test
    public void getCarsForUserId() {
        User user = new User();
        user.setId(1L);

        Pageable pageable = mock(Pageable.class);

        when(userService.getUserDb(anyLong())).thenReturn(user);
        List<Car> cars = new ArrayList<>();
        when(carRepo.findByUserId(pageable, user.getId())).thenReturn(new PageImpl<>(cars));

        List<Long> ids = cars.stream()
                        .map(Car::getId)
                                .collect(Collectors.toList());

        Page<CarInfoResponse> brand = carService.getCarsForUserId(1L, 1, 10, "Brand", Sort.Direction.ASC);
        List<Long> respIds = brand.getContent().stream().map(CarInfoResponse::getId).collect(Collectors.toList());

        assertEquals(ids, respIds);





    }
}