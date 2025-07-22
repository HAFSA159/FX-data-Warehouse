package com.warehouse.controllers;

import com.warehouse.Dto.ResultDto;
import com.warehouse.services.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/csv/import")
@RequiredArgsConstructor
public class DealController {


    private final DealService dealService;

    @PostMapping
    public void importDeals(@RequestParam("file") MultipartFile file) throws IOException {
        dealService.importCsv(file);

    }
}