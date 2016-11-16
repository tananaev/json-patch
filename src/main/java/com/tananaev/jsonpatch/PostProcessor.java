/*
 Copyright 2014 C. A. Fitzgerald

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.tananaev.jsonpatch;

import com.tananaev.jsonpatch.operation.MoveOperation;
import com.tananaev.jsonpatch.operation.RemoveOperation;
import com.tananaev.jsonpatch.operation.AbsOperation;

public class PostProcessor {

    public void process(JsonPatch patch) {

        if (patch.isEmpty()){
            return;
        }

        AbsOperation operation = patch.getLast();

        if (operation.getOperationName().equals("remove")) {
            if (isNumeric(operation.path.tail())) {
                //unwindMoves(patch);
            }
        }
    }

    private void unwindMoves(JsonPatch patch) {

        RemoveOperation removeOperation = (RemoveOperation) patch.removeLast();

        while (!patch.isEmpty()){
            AbsOperation priorOperation = patch.removeLast();
            if (priorOperation.getOperationName().equals("move")) {
                if (priorOperation.path.head().equals(removeOperation.path.head())) {
                    //operating on same element
                    MoveOperation priorMove = (MoveOperation) priorOperation;
                    if (priorMove.path.head().equals(priorMove.from.head())) {
                        //move is within local element
                        try {
                            int removeIndex = Integer.parseInt(removeOperation.path.tail());
                            int moveToIndex = Integer.parseInt(priorMove.path.tail());
                            int moveFromIndex = Integer.parseInt(priorMove.from.tail());

                            if (moveToIndex < moveFromIndex) {
                                if (removeIndex - moveToIndex == 1) {
                                    removeOperation.path = priorMove.path;
                                }
                            } else {
                                patch.addLast(priorOperation);
                                break;
                            }
                        } catch ( NumberFormatException e){
                            patch.addLast(priorOperation);
                            break;
                        }
                    } else {
                        patch.addLast(priorOperation);
                        break;
                    }
                } else {
                    patch.addLast(priorOperation);
                    break;
                }
            } else {
                patch.addLast(priorOperation);
                break;
            }
        }

        patch.addLast(removeOperation);

    }

    public static boolean isNumeric(String str){
        try{
            Integer.parseInt(str);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
