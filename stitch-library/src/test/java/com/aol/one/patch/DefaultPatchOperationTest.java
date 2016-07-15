/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 1/29/16.
 */

public class DefaultPatchOperationTest {

  private DefaultPatchOperation patchOperation;

  @Before
  public void setup() throws PatchException {
    patchOperation = new DefaultPatchOperation(Operation.ADD, "/foo/bar/baz") {
    };
  }

  @Test
  public void testCreation() {
    assertThat(patchOperation.getOperation(), is(Operation.ADD));
    List<String> pathTokens = patchOperation.getPathTokens();
    Assert.assertEquals(3, pathTokens.size());
    Assert.assertEquals("foo", pathTokens.get(0));
    Assert.assertEquals("baz", pathTokens.get(2));
  }



}
