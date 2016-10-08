/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

/**
 *         The intent here is to help the library user to figure out the source of the error.
 */
public class ErrorCodes {

  public static final int ERR_UNKNOWN = -1;

  // see
  // Even if we move around codes in a future release, this should not break library caller.
  // http://stackoverflow.com/questions/3524150/is-it-possible-to-disable-javacs-inlining-of-static-final-variables
  //

  // convention: 40000 series are errors originating from client (of library user) space
  public static final int ERR_UNSUPPORTED_PATCH_OP = Integer.valueOf(40001).intValue();
  public static final int ERR_INVALID_PATH = Integer.valueOf(40002).intValue();

  // convention: 50000 series are errors originating from library user space
  public static final int ERR_NULL_PATCHABLE = Integer.valueOf(50001).intValue();
  public static final int ERR_NO_PATCH_OP = Integer.valueOf(50002).intValue();
  public static final int ERR_METHOD_TO_PATCH_NOT_FOUND = Integer.valueOf(50003).intValue();
  public static final int ERR_INVALID_DESCENDANT_INDEX = Integer.valueOf(50004).intValue();
  public static final int ERR_INVALID_DESCENDANT_OBJ = Integer.valueOf(50005).intValue();
  public static final int ERR_INVALID_PARENT_PATH_OBJ = Integer.valueOf(50006).intValue();

  // convention: 60000 series are programming errors in this library. Mostly used by 
  //             PatchRuntimeException
  public static final int ERR_INVALID_PATH_TOKENS_OBJ = Integer.valueOf(60001).intValue();
}
