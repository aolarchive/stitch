/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch.testobj;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/5/16.
 */

public class Level1 {

  private List<Level2> level2;

  public Level1() {
    level2 = new ArrayList<>();
  }

  public List<Level2> getLevel2() {
    return level2;
  }

  public void setLevel2(List<Level2> level2) {
    this.level2 = level2;
  }
}
