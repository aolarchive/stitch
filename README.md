Stitch Library
=============

This library helps in implementing HTTP PATCH method on Web Resources exposed by Restful APIs.

Introduction to PATCH
---------------------
Api clients require partial updates to Resources managed by APIs.  Many a times, these resources consist of multiple unrelated information "bags".
One option is to surface these individually managed bags as sub resources.
Even then, this sub resource might be large enough such that API users encounter the following situations
sending the entire "bag" every time with PUT request may become prohibitive (ex. due bandwidth costs in case of mobile)
ensuring updates made after GET are not lost (by including versioning at the resource level)

HTTP has introduced a new method PATCH, that will allow incremental update of resources

Say, there is a resource  /api/v1/ping-pong/player

{
     "id" : 111,
     "name": "Super Awesome Player",
     "speed_calculation_mistake_factor" : 0.05
}

To update "speed_calculation_mistake_factor" using HTTP Patch, the body of request may look like

[
    { "op": "update", "path": "/speed_calculation_mistake_factor", "value": 0.10 }
]

On Success, typical response would be

200 OK -or- 204 No Content

With optional Content-Location header pointing to the resource.

Typical Operations
------------------
Typical operations used with PATCH include add, remove, update, replace, copy, test


Library Specific Information
----------------------------

Operations supported
--------------------
* add
* replace
* remove

Paths
-----
Paths are evaluated as JSON Pointers [2].

E.g.
1. With json document
{
  "foo": ["abc", "efgh]
}
path "/foo/0" evalutes to "abc"

2. With json document
{
  "foo": { "0": 100,
           "xyz": 200
         }
}
path "/f00/0" evaluates to 100

Library Usage
-------------

The Stitch Library provides a Patcher interface. A DefaultPatcher is available for use as day to day Patcher implementation. PatchOperations are run on an object and the patcher expects one to be given. The Library also provides an interface named "Patchable" for objects like these. It is optional for objects to be passed in to patcher to implement this interface, but doing so will help the developer of the to be patched object and the library.

Here is a sample usage.

    User user = new User();  // might implement Patchable interface or might provide a setName() method.
    AddOperation setNameOperation = new AddOperation("/name", new TextNode("updatedName"));
    Patcher patcher = new DefaultPatcher();
    patcher.patch(user, setNameOperation);

Multiple examples are provided the stitch-library-examples module. See [Examples README.md](./stitch-library-examples/README.md).


References
----------

[1] IETF JSON Patch - https://tools.ietf.org/html/draft-ietf-appsawg-json-patch-10

[2] IETF JSON Pointer - https://tools.ietf.org/html/draft-ietf-appsawg-json-pointer-07