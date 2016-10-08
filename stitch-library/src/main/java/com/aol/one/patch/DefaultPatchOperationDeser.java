/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DefaultPatchOperationDeser extends JsonDeserializer<DefaultPatchOperation> {

  @Override
  public DefaultPatchOperation deserialize(JsonParser jsonParser, 
      DeserializationContext deserializationContext) throws IOException {

    JsonNode node = jsonParser.getCodec().readTree(jsonParser);

    String op = node.get("op").asText();
    String path = node.get("path").asText();

    if (op.equalsIgnoreCase("remove")) {
      try {
        return new RemoveOperation(path);
      } catch (PatchException ex) {
        throw new PatchRuntimeException(ErrorCodes.ERR_INVALID_PATH, path, ex);
      }
    }

    if (!op.equalsIgnoreCase("add") && !op.equalsIgnoreCase("replace")) {
      throw new PatchRuntimeException(ErrorCodes.ERR_UNSUPPORTED_PATCH_OP, "unknown op: " + op);
    }

    JsonNode value = node.get("value");
    if (op.equalsIgnoreCase("add")) {
      try {
        return new AddOperation(path, value);
      } catch (PatchException ex) {
        throw new PatchRuntimeException(ErrorCodes.ERR_INVALID_PATH, path, ex);
      }
    }

    try {
      return new ReplaceOperation(path, value);
    } catch (PatchException ex) {
      throw new PatchRuntimeException(ErrorCodes.ERR_INVALID_PATH, path, ex);
    }
  }
}
