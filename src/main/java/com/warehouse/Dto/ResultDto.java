package com.warehouse.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ResultDto {

    private int saved;
    private int duplicates;
    private int invalid;
    private List<String> errors;
}