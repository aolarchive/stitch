README-DbShoppingCartExample
============================

Jersey Application Using Spring Library Example
--------------------------------------------------

This example implements a shopping cart resource allowing users to add, remove and update products in shopping cart. Using Apache Derby, and Mybatis, the shopping cart will be persisted in a relational database.

The following PATCH operations will be supported

* op="replace" path="/name" value="name of cart"
* op="add"     path="/products/100"     value={"count": 1}
* op="replace" path="/products/100"     value={"count": 1}
* op="replace" path="/products/100/count" value=10
* op="remove"  path="/products/100"


To clean up a previously initialized db, run the following cmd in Terminal 1.

$ rm -rf /tmp/test-cart-db.db

(Of course, no need to delete the db if you'd like to reuse db from previuos runs)

To run this example, use the following command in Terminal 1.

$ mvn clean package tomcat7:run

Tomcat7 will start up and load up the Shopping Cart App creating db in /tmp/test-cart-db.db with test data (assuming initiazation for new db)

Test data include, Cart #1, Two products #1 and #2, and two counts of Product #1 placed in Cart #1.

In another console, run the curl command

$ curl -v 'http://localhost:8080/cart-db/carts/1?view=products' -H 'content-type: application/json'

Output similar to below should appear.

<pre>
$ {"name":"Cart #1 - creation ts: Thu Feb 25 16:17:15 PST 2016","productInfoList":[{"cartId":1,"productId":1,"count":2}],"id":1}
</pre>

We have two counts of Product id #1 in Cart #1.

Let us run the following cmd

$ curl -X PATCH 'http://localhost:8080/cart-db/carts/1' -d '[{"op": "add", "path": "/products/2", "value":{"count":2}}, {"op": "replace", "path": "/products/2", "value":{"count":4}}, {"op":"remove", "path":"/products/2"}, {"op":"replace", "path":"/name", "value":"My Cart name updated"}]' -H 'Content-type: application/json'

The above commands add, replace and delete product #2 to the cart and rename the cart. After the transaction is complete, the net effect is that only cart name gets changed.

Output of the above would look like

<pre>

Cart before ALL patch operations: CartWithProducts[id=1,name=Cart #1 - creation ts: Thu Feb 25 16:17:15 PST 2016,productInfoList=[[cartId=1,productId=1,count=2]]]
-------------------

Cart after patch operation [op: ADD; path: "/products/2"; value: {"count":2}] : CartWithProducts[id=1,name=Cart #1 - creation ts: Thu Feb 25 16:17:15 PST 2016,productInfoList=[[cartId=1,productId=1,count=2], [cartId=1,productId=2,count=2]]]
-------------------

Cart after patch operation [op: REPLACE; path: "/products/2"; value: {"count":4}] : CartWithProducts[id=1,name=Cart #1 - creation ts: Thu Feb 25 16:17:15 PST 2016,productInfoList=[[cartId=1,productId=1,count=2], [cartId=1,productId=2,count=4]]]
-------------------

Cart after patch operation [op: REMOVE; path: "/products/2"] : CartWithProducts[id=1,name=Cart #1 - creation ts: Thu Feb 25 16:17:15 PST 2016,productInfoList=[[cartId=1,productId=1,count=2]]]
-------------------

Cart after patch operation [op: REPLACE; path: "/name"; value: "My Cart name updated"] : CartWithProducts[id=1,name=Cart #1 - creation ts: Thu Feb 25 16:17:15 PST 2016,productInfoList=[[cartId=1,productId=1,count=2]]]
-------------------
Cart after ALL patch operations: CartWithProducts[id=1,name=My Cart name updated,productInfoList=[[cartId=1,productId=1,count=2]]]
-------------------
</pre>

So, after all operations are complete, we end up with a cart with two counts of Product #1.

Let us go ahead and confirm with another request

$ curl -v 'http://localhost:8080/cart-db/carts/1?view=products' -H 'content-type: application/json'

You should see output similar to

<pre>
{"name":"My Cart name updated","productInfoList":[{"cartId":1,"productId":1,"count":2}],"id":1}
</pre>

confirming that the cart indeed only contains two counts of Product #1

This completes the demo example.

---

To list all carts:

$ curl -v 'http://localhost:8080/cart-db/carts/' -H 'content-type: application/json'

To list all products:

$ curl -v 'http://localhost:8080/cart-db/products/' -H 'content-type: application/json'

To add a new cart:

$ curl -v 'http://localhost:8080/cart-db/carts/' -H 'content-type: application/json' -X POST -d '{"name":"new cart"}'

To add a new product:

$ curl -v 'http://localhost:8080/cart-db/products/' -H 'content-type: application/json' -X POST -d '{"name":"new product"}'
