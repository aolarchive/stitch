// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.persistence;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

import com.aol.one.patch.examples.AppExceptionMapper;
import com.aol.one.patch.examples.persistence.model.Cart;
import com.aol.one.patch.examples.persistence.model.CartProductInfo;
import com.aol.one.patch.examples.persistence.model.Product;
import com.aol.one.patch.examples.persistence.mybatis.CartMapper;
import com.aol.one.patch.examples.persistence.mybatis.CartProductsMapper;
import com.aol.one.patch.examples.persistence.mybatis.ProductMapper;

import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jaxb.internal.XmlCollectionJaxbProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.ws.rs.ApplicationPath;
import javax.xml.ws.WebServiceException;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/17/16.
 */

@ApplicationPath("/cart-db")
public class CartDbApp extends ResourceConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(CartDbApp.class);

  public CartDbApp() {

    // Register resources and providers using package-scanning.
    // packages("<your package here>");

    // other registrations
    register(AppExceptionMapper.class);

    Injector injector = Guice.createInjector(new AppGuiceModule());
    register(injector.getInstance(CartsResource.class));
    register(injector.getInstance(ProductsResource.class));

    // create all db tables
    try (SqlSession sqlSession = injector.getInstance(SqlSession.class)) {
      ensureTables(sqlSession);
    }

    // Register my custom provider - not needed if it's in my.package.
    // register(SecurityRequestFilter.class);
    // Register an instance of LoggingFilter.
    // register(new LoggingFilter(LOGGER, true));

    // Enable Tracing support.
    //property(ServerProperties.TRACING, "ALL");

  }

  private void ensureTables(SqlSession sqlSession) {

    Connection connection = sqlSession.getConnection();
    int numRows = 0;
    final String table = "CARTS";
    try {
      DatabaseMetaData dbmd = connection.getMetaData();
      ResultSet rs = dbmd.getTables( null, "APP", table.toUpperCase(), null);
      while( rs.next() ) ++numRows;
    } catch (SQLException e) {
      throw new WebServiceException("unable to query metadata", e);
    }

    if (numRows == 0) {
      LOGGER.info("creating CARTS table...");
      sqlSession.update("com.aol.one.patch.examples.persistence.mybatis.CartMapper.createTable");
      LOGGER.info("creating PRODUCTS table...");
      sqlSession.update("com.aol.one.patch.examples.persistence.mybatis.ProductMapper.createTable");
      LOGGER.info("creating CART_PRODUCTS table...");
      sqlSession.update("com.aol.one.patch.examples.persistence.mybatis.CartProductsMapper.createTable");

      CartMapper cartMapper = sqlSession.getMapper(CartMapper.class);
      ProductMapper productMapper = sqlSession.getMapper(ProductMapper.class);
      CartProductsMapper cartProductsMapper = sqlSession.getMapper(CartProductsMapper.class);

      // create some dataset
      Cart cart = new Cart();
      cart.setId(1);
      Date date = new Date();
      cart.setName("Cart #1 - creation ts: " + date.toString());
      cartMapper.createCart(cart);

      Product p1 = new Product();
      p1.setId(1);
      p1.setName("Product 1");
      productMapper.createProduct(p1);

      Product p2 = new Product();
      p2.setId(2);
      p2.setName("Product 2");
      productMapper.createProduct(p2);

      CartProductInfo info = new CartProductInfo();
      info.setCartId(1);
      info.setProductId(1);
      info.setCount(2);
      cartProductsMapper.addCartProduct(info);

      sqlSession.commit();
    }

  }



}
