package com.example.fetchtakehome.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDTO {
    @Pattern(regexp = "^[\\w\\s\\-&]+$", message = "Retailer name contains invalid characters. Only alphanumeric, spaces, hyphens, and ampersands are allowed.")
    private String retailer;

    @NotNull(message = "Purchase date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    @NotNull(message = "Purchase time cannot be null")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime purchaseTime;

    @NotNull(message = "Total cannot be null")
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Total format is invalid")
    private String total;

    @Valid
    private List<ItemDTO> items;
}
