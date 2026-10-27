package com.example.wishlist.controller;

import com.example.wishlist.entity.Item;
import com.example.wishlist.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(item -> ResponseEntity.ok(item))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item savedItem = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        return itemRepository.findById(id)
                .map(item -> {
                    item.setName(itemDetails.getName());
                    item.setPrice(itemDetails.getPrice());
                    item.setUrl(itemDetails.getUrl());
                    item.setMemo(itemDetails.getMemo());
                    Item updatedItem = itemRepository.save(item);
                    return ResponseEntity.ok(updatedItem);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 5. 削除 (DELETE /api/notes/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
