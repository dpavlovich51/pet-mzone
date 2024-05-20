package com.mzone.main.user.repository;

import com.mzone.main.user.entity.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String login);

}
