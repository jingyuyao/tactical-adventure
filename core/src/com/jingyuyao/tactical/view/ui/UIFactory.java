package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.model.state.Action;

public interface UIFactory {

  ActionButton createActionButton(Action action);
}
