package blockchain;

import blockchain.model.Block;
import blockchain.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockChain {
    private List<Block> blockChain;
    private volatile List<String> messageList;
    private volatile int prefixLength;
    private String prefixChar;
    private static volatile AtomicInteger currentBlockIndex = new AtomicInteger(0);//CHANGE!!

    public BlockChain() {
        this.blockChain = Collections.synchronizedList(new LinkedList<Block>());
        this.messageList = Collections.synchronizedList(new ArrayList<String>());
        prefixLength = 0;
        prefixChar = "0";
        currentBlockIndex.getAndIncrement();
    }

    public synchronized void addBlock(Block block) {
//        if (currentBlockIndex.get() == 6) {
//            System.out.println("MAXIMUM BLOCKCHAIN SIZE REACHED!");
//            return;
//        }

        int currentBlockId = block.getId();
//        int newId = blockChain.size() + 1;
        int newId = currentBlockIndex.get();//CHANGE!!

        //Checks again to make sure that the id of the new block is correct
        if(currentBlockId != newId) {
            //block.setId(newId);
            block.setId(currentBlockIndex.get()); //CHANGE!!
        }

        blockChain.add(block);
        currentBlockIndex.getAndIncrement();//CHANGE!!
        //Updates the hashcode prefix rules according to the time needed to generate the block(if it's less than 60 seconds it increases the complexity of the prefix otherwise it decreases it)
        regulateBlockCreation(block);

    }

    public void display() {
        for (Block currentBlock : blockChain) {
            System.out.println(currentBlock.toString());
        }
    }

    public boolean isValidBlockchain() {
        int blockChainSize = blockChain.size();

        if(blockChainSize == 0) {
            return false;
        }

        //If a single element is present no other checks are performed and the blockchain is considered valid
        if (blockChainSize == 1) {
            return true;
        }

        String previousBlockCurrentHash = blockChain.get(0).getCurrentHash();
        for (int i = 0; i < blockChain.size(); i++) {
            Block block = blockChain.get(i);

            String currentBlockPreviousHash = null;

            //For the first block there's no previous hash stored
            if (i == 0) {
                currentBlockPreviousHash = block.getCurrentHash();
            } else {
                currentBlockPreviousHash = block.getPreviousHash();
            }

            if (!currentBlockPreviousHash.equals(previousBlockCurrentHash)) {
                return false;
            }

            //Sets the hash of the previous block for comparison with the next block from the list
            previousBlockCurrentHash = block.getCurrentHash();
        }

        return true;
    }

    public synchronized boolean isValidBlock(Block newBlock) {
        if(blockChain.size() == 0) {
            return true;
        }

        if(newBlock == null) {
            return false;
        }

        //Retrieves the last valid block that was inserted into the blockchain
        int lastValidBlockIndex = blockChain.size() - 1;
        Block lastValidBlock = blockChain.get(lastValidBlockIndex);

        //Checks if the previous hash from the new block matches the current hash of the last valid block from the blockchain
        boolean hashCodesMatch = newBlock.getPreviousHash().equals(lastValidBlock.getCurrentHash());

        String newBlockCurrentHash = newBlock.getCurrentHash();
        String requiredPrefix = SecurityUtils.generateRequiredPrefix(prefixChar, prefixLength);

        //The prefix must not be an empty string and it must be present at the beginning of the hashcode
        boolean isValidPrefix = !"".equals(requiredPrefix) && newBlockCurrentHash.startsWith(requiredPrefix);

        return hashCodesMatch && isValidPrefix;
    }

    public synchronized int getBlockIndex() {
//        int currentListSize = this.blockChain.size();
//
//        //Empty list scenario
//        if (currentListSize == 0) {
//            return 1;
//        }
//
//        Block lastBlock = this.blockChain.get(currentListSize - 1);
//
//        if (lastBlock == null) {
//            return 1;
//        }

        //ID generation when the blockchain contains at least an element
//        int previousBlockId = lastBlock.getId();
        //int newBlockId = previousBlockId + 1;
        int newBlockId = currentBlockIndex.get();//CHANGE!!
        //int newBlockId = blockChain.size() + 1;

        return newBlockId;
    }

    public synchronized String getPreviousBlockHash() {
        int currentListSize = this.blockChain.size();

        //Empty list scenario
        if (currentListSize == 0) {
            return "0";
        }

        Block lastBlock = this.blockChain.get(currentListSize - 1);

        if (lastBlock == null) {
            return "0";
        }

        //Returns the hash of the previous block
        return lastBlock.getCurrentHash();
    }

    public synchronized int getSize() {
        //return this.blockChain.size();
        return currentBlockIndex.get();//CHANGE!!
    }

    private void regulateBlockCreation(Block newBlock) {
        if (newBlock == null) {
            return;
        }

        int timeRequiredForGeneration = newBlock.getGenerationTime();

        String message = null;
        if(timeRequiredForGeneration > 60) {
            if(prefixLength > 0) {
                prefixLength--;
                message = String.format("N was decreased by %d", prefixLength);
            }
        } else if(timeRequiredForGeneration < 10){
            prefixLength++;
            message = String.format("N was increased by %d", prefixLength);
        } else {
            message = "N stays the same";
        }

        newBlock.setAdditionalInfo(message);
        //Clears the message list if a new block was successfully generated
        messageList.clear();
    }

    public int getPrefixLength() {
        return this.prefixLength;
    }

    public String getPrefixChar() {
        return this.prefixChar;
    }

    public synchronized void addMessage(String newMessage) {
        messageList.add(newMessage);
    }

    public synchronized String getMessage() {
        System.out.println("MESSAGE LIST SIZE: " + messageList.size());
        return this.messageList
                .stream()
                .reduce("", (x, y) -> x + y + "\n");
    }


}
