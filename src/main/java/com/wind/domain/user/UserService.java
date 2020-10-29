package com.wind.domain.user;

import com.wind.config.exception.ServerApiException;
import com.wind.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity add(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDto.getEmail());
        userEntity.setName(userDto.getName());
        userRepository.save(userEntity);
        return userEntity;
    }

    public UserEntity findByUserId(int uId) {
        throw new ServerApiException("user.error.not_found", uId);
    }

    public Iterable<UserEntity> all() {
        return userRepository.findAll();
    }

}
