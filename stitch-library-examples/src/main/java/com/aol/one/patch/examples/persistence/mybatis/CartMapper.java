package com.aol.one.patch.examples.persistence.mybatis;

import com.aol.one.patch.examples.persistence.model.Cart;

import java.util.List;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/19/16.
 */
public interface CartMapper {

  List<Cart> selectAllCarts();

  void createCart(Cart cart);

  Cart selectCart(int id);

  void saveCart(Cart cart);
}
