/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch.testobj;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/5/16.
 */

public class Level3 {

  private String someString;

  public Level3() {
    this(null);
  }

  public Level3(String someString) {
    this.someString = someString;
  }

  public String getSomeString() {
    return someString;
  }

  public void setSomeString(String someString) {
    this.someString = someString;
  }
}
