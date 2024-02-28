package io.hexlet.spring.specification;

import io.hexlet.spring.dto.page.PageParamsDTO;
import io.hexlet.spring.model.PageModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PageSpecification {
    public Specification<PageModel> build(PageParamsDTO params) {
        return withAssigneeId(params.getAssigneeId())
                .and(withCreatedAtGt(params.getCreatedAtGt()));
    }

    private Specification<PageModel> withAssigneeId(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null ? cb.conjunction() : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    private Specification<PageModel> withCreatedAtGt(LocalDate date) {
        return (root, query, cb) -> date == null ? cb.conjunction() : cb.greaterThan(root.get("createdAt"), date);
    }
}
