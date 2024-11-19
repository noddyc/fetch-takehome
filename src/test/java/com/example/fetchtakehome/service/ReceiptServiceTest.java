package com.example.fetchtakehome.service;
import com.example.fetchtakehome.dto.ItemDTO;
import com.example.fetchtakehome.dto.ReceiptDTO;
import com.example.fetchtakehome.entity.Item;
import com.example.fetchtakehome.entity.Receipt;
import com.example.fetchtakehome.mapper.ReceiptMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ReceiptServiceTest {

    @Mock
    private ReceiptMapper receiptMapper;

    @InjectMocks
    private ReceiptService receiptService;

    @BeforeEach
    public void initMocks() {
        receiptService.getMap().put(UUID.fromString("0acff307-a5f7-11ef-a3ac-a345b22d6819"),
                new Receipt(
                        "Retailer", LocalDate.now(), LocalTime.of(15, 1),
                        new BigDecimal("12.54"),
                        List.of(new Item("Dorito", new BigDecimal("10.00")),
                                new Item("Coke", new BigDecimal("2.54"))), 1000L));
    }

    @Test
    void testReceiptProcessing_Success() {
        ReceiptDTO receiptDTO = new ReceiptDTO("Retailer", LocalDate.now(), LocalTime.of(13, 1),
                "10.00", List.of(new ItemDTO("Dorito", "10.00")));
        Receipt mockReceipt = new Receipt(
                "Retailer", LocalDate.now(), LocalTime.of(13, 1),
                new BigDecimal("10.00"), List.of(new Item("Dorito", new BigDecimal("10.00"))), 1000L);

        when(receiptMapper.toEntity(eq(receiptDTO), anyLong())).thenReturn(mockReceipt);
        UUID uuid = receiptService.receiptProcessing(receiptDTO);
        assertNotNull(uuid);
        verify(receiptMapper, times(1)).toEntity(any(ReceiptDTO.class), anyLong());
    }

    @Test
    void testCalculatePoints_Failure() {
        Optional<Long> total = receiptService.calculatePoints(UUID.fromString("070acff3-a5f7-11ef-a3ac-a345b22d6819"));
        assert(total.isEmpty());
    }

    @Test
    void testCalculatePoints_Success() {
        Optional<Long> total = receiptService.calculatePoints(UUID.fromString("0acff307-a5f7-11ef-a3ac-a345b22d6819"));
        assert(total.isPresent());
        assert(total.get().equals(31L));
    }
}