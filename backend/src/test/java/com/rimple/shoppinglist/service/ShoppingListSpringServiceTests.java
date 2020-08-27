package com.rimple.shoppinglist.service;


import com.rimple.shoppinglist.model.ShoppingListItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingListSpringServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShoppingListService shoppingListService;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void resetList() {
        shoppingListService.removeAllItems("KensList");

    }
    @Test
    void doAllRight() {
        assertEquals(2, 2);
    }

    @Test
    void springBooted() throws Exception {
        assertNotNull(context);
    }

    @Test
    void testCreateAndFetchItem() {
        shoppingListService.addItem(
               "KensList",
               new ShoppingListItem("key1", "Eggs", new BigDecimal("3.50")));
        assertEquals(1, shoppingListService.listItems("KensList").stream().count());
    }

    @Test
    void testCreateAndUpdateAndFetchItem() {
        shoppingListService.addItem(
                "KensList",
                new ShoppingListItem("key1", "Eggs", new BigDecimal("3.50")));

        shoppingListService.updateItem("KensList",
               new ShoppingListItem("key1", "Eggs", new BigDecimal("2.25")));

        ShoppingListItem item = shoppingListService.getItem("KensList", "key1");
        assertEquals(new BigDecimal("2.25"), item.getPrice());
    }

    @Test
    void testDeleteItem() {
        shoppingListService.addItem(
                "KensList",
                new ShoppingListItem("key1", "Eggs", new BigDecimal("3.50")));

        shoppingListService.deleteItem("KensList", "key1");

        assertEquals(0, shoppingListService.listItems("KensList").stream().count());
    }

}
