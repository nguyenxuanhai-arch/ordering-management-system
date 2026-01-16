package org.oms.orderingmanagementsystem.services.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.oms.orderingmanagementsystem.dtos.response.UserResponse;
import org.oms.orderingmanagementsystem.mappers.UserResponseMapper;
import org.oms.orderingmanagementsystem.repositories.UserRepository;
import org.oms.orderingmanagementsystem.services.interfaces.UserServiceInterface;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userResponseMapper::toResponse)
                .collect(Collectors.toList());
    }
}