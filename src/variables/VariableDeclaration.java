package variables;

import expressions.BinaryExpression;
import expressions.Expression;
import expressions.LiteralExpression;
import expressions.TypedValue;
import lists.ListExpression;
import lists.ZardList;
import maps.MapExpression;
import maps.MapZard;
import tokens.Token;
import variables.exceptions.ExceptionVar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableDeclaration extends Statement {
    private final Token type;
    private final String name;
    private final Expression value;

    public VariableDeclaration(Token type, String name, Expression value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public void execute(VariableTable table) {
        if (table.hasVariable(name)) {
            throw new ExceptionVar(name);
        }

        Object evaluatedValue = (value != null) ? evaluateExpressionWithCollectionSupport(value, table) : getDefaultValue();

        table.setVariable(name, new TypedValue(evaluatedValue, type.getValue()));
    }

    private Object evaluateExpressionWithCollectionSupport(Expression value, VariableTable table) {
        if (value instanceof ListExpression listExpr) {
            return createZardList(listExpr, table);
        } else if (value instanceof MapExpression mapExpr) {
            return createZardMap(mapExpr, table);
        }
        return evaluateExpression(value, table);
    }

    private ZardList createZardList(ListExpression listExpr, VariableTable table) {
        List<Object> evaluatedElements = listExpr.getElements().stream()
                .map(expr -> expr.evaluate(table).getValue())
                .toList();
        return new ZardList(evaluatedElements);
    }

    private MapZard createZardMap(MapExpression mapExpr, VariableTable table) {
        Map<Object, Object> evaluatedMap = new HashMap<>();

        for (Map.Entry<Expression, Expression> entry : mapExpr.getElements().entrySet()) {
            Object key = entry.getKey().evaluate(table).getValue();
            Object value = entry.getValue().evaluate(table).getValue();
            evaluatedMap.put(key, value);
        }

        return new MapZard(evaluatedMap);
    }

    private Object evaluateExpression(Expression expr, VariableTable table) {
        if (expr instanceof LiteralExpression) {
            String value = ((LiteralExpression) expr).token.getValue();
            return convertToType(value, type.getValue());
        } else if (expr instanceof ListExpression) {
            return ((ListExpression) expr).getElements();
        } else if (expr instanceof BinaryExpression) {
            return expr.evaluate(table).getValue();
        } else if (expr instanceof VariableReference) {
            return expr.evaluate(table).getValue();
        }

        throw new RuntimeException("Erro ao avaliar expressão: tipo desconhecido " + expr.getClass().getSimpleName());
    }

    private Object convertToType(String value, String type) {
        return switch (type) {
            case "int" -> Integer.parseInt(value);
            case "double" -> Double.parseDouble(value);
            case "bool" -> Boolean.parseBoolean(value);
            case "string" -> value;
            case "list", "map" -> throw new RuntimeException("Erro: Listas e Mapas devem ser avaliados separadamente.");
            default -> throw new RuntimeException("Tipo desconhecido: " + type);
        };
    }

    private Object getDefaultValue() {
        return switch (type.getValue()) {
            case "int" -> 0;
            case "double" -> 0.0;
            case "string" -> "";
            case "bool" -> false;
            case "list" -> new ZardList(new ArrayList<>());
            case "map" -> new MapZard(new HashMap<>());
            default -> null;
        };
    }

    public Token getType() {
        return type;
    }

    public Expression getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "VariableDeclaration{name='" + name + "', type='" + type.getValue() + "', value=" + value + "}";
    }
}
