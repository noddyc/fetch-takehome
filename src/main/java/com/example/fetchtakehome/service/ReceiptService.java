package com.example.fetchtakehome.service;

import com.example.fetchtakehome.dto.ReceiptDTO;
import com.example.fetchtakehome.entity.Item;
import com.example.fetchtakehome.entity.Receipt;
import com.example.fetchtakehome.exception.DuplicateReceiptException;
import com.example.fetchtakehome.mapper.ReceiptMapper;
import com.fasterxml.uuid.Generators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ReceiptService {

    private final ConcurrentHashMap<UUID, Receipt> map = new ConcurrentHashMap<>();

    private final ReceiptMapper receiptMapper;

    public ReceiptService(ReceiptMapper receiptMapper) {
        this.receiptMapper = receiptMapper;
    }

    public UUID receiptProcessing(ReceiptDTO receiptDTO){
        UUID uuid = Generators.timeBasedGenerator().generate();
        long timeCreated = uuid.timestamp();
        Receipt receipt = receiptMapper.toEntity(receiptDTO, timeCreated);
        Receipt result = map.put(uuid, receipt);
        if(result != null){
            throw new DuplicateReceiptException("Error occurs, Please try again");
        }
        return uuid;
    }

    public Optional<Long> calculatePoints(UUID uuid){
        if(!map.containsKey(uuid)){
            return Optional.empty();
        }
        Receipt receipt = map.get(uuid);
        long totalPoints  = 0;
        totalPoints += calculateAlphanumericChar(receipt.getRetailer());
        totalPoints += isRoundDollar(receipt.getTotal()) ? 50:0;
        totalPoints += isMultipleOfQuarter(receipt.getTotal()) ? 25:0;
        totalPoints += (receipt.getItems().size() / 2) * 5L;
        totalPoints += calculateDescriptionPoints(receipt.getItems());
        totalPoints += isOddDate(receipt.getPurchaseDate()) ? 6:0;
        totalPoints += isBetween2And4(receipt.getPurchaseTime()) ? 10:0;
        return Optional.of(totalPoints);
    }

    private long calculateAlphanumericChar(String retailer){
        long count = 0L;
        for(int i = 0; i < retailer.length(); i++){
            char ch = retailer.charAt(i);
            if(Character.isLetterOrDigit(ch)){
                count++;
            }
        }
        return count;
    }

    private long calculateDescriptionPoints(List<Item> items){
        long count = 0L;
        BigDecimal multiplier = new BigDecimal("0.2");
        for(Item item : items){
            int itemDescriptionLength = item.getShortDescription().trim().length();
            if(itemDescriptionLength % 3 == 0){
                BigDecimal result = item.getPrice().multiply(multiplier).setScale(0, RoundingMode.CEILING);
                count += result.longValue();
            }
        }
        return count;
    }

    private boolean isRoundDollar(BigDecimal total){
        return total.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0;
    }

    private boolean isMultipleOfQuarter(BigDecimal total){
        BigDecimal quarter = new BigDecimal("0.25");
        BigDecimal remainder = total.remainder(quarter);
        return remainder.compareTo(BigDecimal.ZERO) == 0;
    }

    private boolean isOddDate(LocalDate date){
        int dayOfMonth = date.getDayOfMonth();
        return dayOfMonth % 2 != 0;
    }

    private boolean isBetween2And4(LocalTime time){
        LocalTime start = LocalTime.of(14, 0);
        LocalTime end = LocalTime.of(16, 0);
        return !time.isBefore(start) && time.isBefore(end);
    }

    public ConcurrentHashMap<UUID, Receipt> getMap() {
        return map;
    }
}
