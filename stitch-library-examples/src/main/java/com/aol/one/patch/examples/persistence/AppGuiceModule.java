// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/19/16.
 */

public class AppGuiceModule extends AbstractModule {

  private static final String MYBATIS_PROP_FILE = "mybatis-config.xml";

  @Override
  protected void configure() {

    try (InputStream inputStream = Resources.getResourceAsStream(MYBATIS_PROP_FILE)) {
      SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
      bind(SqlSessionFactory.class).toInstance(factory);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Inject
  @Provides
  SqlSession provideSqlSession(SqlSessionFactory factory) {
    return factory.openSession(false);
  }
}
