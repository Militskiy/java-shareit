package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Valid
@Value
@Builder
@Jacksonized
public class UpdateItemDto implements Serializable {

    @Pattern(regexp = "^[^ ].*[^ .]$", message = "Name cannot be empty")
    @Schema(example = "Item")
    String name;
    @Pattern(regexp = "^[^ ].*[^ .]$", message = "Description cannot be empty")
    @Schema(example = "Test item")
    String description;
    Boolean available;
}