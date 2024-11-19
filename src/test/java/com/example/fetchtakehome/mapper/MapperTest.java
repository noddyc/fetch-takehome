package com.example.fetchtakehome.mapper;
import com.example.fetchtakehome.dto.ItemDTO;
import com.example.fetchtakehome.dto.ReceiptDTO;
import com.example.fetchtakehome.entity.Item;
import com.example.fetchtakehome.entity.Receipt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class MapperTest {

    @Autowired
    private ItemMapper itemMapperImpl = new ItemMapperImpl();

    @Autowired
    private ReceiptMapper receiptMapperImpl = new ReceiptMapperImpl(itemMapperImpl);

    @Test
    void testItemDTOToEntity_Success() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setShortDescription("Coke");
        itemDTO.setPrice("5.50");
        Item item = itemMapperImpl.toEntity(itemDTO);
        assertNotNull(item);
        assertEquals("Coke", item.getShortDescription());
        assertEquals(new BigDecimal("5.50"), item.getPrice());
    }

    @Test
    void testItemDTOToEntity_NullInput() {
        Item item = itemMapperImpl.toEntity(null);
        assertNull(item);
    }

    @Test
    void testReceiptDTOToEntity_NullReceiptDTO() {
        Receipt receipt = receiptMapperImpl.toEntity(null, System.currentTimeMillis());
        assertNull(receipt);
    }

    @Test
    void testToEntity_Success() throws Exception {
        ReceiptDTO receiptDTO = new ReceiptDTO();
        receiptDTO.setRetailer("Retailer");
        receiptDTO.setPurchaseDate(LocalDate.now());
        receiptDTO.setPurchaseTime(LocalTime.of(15, 30));
        receiptDTO.setTotal("100.00");
        receiptDTO.setItems(List.of(new ItemDTO("Item1", "10.00")));
        long timeCreated = System.currentTimeMillis();
        Receipt receipt = receiptMapperImpl.toEntity(receiptDTO, timeCreated);
        assertNotNull(receipt);
        assertEquals(1, receipt.getItems().size());
        assertEquals("Item1", receipt.getItems().getFirst().getShortDescription());
        assertEquals(timeCreated, receipt.getTimeCreated());
    }
}
