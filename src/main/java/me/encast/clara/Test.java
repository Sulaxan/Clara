package me.encast.clara;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.encast.clara.util.map.ClaraMapResource;
import me.encast.clara.util.map.SerializableMaterialData;
import org.bukkit.Material;

public class Test {

    public static void main(String[] args) {
        ClaraMapResource resource = new ClaraMapResource();
        int[][] CIRCLE = new int[16][16];
        CIRCLE[6][2] = 1;
        CIRCLE[7][2] = 1;
        CIRCLE[8][2] = 1;
        CIRCLE[9][2] = 1;
        CIRCLE[10][3] = 1;
        CIRCLE[11][3] = 1;
        CIRCLE[12][4] = 1;
        CIRCLE[12][5] = 1;
        CIRCLE[13][6] = 1;
        CIRCLE[13][7] = 1;
        CIRCLE[13][8] = 1;
        CIRCLE[13][9] = 1;
        CIRCLE[12][10] = 1;
        CIRCLE[12][11] = 1;
        CIRCLE[11][12] = 1;
        CIRCLE[10][12] = 1;
        CIRCLE[9][13] = 1;
        CIRCLE[8][13] = 1;
        CIRCLE[7][13] = 1;
        CIRCLE[6][13] = 1;
        CIRCLE[5][12] = 1;
        CIRCLE[4][12] = 1;
        CIRCLE[3][11] = 1;
        CIRCLE[3][10] = 1;
        CIRCLE[2][9] = 1;
        CIRCLE[2][8] = 1;
        CIRCLE[2][7] = 1;
        CIRCLE[2][6] = 1;
        CIRCLE[3][5] = 1;
        CIRCLE[3][4] = 1;
        CIRCLE[4][3] = 1;
        CIRCLE[5][3] = 1;


        // Overhang
        CIRCLE[6][1] = 2;
        CIRCLE[7][1] = 2;
        CIRCLE[8][1] = 2;
        CIRCLE[9][1] = 2;
        CIRCLE[10][2] = 2;
        CIRCLE[11][2] = 2;
        CIRCLE[12][3] = 2;
        CIRCLE[13][4] = 2;
        CIRCLE[13][5] = 2;
        CIRCLE[14][6] = 2;
        CIRCLE[14][7] = 2;
        CIRCLE[14][8] = 2;
        CIRCLE[14][9] = 2;
        CIRCLE[13][10] = 2;
        CIRCLE[13][11] = 2;
        CIRCLE[12][12] = 2;
        CIRCLE[11][13] = 2;
        CIRCLE[10][13] = 2;
        CIRCLE[9][14] = 2;
        CIRCLE[7][14] = 2;
        CIRCLE[8][14] = 2;
        CIRCLE[6][14] = 2;
        CIRCLE[5][13] = 2;
        CIRCLE[4][13] = 2;
        CIRCLE[3][12] = 2;
        CIRCLE[2][11] = 2;
        CIRCLE[2][10] = 2;
        CIRCLE[1][9] = 2;
        CIRCLE[1][8] = 2;
        CIRCLE[1][7] = 2;
        CIRCLE[1][6] = 2;
        CIRCLE[2][5] = 2;
        CIRCLE[2][4] = 2;
        CIRCLE[3][3] = 2;
        CIRCLE[4][2] = 2;
        CIRCLE[5][2] = 2;
        resource.setIslandAttributes(CIRCLE);
        resource.setMaterials(new SerializableMaterialData[] {
                new SerializableMaterialData(Material.GRASS, (byte) 0)
        });
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(resource));
    }
}
