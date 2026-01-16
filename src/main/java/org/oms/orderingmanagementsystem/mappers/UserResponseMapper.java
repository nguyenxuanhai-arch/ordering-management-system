package org.oms.orderingmanagementsystem.mappers;

import org.mapstruct.Mapper;
import org.oms.orderingmanagementsystem.dtos.response.UserResponse;
import org.oms.orderingmanagementsystem.entities.User;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponse toResponse(User user);
}