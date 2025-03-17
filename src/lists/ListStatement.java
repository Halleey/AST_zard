package lists;

import expressions.Expression;
import expressions.TypedValue;
import variables.Statement;
import variables.VariableTable;

import java.util.List;

public class ListStatement extends Statement {
    private final String listName;
    private final String operation;
    private final List<Expression> arguments;

    public ListStatement(String listName, String operation, List<Expression> arguments) {
        this.listName = listName;
        this.operation = operation;
        this.arguments = arguments;
    }

    @Override
    public void execute(VariableTable table) {
        TypedValue value = table.getVariable(listName);

        if (!(value.getValue() instanceof ZardList)) {
            throw new RuntimeException("Erro: " + listName + " não é uma lista.");
        }

        ZardList list = (ZardList) value.getValue();

        switch (operation) {
            case "add" -> {
                if (arguments.size() != 1) {
                    throw new RuntimeException("Erro: add() espera um único argumento.");
                }
                list.add(arguments.get(0).evaluate(table).getValue());
            }
            case "remove" -> {
                if (arguments.size() != 1) {
                    throw new RuntimeException("Erro: remove() espera um único argumento (índice).");
                }
                int index = (int) arguments.get(0).evaluate(table).getValue();
                list.remove(index);
            }
            case "get" -> {
                if (arguments.size() != 1) {
                    throw new RuntimeException("Erro: get() espera um único argumento (índice).");
                }
                int index = (int) arguments.get(0).evaluate(table).getValue();
                System.out.println(list.get(index)); // Apenas imprime o valor por enquanto
            }
            default -> throw new RuntimeException("Erro: Operação desconhecida em lista: " + operation);
        }
    }

    @Override
    public String toString() {
        return "ListStatement{" + "listName='" + listName + "', operation='" + operation + "', arguments=" + arguments + "}";
    }
}

