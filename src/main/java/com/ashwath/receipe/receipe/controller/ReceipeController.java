package com.ashwath.receipe.receipe.controller;

import com.ashwath.receipe.receipe.dto.Receipe;
import com.ashwath.receipe.receipe.repository.ReceipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
public class ReceipeController {
    private final String url = "https://s3-ap-southeast-1.amazonaws.com/he-public-data/reciped9d7b8c.json";

    @Autowired
    ReceipeRepository receipeRepository;
    @Autowired
    RestTemplate restTemplate;

    @GetMapping(value = "/")
    public ResponseEntity<List<Receipe>> id() {
        if (receipeRepository.findAll().size() == 0) {
            ResponseEntity<List<Receipe>> receipes = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Receipe>>() {
                    });
            if (receipes.getBody() == null) {
                return ResponseEntity.ok().build();
            }
            receipeRepository.saveAll(receipes.getBody());
        }
        return ResponseEntity.ok(receipeRepository.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getReceipe(@PathVariable(value = "id") Long id) {
        Optional<Receipe> receipe = receipeRepository.findById(id);
        if (receipe.isPresent()) {
            return ResponseEntity.ok(receipe.get());
        } else {
            return ResponseEntity.badRequest().body("Receipe id " + id + " not found. Please check the path specified.");
        }
    }

    @GetMapping(value = "/{id}/show")
    public ResponseEntity<String> getReceipeImage(@PathVariable(value = "id") Long id) {
        Optional<Receipe> receipe = receipeRepository.findById(id);
        return receipe.map(value -> ResponseEntity.ok(value.getImage()))
                .orElseGet(() -> ResponseEntity.badRequest().body("Receipe id " + id + " not found. Please check the path specified."));
    }

    @PostMapping(value = "/")
    public ResponseEntity<Receipe> addToReceipe(@RequestBody Receipe receipe) {
        return ResponseEntity.ok(receipeRepository.save(receipe));
    }
}
