package com.mzone.main.registration.command;

import com.mzone.main.registration.entity.EmailValue;
import lombok.Data;

@Data
public class SendRegisterCodeCommand {
    private final EmailValue email;
}