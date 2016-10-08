/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch.testobj;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author elpearson16
 *
 */
public class PatchChildTestObject {

  private String strField;
  private double doubleField;

  /**
   * @param strField the strField to set
   */
  public void setStrField(String strField) {
    this.strField = strField;
  }

  /**
   * @return the strField
   */
  public String getStrField() {
    return strField;
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

  public void replaceStrField(JsonNode newValue) {
    this.strField = newValue.asText();
  }

  /**
   * @param field
   * @param newValueNode
   */
  public void replaceValue(String field, JsonNode newValueNode) {

    if (field.equals("doubleField")) {
      this.doubleField = newValueNode.asDouble();
    }

    if (field.equals("strField")) {
      this.strField = newValueNode.asText();
    }
  }

  public void addDoubleField(JsonNode node) {
    this.doubleField = doubleField + node.asDouble();
  }

  public void removeStrField() {
    this.strField = null;
  }
}
