package org.oms.orderingmanagementsystem.services.impls;

import lombok.RequiredArgsConstructor;
import org.oms.orderingmanagementsystem.commons.BaseService;
import org.oms.orderingmanagementsystem.commons.BaseSpecification;
import org.oms.orderingmanagementsystem.commons.OrderFetchSpecification;
import org.oms.orderingmanagementsystem.dtos.response.OrderResponse;
import org.oms.orderingmanagementsystem.entities.Order;
import org.oms.orderingmanagementsystem.mappers.OrderMapper;
import org.oms.orderingmanagementsystem.repositories.OrderRepository;
import org.oms.orderingmanagementsystem.securities.filters.ParameterFilter;
import org.oms.orderingmanagementsystem.services.interfaces.OrderServiceInterface;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService extends BaseService implements OrderServiceInterface {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private static final String[] KEYWORD_FIELDS = {
           "status"
    };
    private static final int MAX_PAGE_SIZE = 100;

    /**
     * FIXED: Changed to return OrderResponse directly
     * Maps within transaction so lazy-loaded relationships are accessible
     * This prevents LazyInitializationException for v1 endpoint
     */
    @Transactional
    @Override
    public Slice<OrderResponse> pagination(Map<String, String[]> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")[0]) - 1 : 0;
        int size = params.containsKey("perPage") ? Integer.parseInt(params.get("perPage")[0]) : 12;

        // FIXED: Enforce max page size
        if (size <= 0) {
            size = 12;
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

        Specification<Order> specification = Specification.where(
                        BaseSpecification.<Order>keyword(keyword, KEYWORD_FIELDS));

        if (keyword != null && !keyword.isBlank()) {
            specification = specification.and(OrderFetchSpecification.joinUserFilter(keyword));
        }

        if (!filterSimple.isEmpty()) {
            specification = specification.and(BaseSpecification.whereSpec(filterSimple));
        }

        if (!filterComplex.isEmpty()) {
            specification = specification.and(BaseSpecification.complexWhereSpec(filterComplex));
        }

        Slice<Order> orders = orderRepository.findAll(specification, pageable);

        // Map to OrderResponse WITHIN transaction so lazy relationships are accessible
        return orderMapper.toPageResponse(orders);
    }
}
