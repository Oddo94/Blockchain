package blockchain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Block {
    private int minerNumber;
    private int id;
    private long timeStamp;
    private int magicNumber;
    private String previousHash;
    private String currentHash;
    private String blockData;
    private int generationTime;
    private String additionalInfo;

    public String toString() {
        return String.format("Block:\nCreated by miner # %d\nId: %d\nTimestamp: %d\nMagic number:%d\nHash of the previous block:\n%s\nHash of the block:\n%s\nBlock data:\n%s\nBlock was generating for %d seconds\n%s\n",
                this.minerNumber, this.id, this.timeStamp, this.magicNumber, this.previousHash, this.currentHash, this.blockData, this.generationTime, this.additionalInfo);
    }
}
