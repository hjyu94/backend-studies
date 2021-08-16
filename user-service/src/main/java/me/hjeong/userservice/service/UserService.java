package me.hjeong.userservice.service;

import me.hjeong.userservice.dto.UserDto;
import me.hjeong.userservice.repository.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();

    UserDto getUserDetailsByEmail(String username);

}
