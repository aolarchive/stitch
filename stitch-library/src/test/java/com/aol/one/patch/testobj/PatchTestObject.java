/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch.testobj;

import static org.mockito.Mockito.spy;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatchTestObject {

  private String strField;
  private double doubleField;

  private PatchChildTestObject child;
  private long longField;

  private Map<String, JsonNode> someIdMap;
  private List<JsonNode> someIdList;

  public PatchTestObject() {
    someIdMap = spy(new HashMap<String, JsonNode>());
    someIdList = spy(new ArrayList<JsonNode>());
  }

  public List<JsonNode> getSomeIdList() {
    return someIdList;
  }

  public void setSomeIdList(List<JsonNode> someIdList) {
    this.someIdList = someIdList;
  }

  public Map<String, JsonNode> getSomeIdMap() {
    return someIdMap;
  }

  public void setSomeIdMap(Map<String, JsonNode> someIdMap) {
    this.someIdMap = someIdMap;
  }

  /**
   * @return the strField
   */
  public String getStrField() {
    return strField;
  }

  /**
   * @param strField the strField to set
   */
  public void setStrField(String strField) {
    this.strField = strField;
  }

  /**
   * @return the doubleField
   */
  public double getDoubleField() {
    return doubleField;
  }

  /**
   * @param doubleField the doubleField to set
   */
  public void setDoubleField(double doubleField) {
    this.doubleField = doubleField;
  }

  /**
   * @param child the child to set
   */
  public void setChild(PatchChildTestObject child) {
    this.child = child;
  }

  /**
   * @return the child
   */
  public PatchChildTestObject getChild() {
    return child;
  }

  /**
   * @return the longField
   */
  public long getLongField() {
    return longField;
  }

  /**
   * @param longField the longField to set
   */
  public void setLongField(long longField) {
    this.longField = longField;
  }

  public void replaceStrField(JsonNode node) {
    this.strField = node.asText();
  }

  public void replaceDoubleField(JsonNode node) {
    this.doubleField = node.asDouble();
  }

  public void addStrField(JsonNode node) {
    this.strField = strField + node.asText();
  }

  public void addValue(String field, JsonNode node) {
    if (field.equals("doubleField")) {
      doubleField = doubleField + node.asDouble();
    }
  }

  public void removeDoubleField() {
    this.doubleField = -1.1;
  }
}
