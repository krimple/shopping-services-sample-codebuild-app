package com.rimple.shoppinglist.web;

import com.rimple.shoppinglist.model.ShoppingListItem;
import com.rimple.shoppinglist.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shoppinglist")
public class ShoppingListController {
    private ShoppingListService shoppingListService;

    @Autowired

    public ShoppingListController(@Qualifier("dynamoDBService") ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping("")
    public List<ShoppingListItem> listItems(
            @CookieValue(value = "listToken") String listKey) {
        return this.shoppingListService.listItems(listKey);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void addItemToList(
            @RequestBody ShoppingListItem item,
            @RequestAttribute("shoppingListId") String listKey) {
       this.shoppingListService.addItem(listKey, item);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListItem getSingleItem (
            @PathVariable("id") String itemKey,
            @RequestAttribute("shoppingListId") String listKey) {
        return this.shoppingListService.getItem(listKey, itemKey);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateItem (
            @PathVariable("id") String itemKey,
            @RequestBody ShoppingListItem item,
            @RequestAttribute("shoppingListId") String listKey) {
        this.shoppingListService.updateItem(listKey, item);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateItem (
            @PathVariable("id") String itemKey,
            @RequestAttribute("shoppingListId") String listKey) {
        this.shoppingListService.deleteItem(listKey, itemKey);
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAllItems(
            @RequestAttribute("shoppingListId") String listKey
    ) {
        this.shoppingListService.removeAllItems(listKey);
    }
}
