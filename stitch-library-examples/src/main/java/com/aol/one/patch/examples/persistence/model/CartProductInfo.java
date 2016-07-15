// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.persistence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/23/16.
 */

public class CartProductInfo {

  private int cartId;
  private int productId;
  private int count;

  public int getCartId() {
    return cartId;
  }

  public void setCartId(int cartId) {
    this.cartId = cartId;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
        .append("cartId", cartId)
        .append("productId", productId)
        .append("count", count)
        .toString();
  }
}
