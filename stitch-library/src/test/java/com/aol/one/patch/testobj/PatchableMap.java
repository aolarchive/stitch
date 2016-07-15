/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch.testobj;

import com.aol.one.patch.Patchable;
import com.fasterxml.jackson.databind.JsonNode;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 1/29/16.
 */

public class PatchableMap extends HashMap<String, String> implements Patchable {


  @Override
  // return this object for all child paths
  public Patchable getPatchObjectByKey(String key) {
    return this;
  }

  @Override
  public void addValue(String key, JsonNode value) {
    if (key != null && value != null) {
      this.put(key, value.asText());
    }
  }

  @Override
  public void replaceValue(String key, JsonNode value) {
    addValue(key, value);
  }

  @Override
  public void removeValue(String key) {
    this.remove(key);
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    for (String key: super.keySet()) {
      builder.append(key, this.get(key));
    }
    return builder.toString();
  }
}


