/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

public class PatcherFactory {

  private PatcherFactory() {
    // nothing to implement
  }

  public static Patcher getDefaultPatcher() {
    return new DefaultPatcher();
  }

}
