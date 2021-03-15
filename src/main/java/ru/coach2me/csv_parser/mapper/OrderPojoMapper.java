package ru.coach2me.csv_parser.mapper;

import org.javamoney.moneta.FastMoney;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.coach2me.csv_parser.Dto.OrderPojoDto;
import ru.coach2me.csv_parser.models.Order;
import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class OrderPojoMapper {

    @Autowired
    private ModelMapper mapper;


    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Order.class, OrderPojoDto.class)
                .addMappings(m -> m.skip(OrderPojoDto::setAmount)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderPojoDto::setId)).setPostConverter(toDtoConverter());

        mapper.createTypeMap(OrderPojoDto.class, Order.class)
                .addMappings(m -> m.skip(Order::setOrderId)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setAmount)).setPostConverter(toEntityConverter());
    }


    public Order toEntity(OrderPojoDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Order.class);
    }
    public OrderPojoDto toDto(Order entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, OrderPojoDto.class);
    }

    
    
    // ---------------- TO_DTO_CONVERTER -----------------
    private Converter<Order, OrderPojoDto> toDtoConverter() {
        return context -> {
            Order source = context.getSource();
            OrderPojoDto destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }
    // своя логика, для пропущенных полей в setupMapper()
    private void mapSpecificFields(Order source, OrderPojoDto destination) {
        destination.setAmount(FastMoney.of(Long.parseLong(source.getAmount()), source.getCurrency()));
        String fixCsvIdField = source.getOrderId().replaceAll("[^0-9]", "");// FIX bug in !csv Id field
        destination.setId(Long.parseLong(fixCsvIdField));
    }



    // ---------------- TO_ENTITY_CONVERTER -----------------
    private Converter<OrderPojoDto, Order> toEntityConverter() {
        return context -> {
            OrderPojoDto source = context.getSource();
            Order destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }
    // своя логика, для пропущенных полей в setupMapper()
    void mapSpecificFields(OrderPojoDto source, Order destination) {
        destination.setOrderId(source.getId().toString());
        destination.setAmount(source.getAmount().getNumber().toString());
        destination.setAmount(source.getAmount().getCurrency().getCurrencyCode());
    }
}