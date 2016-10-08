/*
 * See the file "LICENSE.TXT" for the full license governing this code.
 */

/*
 * See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aol.one.patch.testobj.Level1;
import com.aol.one.patch.testobj.Level2;
import com.aol.one.patch.testobj.Level3;
import com.aol.one.patch.testobj.PatchChildTestObject;
import com.aol.one.patch.testobj.PatchTestObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultPatcherTest {

  private PatchTestObject testObject;
  private PatchChildTestObject childTestObject;
  private DefaultPatcher patcher;

  @Before
  public void setup() {
    patcher = new DefaultPatcher();
    testObject = spy(new PatchTestObject());

    testObject.setDoubleField(10d);
    testObject.setStrField("1string");

    childTestObject = spy(new PatchChildTestObject());
    childTestObject.setStrField("2string");

    testObject.setChild(childTestObject);
  }

  @Test(expected = PatchException.class)
  public void testEmptyObjectPatch() throws PatchException {
    List<PatchOperation> list = new ArrayList<>();
    patcher.patch(null, list);
  }

  @Test(expected = PatchException.class)
  public void testEmptyOperationPatch() throws PatchException {
    List<PatchOperation> list = new ArrayList<>();
    patcher.patch(testObject, list);
  }

  @Test(expected = NullPointerException.class)
  public void testNullOperationPatch() throws PatchException {
    List<PatchOperation> list = new ArrayList<>();
    PatchOperation patchOperation = new PatchOperation() {
      @Override
      public String getPath() {
        return null;
      }

      @Override
      public Operation getOperation() {
        return null;
      }

      @Override
      public JsonNode getValue() {
        return null;
      }
    };

    list.add(patchOperation);
    patcher.patch(testObject, list);
  }

  @Test
  public void testReplaceForSuccess() throws PatchException {

    List<PatchOperation> operations = new ArrayList<>();

    TextNode strNode = new TextNode("1.1String");
    DoubleNode doubleNode = new DoubleNode(10.1d);
    LongNode longNode = new LongNode(200L);
    TextNode childStrNode = new TextNode("2.1String");
    DoubleNode childDoubleNode = new DoubleNode(102.1);

    operations.add(new ReplaceOperation("/doubleField", doubleNode));
    operations.add(new ReplaceOperation("/strField", strNode));
    operations.add(new ReplaceOperation("/longField", longNode));
    operations.add(new ReplaceOperation("/child/strField", childStrNode));
    operations.add(new ReplaceOperation("/child/doubleField", childDoubleNode));

    patcher.patch(testObject, operations);

    assertThat(testObject.getDoubleField(), is(doubleNode.asDouble()));
    assertThat(testObject.getStrField(), is(strNode.asText()));
    assertThat(testObject.getLongField(), is(longNode.longValue()));
    verify(testObject).setStrField(strNode.asText());
    verify(testObject).setDoubleField(doubleNode.doubleValue());
    // replacement of long field via setLongField
    verify(testObject).setLongField(longNode.longValue());

    // child
    assertThat(childTestObject.getDoubleField(), is(childDoubleNode.asDouble()));
    assertThat(childTestObject.getStrField(), is(childStrNode.asText()));
    verify(childTestObject).setStrField(childStrNode.textValue());
    verify(childTestObject).setDoubleField(childDoubleNode.doubleValue());
  }

  @Test(expected = PatchException.class)
  public void testReplaceForFailure() throws PatchException {
    List<PatchOperation> operations = new ArrayList<>();
    operations.add(new ReplaceOperation("/longField", new DoubleNode(10.1d)));
    patcher.patch(testObject, operations);
  }

  @Test
  public void testAddToMap() throws PatchException {
    List<PatchOperation> operations = new ArrayList<>();

    DoubleNode displayNode = new DoubleNode(10.1d);
    operations.add(new AddOperation("/someIdMap/0", displayNode));

    patcher.patch(testObject, operations);

    // verify
    verify(testObject.getSomeIdMap()).put("0", displayNode);
  }

  @Test
  public void testAddToList() throws PatchException {
    List<PatchOperation> operations = new ArrayList<>();

    DoubleNode displayNode = new DoubleNode(10.1d);
    operations.add(new AddOperation("/someIdList/0", displayNode));

    patcher.patch(testObject, operations);

    // verify
    verify(testObject.getSomeIdList()).add(0, displayNode);
  }

  @Test
  public void testAddForSuccess() throws PatchException {
    List<PatchOperation> operations = new ArrayList<>();

    operations.add(new AddOperation("/doubleField", new DoubleNode(10.1d)));
    operations.add(new AddOperation("/strField", new TextNode("1.1String")));
    operations.add(new AddOperation("/child/doubleField", new DoubleNode(102.1)));

    patcher.patch(testObject, operations);

    Assert.assertEquals(10.1d, testObject.getDoubleField(), 0);
    Assert.assertEquals("1.1String", testObject.getStrField());

    Assert.assertEquals(102.1d, testObject.getChild().getDoubleField(), 0);
    Assert.assertEquals("2string", testObject.getChild().getStrField());
  }

  @Test(expected = PatchException.class)
  public void testAddForFailure() throws PatchException {
    List<PatchOperation> operations = new ArrayList<>();
    operations.add(new AddOperation("/child/someBlahField", new TextNode("test")));
    patcher.patch(testObject, operations);
  }

  @Test
  public void testRemoveForSuccess() throws PatchException {
    List<PatchOperation> operations = new ArrayList<>();
    operations.add(new RemoveOperation("/doubleField"));
    operations.add(new RemoveOperation("/child/strField"));

    patcher.patch(testObject, operations);

    Assert.assertEquals(-1.1d, testObject.getDoubleField(), 0);
    Assert.assertEquals(null, testObject.getChild().getStrField());
  }

  @Test(expected = PatchException.class)
  public void testRemoveForFailure() throws PatchException {
    List<PatchOperation> operations = new ArrayList<>();
    operations.add(new RemoveOperation("/strField/1.1"));
    patcher.patch(testObject, operations);
  }


  @Test
  public void testRemoveFromMap() throws PatchException {
    Map<String, JsonNode> map = new HashMap<>();
    map.put("1", new DoubleNode(100));
    map.put("2", new DoubleNode(200));
    RemoveOperation removeOperation = new RemoveOperation("/1");

    Patcher patcher = PatcherFactory.getDefaultPatcher();
    patcher.patch(map, removeOperation);

    Assert.assertThat(map.get("1"), is(nullValue()));
  }

  @Test
  public void testRemoveFromList() throws PatchException {

    List<String> list = new ArrayList<>();
    List<String> spyList = spy(list);

    spyList.add("100");
    spyList.add("200");
    spyList.add("300");

    RemoveOperation removeOperation = new RemoveOperation("/1");

    Patcher patcher = PatcherFactory.getDefaultPatcher();
    patcher.patch(spyList, removeOperation);

    // verify that remove(1) was called
    verify(spyList).remove(1);

    assertThat(spyList.get(1), is("300"));
    assertThat(spyList.size(), is(2));

  }

  @Test
  public void testRemoveFromPatchable() throws PatchException {

    Patchable mockPatchable = mock(Patchable.class);
    Patchable mockChildPatchable = mock(Patchable.class);

    when(mockPatchable.getPatchObjectByKey("xyz")).thenReturn(mockChildPatchable);
    doNothing().when(mockChildPatchable).removeValue("key");

    RemoveOperation removeOperation = new RemoveOperation("/xyz/key");
    Patcher patcher = PatcherFactory.getDefaultPatcher();
    patcher.patch(mockPatchable, removeOperation);

    verify(mockPatchable).getPatchObjectByKey("xyz");
    verify(mockChildPatchable).removeValue("key");

  }

  @Test
  public void testRemoveFromObject() throws PatchException {

    PatchTestObject testObjectSpy = spy(new PatchTestObject());
    PatchChildTestObject childSpy = spy(new PatchChildTestObject());

    testObjectSpy.setChild(childSpy);

    RemoveOperation removeOperation = new RemoveOperation("/child/strField");
    Patcher patcher = PatcherFactory.getDefaultPatcher();
    patcher.patch(testObjectSpy, removeOperation);

    verify(testObjectSpy).getChild();
    verify(childSpy).removeStrField();
  }

  @Test
  public void testAddToMultiLevelObjects() throws PatchException {

    // setup
    Level1 l1 = new Level1();
    Level2 l2 = new Level2();
    Level3 l3 = new Level3();
    l2.setLevel3(l3);
    List<Level2> l2list = new ArrayList<>();
    l2list.add(l2);
    l1.setLevel2(l2list);

    String string = "blahString";
    PatchOperation operation = new AddOperation("/level2/0/level3/someString", 
        new TextNode(string));

    // verify precondition
    assertTrue(l3.getSomeString() == null);

    // run test
    patcher.patch(l1, operation);

    assertTrue(l3.getSomeString().equals(string));
  }

}
