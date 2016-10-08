/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

import com.fasterxml.jackson.databind.JsonNode;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Default patcher implementation that does patching of the object with the specified patch 
 * operations list.
 */
public class DefaultPatcher implements Patcher {


  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPatcher.class);

  // make sure these methods are in Patchable interface
  private static final String STANDARD_PATCH_OBJ_GETTER = "getPatchObjectByKey";
  private static final String STANDARD_ADD_METHOD = "addValue";
  private static final String STANDARD_REPLACE_METHOD = "replaceValue";
  private static final String STANDARD_REMOVE_METHOD = "removeValue";

  /**
   * @see DefaultPatcher#patch(Object, List) for exceptions
   */
  @Override
  public void patch(Object objectToPatch, PatchOperation patchOperationList) throws PatchException {
    List<PatchOperation> list = new ArrayList<>();
    list.add(patchOperationList);
    patch(objectToPatch, list);
  }

  /**
   * This implementation assumes that caller has validated patchOperationList.
   *
   * @throws RuntimeException on what are deemed programming errors related to this Patcher. 
   *         PatchExceptions otherwise
   */
  @Override
  public void patch(Object objectToPatch, List<PatchOperation> patchOperationList) 
      throws PatchException {

    if (objectToPatch == null) {
      throw new PatchException(ErrorCodes.ERR_NULL_PATCHABLE);
    }

    if (patchOperationList == null || patchOperationList.isEmpty()) {
      throw new PatchException(ErrorCodes.ERR_NO_PATCH_OP);
    }

    try {
      for (PatchOperation patchOperation : patchOperationList) {
        if (patchOperation == null) {
          continue;
        }

        Operation operation = patchOperation.getOperation();
        switch (operation) {
          case ADD:
            handleAddOperation(objectToPatch, (AddOperation) patchOperation);
            break;
          case REPLACE:
            handleReplaceOperation(objectToPatch, (ReplaceOperation) patchOperation);
            break;
          case REMOVE:
            handleRemoveOperation(objectToPatch, (RemoveOperation) patchOperation);
            break;
          default:
            throw new PatchException(ErrorCodes.ERR_UNSUPPORTED_PATCH_OP);
        }
      }
    } catch (PatchException | RuntimeException ex) {
      throw ex;
    } catch (Exception ex) { // handle remaining checked exception hierarchy
      throw new PatchException(ErrorCodes.ERR_UNKNOWN, ex);
    }
  }

  private void handleAddOperation(Object objectToPatch, AddOperation operation)
      throws IllegalAccessException, InvocationTargetException, PatchException {

    LOGGER.debug("processing operation: {}", operation);

    PathTokens pathTokens = operation.getPathTokens();
    // navigate until we reach leaf token (i.e exclude leaf token)
    Object result = getDescendantObject(objectToPatch, pathTokens, 
        pathTokens.getLastTokenParentIndex());
    if (result == null) {
      throw new PatchException(ErrorCodes.ERR_INVALID_DESCENDANT_OBJ);
    }
    invokeAddMethod(result, pathTokens.getLastToken(), operation.getValue());
  }

  private void handleReplaceOperation(Object objectToPatch, ReplaceOperation operation)
      throws IllegalAccessException, InvocationTargetException, PatchException {

    LOGGER.debug("processing operation: {}", operation);

    PathTokens pathTokens = operation.getPathTokens();
    // navigate until we reach leaf token (i.e exclude leaf token)
    Object result = getDescendantObject(objectToPatch, pathTokens, 
        pathTokens.getLastTokenParentIndex());
    if (result == null) {
      throw new PatchException(ErrorCodes.ERR_INVALID_DESCENDANT_OBJ);
    }
    invokeReplaceMethod(result, pathTokens.getLastToken(), operation.getValue());
  }

  private void handleRemoveOperation(Object objectToPatch, RemoveOperation operation)
      throws IllegalAccessException, InvocationTargetException, PatchException {

    LOGGER.debug("processing operation: {}", operation);

    PathTokens pathTokens = operation.getPathTokens();
    // navigate until we reach leaf token (i.e exclude leaf token)
    Object result = getDescendantObject(objectToPatch, pathTokens, 
        pathTokens.getLastTokenParentIndex());
    if (result == null) {
      throw new PatchException(ErrorCodes.ERR_INVALID_DESCENDANT_OBJ);
    }
    invokeRemoveMethod(result, pathTokens.getLastToken());
  }

  /**
   * object corresponding to path key at descendantIndex
   */
  private Object getDescendantObject(Object objectToPatch, PathTokens pathTokens, 
      int descendantIndex) throws PatchException, IllegalAccessException, 
      InvocationTargetException {

    if (pathTokens == null || pathTokens.isEmpty()) {
      throw new PatchRuntimeException(ErrorCodes.ERR_INVALID_PATH_TOKENS_OBJ);
    }

    if (descendantIndex < -1 || descendantIndex >= pathTokens.size()) {
      throw new PatchRuntimeException(ErrorCodes.ERR_INVALID_DESCENDANT_INDEX, 
          Integer.toString(descendantIndex));
    }

    if (descendantIndex == -1) {
      return objectToPatch;
    }

    int index = 0;
    Object parentObject = objectToPatch;
    Object descendantObject = null;

    while (index <= descendantIndex) {

      String token = pathTokens.get(index);

      if (parentObject == null) {
        throw new PatchException(ErrorCodes.ERR_INVALID_PARENT_PATH_OBJ,
            "unable to retrieve descendant object from null parent while processing token: " 
            + token);
      }

      descendantObject = null;

      if (parentObject instanceof Patchable) {
        Patchable tmp = (Patchable) parentObject;
        descendantObject = tmp.getPatchObjectByKey(token);
      }

      // if token is numeric, action to apply depends on previous descendant object
      if (descendantObject == null && StringUtils.isNumeric(token)) {
        if (parentObject instanceof java.util.List) {
          List tmpList = (List) parentObject;
          descendantObject = tmpList.get(Integer.parseInt(token));
        } else if (parentObject instanceof java.util.Map) {
          Map tmpMap = (Map) parentObject;
          descendantObject = tmpMap.get(token);
        }
      }

      // last effort
      if (descendantObject == null) {
        descendantObject = invokeAccessorMethod(parentObject, token);
      }

      parentObject = descendantObject;
      index++;
    }

    return descendantObject;
  }


  private List<MethodData> generateMethodData(List<String> methodNames) {
    List<MethodData> dataList = new ArrayList<>();
    for (String methodName : methodNames) {
      dataList.add(new MethodData(methodName));
    }
    return dataList;
  }

  // given a json node, generates a list of possible method parameterTypes and their arguments
  private List<MethodData> generateMethodData(List<String> methodNames, JsonNode valueNode) {

    List<MethodData> dataList = new ArrayList<>();
    if (valueNode.isTextual()) {
      for (String methodName : methodNames) {
        dataList.add(new MethodData(methodName, String.class, valueNode.asText()));
      }
    } else if (valueNode.isNumber()) {
      for (String methodName : methodNames) {
        if (valueNode.isIntegralNumber()) {
          dataList.add(new MethodData(methodName, Long.TYPE, valueNode.asLong()));
          dataList.add(new MethodData(methodName, Long.class, valueNode.asLong()));
          dataList.add(new MethodData(methodName, Integer.TYPE, valueNode.asInt()));
          dataList.add(new MethodData(methodName, Integer.class, valueNode.asInt()));
          dataList.add(new MethodData(methodName, Short.TYPE, (short) valueNode.asInt()));
          dataList.add(new MethodData(methodName, Short.class, (short) valueNode.asInt()));
        } else {
          dataList.add(new MethodData(methodName, Double.TYPE, valueNode.asDouble()));
          dataList.add(new MethodData(methodName, Double.class, valueNode.asDouble()));
          dataList.add(new MethodData(methodName, Float.TYPE, valueNode.asDouble()));
          dataList.add(new MethodData(methodName, Float.class, valueNode.asDouble()));
        }
      }
    } else if (valueNode.isBoolean()) {
      for (String methodName : methodNames) {
        dataList.add(new MethodData(methodName, Boolean.TYPE, valueNode.asBoolean()));
        dataList.add(new MethodData(methodName, Boolean.class, valueNode.asBoolean()));
      }

    }

    // default
    for (String methodName : methodNames) {
      dataList.add(new MethodData(methodName, JsonNode.class, valueNode));
    }

    return dataList;
  }

  //
  // Note: ensure object is not null
  //
  private void invokeAddMethod(Object object, String fieldName, JsonNode valueNode)
      throws IllegalAccessException, InvocationTargetException, PatchException {

    if (object instanceof Patchable) {
      Patchable patchable = (Patchable) object;
      patchable.addValue(fieldName, valueNode);
      return;
    }

    if (StringUtils.isNumeric(fieldName) && object instanceof java.util.List) {
      List tmpList = (List) object;
      // WARN: inserting JsonNode into list
      tmpList.add(Integer.parseInt(fieldName), valueNode);
      return;
    }

    if (object instanceof java.util.Map) {
      Map tmpMap = (Map) object;
      // WARN: adding (String, Json) entry to Map, type of Map not known.
      tmpMap.put(fieldName, valueNode);
      return;
    }

    // first try to find set+CapitalizedField or add+CapitalizedField
    List<String> methodNames = new ArrayList<>();
    methodNames.add("set" + StringUtils.capitalize(fieldName));
    methodNames.add("add" + StringUtils.capitalize(fieldName));

    List<MethodData> methodDataList = generateMethodData(methodNames, valueNode);

    // final try, standard method
    List<Class<?>> argTypes = new ArrayList<>();
    argTypes.add(String.class);
    argTypes.add(JsonNode.class);

    List<Object> params = new ArrayList<>();
    params.add(fieldName);
    params.add(valueNode);

    MethodData standardMethodData = new MethodData(STANDARD_ADD_METHOD, argTypes, params);
    methodDataList.add(standardMethodData);

    invokeMethodFromMethodDataList(object, methodDataList);
  }

  //
  // Note: ensure object is not null
  //
  private void invokeReplaceMethod(Object object, String fieldName, JsonNode valueNode)
      throws IllegalAccessException, InvocationTargetException, PatchException {

    if (object instanceof Patchable) {
      Patchable patchable = (Patchable) object;
      patchable.replaceValue(fieldName, valueNode);
      return;
    }

    boolean isFieldNameNumeric = StringUtils.isNumeric(fieldName);

    if (isFieldNameNumeric && object instanceof java.util.List) {
      List tmpList = (List) object;
      // WARN: inserting JsonNode into list
      // NOTE: not add, but set.
      tmpList.set(Integer.parseInt(fieldName), valueNode);
      return;
    }

    if (isFieldNameNumeric && object instanceof java.util.Map) {
      Map tmpMap = (Map) object;
      // WARN: removing string key from Map, key type of Map not known.
      tmpMap.remove(fieldName);
      // WARN: adding JsonNode into Map
      tmpMap.put(fieldName, valueNode);
      return;
    }

    // first try to find replace+CapitalizedField
    List<String> methodNames = new ArrayList<>();
    methodNames.add("replace" + StringUtils.capitalize(fieldName));
    // next, set + capitalizedField
    methodNames.add("set" + StringUtils.capitalize(fieldName));
    List<MethodData> methodDataList = generateMethodData(methodNames, valueNode);

    // final try, standard method
    List<Class<?>> argTypes = new ArrayList<>();
    argTypes.add(String.class);
    argTypes.add(JsonNode.class);

    List<Object> params = new ArrayList<>();
    params.add(fieldName);
    params.add(valueNode);

    MethodData standardMethodData = new MethodData(STANDARD_REPLACE_METHOD, argTypes, params);
    methodDataList.add(standardMethodData);

    invokeMethodFromMethodDataList(object, methodDataList);
  }

  //
  // Note: ensure object is not null
  //
  private void invokeRemoveMethod(Object object, String fieldName)
      throws IllegalAccessException, InvocationTargetException, PatchException {


    if (object instanceof Patchable) {
      Patchable patchable = (Patchable) object;
      patchable.removeValue(fieldName);
      return;
    }

    boolean isFieldNameNumeric = StringUtils.isNumeric(fieldName);

    if (isFieldNameNumeric && object instanceof java.util.List) {
      List tmpList = (List) object;
      tmpList.remove(Integer.parseInt(fieldName));
      return;
    }

    if (isFieldNameNumeric && object instanceof java.util.Map) {
      Map tmpMap = (Map) object;
      tmpMap.remove(fieldName);
      return;
    }

    // first try to find remove+CapitalizedField
    List<String> methodNames = new ArrayList<>();
    methodNames.add("remove" + StringUtils.capitalize(fieldName));

    List<MethodData> methodDataList = generateMethodData(methodNames);

    // final try, standard method
    List<Class<?>> argTypes = new ArrayList<>();
    argTypes.add(String.class);

    List<Object> params = new ArrayList<>();
    params.add(fieldName);

    MethodData standardMethodData = new MethodData(STANDARD_REMOVE_METHOD, argTypes, params);
    methodDataList.add(standardMethodData);

    invokeMethodFromMethodDataList(object, methodDataList);
  }

  private Object invokeAccessorMethod(Object object, String fieldName)
      throws PatchException, IllegalAccessException, InvocationTargetException {

    Class<?> clazz = object.getClass();

    boolean isFieldNameNumeric = StringUtils.isNumeric(fieldName);

    if (isFieldNameNumeric && object instanceof java.util.List) {
      List tmpList = (List) object;
      return tmpList.get(Integer.parseInt(fieldName));
    }

    if (isFieldNameNumeric && object instanceof java.util.Map) {
      Map tmpMap = (Map) object;
      return tmpMap.get(fieldName);
    }

    // try to find get+CapitalizedField
    // TODO: improve this to convert from snake_case to CamelCase
    List<String> methodNames = new ArrayList<>();
    methodNames.add("get" + StringUtils.capitalize(fieldName));
    methodNames.add(STANDARD_PATCH_OBJ_GETTER);

    List<MethodData> methodDataList = generateMethodData(methodNames);

    Set<String> triedMethodNames = new LinkedHashSet<>();

    for (MethodData methodData : methodDataList) {
      Method method;
      if (methodData.hasParameters()) {
        method =
            MethodUtils.getAccessibleMethod(clazz, methodData.methodName,
                methodData.getParameterTypes().toArray(new Class<?>[0]));
        if (method != null && methodData.hasArguments()) {
          return method.invoke(object, methodData.arguments.toArray());
        }
      } else {
        method = MethodUtils.getAccessibleMethod(clazz, methodData.methodName);
        if (method != null) {
          return method.invoke(object);
        }
      }

      triedMethodNames.add(methodData.methodName);
    }

    String exMsg = getExMsgForMethodsNotFound(clazz, 
        triedMethodNames.toArray(new String[triedMethodNames.size()]));
    throw new PatchException(ErrorCodes.ERR_METHOD_TO_PATCH_NOT_FOUND, exMsg);
  }


  private void invokeMethodFromMethodDataList(Object object, List<MethodData> methodDataList)
      throws IllegalAccessException, InvocationTargetException, PatchException {

    Class<?> clazz = object.getClass();
    Set<String> triedMethods = new LinkedHashSet<>();

    for (MethodData methodData : methodDataList) {
      Method method;
      if (methodData.hasParameters()) {
        int size = methodData.getParameterTypes().size();
        method =
            MethodUtils.getAccessibleMethod(clazz, methodData.methodName,
                methodData.getParameterTypes().toArray(new Class<?>[size]));
        if (method != null && methodData.hasArguments()) {
          method.invoke(object, methodData.getArguments().toArray());
          return;
        }
      } else {
        method = MethodUtils.getAccessibleMethod(clazz, methodData.methodName);
        if (method != null) {
          method.invoke(object);
          return;
        }
      }

      triedMethods.add(methodData.methodName);
    }

    throw new PatchException(ErrorCodes.ERR_METHOD_TO_PATCH_NOT_FOUND,
        getExMsgForMethodsNotFound(clazz, triedMethods.toArray(new String[triedMethods.size()])));

  }


  private String getExMsgForMethodsNotFound(Class<?> clazz, String... varargs) {
    String methods = StringUtils.join(varargs, ", ");
    return "Couldn't find one of these methods: " + methods + " in " + clazz;
  }


  private static class MethodData {

    private String methodName = null;
    private List<Class<?>> parameterTypes = new ArrayList<>();
    private List<Object> arguments = new ArrayList<>();

    public MethodData(String methodName, Class<?> clazz, Object object) {
      this.methodName = methodName;
      this.parameterTypes.add(clazz);
      this.arguments.add(object);
    }

    /**
     * @param clazzList  when null, method doesn't take any parameters
     * @param objectList when null, method doesn't take any parameters
     */
    public MethodData(String methodName, List<Class<?>> clazzList, List<Object> objectList) {
      this.methodName = methodName;
      if (clazzList == null) {
        this.parameterTypes = null;
      } else {
        this.parameterTypes.addAll(clazzList);
      }
      if (objectList == null) {
        this.arguments = null;
      } else {
        this.arguments.addAll(objectList);
      }
    }

    /**
     * This is to be used for methods without any parameters.
     */
    public MethodData(String methodName) {
      this.methodName = methodName;
      this.parameterTypes = null;
      this.arguments = null;
    }

    public boolean hasParameters() {
      return parameterTypes != null;
    }

    public List<Class<?>> getParameterTypes() {
      return parameterTypes;
    }

    public boolean hasArguments() {
      return arguments != null;
    }

    public List<Object> getArguments() {
      return arguments;
    }


  }
}
