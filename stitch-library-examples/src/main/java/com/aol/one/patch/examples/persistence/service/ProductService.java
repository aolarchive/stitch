/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch.examples.persistence.service;

import com.google.inject.Inject;

import com.aol.one.patch.examples.persistence.model.Product;
import com.aol.one.patch.examples.persistence.mybatis.ProductMapper;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class ProductService {

  private SqlSessionFactory sqlSessionFactory;

  @Inject
  public ProductService(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  public List<Product> getAllProducts() {
    try (SqlSession sqlSession = getSqlSession()) {
      return getProductMapper(sqlSession).selectAllProducts();
    }
  }

  public Product getProductById(int id) {
    try (SqlSession sqlSession = getSqlSession()) {
      return getProductMapper(sqlSession).selectProduct(id);
    }
  }

  public void saveProduct(Product product) {
    try (SqlSession sqlSession = getSqlSession()) {
      getProductMapper(sqlSession).saveProduct(product);
      sqlSession.commit();
    }
  }

  public void createProduct(Product product) {
    try (SqlSession sqlSession = getSqlSession()) {
      getProductMapper(sqlSession).createProduct(product);
      sqlSession.commit();
    }
  }

  private SqlSession getSqlSession() {
    return sqlSessionFactory.openSession(false);
  }

  private ProductMapper getProductMapper(SqlSession sqlSession) {
    return sqlSession.getMapper(ProductMapper.class);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("sqlSessionFactory", sqlSessionFactory)
        .toString();
  }


}
