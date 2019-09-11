package com.ebran.todo.Todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getAllItems() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<?> getItem(@PathVariable int id) {
        if (service.findOneById(id) == null) {
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(service.findOneById(id), HttpStatus.OK);
    }

    @PostMapping("/todo")
    public ResponseEntity<Object> create(@RequestBody Todo item) {
        if (item.getItem() == "")
            return new ResponseEntity<>("Please enter any item",HttpStatus.BAD_REQUEST);

        Todo addedItem = service.addNewItem(item);
        return new ResponseEntity<>(addedItem, HttpStatus.CREATED);
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable int id) {
        boolean item = service.deleteOneById(id);
        if(item == false){
            return new ResponseEntity<>("Item not deleted", HttpStatus.GONE);
        } else {
            return new ResponseEntity<>("Item Deleted", HttpStatus.OK);
        }
    }

    @PatchMapping("/todo/{id}")
    public ResponseEntity<?> editItem(@PathVariable int id, @RequestParam String item) {
        if (item == "")
            return new ResponseEntity<>("Please enter any item",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(service.editOneById(id, item), HttpStatus.OK);
    }
}
