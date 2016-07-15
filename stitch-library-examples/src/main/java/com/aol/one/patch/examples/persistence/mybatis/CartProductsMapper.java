package com.aol.one.patch.examples.persistence.mybatis;

import com.aol.one.patch.examples.persistence.model.CartProductInfo;

import java.util.List;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/23/16.
 */
public interface CartProductsMapper {
  List<CartProductInfo> getCartProducts(int cartId);
  void deleteCartProduct(CartProductInfo info);
  void addCartProduct(CartProductInfo info);
  void replaceCartProduct(CartProductInfo info);
}

