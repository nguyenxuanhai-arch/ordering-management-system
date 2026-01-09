package org.oms.orderingmanagementsystem.commons;

import org.oms.orderingmanagementsystem.securities.filters.ParameterFilter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BaseService {
    protected Sort createSort(String sortParam) {
        if (sortParam == null || sortParam.isEmpty()) {
            return Sort.by(Sort.Order.asc("id"));
        }
        String[] parts = sortParam.split(",");
        String field = parts[0];
        String sortDirection = (parts.length > 1) ? parts[1] : "asc";

        if ("desc".equalsIgnoreCase(sortDirection)) {
            return Sort.by(Sort.Order.desc(field));
        } else {
            return Sort.by(Sort.Order.asc(field));
        }
    }

    protected String getParam(Map<String, String[]> params, String key) {
        if (!params.containsKey(key)) return null;
        String[] values = params.get(key);
        if (values == null || values.length == 0) return null;
        return values[0].trim();
    }


    protected Sort sortParam(Map<String, String[]> parameters) {
        String sortParam = parameters.containsKey("sort") ? parameters.get("sort")[0] : null;
        return createSort(sortParam);
    }

    protected <T> Specification<T> specificationParam(Map<String, String[]> parameters, String[] searchKey) {
        String keyword = ParameterFilter.filtertKeyword(parameters);
        Map<String, String> filterSimple = ParameterFilter.filterSimple(parameters);
        Map<String, Map<String, String>> filterComplex = ParameterFilter.filterComplex(parameters);

        return Specification.where(
                        BaseSpecification.<T>keyword(keyword, searchKey))
                .and(BaseSpecification.<T>whereSpec(filterSimple)
                        .and(BaseSpecification.complexWhereSpec(filterComplex)));
    }
}
