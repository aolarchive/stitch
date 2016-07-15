/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import java.util.List;

/**
 * Patcher - the interface to implement various patchers.
 */
public interface Patcher {
  void patch(Object objectToPatch, PatchOperation patchOp) throws PatchException;
  void patch(Object objectToPatch, List<PatchOperation> patchOps) throws PatchException;
}
