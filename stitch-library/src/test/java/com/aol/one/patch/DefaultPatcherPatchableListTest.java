/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.aol.one.patch.testobj.PatchableList;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Before;
import org.junit.Test;


/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/1/16.
 */

public class DefaultPatcherPatchableListTest {

  private PatchableList patchableList;

  @Before
  public void setup() {
    patchableList = new PatchableList();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testPatchableAddValueOutOfBounds() throws PatchException {

    PatchOperation operation = new AddOperation("/1", new TextNode("100"));
    Patcher patcher = PatcherFactory.getDefaultPatcher();

    PatchableList list = spy(patchableList);
    patcher.patch(list, operation);
  }

  @Test
  public void testPatchableAddValue() throws PatchException {

    PatchOperation operation = new AddOperation("/0", new TextNode("100"));
    Patcher patcher = PatcherFactory.getDefaultPatcher();

    PatchableList list = spy(new PatchableList());
    patcher.patch(list, operation);
    verify(list).add(0, "100");
  }

  @Test
  public void testPatchableChildAddValue() throws PatchException {

    PatchOperation operation = new AddOperation("/child/childAddedValue", new TextNode("100"));
    Patcher patcher = PatcherFactory.getDefaultPatcher();

  // TODO: update

  }

  @Test
  public void testPatchableReplaceValue() throws PatchException {
    PatchOperation operation = new ReplaceOperation("/replacedValue", new TextNode("300"));
    Patcher patcher = PatcherFactory.getDefaultPatcher();

// TODO: update

  }

  @Test
  public void testPatchableRemoveValue() throws PatchException {
    PatchOperation operation = new RemoveOperation("/removeValue");
    Patcher patcher = PatcherFactory.getDefaultPatcher();

  // TODO: update

  }


}
