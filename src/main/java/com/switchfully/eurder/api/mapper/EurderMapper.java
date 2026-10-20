package com.switchfully.eurder.api.mapper;

import com.switchfully.eurder.api.dto.CreateEurderDto;
import com.switchfully.eurder.api.dto.EurderDto;
import com.switchfully.eurder.api.dto.ItemGroupDto;
import com.switchfully.eurder.domain.Eurder;
import com.switchfully.eurder.domain.ItemGroup;
import com.switchfully.eurder.service.ItemGroupRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EurderMapper {

    public List<ItemGroupRequest> toItemGroupRequests(CreateEurderDto dto) {
        return dto.itemGroups().stream()
                .map(line -> new ItemGroupRequest(line.itemId(), line.quantity()))
                .toList();
    }

    public EurderDto toEurderDto(Eurder eurder) {
        List<ItemGroupDto> itemGroupDtos = eurder.getItemGroups().stream()
                .map(itemGroup -> toItemGroupDto(itemGroup))
                .toList();
        return new EurderDto(eurder.getId(), eurder.getCustomerId(), itemGroupDtos, eurder.getTotalPrice());
    }

    private ItemGroupDto toItemGroupDto(ItemGroup itemGroup) {
        return new ItemGroupDto(itemGroup.getItemId(), itemGroup.getQuantity(),
                itemGroup.getPrice(), itemGroup.getShippingDate());
    }
}