package ru.dasxunya.other;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.dasxunya.core.HumanBeing;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

@Data
@Jacksonized
public class Keeper implements Serializable {

    private final LinkedList<HumanBeing> humanBeings = new LinkedList<>();

    private final Set<String> scriptNames = new TreeSet<>();

    public boolean addScriptName(String name) {
        return scriptNames.add(name);
    }

    public void clearScriptNames() {
        scriptNames.clear();
    }
}
