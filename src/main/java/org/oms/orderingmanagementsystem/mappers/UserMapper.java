package org.oms.orderingmanagementsystem.mappers;

import org.mapstruct.Mapper;
import org.oms.orderingmanagementsystem.dtos.response.UserResponse;
import org.oms.orderingmanagementsystem.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
    default Slice<UserResponse> toResponseSlice(Slice<User> users) {
        return users.map(this::toResponse);
    }
    default Page<UserResponse> toResponsePage(Page<User> users) {
        return users.map(this::toResponse);
    }

}
