package org.oms.orderingmanagementsystem.services.impls;

import lombok.RequiredArgsConstructor;
import org.oms.orderingmanagementsystem.commons.BaseService;
import org.oms.orderingmanagementsystem.commons.BaseSpecification;
import org.oms.orderingmanagementsystem.commons.UserFethchSpecification;
import org.oms.orderingmanagementsystem.entities.User;
import org.oms.orderingmanagementsystem.mappers.UserMapper;
import org.oms.orderingmanagementsystem.securities.filters.ParameterFilter;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.oms.orderingmanagementsystem.dtos.response.UserResponse;
import org.oms.orderingmanagementsystem.repositories.UserRepository;
import org.oms.orderingmanagementsystem.services.interfaces.UserServiceInterface;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String[] KEYWORD_FIELDS = {
            "name"
    };
    private static final int MAX_PAGE_SIZE = 100;


    @Transactional
    @Override
    public Page<UserResponse> pagination(Map<String, String[]> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")[0]) - 1 : 0;
        int size = params.containsKey("perPage") ? Integer.parseInt(params.get("perPage")[0]) : 20;

        if (size <= 0) {
            size = 20;
        } else if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }

        if (page < 0) {
            page = 0;
        }

        Sort sort = sortParam(params);

        String keyword = ParameterFilter.filtertKeyword(params);
        Map<String, String> filterSimple = ParameterFilter.filterSimple(params);
        Map<String, Map<String, String>> filterComplex = ParameterFilter.filterComplex(params);


        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<User> specification = Specification
                .where(BaseSpecification.keyword(keyword, KEYWORD_FIELDS));

        if (!filterSimple.isEmpty()) {
            specification = specification.and(BaseSpecification.whereSpec(filterSimple));
//
        }

        if (!filterComplex.isEmpty()) {
            specification = specification.and(BaseSpecification.complexWhereSpec(filterComplex));
        }


        Page<User> users = userRepository.findAll(specification, pageable);

        return userMapper.toResponsePage(users);
    }
}