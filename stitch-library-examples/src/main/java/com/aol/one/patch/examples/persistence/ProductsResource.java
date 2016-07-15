// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.persistence;

import com.google.inject.Inject;

import com.aol.one.patch.examples.AppException;
import com.aol.one.patch.examples.persistence.model.Product;
import com.aol.one.patch.examples.persistence.service.ProductService;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/23/16.
 */

@Path("/products")
public class ProductsResource {

  private ProductService productService;

  @Inject
  public ProductsResource(ProductService productService) {
    this.productService = productService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Product> getAllProducts() {
    try {
      return productService.getAllProducts();
    } catch (Exception ex) {
      throw new AppException(ex);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public Product doGet(@PathParam("id") int productId) {
    try {
      return productService.getProductById(productId);
    } catch (Exception ex) {
      throw new AppException(ex);
    }
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public void updateCart(@PathParam("id") int productId, Product product) {
    try {
      product.setId(productId);
      productService.saveProduct(product);
    } catch (Exception ex) {
      throw new AppException(ex);
    }
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Product doPost(Product product) {

    System.err.println("productService: " + productService);

    if (product == null) {
      throw new AppException("empty cart specified");
    }

    if (product.getId() != 0) {
      throw new AppException("id cannot be specified here");
    }

    try {
      productService.createProduct(product);
      return product;
    } catch (Exception ex) {
      throw new AppException(ex);
    }
  }

}

