package io.hexlet.spring.repositories;


import io.hexlet.spring.model.PageModel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<PageModel, Long>, JpaSpecificationExecutor<PageModel> {
     Optional<PageModel> findByName(String name);
}
