package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

    @Before
    public void setUp() {

        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void testGetAllItems() {
        Item item1 = new Item();
        item1.setName("Round Widget");
        item1.setPrice(BigDecimal.valueOf(2.99));
        item1.setDescription("A widget that is round");

        Item item2 = new Item();
        item2.setName("Square Widget");
        item2.setPrice(BigDecimal.valueOf(1.99));
        item2.setDescription("A widget that is square");

        List<Item> items = new ArrayList() {{
            add(item1);
            add(item2);
        }};

        Mockito.when(itemRepository.findAll()).thenReturn(items);
        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();
        assertNotNull(itemList);
    }

    @Test
    public void testGetItemById() {
        Item item = new Item();
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Mockito.when(itemRepository.findById(1l)).thenReturn(Optional.of(item));
        final ResponseEntity<Item> response = itemController.getItemById(1l);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item1 = response.getBody();
        assertNotNull(item1);
        assertEquals("Round Widget", item1.getName());
    }


    @Test
    public void testGetItemsByName() {
        Item item = new Item();
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        List<Item> items = Arrays.asList(item);

        Mockito.when(itemRepository.findByName("Round Widget")).thenReturn(items);
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();

        assertNotNull(itemList);
        assertEquals(1, itemList.size());
        assertEquals("Round Widget", itemList.get(0).getName());
    }
}
