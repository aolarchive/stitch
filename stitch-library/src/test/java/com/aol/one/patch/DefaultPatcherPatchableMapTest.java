/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import com.aol.one.patch.testobj.PatchableMap;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * This
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 1/29/16.
 */

public class DefaultPatcherPatchableMapTest {

  private PatchableMap patchableMap;

  @Before
  public void setup() {
    patchableMap = new PatchableMap();
  }

  @Test
  public void testPatchableAddValue() throws PatchException {

    PatchOperation operation = new AddOperation("/addedValue", new TextNode("100"));
    Patcher patcher = PatcherFactory.getDefaultPatcher();

    assertThat(patchableMap.get("addedValue"), nullValue());
    patcher.patch(patchableMap, operation);
    assertThat(patchableMap.get("addedValue"), is("100"));

  }

  @Test
  public void testPatchableChildAddValue() throws PatchException {

    PatchOperation operation = new AddOperation("/child/childAddedValue", new TextNode("100"));
    Patcher patcher = PatcherFactory.getDefaultPatcher();

    assertThat(patchableMap.get("childAddedValue"), nullValue());
    patcher.patch(patchableMap, operation);
    assertThat(patchableMap.get("childAddedValue"), is("100"));

  }

  @Test
  public void testPatchableReplaceValue() throws PatchException {
    PatchOperation operation = new ReplaceOperation("/replacedValue", new TextNode("300"));
    Patcher patcher = PatcherFactory.getDefaultPatcher();

    patchableMap.put("replacedValue", "200");
    assertThat(patchableMap.get("replacedValue"), is("200"));
    patcher.patch(patchableMap, operation);
    assertThat(patchableMap.get("replacedValue"), is("300"));

  }

  @Test
  public void testPatchableRemoveValue() throws PatchException {
    PatchOperation operation = new RemoveOperation("/removeValue");
    Patcher patcher = PatcherFactory.getDefaultPatcher();

    patchableMap.put("removeValue", "200");
    assertThat(patchableMap.get("removeValue"), is("200"));
    patcher.patch(patchableMap, operation);
    assertThat(patchableMap.get("removeValue"), nullValue());

  }

}
