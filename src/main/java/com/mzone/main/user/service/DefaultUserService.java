package com.mzone.main.user.service;

import com.mzone.main.entity.UserRole;
import com.mzone.main.registration.command.CreateNewAccountCommand;
import com.mzone.main.registration.entity.EmailValue;
import com.mzone.main.registration.event.NewAccountSuccessfullyRegisteredEvent;
import com.mzone.main.security.JwtProvider;
import com.mzone.main.user.entity.*;
import com.mzone.main.user.repository.*;
import lombok.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@AllArgsConstructor
public class DefaultUserService implements UserService, UserDetailsService{

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(EmailValue.create(email)).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public Optional<UserEntity> findByEmail(EmailValue email) {
        return userRepository.findByEmail(email.getValue());
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public NewAccountSuccessfullyRegisteredEvent createAccount(CreateNewAccountCommand command) {
        final UserEntity saved = save(UserEntity.create(
                command.getId(),
                command.getFirstName(),
                command.getLastName(),
                command.getGender(),
                command.getBirthday(),
                command.getEmail(),
                command.getPassword(),
                command.getRoles()
        ));

        return new NewAccountSuccessfullyRegisteredEvent(
                jwtProvider.createToken(saved.getEmail(),
                        Collections.singleton(UserRole.USER)),
                jwtProvider.createRefreshToken(saved.getEmail(),
                        Collections.singleton(UserRole.USER))
        );
    }
}
