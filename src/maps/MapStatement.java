package maps;

import expressions.Expression;
import expressions.TypedValue;
import variables.Statement;
import variables.VariableTable;

import java.util.List;

public class MapStatement extends Statement {
    private final String mapName;
    private final String operation;
    private final List<Expression> arguments;

    public MapStatement(String mapName, String operation, List<Expression> arguments) {
        this.mapName = mapName;
        this.operation = operation;
        this.arguments = arguments;
    }

    @Override
    public void execute(VariableTable table) {
        TypedValue value = table.getVariable(mapName);

        if (!(value.getValue() instanceof MapZard map)) {
            throw new RuntimeException(mapName + " não é um Map");
        }

        switch (operation) {
            case "put" -> {
                if (arguments.size() != 2) {
                    throw new RuntimeException("Uso correto: map.put(chave, valor)");
                }
                Object key = arguments.get(0).evaluate(table).getValue();
                Object val = arguments.get(1).evaluate(table).getValue();
                map.put(key, val);
            }

            case "get" -> {
                if (arguments.size() != 1) {
                    throw new RuntimeException("Uso correto: map.get(chave)");
                }
                Object key = arguments.get(0).evaluate(table).getValue();
                System.out.println(map.get(key));
            }

            case "remove" -> {
                if (arguments.size() != 1) {
                    throw new RuntimeException("Uso correto: map.remove(chave)");
                }
                Object key = arguments.get(0).evaluate(table).getValue();
                map.remove(key);
            }

            case "size" -> {
                if (!arguments.isEmpty()) {
                    throw new RuntimeException("Uso correto: map.size()");
                }
                System.out.println(map.size());
            }

            case "clear" -> {
                if (!arguments.isEmpty()) {
                    throw new RuntimeException("Uso correto: map.clear()");
                }
                map.clear();
            }

            default -> throw new RuntimeException("Operação inválida: " + operation);
        }
    }
}

