/*
 * See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This interface will be used by Patcher.
 */
@JsonDeserialize(as = DefaultPatchOperation.class)
public interface PatchOperation {

  String getPath();

  Operation getOperation();

  JsonNode getValue();
}
