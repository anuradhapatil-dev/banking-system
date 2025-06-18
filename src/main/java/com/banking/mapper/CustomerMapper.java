package com.banking.mapper;

import com.banking.dto.CustomerRequestDTO;
import com.banking.dto.CustomerResponseDTO;
import com.banking.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer toEntity(CustomerRequestDTO dto);
    CustomerResponseDTO toDto(Customer entity);
}
