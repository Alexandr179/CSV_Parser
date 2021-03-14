package ru.coach2me.csv_parser.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderTo {

    private Long id;

    private MonetaryAmount amount;

    private String currency;

    private String comment;
}