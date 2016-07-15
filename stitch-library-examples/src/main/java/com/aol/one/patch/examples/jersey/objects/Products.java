// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.jersey.objects;

import com.aol.one.patch.Patchable;
import com.fasterxml.jackson.databind.JsonNode;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/16/16.
 */
public class Products implements Patchable {

  private Map<String, Integer> productCounts = new HashMap<>();

  @Override
  public Patchable getPatchObjectByKey(String s) {
    return this;
  }

  @Override
  public void addValue(String s, JsonNode jsonNode) {

    validateId(s);

    if (jsonNode.has("count")) {
      int countInput = jsonNode.get("count").asInt();
      if (countInput <= 0) {
        throw new RuntimeException("invalid count: " + countInput);
      }
      Integer count = countInput;
      productCounts.put(s, count);
    } else {
      throw new RuntimeException("invalid value received: " + jsonNode + " for product Id: " + s + " type: " + jsonNode.getNodeType().toString());
    }
  }

  @Override
  public void replaceValue(String s, JsonNode jsonNode) {
    addValue(s, jsonNode);
  }

  @Override
  public void removeValue(String s) {
    validateId(s);
    productCounts.remove(s);
  }

  private void validateId(String id) {
    if (StringUtils.isBlank(id)) {
      throw new RuntimeException("empty product id specified");
    }

    if (!StringUtils.isNumeric(id)) {
      throw new RuntimeException("invalid product id: " + id);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("productCounts", productCounts)
        .toString();
  }
}
