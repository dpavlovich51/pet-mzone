package com.mzone.main.registration.repository;

import com.mzone.main.registration.entity.EmailRegistrationCodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.UUID;

public interface EmailRegistrationCodeRepository extends CrudRepository<EmailRegistrationCodeEntity, UUID> {

    Collection<EmailRegistrationCodeEntity> findByEmailAndCode(String email, String code);

    Collection<EmailRegistrationCodeEntity> findByEmail(String email);

}
