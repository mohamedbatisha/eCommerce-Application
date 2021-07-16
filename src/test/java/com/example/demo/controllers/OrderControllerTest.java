package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    private UserRepository userRepository = Mockito.mock((UserRepository.class));

    @Before
    public void setUp() {

        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

    }

    @Test
    public void testSubmit() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testPassword");

        Item item = new Item();
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(Arrays.asList(item));
        cart.setTotal(BigDecimal.valueOf(2.99));

        user.setCart(cart);

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(user);
        final ResponseEntity<UserOrder> response = orderController.submit("testuser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();

        assertNotNull(userOrder);
        assertEquals(user, userOrder.getUser());
    }

    @Test
    public void submitUserNotAvailable() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testPassword");

        Item item = new Item();
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(Arrays.asList(item));
        cart.setTotal(BigDecimal.valueOf(2.99));

        user.setCart(cart);

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(null);
        final ResponseEntity<UserOrder> response = orderController.submit("testuser");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetOrdersForUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testPassword");

        Item item = new Item();
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(Arrays.asList(item));
        cart.setTotal(BigDecimal.valueOf(2.99));

        user.setCart(cart);

        UserOrder Order = new UserOrder();
        Order.setUser(user);

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(user);
        Mockito.when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(Order));

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testuser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> userOrders = response.getBody();

        assertNotNull(userOrders);
        assertEquals(user, userOrders.get(0).getUser());
    }

}
