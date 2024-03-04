package com.example.itmo.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import com.example.itmo.model.dto.request.UserInfoRequest;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)


public class UserInfoResponse extends UserInfoRequest {
    Long id;
    CarInfoResponse car; {
    }
}
