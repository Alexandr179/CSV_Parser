package ru.coach2me.csv_parser.Dto;

import lombok.*;
import org.javamoney.moneta.FastMoney;

/**
 * Основные классы в Moneta: Monetary, Money, FastMoney, а также интерфейсы CurrencyUnit и MonetaryAmount......
 * https://alta-systems.ru/java-best-practices/%D0%BE%D1%82-float-%D0%B4%D0%BE-jsr-354-%D0%BA%D0%B0%D0%BA-%D0%BF%D1%80%D0%B0%D0%B2%D0%B8%D0%BB%D1%8C%D0%BD%D0%BE-%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0%D1%82%D1%8C-%D1%81-%D0%B4%D0%B5%D0%BD%D0%B5/
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(callSuper = true)
@Builder
public class OrderPojoDto {

    private Long id;

    private FastMoney amount;

    private String comment;

    @Override
    public String toString() {
        return "OrderPojoDto{" +
                "id=" + id +
                ", amount=" + amount +
                ", comment='" + comment + '\'' +
                '}';
    }
}