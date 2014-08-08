package net.riotopsys.json_patch;

import net.riotopsys.json_patch.operation.AbsOperation;
import net.riotopsys.json_patch.operation.MoveOperation;
import net.riotopsys.json_patch.operation.RemoveOperation;

/**
 * Created by afitzgerald on 8/7/14.
 */
public class PostProcessor {

    public void process(JsonPatch patch) {

        if ( patch.isEmpty() ){
            return;
        }

        AbsOperation operation = patch.getLast();

        if ( operation.getOperationName().equals("remove")){
            if ( isNumeric(operation.path.tail()) ) {
                unwindMoves(patch);
            }

        }

    }

    private void unwindMoves(JsonPatch patch) {

        RemoveOperation removeOperation = (RemoveOperation) patch.removeLast();

        while ( !patch.isEmpty()){
            AbsOperation priorOperation = patch.removeLast();
            if ( priorOperation.getOperationName().equals("move")){
                if ( priorOperation.path.head().equals(removeOperation.path.head())){
                    //operating on same element
                    MoveOperation priorMove = (MoveOperation) priorOperation;
                    if ( priorMove.path.head().equals(priorMove.from.head())){
                        //move is within local element

//                        if ( removeOperation.path.equals(priorMove.from) ){
                            int removeIndex = Integer.parseInt(removeOperation.path.tail());
                            int moveIndev = Integer.parseInt(priorMove.path.tail());
                            if ( removeIndex - moveIndev == 1 ){
                                removeOperation.path = priorMove.path;
                            }

//                        } else {
//                            patch.addLast(priorOperation);
//                            break;
//                        }

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
            int d = Integer.parseInt(str);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }


}
