package com.ebran.todo.Todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/todo")
    public List<Todo> getAllItems() {
        return service.findAll();
    }

    @GetMapping("/todo/{id}")
    public Todo getItem(@PathVariable int id) {
        return service.findOneById(id);
    }

    @PostMapping("/todo")
    public ResponseEntity<Object> create(@RequestBody Todo item) {
        Todo addedItem = service.addNewItem(item);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(addedItem.getId())
                            .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/todo/{id}")
    public void deleteItem(@PathVariable int id) {
        Todo item = service.deleteOneById(id);
    }

    @PatchMapping("/todo/{id}")
    public void editItem(@PathVariable int id, @RequestParam String item) {
        service.editOneById(id, item);
    }
}
