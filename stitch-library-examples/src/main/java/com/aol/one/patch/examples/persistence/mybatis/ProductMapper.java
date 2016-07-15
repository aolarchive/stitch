// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.persistence.mybatis;

import com.aol.one.patch.examples.persistence.model.Product;

import java.util.List;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/23/16.
 */

public interface ProductMapper {

  List<Product> selectAllProducts();

  void createProduct(Product product);

  Product selectProduct(int id);

  void saveProduct(Product product);

}
