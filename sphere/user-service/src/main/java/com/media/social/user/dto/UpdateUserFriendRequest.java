package com.media.social.user.dto;

import com.media.social.user.validator.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateUserFriendRequest {
    @ValidEnum(enumClass = Operation.class,message = "invalid operation type")
    private String operation;

    @NotBlank(message = "friend's user id cannot be blank")
    private UUID friendUserId;

}
