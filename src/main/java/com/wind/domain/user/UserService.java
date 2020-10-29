package com.wind.domain.user;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.wind.config.exception.ServerApiException;
import com.wind.dto.UserDto;
import com.wind.module.akka.SpringProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final ActorSystem actorSystem;

    public UserService(UserRepository userRepository, ActorSystem actorSystem) {
        this.userRepository = userRepository;
        this.actorSystem = actorSystem;
    }

    public void tellUserActor(String message) {
        ActorRef actorRef = actorSystem.actorOf(SpringProps.create(actorSystem, UserActor.class));
        actorRef.tell(message, ActorRef.noSender());
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
