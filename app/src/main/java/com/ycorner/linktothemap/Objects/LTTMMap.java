package com.ycorner.linktothemap.Objects;

/**
 * Created by Michael Yoon Huh on 10/22/2015.
 */
public class LTTMMap {

    private int mapResourceId;
    private String mapLevelId;
    private String mapName;

    private int mapLevelB1;
    private int mapLevelB2;
    private int mapLevelB3;
    private int mapLevelB4;
    private int mapLevelB5;
    private int mapLevel1F;
    private int mapLevel2F;
    private int mapLevel3F;
    private int mapLevel4F;
    private int mapLevel5F;

    public LTTMMap() {
        this.mapLevel1F = -1;
        this.mapLevel2F = -1;
        this.mapLevel3F = -1;
        this.mapLevel4F = -1;
        this.mapLevel5F = -1;
        this.mapLevelB1 = -1;
        this.mapLevelB2 = -1;
        this.mapLevelB3 = -1;
        this.mapLevelB4 = -1;
        this.mapLevelB5 = -1;
    }

    public LTTMMap(int level1F, int level2F, int level3F, int level4F, int level5F, int levelB1, int levelB2, int levelB3, int levelB4, int levelB5) {
        this.mapLevel1F = level1F;
        this.mapLevel2F = level2F;
        this.mapLevel3F = level3F;
        this.mapLevel4F = level4F;
        this.mapLevel5F = level5F;
        this.mapLevelB1 = levelB1;
        this.mapLevelB2 = levelB2;
        this.mapLevelB3 = levelB3;
        this.mapLevelB4 = levelB4;
        this.mapLevelB5 = levelB5;
    }


    public int getMapResourceId() {
        return mapResourceId;
    }

    public void setMapResourceId(int mapResourceId) {
        this.mapResourceId = mapResourceId;
    }

    public String getMapLevelId() {
        return mapLevelId;
    }

    public void setMapLevelId(String mapLevelId) {
        this.mapLevelId = mapLevelId;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
