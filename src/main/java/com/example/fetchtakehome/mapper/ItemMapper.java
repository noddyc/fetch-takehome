package com.example.fetchtakehome.mapper;

import com.example.fetchtakehome.dto.ItemDTO;
import com.example.fetchtakehome.entity.Item;


public interface ItemMapper {

    Item toEntity(ItemDTO itemDTO);
}
