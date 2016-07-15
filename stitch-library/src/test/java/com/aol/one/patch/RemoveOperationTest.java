/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import com.aol.one.patch.Operation;
import com.aol.one.patch.RemoveOperation;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RemoveOperationTest {

  @Test
  public void testCreation() throws PatchException {
    RemoveOperation op = new RemoveOperation("/foo/bar/zoo");
    Assert.assertEquals(Operation.REMOVE, op.op);

    List<String> pathTokens = op.getPathTokens();
    Assert.assertEquals(3, pathTokens.size());
    Assert.assertEquals("foo", pathTokens.get(0));
    Assert.assertEquals("zoo", pathTokens.get(2));
  }

}
