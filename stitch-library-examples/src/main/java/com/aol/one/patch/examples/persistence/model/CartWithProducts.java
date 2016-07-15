// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.persistence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/24/16.
 */

public class CartWithProducts extends Cart {

  private List<CartProductInfo> productInfoList;

  public List<CartProductInfo> getProductInfoList() {
    return productInfoList;
  }

  public void setProductInfoList(List<CartProductInfo> productInfoList) {
    this.productInfoList = productInfoList;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("name", getName())
        .append("productInfoList", productInfoList)
        .toString();
  }
}
