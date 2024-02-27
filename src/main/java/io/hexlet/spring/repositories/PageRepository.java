package io.hexlet.spring.repositories;


import io.hexlet.spring.model.Page;
import io.hexlet.spring.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
     Optional<Page> findByName(String name);
}
