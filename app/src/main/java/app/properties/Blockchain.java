package app.properties;

import app.utilities.StringUtil;

import java.util.LinkedList;

public class Blockchain extends LinkedList<Block> {

    public boolean isValid() {
        for (Block block : this) {
            String calculatedHash =
                    StringUtil.applySha256(
                            block.getPreviousBlockHash()
                                    + Long.toString(block.getTimestamp())
                                    + Integer.toString(block.getIndex())
                    );
            if (!block.getHash().equals(calculatedHash)) {
                return false;
            }
        }
        return true;
    }
}
