Stitch Library
==============

This library helps in implementing HTTP PATCH method on Web Resources exposed by Restful APIs.

Getting Started
---------------------

The Stitch Library provides a Patcher interface. A DefaultPatcher is available for use as day to day Patcher implementation. PatchOperations are run on an object and the patcher expects one to be given. The Library also provides an interface named "Patchable" for objects like these. It is optional for objects to be passed in to patcher to implement this interface, but doing so will help the developer of the to be patched object and the library.

Here is a sample usage.

    User user = new User();  // might implement Patchable interface or might provide a setName() method.
    AddOperation setNameOperation = new AddOperation("/name", new TextNode("updatedName"));
    Patcher patcher = new DefaultPatcher();
    patcher.patch(user, setNameOperation);

For use in *Jersey* based applications, see See [Examples README.md](../stitch-library-examples/README.md)

# Maven

Add the following dependency to your pom to fetch the Stitch Library

<pre>
&lt;dependency&gt;
  &lt;groupId&gt;com.aol.one&lt;/groupId&gt;
  &lt;artifactId&gt;stitch-library&lt;/artifactId&gt;
  &lt;version&gt;0.4.0&lt;/version&gt;
&lt;/dependency&gt;
</pre>

# Interfaces
- PatchOperation
- Patcher
- Patchable

# DefaultPatcher

Multiple examples are provided the stitch-library-examples module. See [Examples README.md](../stitch-library-examples/README.md).
