/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch.testobj;

import com.aol.one.patch.Patchable;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/1/16.
 */

public class PatchableList extends ArrayList<String> implements Patchable {

  @Override
  // return this object for all child paths
  public Patchable getPatchObjectByKey(String key) {
    return this;
  }

  @Override
  public void addValue(String key, JsonNode value) {
    this.add(Integer.parseInt(key), value.asText());
  }

  @Override
  public void replaceValue(String key, JsonNode value) {
    removeValue(key);
    addValue(key, value);
  }

  @Override
  public void removeValue(String key) {
    this.remove(Integer.parseInt(key));
  }

}
