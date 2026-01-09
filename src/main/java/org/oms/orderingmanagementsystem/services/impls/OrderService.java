package org.oms.orderingmanagementsystem.services.impls;

import lombok.RequiredArgsConstructor;
import org.oms.orderingmanagementsystem.commons.BaseService;
import org.oms.orderingmanagementsystem.commons.BaseSpecification;
import org.oms.orderingmanagementsystem.commons.OrderFetchSpecification;
import org.oms.orderingmanagementsystem.entities.Order;
import org.oms.orderingmanagementsystem.repositories.OrderRepository;
import org.oms.orderingmanagementsystem.securities.filters.ParameterFilter;
import org.oms.orderingmanagementsystem.services.interfaces.OrderServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService extends BaseService implements OrderServiceInterface {
    private final OrderRepository orderRepository;
    private static final String[] KEYWORD_FIELDS = {
           "status"
    };

    @Override
        public Page<Order> pagination(Map<String, String[]> params) {
            int page = params.containsKey("page") ? Integer.parseInt(params.get("page")[0]) - 1 : 0;
            int size = params.containsKey("perPage") ? Integer.parseInt(params.get("perPage")[0]) : 12;
            String productName = getParam(params, "productName");
            Sort sort = sortParam(params);

            String keyword = ParameterFilter.filtertKeyword(params);
            Map<String, String> filterSimple = ParameterFilter.filterSimple(params);
            Map<String, Map<String, String>> filterComplex = ParameterFilter.filterComplex(params);

            Pageable pageable = PageRequest.of(page, size, sort);
            Specification<Order> specification = Specification.where(
                            BaseSpecification.<Order>keyword(keyword, KEYWORD_FIELDS))
                    .and(BaseSpecification.<Order>whereSpec(filterSimple)
                            .and(BaseSpecification.complexWhereSpec(filterComplex)))
                    .and(OrderFetchSpecification.joinUserFilter(keyword))
                    .and(OrderFetchSpecification.joinProductByName(productName));



            return orderRepository.findAll(specification, pageable);
    }
}
