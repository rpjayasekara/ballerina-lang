 // Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 //
 // WSO2 Inc. licenses this file to you under the Apache License,
 // Version 2.0 (the "License"); you may not use this file except
 // in compliance with the License.
 // You may obtain a copy of the License at
 //
 //   http://www.apache.org/licenses/LICENSE-2.0
 //
 // Unless required by applicable law or agreed to in writing,
 // software distributed under the License is distributed on an
 // "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 // KIND, either express or implied.  See the License for the
 // specific language governing permissions and limitations
 // under the License.

function f1() {
    int i = 0; // unused `i`

    int j; // unused `j`

    int k; // unused `k`
    k = 0;
}

function f2() {
    int l; // used `l`
    l = 0;
    int[] arr = []; // unused `arr`
    arr[l] = 1;

    int m = 0; // used `m`
    arr[m] = 1;

    string n = "x"; // used `n`

    record {int x;} o = {x: 12}; // unused `o`
    o.x = 1;
    o[n] = 1;

    int[] arr2 = []; // used `arr2`
    int l2 = 1; // used `l2`
    int i = arr2[l2]; // unused `i`
    arr2[l] = 1;

    record {int x;} o2 = {x: 12}; // used `o2`
    int _ = o2.x;

    string n2 = "x"; // used `n2`
    record {int x;} o3 = {x: 12}; // used `o3`
    anydata a = o3[n2]; // unused `a`
}

function f3() {
    int[3] [a, b, c] = [1, 2, 3]; // unused `a`, `b`, `c`

    [int, record { boolean e; string f; }, boolean...] [d, {e, ...f}, ...g] = // unused `d`, `e`, `f`, `g`
        [1, {e: true, f: "str"}, true, false];
    [d, f] = [1, {f: ""}];

    [int, error<record { int i; string j?; }>, [int, string]] [h, error(m, i = i, ...k), l] = // unused `h`, `m`, `i`, `k`, `l`
        [1, error("err", i = 1), [1, ""]];
    h = 1;
    m = "";
    i = 2;
    k = {};
    l = [];
}

function f4() {
    int[3] [a, b, c] = [1, 2, 3]; // unused `c`
    int _ = a + b;

    [int, record { boolean e; string f; }, boolean...] [d, {e, ...f}, ...g] = // unused `f`
        [1, {e: true, f: "str"}, true, false];
    _ = [d, e, g];

    [int, error<record { int i; string j?; }>, [int, string]] [h, error(m, i = i, ...k), l] = // unused `m`, `k`
        [1, error("err", i = 1), [1, ""]];
    h = l[0] + i;
}

function f5() {
    int i = 1; // used within anon function
    int j = 2; // used within anon function
    int k = 3; // used within anon function
    int l = 4; // unused

    var _ = function () returns int {
        int m; // unused
        int n = 1; // used within anon function
        int o = 2; // used within anon function

        function () returns int _ = () => n;

        var p = function () {
            _ = o;
        };

        return i;
    };

    function (int v) returns string _ = function (int v) returns string => (v + j).toString();

    function () returns int _ = () => k * 1;
}

function f6() {
    worker w1 { // Transformed to an async call, warnings shouldn't be logged for those variables.
    }

    future<()> a = start fn(); // unused
}

function fn() {
}

function f7() returns int {
    do {
        check foo();
        check bar();
    } on fail error e { // unused
        return 1;
    }

    return 0;
}

function foo() returns error? => ();
function bar() returns error? => ();

function f8() {
    error? err = ();
    if err is () { // used

    }
}

int globalVar = 0; // OK, warning only for local variables

record {
    int a;
    error b;
    string[2] c;
} {a: ga, b: error(gbm, ...gbr), c: [gc1, gc2]} = {a: 1, b: error(""), c: []}; // OK, warning only for local variables

function f9(int a, int b = 1, int... c) { // OK, warning only for local variables
}

function f11() {
    var l = 0; // used `l`
    var arr = <int[]> []; // unused `arr`
    arr[l] = 1;

    var n = "x"; // used `n`

    var o = <record {int x;}> {x: 12}; // unused `o`
    o.x = 1;
    o[n] = 1;

    var arr2 = <int[]> []; // used `arr2`
    var l2 = 1; // used `l2`
    var i = arr2[l2]; // unused `i`
    arr2[l] = 1;

    var o2 = <record {int x;}> {x: 12}; // used `o2`
    int _ = o2.x;

    var n2 = "x"; // used `n2`
    var o3 = <record {int x;}> {x: 12}; // used `o3`
    var a = o3[n2]; // unused `a`
}
