// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.persistence.model;

import com.aol.one.patch.PatchException;
import com.aol.one.patch.Patchable;
import com.aol.one.patch.PatchableException;
import com.aol.one.patch.examples.persistence.service.ServicesHolder;
import com.fasterxml.jackson.databind.JsonNode;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;


/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/23/16.
 */


/*
* support:
* op="replace" path="/name" value="name of cart"
* op="add"     path="/products/100"     value={"count": 1}
* op="replace"     path="/products/100"     value={"count": 1}
* op="replace" path="/products/100/count" value=10
* op="remove"  path="/products/100"
*
* NOTE: PatchableCart handles operation at root ("/")
*/

public class PatchableCart implements Patchable {

  public static final int ERR_UNSUPPORTED_KEY_IN_PATH = 100;
  public static final int ERR_UNSUPPORTED_OP_FOR_KEY = 101;
  public static final int ERR_CONSTRAINT_FAIL = 102;
  public static final int ERR_INVALID_VALUE = 103;

  private int cartId;
  private SqlSession sqlSession;
  private ServicesHolder servicesHolder;

  public PatchableCart(SqlSession sqlSession, ServicesHolder servicesHolder, int cartId) {
    this.sqlSession = sqlSession;
    this.servicesHolder = servicesHolder;
    this.cartId = cartId;
  }

  @Override
  public Patchable getPatchObjectByKey(String key) throws PatchableException {

    if (key.equalsIgnoreCase("products")) {
      // make sure to return new CartProductInfo every time.
      CartProductInfo info = new CartProductInfo();
      info.setCartId(cartId);
      return new PatchableCartProduct(sqlSession, this.servicesHolder, info);
    }

    throw new PatchableException(ERR_UNSUPPORTED_KEY_IN_PATH, "unsupported key: " + key);
  }

  @Override
  public void addValue(String key, JsonNode value) throws PatchableException {
    throw new PatchableException(ERR_UNSUPPORTED_OP_FOR_KEY, "cannot add: " + value + " to " + key);
  }

  @Override
  public void replaceValue(String key, JsonNode value) throws PatchableException {

    if (StringUtils.isNotBlank(key) && key.equalsIgnoreCase("name")) {

      if (value != null && value.isTextual() && StringUtils.isNotBlank(value.asText())) {
        String name = value.asText();
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setName(name);
        // save in db
        servicesHolder.getCartService().saveCart(cart);
        // all done
        return;
      } else {
        throw new PatchableException(ERR_INVALID_VALUE, "invalid value: " + value + " for key: " + key);
      }
    }

    throw new PatchableException(ERR_UNSUPPORTED_OP_FOR_KEY, "cannot replace: " + key);
  }

  @Override
  public void removeValue(String key) throws PatchableException {
    throw new PatchableException(ERR_UNSUPPORTED_OP_FOR_KEY, "cannot remove: " + key);
  }
}
