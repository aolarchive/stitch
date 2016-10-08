/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

@JsonDeserialize(using = DefaultPatchOperationDeser.class)
// @formatter:on
public abstract class DefaultPatchOperation implements PatchOperation {

  private DefaultPatchOperation() {
    // nothing to do here
  }

  protected Operation op;
  protected String path;
  protected JsonNode value;

  @JsonIgnore
  private PathTokens pathTokens;

  protected DefaultPatchOperation(final Operation op, final String path) throws PatchException {
    this(op, path, null);
  }

  protected DefaultPatchOperation(final Operation op, final String path, final JsonNode value) 
      throws PatchException {
    this.op = op;
    setValue(value);
    setPath(path);
  }


  @Override
  public Operation getOperation() {
    return op;
  }

  @Override
  public String getPath() {
    return path;
  }

  public void setPath(String path) throws PatchException {
    this.path = path;
    pathTokens = new PathTokens(this.path);
  }

  @Override
  public JsonNode getValue() {
    return value;
  }

  public void setValue(JsonNode value) {
    if (value != null) {
      this.value = value.deepCopy();
    } else {
      this.value = value;
    }
  }

  public PathTokens getPathTokens() {
    return pathTokens;
  }

  @Override
  public String toString() {
    return "op: " + op + "; path: \"" + path + "\"; value: " + value;
  }
}
