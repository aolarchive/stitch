/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch.examples.persistence.service;

import com.google.inject.Inject;

import com.aol.one.patch.examples.persistence.model.Cart;
import com.aol.one.patch.examples.persistence.model.CartProductInfo;
import com.aol.one.patch.examples.persistence.model.CartWithProducts;
import com.aol.one.patch.examples.persistence.mybatis.CartMapper;
import com.aol.one.patch.examples.persistence.mybatis.CartProductsMapper;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/19/16.
 */

public class CartService {

  private SqlSessionFactory sqlSessionFactory;

  @Inject
  public CartService(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  public List<Cart> getAllCarts() {
    try (SqlSession sqlSession = getSqlSession()) {
      return getAllCarts(sqlSession);
    }
  }

  public List<Cart> getAllCarts(SqlSession sqlSession) {
    return getCartMapper(sqlSession).selectAllCarts();
  }


  public Cart getCartById(int id) {
    try (SqlSession sqlSession = getSqlSession()) {
      return getCartById(sqlSession, id);
    }
  }

  public Cart getCartById(SqlSession sqlSession, int id) {
    return getCartMapper(sqlSession).selectCart(id);
  }


  public void saveCart(Cart cart) {
    try (SqlSession sqlSession = getSqlSession()) {
      getCartMapper(sqlSession).saveCart(cart);
      sqlSession.commit();
    }
  }

  public void createCart(Cart cart) {
    try (SqlSession sqlSession = getSqlSession()) {
      getCartMapper(sqlSession).createCart(cart);
      sqlSession.commit();
    }
  }

  // NOTE: this method does not commit transactions
  public void addProductInfo(SqlSession sqlSession, CartProductInfo info) {
    sqlSession.getMapper(CartProductsMapper.class).addCartProduct(info);
  }

  // NOTE: this method does not commit transactions
  public void replaceProductInfo(SqlSession sqlSession, CartProductInfo info) {
    sqlSession.getMapper(CartProductsMapper.class).replaceCartProduct(info);
  }

  // NOTE: this method does not commit transactions
  public void deleteCartProductInfo(SqlSession sqlSession, CartProductInfo info) {
    sqlSession.getMapper(CartProductsMapper.class).deleteCartProduct(info);
  }


  public List<CartProductInfo> getProductInfo(SqlSession sqlSession, int cartId) {
    return getCartProductsMapper(sqlSession).getCartProducts(cartId);
  }

  public CartWithProducts getCartWithProducts(int id) {
    try (SqlSession sqlSession = getSqlSession()) {
      return getCartWithProducts(sqlSession, id);
    }
  }

  public CartWithProducts getCartWithProducts(SqlSession sqlSession, int cartId) {
    CartWithProducts cartWithProducts = new CartWithProducts();
    Cart cart = this.getCartById(sqlSession, cartId);
    cartWithProducts.setId(cart.getId());
    cartWithProducts.setName(cart.getName());
    cartWithProducts.setProductInfoList(getProductInfo(sqlSession, cartId));
    return cartWithProducts;
  }

  // WARN: caller to handle session transaction commit and close
  public SqlSession getSqlSession() {
    return sqlSessionFactory.openSession(false);
  }

  private CartMapper getCartMapper(SqlSession sqlSession) {
    return sqlSession.getMapper(CartMapper.class);
  }

  private CartProductsMapper getCartProductsMapper(SqlSession sqlSession) {
    return sqlSession.getMapper(CartProductsMapper.class);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("sqlSessionFactory", sqlSessionFactory)
        .toString();
  }
}
