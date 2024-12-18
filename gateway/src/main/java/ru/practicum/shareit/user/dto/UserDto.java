package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.annotation.Create;

@Data
public class UserDto {
    private Long id;
    @NotNull(groups = Create.class)
    private String name;
    @NotNull(groups = Create.class)
    @Email(groups = Create.class)
    private String email;
}
