// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.persistence.model;

import com.aol.one.patch.PatchException;
import com.aol.one.patch.Patchable;
import com.aol.one.patch.PatchableException;
import com.aol.one.patch.examples.persistence.service.ServicesHolder;
import com.fasterxml.jackson.databind.JsonNode;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This object will act as the proxy for the actual product to be updated. Currently it assumes that only information in cartProductInfo will be updated.
 *
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/23/16.
 */

/*
* support:
* op="add"     path="/products/100"     value={"count": 1}
* op="replace" path="/products/100/count" value=10
* op="remove"  path="/products/100"
*
* NOTE: PatchableCartProduct handles operations at "/products/*"
*
* Methods could be invoked in two situations, when product Id IS/IS NOT known.
*/
public class PatchableCartProduct implements Patchable {

  public static final int ERR_UNSUPPORTED_KEY_IN_PATH = 100;
  public static final int ERR_UNSUPPORTED_OP_FOR_KEY = 101;
  public static final int ERR_CONSTRAINT_FAIL = 102;
  public static final int ERR_INVALID_VALUE = 103;


  private Logger LOGGER = LoggerFactory.getLogger(PatchableCartProduct.class);

  private final SqlSession sqlSession;
  private final ServicesHolder servicesHolder;
  private final CartProductInfo cartProductInfo;


  public PatchableCartProduct(final SqlSession sqlSession, final ServicesHolder servicesHolder, final CartProductInfo cartProductInfo) {
    this.sqlSession = sqlSession;
    this.servicesHolder = servicesHolder;
    this.cartProductInfo = cartProductInfo;
  }


  @Override
  // * op="replace" path="/products/100/count" value=10
  public Patchable getPatchObjectByKey(String key) throws PatchableException {

    if (holdProductId()) {
      // for paths like /products/100/count,  this objects, addValue("count", xyz) or replaceValue("count", xyz) is invoked.
      // for all paths like this: /products/100/blah/blah2/.... throw exception
      throw new PatchableException(ERR_UNSUPPORTED_KEY_IN_PATH, "key: " + key + " not supported here");
    }

    // doesn't hold product id, so key is expected to be a product id.
    validateProductId(key);
    this.cartProductInfo.setProductId(Integer.parseInt(key));

    return this;
  }

  @Override

  //  * op="add"     path="/products/100"     value={"count": 1}
  public void addValue(String key, JsonNode value) throws PatchableException {

    if (holdProductId()) {
      throw new PatchableException(ERR_UNSUPPORTED_OP_FOR_KEY, "add not supported for key: " + key);
    }

    validateProductId(key);
    int productId = extractProductId(key);
    this.cartProductInfo.setProductId(productId);
    if (value.hasNonNull("count")) {
      this.cartProductInfo.setCount(value.get("count").asInt());
      try {
        servicesHolder.getCartService().addProductInfo(sqlSession, cartProductInfo);
      } catch (RuntimeException ex) {
        LOGGER.debug("could not add to cart. exception: thrown", ex);
        throw new PatchableException(ERR_CONSTRAINT_FAIL, "cannot add product id: " + productId + "; it might already exist in cart", ex);
      }
      // all done
      return;
    } else {
      throw new PatchableException(ERR_INVALID_VALUE, "invalid value: " + value + " to add to: " + key);
    }

  }

  @Override
  //* op="replace" path="/products/100/count" value=10
  //* op="replace"     path="/products/100"     value={"count": 1}  <-- valid only when product id 100 is already in cart

  public void replaceValue(String key, JsonNode value) throws PatchableException {

    // replaceValue("100", {"count":1})
    // replaceValue("count", 10)


    if (!holdProductId()) {
      // this must be product id
      validateProductId(key);
      int productId = extractProductId(key);
      // this could be a partial replacement of product object. So cannot call addValue here in the general case.
      this.cartProductInfo.setProductId(productId);
      if (value.hasNonNull("count")) {
        this.cartProductInfo.setCount(value.get("count").asInt());
        try {
          servicesHolder.getCartService().replaceProductInfo(sqlSession, cartProductInfo);
        } catch (RuntimeException ex) {
          LOGGER.debug("could not add to cart. exception: thrown", ex);
          throw new PatchableException(ERR_CONSTRAINT_FAIL, "cannot add product id: " + productId + "; it might already exist in cart", ex);
        }
        // all done
        return;
      } else {
        throw new PatchableException(ERR_INVALID_VALUE, "invalid value: " + value + " to add to: " + key);
      }
    } // no product id

    // only count is supported as of now.
    if (key.equalsIgnoreCase("count")) {
      if (value.isIntegralNumber()) {
        this.cartProductInfo.setCount(value.asInt());
        // save in db
        servicesHolder.getCartService().replaceProductInfo(sqlSession, this.cartProductInfo);
        // all done
        return;
      } else {
        throw new PatchableException(ERR_INVALID_VALUE, "cannot set product count to: " + value);
      }
    }

    throw new PatchableException(ERR_UNSUPPORTED_KEY_IN_PATH, "unsupported replace op. value: " + value + " to: " + key);
  }

  @Override
  public void removeValue(String key) throws PatchableException {
    if (StringUtils.isNumeric(key)) {
      int productId = Integer.parseInt(key);
      this.cartProductInfo.setProductId(productId);
      // save in db
      servicesHolder.getCartService().deleteCartProductInfo(sqlSession, this.cartProductInfo);
      // all done
      return;
    }

    throw new PatchableException(ERR_UNSUPPORTED_KEY_IN_PATH, "cannot remove product id: " + key);
  }

  private boolean holdProductId() {
    return cartProductInfo.getProductId() > 0;
  }

  private void validateProductId(String id) throws PatchableException {
    if (StringUtils.isNotBlank(id) && StringUtils.isNumeric(id)) {
      return;
    }

    throw new PatchableException(ERR_INVALID_VALUE, "invalid product id: " + id);
  }

  private int extractProductId(String id) {
    return Integer.parseInt(id);
  }
}
