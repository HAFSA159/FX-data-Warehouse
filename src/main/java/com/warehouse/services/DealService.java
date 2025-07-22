package com.warehouse.services;


import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.warehouse.Dto.DealsDto;
import com.warehouse.Dto.ResultDto;
import com.warehouse.models.Deal;
import com.warehouse.repository.DealRepository;
import com.warehouse.validation.DealValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class DealService {


    private final DealRepository dealRepository;
    private final DealValidator validator;

    public ResultDto importCsv(MultipartFile file) throws IOException {


        int successCount = 0, duplicateCount = 0, invalidCount = 0;
        List<String> errorMessages = new ArrayList<>();

        if (file.isEmpty()) {
            log.warn("File is empty");
            throw new IllegalArgumentException("File is empty");
        }


        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();

        if (contentType == null || (!contentType.equals("text/csv") && !contentType.equals("application/vnd.ms-excel"))
                || (fileName != null && !fileName.toLowerCase().endsWith(".csv"))) {
            log.warn("Only CSV files are allowed");
            throw new IllegalArgumentException("Invalid file type. Only CSV files are allowed");
        }

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CsvToBean<DealsDto> csvToBean = new CsvToBeanBuilder<DealsDto>(reader)
                    .withType(DealsDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();


            List<DealsDto> dtoList;

            try {
                dtoList = csvToBean.parse();
            } catch (RuntimeException e) {
                log.error("Error parsing CSV file", e);
                throw new IllegalArgumentException("Invalid CSV format" + e.getMessage(), e);
            }

            Set<String> appearedID = new HashSet<>();
            for (DealsDto dto : dtoList) {
                String reason = validator.validate(Deal.toEntity(dto));

                if (!appearedID.add(dto.getDealId())) {
                    errorMessages.add("Duplicate deal in file [" + dto.getDealId() + "] ignored.");
                    duplicateCount++;
                    continue;
                }

                if (reason != null) {
                    errorMessages.add("Invalid deal [" + dto.getDealId() + "]: " + reason);
                    invalidCount++;
                    continue;
                }

                if (dealRepository.existsByDealId(dto.getDealId())) {
                    errorMessages.add("Duplicate deal [" + dto.getDealId() + "] ignored.");
                    duplicateCount++;
                    continue;
                }

                Deal deal = Deal.toEntity(dto);
                dealRepository.save(deal);
                successCount++;
            }

            log.info("CSV import completed: {} saved, {} duplicates, {} invalid.", successCount, duplicateCount, invalidCount);

            return ResultDto.builder().
                    saved(successCount).
                    duplicates(duplicateCount).
                    errors(errorMessages).
                    invalid(invalidCount).
                    build();


        }
    }

}