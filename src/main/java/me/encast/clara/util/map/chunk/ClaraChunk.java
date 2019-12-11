package me.encast.clara.util.map.chunk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ClaraChunk {

    private int x;
    private int z;
    private String biomeId;
    private boolean dungeonGate;

    public ClaraChunk() {
    }

    public ClaraChunk(int x, int z) {
        this.x = x;
        this.z = z;
    }
}
