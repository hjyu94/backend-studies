package me.hjeong.userservice.service;

import me.hjeong.userservice.dto.UserDto;
import me.hjeong.userservice.repository.UserEntity;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();

}
