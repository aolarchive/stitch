/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class AddOperation extends DefaultPatchOperation {

  @JsonCreator
  public AddOperation(@JsonProperty("path") final String path,
                      @JsonProperty("value") final JsonNode value) throws PatchException {
    super(Operation.ADD, path, value);
  }
}
