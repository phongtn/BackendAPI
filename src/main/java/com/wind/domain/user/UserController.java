package com.wind.domain.user;

import com.wind.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/actor/{message}")
    public void tellUserActor(@PathVariable String message) {
        userService.tellUserActor(message);
    }

    @PostMapping("/add")
    public UserEntity addNewUser(@RequestBody UserDto userDto) {
        return userService.add(userDto);
    }

    @GetMapping("/{userId}")
    public UserEntity addNewUser(@PathVariable int userId) {
        return userService.findByUserId(userId);
    }

    @GetMapping(path = "/all")
    public Iterable<UserEntity> getAllUsers() {
        return userService.all();
    }

}
