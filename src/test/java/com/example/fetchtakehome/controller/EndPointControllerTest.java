package com.example.fetchtakehome.controller;

import com.example.fetchtakehome.dto.ItemDTO;
import com.example.fetchtakehome.dto.ReceiptDTO;
import com.example.fetchtakehome.service.ReceiptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@WebMvcTest(EndPointController.class)
public class EndPointControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReceiptService receiptService;

    @Test
    void testPostReceiptsProcess_InvalidRequest() throws Exception {
        ReceiptDTO invalidReceiptDTO = new ReceiptDTO();
        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReceiptDTO)))
                .andExpect(status().isBadRequest()).andExpect(content().string("The receipt is invalid"));
    }

    @Test
    void testPostReceiptsProcess_ValidRequest() throws Exception {
        ReceiptDTO receiptDTO = new ReceiptDTO("Retailer", LocalDate.now(), LocalTime.of(13, 1),
                "10.00", List.of(new ItemDTO("Dorito", "10.00")));

        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receiptDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPoint_Failure() throws Exception {
        String nonExistingId = "123e4567-e89b-12d3-a456-426614174000";
        when(receiptService.calculatePoints(UUID.fromString(nonExistingId))).thenReturn(Optional.empty());
        mockMvc.perform(get("/receipt/{id}/points", nonExistingId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No receipt found for that id"));
    }


    @Test
    void testGetPoints_Success() throws Exception {
        String existingId = "0acff307-a5f7-11ef-a3ac-a345b22d6819";
        when(receiptService.calculatePoints(UUID.fromString(existingId))).thenReturn(Optional.of(85L));
        mockMvc.perform(get("/receipt/{id}/points", existingId))
                .andExpect(status().isOk()).andExpect(content().string("85"));
    }
}
