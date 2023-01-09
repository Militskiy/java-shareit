package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Value
@Builder
@Jacksonized
@Valid
public class ItemRequestCreateDto implements Serializable {
    @NotBlank
    String description;
}