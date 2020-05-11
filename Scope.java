import java.util.*;

enum ScopeType {
    LOOP, FUNCTION, GLOBAL;
}

public class Scope {
    public Map<String, String> variableTypes = new HashMap<>();
    public Map<String, Value> variableValues = new HashMap<>();
    public ScopeType type;
    public Scope(ScopeType type) {
        this.type = type;
    }
}