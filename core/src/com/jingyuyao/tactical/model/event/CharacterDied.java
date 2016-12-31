package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Character;

public class CharacterDied extends BaseEvent<Character> {
    public CharacterDied(Character object) {
        super(object);
    }
}
