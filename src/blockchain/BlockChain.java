package blockchain;

import blockchain.model.Block;
import blockchain.utils.SecurityUtils;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class BlockChain {
    private volatile List<Block> blockChain;
    private static volatile int blockChainSize;
    private volatile int prefixLength;
    private String prefixChar;

    public BlockChain() {
        this.blockChain = Collections.synchronizedList(new LinkedList<Block>());;
        prefixLength = 0;
        prefixChar = "0";
    }

    public synchronized void addBlock(Block block) {
        if (blockChain.size() == 5) {
            System.out.println("MAXIMUM BLOCKCHAIN SIZE REACHED!");
            return;
        }

        int newId = blockChain.size() + 1;
        block.setId(newId);
        blockChain.add(block);
        //Updates the hashcode prefix rules accrding to the time needed to generate the block(if it's less than 60 seconds it increases the complexity of the prefix otherwise it decreases it)
        regulateBlockCreation(block);
        blockChainSize++;

    }

//    public Block generateBlock(String requiredPrefixChar, int totalCharCount) {
//        System.out.println("Inside the generate block method");
//        //Retrieves the elements from which the hashcode of the block will be generated
//        int id = getBlockIndex(blockChain);
//        long timeStamp = new Date().getTime();
//        int magicNumber = SecurityUtils.generateMagicNumber();
//        String previousHash = getPreviousBlockHash(blockChain);
//
//        //This variable will hold the hashcode value once this complies to all the requirements
//        String currentHash = null;
//
//        //Creates the input which will be used to generate the hashcode
//        String hashMethodInput = String.format("%d%d%%ds%s", id, timeStamp, magicNumber, previousHash, currentHash);
//
//        //Retrieves the required prefix of the hashcode(containing the specified character and having the specified length)
//        String hashCodePrefix = SecurityUtils.generateRequiredPrefix(requiredPrefixChar, totalCharCount);
//
//        long startTime = System.currentTimeMillis();
//        while(true) {
//            System.out.println("Trying to find a valid hashcode....");
//            String generatedHashCode = SecurityUtils.applySha256(hashMethodInput);
//            boolean isValidHashCode = SecurityUtils.isValidHashcode(hashCodePrefix, generatedHashCode);
//
//            System.out.println(String.format("Magic number: %d\nGenerated hashcode: %s\nIs valid hashcode: %s\n", magicNumber, generatedHashCode, isValidHashCode));
//
//            if(isValidHashCode) {
//                System.out.println("Found the correct hashcode! Exiting the loop...");
//                currentHash = generatedHashCode;
//                break;
//            }
//
//            //If the generated hashcode does not start with the required prefix a new magic number is generated and the whole process repeats
//            magicNumber = SecurityUtils.generateMagicNumber();
//            hashMethodInput = String.format("%d%d%%ds%s", id, timeStamp, magicNumber, previousHash, currentHash);
//        }
//
//        long endTime = System.currentTimeMillis();
//        int generationTime =(int) (endTime - startTime) / 1000;
//
//        Block block = new Block(id, timeStamp, magicNumber, previousHash, currentHash, generationTime);
//
//        return block;
//    }

    public void display() {
        for (Block currentBlock : blockChain) {
            System.out.println(currentBlock.toString());
        }
    }

    public boolean isValidBlockchain() {
        int blockChainSize = blockChain.size();

//        if(blockChainSize == 0) {
//            return false;
//        }

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
        int currentListSize = this.blockChain.size();

        //Empty list scenario
        if (currentListSize == 0) {
            return 1;
        }

        Block lastBlock = this.blockChain.get(currentListSize - 1);

        if (lastBlock == null) {
            return 1;
        }

        //ID generation when the blockchain contains at least an element
        int previousBlockId = lastBlock.getId();
        int newBlockId = previousBlockId + 1;
//        int newBlockId = blockChainSize + 1;

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
        return blockChainSize;
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
    }

    public int getPrefixLength() {
        return this.prefixLength;
    }

    public String getPrefixChar() {
        return this.prefixChar;
    }



}
