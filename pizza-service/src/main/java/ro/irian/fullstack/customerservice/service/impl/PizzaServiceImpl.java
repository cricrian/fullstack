package ro.irian.fullstack.customerservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.irian.fullstack.customerservice.domain.*;
import ro.irian.fullstack.customerservice.repository.OrderCrudRepository;
import ro.irian.fullstack.customerservice.repository.PizzaCrudRepository;
import ro.irian.fullstack.customerservice.repository.PizzaRepository;
import ro.irian.fullstack.customerservice.service.PizzaService;
import ro.irian.fullstack.customerservice.service.exception.PizzaNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

/**
 * Service for Pizzas.
 *
 * @author Cristi Toth
 */

@Service
public class PizzaServiceImpl implements PizzaService {

    private static final Logger LOG = LoggerFactory.getLogger(PizzaServiceImpl.class);

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private PizzaCrudRepository pizzaCrudRepository;

    @Autowired
    private OrderCrudRepository orderCrudRepository;

    @Override
    public void save(Pizza pizza) {
        pizzaCrudRepository.save(pizza);

    }

    @Override
    public Iterable<Pizza> getAllPizzas() {
        return pizzaCrudRepository.findAll();
    }

    @Override
    public Page<Pizza> getPagedPizzas(Pageable pageable) {
        return pizzaCrudRepository.findAll(pageable);
    }


    @Override
    public Pizza findPizza(String pizzaId) {
        Pizza pizza = pizzaRepository.findPizzaById(pizzaId);

        if (pizza == null) {
            throw new PizzaNotFoundException(pizzaId);
        }

        return pizza;
    }

    @Override
    public Pizza findPizzaByName(String pizzaName) {
        return pizzaCrudRepository.findOneByName(pizzaName);
    }


    @Override
    public List<ReviewVO> getReviewsForAuthor(String authorName) {
        return pizzaRepository.getReviewVOsByAuthor(authorName);
    }

    @Override
    @Transactional
    public void createTestdata() {

        Pizza pizza = new Pizza("pizza1",
                          "4 STAGIONI",
                          27.5,
                          550,
                          "images/quattro.png",
                          "sos rosii, mozzarella, ciuperci, salam, sunca presata, oregano, anghinare");
        pizza.addReview(new Review(5, "I love this pizza!", "joe@example.org", 100000000L));
        pizza.addReview(new Review(4, "It's great!", "miha@example.org", 100000000L));

        pizzaRepository.save(pizza);

        Pizza pizzaPeperoni = new Pizza("pizza2",
                          "PEPPERONI",
                          25D,
                          450,
                          "images/pepperoni.png",
                          "sos rosii, mozzarella, salam picant, oregano");
        pizzaPeperoni.addReview(new Review(5, "It's the best pizza!", "marius@irian.ro", 110000000L));
        pizzaPeperoni.addReview(new Review(1, "It's awful!", "cristi@irian.ro", 130000000L));

        pizzaRepository.save(pizzaPeperoni);

        Pizza pizzaMargarita = new Pizza("pizza3",
                          "MARGARITA",
                          21.5,
                          500,
                          "images/margarita.png",
                          "sos rosii, mozzarella, oregano");
        pizzaMargarita.addReview(new Review(2, "It's too boring!", "cristi@irian.ro", 140000000L));
        pizzaRepository.save(pizzaMargarita);


        PizzaOrder pizzaOrderMargarita = new PizzaOrder(2, pizzaMargarita);
        PizzaOrder pizzaOrderPeperoni = new PizzaOrder(3, pizzaPeperoni);

        Order orderJohn = new Order("customerId2",  Arrays.asList(pizzaOrderMargarita, pizzaOrderPeperoni));

        orderCrudRepository.save(orderJohn);

        PizzaOrder pizzaOrderPeperoniIon = new PizzaOrder(2, pizzaPeperoni);

        Order orderIon = new Order("customerId1",  Arrays.asList(pizzaOrderPeperoniIon));

        orderCrudRepository.save(orderIon);

        LOG.info("Testdata initialized");
    }

}
