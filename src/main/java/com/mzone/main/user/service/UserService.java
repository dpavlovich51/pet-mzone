package com.mzone.main.user.service;

import com.mzone.main.registration.command.CreateNewAccountCommand;
import com.mzone.main.registration.entity.EmailValue;
import com.mzone.main.registration.event.NewAccountSuccessfullyRegisteredEvent;
import com.mzone.main.registration.service.EmailService;
import com.mzone.main.user.entity.*;
import org.springframework.security.core.userdetails.*;

import java.util.*;

public interface UserService {

    Optional<UserEntity> findByEmail(EmailValue email);

    UserEntity save(UserEntity userEntity);

    NewAccountSuccessfullyRegisteredEvent createAccount(CreateNewAccountCommand command);

}
