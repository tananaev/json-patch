# JSON Patch Library - json-patch

[![Build Status](https://travis-ci.org/tananaev/json-patch.svg?branch=master)](https://travis-ci.org/tananaev/json-patch)

## Overview

Java implementation of [RFC 6902 (JSON Patch)](http://tools.ietf.org/html/rfc6902) standard. This library is based on Google Gson JSON library.

This project is a fork of the [original library](https://github.com/riotopsys/json-patch) developed by C. A. Fitzgerald.

## Usage

Include dependency via Gradle:
```groovy
compile 'com.tananaev:json-patch:1.1'
```
or Maven:
```xml
<dependency>
  <groupId>com.tananaev</groupId>
  <artifactId>json-patch</artifactId>
  <version>1.1</version>
</dependency>
```

### Patch creation
```
JsonPatchFactory jpf = new JsonPatchFactory();
JsonPatch patch = jpf.create(original, target);
```

### Patch application
```
 JsonElement target = patch.apply(original);
```

## License

    Apache License, Version 2.0

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
