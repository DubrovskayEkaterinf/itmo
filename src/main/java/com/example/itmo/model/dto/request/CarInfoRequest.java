package com.example.itmo.model.dto.request;

import com.example.itmo.model.enums.CarColor;
import com.example.itmo.model.enums.CarType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)

public class CarInfoRequest {
        @NotEmpty(message = "brand must be set")

        String brand;
        String model;
        CarColor color;
        Integer year;
        Long price;
        Boolean isNew;
        CarType type;
}
