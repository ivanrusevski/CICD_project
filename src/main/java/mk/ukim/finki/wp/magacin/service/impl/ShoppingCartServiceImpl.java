package mk.ukim.finki.wp.magacin.service.impl;

import mk.ukim.finki.wp.magacin.models.Item;
import mk.ukim.finki.wp.magacin.models.ShoppingCart;
import mk.ukim.finki.wp.magacin.models.User;
import mk.ukim.finki.wp.magacin.models.exceptions.InvalidItemIdException;
import mk.ukim.finki.wp.magacin.models.exceptions.InvalidShoppingCartIdException;
import mk.ukim.finki.wp.magacin.models.exceptions.InvalidUsernameOrPasswordException;
import mk.ukim.finki.wp.magacin.models.exceptions.UserNotFoundException;
import mk.ukim.finki.wp.magacin.repository.ItemRepository;
import mk.ukim.finki.wp.magacin.repository.ShoppingCartRepository;
import mk.ukim.finki.wp.magacin.repository.UserRepository;
import mk.ukim.finki.wp.magacin.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }


    @Override
    public ShoppingCart deleteItem(Item item, Long id) {
        ShoppingCart cart = this.shoppingCartRepository.findById(id).orElseThrow(InvalidShoppingCartIdException::new);
        List<Item> itemList = cart.getItems();
        itemList.remove(item);
        cart.setItems(itemList);
        return this.shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart deleteAllItems(Long id) {
        ShoppingCart cart = this.shoppingCartRepository.findById(id).orElseThrow(InvalidShoppingCartIdException::new);
        cart.setItems(new ArrayList<>());
        return cart;
    }

    @Override
    public ShoppingCart getShoppingCart(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());

        return this.shoppingCartRepository
                .findByUser(user)
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart(user);
                    return this.shoppingCartRepository.save(cart);
                });

    }

    @Override
    public List<Item> listAllProductsInShoppingCart(Long id) {
        ShoppingCart cart = this.shoppingCartRepository.findById(id).orElseThrow(InvalidShoppingCartIdException::new);
        return cart.getItems();
    }

    @Override
    public void addProductToShoppingCart(String username, Long id) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUser(this.userRepository.findByUsername(username).orElseThrow(InvalidUsernameOrPasswordException::new)).orElseThrow(InvalidShoppingCartIdException::new);
        Item item = this.itemRepository.findById(id).orElseThrow(InvalidItemIdException::new);
        List<Item> itemList = shoppingCart.getItems();
        itemList.add(item);
        shoppingCart.setItems(itemList);
        this.shoppingCartRepository.save(shoppingCart);
    }
}
