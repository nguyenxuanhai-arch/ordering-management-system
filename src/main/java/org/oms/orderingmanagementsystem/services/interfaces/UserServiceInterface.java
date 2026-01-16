package org.oms.orderingmanagementsystem.services.interfaces;

import org.oms.orderingmanagementsystem.dtos.response.UserResponse;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface UserServiceInterface {
    @Transactional
    Slice<UserResponse> pagination(Map<String, String[]> params);
}
