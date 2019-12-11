package me.encast.clara.util.map.chunk;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClaraChunk {

    private int x;
    private int z;
    private String biomeId;
    private boolean dungeonGate;

    public ClaraChunk() {
    }
}
