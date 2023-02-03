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
    private int id;
    private long timeStamp;
    private String previousHash;
    private String currentHash;

    public String toString() {
        return String.format("Block:\nId: %d\nTimestamp: %d\nHash of the previous block:\n%s\nHash of the block:\n%s\n", this.id, this.timeStamp, this.previousHash, this.currentHash);
    }
}
