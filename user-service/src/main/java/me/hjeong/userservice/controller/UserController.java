package me.hjeong.userservice.controller;

import lombok.RequiredArgsConstructor;
import me.hjeong.userservice.dto.UserDto;
import me.hjeong.userservice.service.UserService;
import me.hjeong.userservice.vo.RequestUser;
import me.hjeong.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/health_check")
    public String status() {
        return "It's working in User Service";
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

}
