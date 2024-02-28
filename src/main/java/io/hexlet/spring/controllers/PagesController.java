package io.hexlet.spring.controllers;

import io.hexlet.spring.dto.page.PageCreateDTO;
import io.hexlet.spring.dto.page.PageDTO;
import io.hexlet.spring.dto.page.PageParamsDTO;
import io.hexlet.spring.dto.page.PageUpdateDTO;
import io.hexlet.spring.exceptions.ResourceAlreadyExistsException;
import io.hexlet.spring.exceptions.ResourceNotFoundException;
import io.hexlet.spring.mapper.PageMapper;
import io.hexlet.spring.model.PageModel;
import io.hexlet.spring.repositories.PageRepository;
import io.hexlet.spring.repositories.UserRepository;
import io.hexlet.spring.services.PageService;
import io.hexlet.spring.specification.PageSpecification;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private PageService pageService;

    @Autowired
    private PageSpecification specBuilder;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public List<PageDTO> index(PageParamsDTO params, @RequestParam(defaultValue = "1") int page) {
        return pageService.getAll(params, page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PageDTO show(@PathVariable long id) {
        return pageService.findById(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public PageDTO create(@Valid @RequestBody PageCreateDTO pageCreateDTO) {
        return pageService.create(pageCreateDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PageDTO update(@PathVariable long id, @RequestBody PageUpdateDTO pageUpdateDTO) {
        return pageService.update(pageUpdateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable long id) {
        pageService.delete(id);
    }

}
