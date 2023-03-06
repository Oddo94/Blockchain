package blockchain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MessageGenerator implements Runnable{
    private BlockChain blockChain;
    private List<String> messageAuthors;

    public MessageGenerator(BlockChain blockChain) {
        this.blockChain = blockChain;
        messageAuthors = new ArrayList<>(Arrays.asList("John", "Michael", "Andrew", "Jessica", "George", "Britney", "Emma", "Amelia"));
    }

    @Override
    public void run() {
        String randomMessage = generateRandomMessage();
        blockChain.addMessage(randomMessage);

    }

    private String generateRandomMessage() {
        String messageAuthor = getRandomMessageAuthor();
        UUID randomMessage= UUID.randomUUID();

        return String.format("%s: %s.", messageAuthor, randomMessage);
    }

    private String getRandomMessageAuthor() {
        int lowerBound = 0;
        int upperBound = messageAuthors.size() -1;

        SecureRandom srand = new SecureRandom(SecureRandom.getSeed(32));
        int index = (int)((upperBound - lowerBound) * srand.nextDouble() + lowerBound);

        return messageAuthors.get(index);
    }
}
