package io.github.natank25.scp_byo.persistent_data.cca.interfaces;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;

import java.util.List;

public interface ListComponent<T> extends ComponentV3
{
    List<T> getList();
    void add(T value);
    void remove(T value);
}
