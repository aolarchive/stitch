/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AddOperationTest {

  @Test
  public void testCreation() throws PatchException {
    AddOperation op = new AddOperation("/foo/bar/zoo", new TextNode("fooValue"));
    Assert.assertEquals(Operation.ADD, op.op);

    List<String> pathTokens = op.getPathTokens();
    Assert.assertEquals(3, pathTokens.size());
    Assert.assertEquals("foo", pathTokens.get(0));
    Assert.assertEquals("zoo", pathTokens.get(2));
    Assert.assertEquals(new TextNode("fooValue"), op.value);
  }

}
