package com.jingyuyao.tactical.map;

import com.badlogic.gdx.maps.tiled.TiledMap;

public interface MapFactory {
    Map create(TiledMap tiledMap);
}
