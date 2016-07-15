/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch.examples.persistence.service;

import com.google.inject.Inject;

import com.aol.one.patch.examples.persistence.service.CartService;
import com.aol.one.patch.examples.persistence.service.ProductService;

public class ServicesHolder {

  private final CartService cartService;
  private final ProductService productService;

  @Inject
  public ServicesHolder(CartService cartService, ProductService productService) {
    this.cartService = cartService;
    this.productService = productService;
  }

  public ProductService getProductService() {
    return productService;
  }

  public CartService getCartService() {
    return cartService;
  }
}
