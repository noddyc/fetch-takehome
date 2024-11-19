package com.example.fetchtakehome.controller;

import com.example.fetchtakehome.dto.ReceiptDTO;
import com.example.fetchtakehome.service.ReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EndPointController {
    private final ReceiptService receiptService;

    @PostMapping("/receipts/process")
    public ResponseEntity<UUID> postReceiptsProcess(@Valid @RequestBody ReceiptDTO receiptDTO) {
        return new ResponseEntity<>(receiptService.receiptProcessing(receiptDTO), HttpStatus.OK);
    }

    @GetMapping("/receipt/{id}/points")
    public ResponseEntity<String> getPoints(@PathVariable String id){
        UUID uuid;
        try{
            uuid = UUID.fromString(id);
        }catch(IllegalArgumentException ex){
            return new ResponseEntity<>( "No receipt found for that id", HttpStatus.NOT_FOUND);
        }
        Optional<Long> result = receiptService.calculatePoints(uuid);
        if(result.isEmpty()){
            return new ResponseEntity<>( "No receipt found for that id", HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(String.valueOf(result.get()), HttpStatus.OK);
        }
    }
}


