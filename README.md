** Please Note**: This project is no longer maintained and contains known security vulnerabilities. Use at your own risk!

Stitch Library
=============

This library helps implement HTTP PATCH method on Web Resources exposed by Restful APIs.

Introduction to PATCH
---------------------
Complex resources consist of multiple independent/interdependent sub-resources. Sending the entire resource every time with PUT request may become prohibitive (mobile)

To support the Api client in resource update, one option is to develop APIs that surface these sub resources with their own GET/PUT methods.  Of course, now the service ends up with multiple APIs. If the sub resources are not truly independent, this option is not feasible.

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

Getting Started
---------------

To get started with the library, see [Library README.md](./stitch-library/README.md)

Multiple examples are provided the stitch-library-examples module. See [Examples README.md](./stitch-library-examples/README.md).


References
----------

[1] IETF JSON Patch - https://tools.ietf.org/html/draft-ietf-appsawg-json-patch-10

[2] IETF JSON Pointer - https://tools.ietf.org/html/draft-ietf-appsawg-json-pointer-07
