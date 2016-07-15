/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Patchers could make use of objects implementing Patchable during patching
 *
 */
public interface Patchable {

  // this method will be invoked when processing individual tokens in path
  Patchable getPatchObjectByKey(String key) throws PatchableException;

  // called on patchable based on operation
  void addValue(String key, JsonNode value) throws PatchableException;
  void replaceValue(String key, JsonNode value) throws PatchableException;
  void removeValue(String key) throws PatchableException;

}
