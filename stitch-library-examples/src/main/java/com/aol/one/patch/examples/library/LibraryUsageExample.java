// Copyright 2016 AOL Platforms.

package com.aol.one.patch.examples.library;

import com.aol.one.patch.AddOperation;
import com.aol.one.patch.DefaultPatcher;
import com.aol.one.patch.PatchException;
import com.aol.one.patch.PatchableException;
import com.aol.one.patch.Patcher;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * In this example, a patch operation object is constructed and applied on an object that needs to be patched
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 1/27/16.
 */

class LibraryUsageExample {



  public static void main(String[] args) throws PatchException {

    // object to be patched
    User user = new User();

    // programmatic construction of patch operations
    AddOperation setNameOperation = new AddOperation("/name", new TextNode("updatedName"));
    AddOperation setAgeOperation = new AddOperation("/age", new IntNode(100));
    AddOperation setYearsOperation = new AddOperation("/years", new LongNode(100));


    System.out.println("Object before patch: " + user);
    Patcher patcher = new DefaultPatcher();

    patcher.patch(user, setNameOperation);
    System.out.println("Object after setNameOperation: " + user);

    patcher.patch(user, setAgeOperation);
    System.out.println("Object after setAgeOperation: " + user);

    patcher.patch(user, setYearsOperation);
    System.out.println("Object after setYearsOperation: " + user);
  }

}

