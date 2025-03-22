package maps;

import expressions.Expression;
import expressions.TypedValue;
import variables.VariableTable;

import java.util.HashMap;
import java.util.Map;

public class MapExpression extends Expression {
    private final Map<Expression, Expression> elements;

    public MapExpression(Map<Expression, Expression> elements) {
        this.elements = elements;
    }

    public Map<Expression, Expression> getElements() {
        return elements;
    }

    @Override
    public TypedValue evaluate(VariableTable table) {
        Map<Object, Object> evaluatedMap = new HashMap<>();

        for (Map.Entry<Expression, Expression> entry : elements.entrySet()) {
            Object key = entry.getKey().evaluate(table).getValue();
            Object value = entry.getValue().evaluate(table).getValue();
            evaluatedMap.put(key, value);
        }

        return new TypedValue(new MapZard(evaluatedMap), "map");
    }

    @Override
    public String toString() {
        return "MapExpression{" + elements + "}";
    }
}

