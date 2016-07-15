/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch.testobj;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/5/16.
 */

public class Level2 {

  private Level3 level3;

  public Level2() {
    this(null);
  }

  public Level2(Level3 level3) {
    this.level3 = level3;
  }

  public Level3 getLevel3() {
    return level3;
  }

  public void setLevel3(Level3 level3) {
    this.level3 = level3;
  }
}
