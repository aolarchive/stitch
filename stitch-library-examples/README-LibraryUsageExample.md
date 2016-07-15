README-LibraryUsageExample.md
=============================

This example provides information about programmatic usage of HTTP Java Patch Library.
It might help if you browse through the code of class com.aol.one.patch.examples.library.LibraryUsageExample.

To run, use:

$ mvn --quiet clean compile exec:java -Dexec.mainClass="com.aol.one.patch.examples.library.LibraryUsageExample"

You should see output similar to:

<pre>
Object before patch: User[age=0,years=<null>,name=<null>]
18:55:01.338 processing operation: op: ADD; path: "/name"; value: "updatedName"
Object after setNameOperation: User[age=0,years=<null>,name=updatedName]
18:55:01.345 processing operation: op: ADD; path: "/age"; value: 100
Object after setAgeOperation: User[age=100,years=<null>,name=updatedName]
18:55:01.362 processing operation: op: ADD; path: "/years"; value: 100
Object after setYearsOperation: User[age=100,years=100,name=updatedName]
</pre>

Initially, all three attributes (age, years, name) are not initialized. You can see that with each operation, the User object is being patched as expected.
