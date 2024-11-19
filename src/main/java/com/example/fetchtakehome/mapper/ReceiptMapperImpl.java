package com.example.fetchtakehome.mapper;

import com.example.fetchtakehome.dto.ItemDTO;
import com.example.fetchtakehome.dto.ReceiptDTO;
import com.example.fetchtakehome.entity.Item;
import com.example.fetchtakehome.entity.Receipt;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ReceiptMapperImpl implements ReceiptMapper{

    private final ItemMapper itemMapper;

    @Override
    public Receipt toEntity(ReceiptDTO receiptDTO, long timeCreated) {
        if(receiptDTO == null){
            return null;
        }
        Receipt receipt = new Receipt();
        receipt.setRetailer(receiptDTO.getRetailer());
        receipt.setPurchaseDate(receiptDTO.getPurchaseDate());
        receipt.setPurchaseTime(receiptDTO.getPurchaseTime());
        receipt.setTotal(new BigDecimal(receiptDTO.getTotal()));

        List<Item> items = new ArrayList<>();
        if(!receiptDTO.getItems().isEmpty()){
            for(ItemDTO itemDTO : receiptDTO.getItems()){
                items.add(itemMapper.toEntity(itemDTO));
            }
        }
        receipt.setItems(items);

        receipt.setTimeCreated(timeCreated);

        return receipt;
    }
}

