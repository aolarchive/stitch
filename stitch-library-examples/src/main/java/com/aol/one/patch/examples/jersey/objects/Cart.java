// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.jersey.objects;

import com.fasterxml.jackson.databind.JsonNode;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/8/16.
 */

/*
* support:
* op="add"     path="/products"     value={"id": 2, "count": 1}
* op="replace" path="/products/100" value={"count": 10}
* op="remove"  path="/products/100"
*/

public class Cart {

  private int id;
  private Products products;

  public Products getProducts() {
    return products;
  }

  // called on add
  public void setProducts(JsonNode node) {
    if (!node.isObject()) {
      throw new RuntimeException("invalid value specified");
    }
    String id = node.get("id").asText();
    products.addValue(id, node);
  }

  public void setProducts(Products products) {
    this.products = products;
  }

  public Cart(int id) {
    this.id = id;
    this.products = new Products();
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", id)
        .append("products", products)
        .toString();
  }
}
