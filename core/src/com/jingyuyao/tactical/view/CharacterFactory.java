package com.jingyuyao.tactical.view;

import com.jingyuyao.tactical.model.Character;

import javax.inject.Inject;
import javax.inject.Provider;

class CharacterFactory {
    private final Provider<Highlighter> highlighterProvider;

    @Inject
    CharacterFactory(Provider<Highlighter> highlighterProvider) {
        this.highlighterProvider = highlighterProvider;
    }

    MapActor<Character> create(int x, int y, float size) {
        return new MapActor<Character>(new Character(x, y), size, highlighterProvider.get());
    }
}
