package com.example.fetchtakehome.mapper;

import com.example.fetchtakehome.dto.ItemDTO;
import com.example.fetchtakehome.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class ItemMapperImpl implements ItemMapper{

    @Override
    public Item toEntity(ItemDTO itemDTO) {
        if(itemDTO == null){
            return null;
        }
        Item item = new Item();
        item.setShortDescription(itemDTO.getShortDescription());
        item.setPrice(new BigDecimal(itemDTO.getPrice()));
        return item;
    }
}
