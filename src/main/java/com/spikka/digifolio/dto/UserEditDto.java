package com.spikka.digifolio.dto;

import lombok.Data;

@Data
public class UserEditDto {
    private Long id;
    private String email;
    private String password; // если пустой, пароль не меняем
    private String fullName;
    private String groupName;
    private String faculty;
}