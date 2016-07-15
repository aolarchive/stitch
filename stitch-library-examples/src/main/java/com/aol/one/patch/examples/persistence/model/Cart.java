// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.persistence.model;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/23/16.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cart {

  Logger LOGGER = LoggerFactory.getLogger(Cart.class);

  @JsonIgnore
  private int id;
  private String name;

  public void setId(int id) {
    this.id = id;
  }

  @JsonProperty("id")
  public int getId() {
    return id;
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
        .append("id", id)
        .append("name", name)
        .toString();
  }

}
