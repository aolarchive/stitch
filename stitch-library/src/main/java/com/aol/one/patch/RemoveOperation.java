/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoveOperation extends DefaultPatchOperation {

  @JsonCreator
  public RemoveOperation(@JsonProperty("path") final String path) throws PatchException {
    super(Operation.REMOVE, path);
  }


  @Override
  public String toString() {
    return "op: " + op + "; path: \"" + path + '"';
  }
}
