package blockchain;

import blockchain.model.Block;
import blockchain.utils.SecurityUtils;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class BlockGenerator implements Runnable {
    private BlockChain blockChain;
    private int maxSize;

    @Override
    public void run() {

        String requiredPrefixChar = blockChain.getPrefixChar();
        int totalCharCount = blockChain.getPrefixLength();
        int currentBlockChainSize = blockChain.getSize();
        boolean hasGeneratedValidBlock = false;
        Block generatedBlock = null;

        long startTime = System.currentTimeMillis();
        while(currentBlockChainSize < maxSize) {
            int newBlockId = blockChain.getBlockIndex();
            String previousBlockHash = blockChain.getPreviousBlockHash();

            generatedBlock = generateBlock(requiredPrefixChar, totalCharCount);

            if(blockChain.isValidBlock(generatedBlock)) {
                hasGeneratedValidBlock = true;
                break;
            }
            currentBlockChainSize = blockChain.getSize();
        }

        long endTime = System.currentTimeMillis();
        int generationTime = (int) (endTime - startTime) / 1000;

        if(hasGeneratedValidBlock) {
            //Checks again just in case another thread has generated a valid block in the meantime
            if(blockChain.getSize() > 0) {
                generatedBlock.setId(blockChain.getBlockIndex());
                generatedBlock.setPreviousHash(blockChain.getPreviousBlockHash());
            }

            //Sets the time needed to successfully generate the block
            generatedBlock.setGenerationTime(generationTime);

            //Adds block to blockchain
            blockChain.addBlock(generatedBlock);
        }


    }
    private synchronized Block generateBlock(String requiredPrefixChar, int totalCharCount) {
        //Retrieves the elements from which the hashcode of the block will be generated
        int id = blockChain.getBlockIndex();
        int minerThreadNumber = extractThreadNumber(Thread.currentThread().getName());
        long timeStamp = new Date().getTime();
        int magicNumber = SecurityUtils.generateMagicNumber();
        String previousHash = blockChain.getPreviousBlockHash();

        //This variable will hold the hashcode value once this complies to all the requirements
        String currentHash = null;

        //Creates the input which will be used to generate the hashcode
        String hashMethodInput = String.format("%d%d%%ds%s", id, timeStamp, magicNumber, previousHash, currentHash);

        //Retrieves the required prefix of the hashcode(containing the specified character and having the specified length)
        String hashCodePrefix = SecurityUtils.generateRequiredPrefix(requiredPrefixChar, totalCharCount);

        currentHash = SecurityUtils.applySha256(hashMethodInput);

        magicNumber = SecurityUtils.generateMagicNumber();
        hashMethodInput = String.format("%d%d%%ds%s", id, timeStamp, magicNumber, previousHash, currentHash);

        Block block = new Block(id, minerThreadNumber, timeStamp, magicNumber, previousHash, currentHash, null,  0, null);

        return block;
    }

    private int extractThreadNumber(String threadName) {
        int threadNumber = 0;
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(threadName);

        if(matcher.find()) {
            threadNumber = Integer.parseInt(matcher.group());
        }

        return threadNumber;
    }
}
