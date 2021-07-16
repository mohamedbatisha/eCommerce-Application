package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private CartRepository cartRepository = Mockito.mock(CartRepository.class);

    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

    @Before
    public void setUp() throws Exception {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

    }

    @Test
    public void testAddToCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);

        User user1 = new User();
        user1.setId(1l);
        user1.setUsername("testuser5");
        user1.setPassword("testPassword5");

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Cart cart1 = new Cart();
        cart1.setUser(user1);
        user1.setCart(cart1);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(user1);
        Mockito.when(itemRepository.findById(1l)).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart2 = response.getBody();
        User user2 = cart2.getUser();

        assertNotNull(cart2);
        assertNotNull(user2);
        assertEquals(user1, cart1.getUser());
        assertEquals(item, cart1.getItems().get(0));
    }

    @Test
    public void testFailAddToCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);

        User user1 = new User();
        user1.setId(2l);
        user1.setUsername("testuser5");
        user1.setPassword("testPassword5");

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Cart cart1 = new Cart();
        cart1.setUser(user1);
        user1.setCart(cart1);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(null);
        Mockito.when(itemRepository.findById(2l)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);

        assertEquals(404, cartResponseEntity.getStatusCodeValue());
    }

    @Test
    public void testRemoveFromCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);

        User user = new User();
        user.setId(1l);
        user.setUsername("test");
        user.setPassword("password");

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(user);
        Mockito.when(itemRepository.findById(1l)).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart2 = response.getBody();

        assertNotNull(cart2);
        assertEquals(0, cart2.getItems().size());
    }
}