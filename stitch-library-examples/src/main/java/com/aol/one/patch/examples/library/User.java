// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.library;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

// Object to be patched needs to be public
public class User {

  private Long years;
  private long age;
  private String name;

  public Long getYears() {
    return years;
  }

  public void setYears(Long years) {
    this.years = years;
  }

  public long getAge() {
    return age;
  }

  public void setAge(long age) {
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("age", age)
        .append("years", years)
        .append("name", name)
        .toString();
  }
}

