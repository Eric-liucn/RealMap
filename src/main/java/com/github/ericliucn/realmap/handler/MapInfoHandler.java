package com.github.ericliucn.realmap.handler;

import com.github.ericliucn.realmap.Main;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.map.MapCanvas;
import org.spongepowered.api.map.MapInfo;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapInfoHandler {

    public static MapInfoHandler instance;
    private Map<UUID, MapInfo> mapInfoMap;

    public MapInfoHandler(){
        instance = this;
        reload();
    }

    @Nullable
    public MapInfo getNewFrameMapInfo(UUID uuid){
        if (!mapInfoMap.containsKey(uuid)) return null;
        MapInfo mapInfo = mapInfoMap.get(uuid);
        List<MapCanvas> mapCanvasList = mapInfo.get(Main.MAP_FRAMES).get();
        int currentFrameIndex = mapInfo.get(Main.MAP_CURRENT_FRAME).get();
        int newFrameIndex = currentFrameIndex + 1;
        if (newFrameIndex >= mapCanvasList.size()){
            mapInfo.offer(Main.MAP_CURRENT_FRAME, 0);
            return mapInfo;
        }
        MapCanvas mapCanvas = mapCanvasList.get(newFrameIndex);
        if (mapCanvas == null){
            mapInfo.offer(Main.MAP_CURRENT_FRAME, 0);
        }else {
            mapInfo.offer(Main.MAP_CURRENT_FRAME, newFrameIndex);
            mapInfo.offer(Keys.MAP_CANVAS, mapCanvas);
        }
        return mapInfo;
    }

    public void reload(){
        mapInfoMap = new ConcurrentHashMap<>();
        for (MapInfo mapInfo : Sponge.server().mapStorage().allMapInfos()) {
            if (mapInfo.supports(Main.MAP_NAME)){
                mapInfoMap.put(mapInfo.uniqueId(), mapInfo);
            }
        }
    }

}
