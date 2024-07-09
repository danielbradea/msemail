package com.bid90.msemail.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long id;
    String firstName;
    String lastName;
    String email;
    String role;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
}