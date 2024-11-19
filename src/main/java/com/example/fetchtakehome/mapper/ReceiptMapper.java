package com.example.fetchtakehome.mapper;

import com.example.fetchtakehome.dto.ReceiptDTO;
import com.example.fetchtakehome.entity.Receipt;


public interface ReceiptMapper {

    Receipt toEntity(ReceiptDTO receiptDTO, long timeCreated);
}
