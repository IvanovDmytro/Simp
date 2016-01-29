#Groundskeeper Willie

![willie](https://upload.wikimedia.org/wikipedia/en/d/dc/GroundskeeperWillie.png)

Utility project that contains reusable blocks, mainly data structures and algorithms.

###Immutable arrays

Considering rise of functional programming that has immutability as one of its corner stones, arrays
are one thing that could not be easily marked/modified to be immutable. 

To address this issue immutable array of all primitive type, Strings and generic array were added.

###Tries

Also called digital tree and sometimes radix tree or prefix tree (as they can be searched by prefixes), 
is an tree data structure that is used to store a dynamic set or associative array where the keys are strings. 
Unlike a binary search tree, no node in the tree stores the key associated with that node; instead, 
its position in the tree defines the key with which it is associated. All the descendants of a node have 
a common prefix of the string associated with that node, and the root is associated with the empty string. 
Values are not necessarily associated with every node. Rather, values tend only to be associated with 
leaves, and with some inner nodes that correspond to keys of interest.

![trie](https://upload.wikimedia.org/wikipedia/commons/b/be/Trie_example.svg)

For more info please [visit][1].

##Download

Download [the latest JAR][1] or grab via Maven:
```xml
<dependency>
  <groupId>org.simp</groupId>
  <artifactId>willie</artifactId>
  <version>0.1.0</version>
</dependency>
```
or Gradle:
```groovy
compile 'org.simp:willie:0.1.0'
```

Willie requires at minimum Java 7 or Android 2.3.

##License

    Copyright 2016 Dmytro Ivanov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [1]: https://en.wikipedia.org/wiki/Trie