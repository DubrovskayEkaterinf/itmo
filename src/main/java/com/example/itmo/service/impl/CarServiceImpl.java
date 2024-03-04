package com.example.itmo.service.impl;

import com.example.itmo.service.CarService;
import com.example.itmo.service.UserService;
import com.example.itmo.exceptions.CustomException;
import com.example.itmo.model.db.entity.Car;
import com.example.itmo.model.db.entity.User;
import com.example.itmo.model.db.repository.CarRepo;
import com.example.itmo.model.dto.request.CarInfoRequest;
import com.example.itmo.model.dto.response.CarInfoResponse;
import com.example.itmo.model.dto.response.UserInfoResponse;
import com.example.itmo.model.enums.CarStatus;
import com.example.itmo.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static com.example.itmo.service.impl.UserServiceImpl.ERR_MSG;


@Slf4j
@Service
@RequiredArgsConstructor


public class CarServiceImpl implements CarService {
    private final ObjectMapper mapper;
    private final CarRepo carRepo;
    private final UserService userService;



    @Override
    public CarInfoResponse createCar(CarInfoRequest request) {
        String brand = request.getBrand();

        carRepo.findByBrand(brand)
                .ifPresent(car -> {
                    throw new CustomException("Car alredy exists", HttpStatus.CONFLICT);
                });

        Car car = mapper.convertValue(request, Car.class);
        car.setStatus(CarStatus.CREATED);
        car.setCreatedAt(LocalDateTime.now());
        car = carRepo.save(car);

        return mapper.convertValue(car, CarInfoResponse.class);

    }

    @Override
    public CarInfoResponse getCar(Long id) {

        return mapper.convertValue(getCarDb(id), CarInfoResponse.class);
    }

    public Car getCarDb(Long id) {
        return carRepo.findById(id).orElseThrow(() -> new CustomException(ERR_MSG, HttpStatus.NOT_FOUND));
    }


    @Override
    public CarInfoResponse updateCar(Long id, CarInfoRequest request) {
        Car car = getCarDb(id);
        car.setBrand(request.getBrand() == null ? car.getBrand() : request.getBrand());
        car.setModel(request.getModel() == null ? car.getModel() : request.getModel());
        car.setColor(request.getColor() == null ? car.getColor() : request.getColor());
        car.setYear(request.getYear() == null ? car.getYear() : request.getYear());
        car.setIsNew(request.getIsNew() == null ? car.getIsNew() : request.getIsNew());
        car.setPrice(request.getPrice() == null ? car.getPrice() : request.getPrice());
        car.setType(request.getType() == null ? car.getType() : request.getType());
        car.setStatus(CarStatus.UPDATE);
        car.setUpdatedAt(LocalDateTime.now());
        car = carRepo.save(car);
        return mapper.convertValue(car, CarInfoResponse.class);
    }


    @Override
    public void deletCar(Long id) {
        Car car = getCarDb(id);
        car.setStatus(CarStatus.DELETED);
        car.setUpdatedAt(LocalDateTime.now());
        carRepo.save(car);

    }


    @Override
    public CarInfoResponse lincCarDriver(Long userId, Long carId) {
        Car car = getCarDb(carId);
        User user = userService.getUserDb(userId);
        user.getCars().add(car);
        userService.updateCarList(user);

        car.setUser(user);
        car = carRepo.save(car);

        UserInfoResponse userInfoResponse = mapper.convertValue(user, UserInfoResponse.class);
        CarInfoResponse carInfoResponse = mapper.convertValue(car, CarInfoResponse.class);

        carInfoResponse.setUser(userInfoResponse);
        return carInfoResponse;
    }

    @Override
    public Car updateUserList(Car car) {
        return carRepo.save(car);
    }




    @Override
    public Page<CarInfoResponse> getAllCars(Integer page, Integer perPage, String sort, Direction order) {
        carRepo.findAll()
                .stream()
                .map(car -> {
                    CarInfoResponse carInfoResponse = mapper.convertValue(car, CarInfoResponse.class);
                    carInfoResponse.setUser(mapper.convertValue(car.getUser(), UserInfoResponse.class));
                    return carInfoResponse;
                })
                .collect(Collectors.toList());
        return (Page<CarInfoResponse>) carRepo;
    }




    @Override
    public Page<CarInfoResponse> getCarsForUserId(Long id, Integer page, Integer perPage, String sort, Direction order) {
        User userDb = userService.getUserDb(id);
        Pageable request = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<Car> allByUserId = carRepo.findByUserId(request, id);


        return new PageImpl<>(allByUserId.stream().
                map(car -> mapper.convertValue(car, CarInfoResponse.class))
                .collect(Collectors.toList()));
    }

}









