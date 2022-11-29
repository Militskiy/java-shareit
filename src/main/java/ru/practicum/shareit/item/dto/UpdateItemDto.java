package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Valid
@Value
@Builder
@Jacksonized
public class UpdateItemDto implements Serializable {
    @Pattern(regexp = "^[^ ].*[^ .]$", message = "Name is not valid")
    @Size(min = 1, max = 20)
    @Schema(example = "Item")
    String name;
    @Pattern(regexp = "^[^ ].*[^ .]$", message = "Description is not valid")
    @Size(min = 1, max = 200)
    @Schema(example = "Test item")
    String description;
    @Schema(example = "true")
    Boolean available;
}