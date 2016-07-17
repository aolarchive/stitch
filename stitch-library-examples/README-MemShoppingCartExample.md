README-MemShoppingCartExample
=============================

Jersey Application using Java Patch Library Example
---------------------------------------------------

This example implements a shopping cart resource allowing users to add, remove and update products in shopping cart in memory. Server restart will remove all carts in memory.

Tomcat7 will be used as servlet container to launch the in memory Shopping Cart App.  This App is not full featured and does not implement cart and product resource creation. In the PATCH request, you can specify ids without bothering to create products or carts first.

After startup, the service will be located at http://localhost:8080/cart-mem/cart/{id}

The following PATCH operations will be supported

* op="add"     path="/products"     value={"id": 2, "count": 1}
* op="replace" path="/products/100" value={"count": 10}
* op="remove"  path="/products/100"

To run the example, use the following commands in Terminal 1.

<pre>
~/stitch $ mvn clean package  
~/stitch $ cd stitch-library-examples/target  
Replace &lt;version&gt; in the command below with version number  
~/stitch $ java -jar stitch-library-examples-&lt;version&gt;-war-exec.jar  
</pre>

Tomcat7 will start up and load up the Shopping Cart App

In another console, run the curl command

$ curl -X PATCH 'http://localhost:8080/cart-mem/cart/100' -d '[{"op": "add", "path": "/products", "value":{"id":100,"count":2}}, {"op": "replace", "path": "/products/100", "value":{"count":4}}, {"op":"remove", "path":"/products/100"}]' -H 'Content-type: application/json'

The above command adds, replaces and deletes the same product to the cart. Output should look similar to:

<pre>
Cart before patch: Cart[id=100,products=Products[productCounts={}]]
-------------------

Cart after patch op: ADD; path: "/products"; value: {"id":100,"count":2} : Cart[id=100,products=Products[productCounts={100=2}]]
-------------------

Cart after patch op: REPLACE; path: "/products/100"; value: {"count":4} : Cart[id=100,products=Products[productCounts={100=4}]]
-------------------

Cart after patch op: REMOVE; path: "/products/100" : Cart[id=100,products=Products[productCounts={}]]
-------------------
</pre>

After running this example, switch to the first console and hit Ctrl+c to stop the Tomcat server.

