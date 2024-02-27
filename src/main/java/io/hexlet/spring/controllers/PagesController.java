package io.hexlet.spring.controllers;

import io.hexlet.spring.dto.page.PageCreateDTO;
import io.hexlet.spring.dto.page.PageDTO;
import io.hexlet.spring.dto.page.PageUpdateDTO;
import io.hexlet.spring.exceptions.ResourceAlreadyExistsException;
import io.hexlet.spring.exceptions.ResourceNotFoundException;
import io.hexlet.spring.mapper.PageMapper;
import io.hexlet.spring.model.Page;
import io.hexlet.spring.repositories.PageRepository;
import io.hexlet.spring.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pages")
public class PagesController {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PageMapper pageMapper;

    @GetMapping
    public ResponseEntity<List<Page>> index(@RequestParam(defaultValue = "1") int page) {
        var result = pageRepository.findAll(PageRequest.of(page - 1, 5)).stream().toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(pageRepository.count()))
                .body(result);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PageDTO show(@PathVariable long id) {
        return pageMapper.map(pageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity with id " + id + " not found")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PageDTO create(@Valid @RequestBody PageCreateDTO pageData) {
        var page = pageMapper.map(pageData);
        if (!pageRepository.findAll().contains(page)) {
            var assignee = page.getAssignee();
            assignee.addPage(page);
            pageRepository.save(page);
        } else {
            throw new ResourceAlreadyExistsException("Resource already exists");
        }
        return pageMapper.map(page);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PageDTO update(@PathVariable long id, @RequestBody PageUpdateDTO pageUpdateDTO) {
        var page = pageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Page with id %d not found", id)));
        pageMapper.update(pageUpdateDTO, page);
        page.setAssignee(userRepository.findById(pageUpdateDTO.getAssigneeId()).get());
        pageRepository.save(page);
        return pageMapper.map(page);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable long id) {
        pageRepository.deleteById(id);
    }

}
