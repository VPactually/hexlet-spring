package io.hexlet.spring.services;

import io.hexlet.spring.dto.page.PageCreateDTO;
import io.hexlet.spring.dto.page.PageDTO;
import io.hexlet.spring.dto.page.PageParamsDTO;
import io.hexlet.spring.dto.page.PageUpdateDTO;
import io.hexlet.spring.exceptions.ResourceAlreadyExistsException;
import io.hexlet.spring.exceptions.ResourceNotFoundException;
import io.hexlet.spring.mapper.PageMapper;
import io.hexlet.spring.repositories.PageRepository;
import io.hexlet.spring.repositories.UserRepository;
import io.hexlet.spring.specification.PageSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class PageService {

    @Autowired
    private PageRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PageMapper bookMapper;

    @Autowired
    private PageSpecification specBuilder;


    public List<PageDTO> getAll() {
        var authors = repository.findAll();
        var result = authors.stream()
                .map(bookMapper::map)
                .toList();
        return result;
    }
    public List<PageDTO> getAll(PageParamsDTO params, @RequestParam(defaultValue = "1") int page) {
        var spec = specBuilder.build(params);
        var result = repository.findAll(spec, PageRequest.of(page - 1, 10))
                .stream().map(bookMapper::map).toList();
        return result;
    }

    public PageDTO create(PageCreateDTO pageCreateDTO) {
        var page = bookMapper.map(pageCreateDTO);
        if (!repository.findAll().contains(page)) {
            var assignee = page.getAssignee();
            assignee.addPage(page);
            repository.save(page);
        } else {
            throw new ResourceAlreadyExistsException("Resource already exists");
        }
        return bookMapper.map(page);
    }

    public PageDTO findById(Long id) {
        var book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        var BookDTO = bookMapper.map(book);
        return BookDTO;
    }

    public PageDTO update(PageUpdateDTO pageUpdateDTO, Long id) {
        var page = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Page with id %d not found", id)));
        bookMapper.update(pageUpdateDTO, page);
        page.setAssignee(userRepository.findById(pageUpdateDTO.getAssigneeId()).get());
        repository.save(page);
        return bookMapper.map(page);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
