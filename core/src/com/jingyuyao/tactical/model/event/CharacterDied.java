package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Character;

public class CharacterDied extends ObjectEvent<Character> {
    public CharacterDied(Character object) {
        super(object);
    }
}
