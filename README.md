# json-patch
An RFC 6902 (JSON Patch) compliant utility

Status Status: [![Build Status](https://secure.travis-ci.org/riotopsys/json-patch.png)](http://travis-ci.org/riotopsys/json-patch)

## Summary
This is a java implementation of [RFC 6902 (JSON Patch)][1]

## Licence

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


## Usage

### Patch creation
```
JsonPatchFactory jpf = new JsonPatchFactory();
JsonPatch patch = jpf.create(original, target);
```

### Patch application
```
 JsonElement target = patch.apply(original);
```

## Download
TBD


[1]:http://tools.ietf.org/html/rfc6902