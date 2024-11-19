package com.example.fetchtakehome.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    @NotNull(message = "Short description can not be null")
    @Pattern(regexp = "^[\\w\\s\\-]+$", message = "Description format is invalid")
    private String shortDescription;

    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Price format is invalid")
    private String price;
}
