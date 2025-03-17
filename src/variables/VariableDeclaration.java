package variables;

import expressions.Expression;
import expressions.LiteralExpression;
import expressions.TypedValue;
import lists.ListExpression;
import tokens.Token;
import variables.exceptions.ExceptionVar;


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

        Object evaluatedValue;
        if (value instanceof ListExpression) {
            evaluatedValue = ((ListExpression) value).getElements(); // Retorna a lista já processada
        } else {
            evaluatedValue = (value != null) ? evaluateExpression(value) : getDefaultValue();
        }

        table.setVariable(name, new TypedValue(evaluatedValue, type.getValue())); // Define o tipo corretamente
    }


    private Object evaluateExpression(Expression expr) {
        if (expr instanceof LiteralExpression) {
            String value = ((LiteralExpression) expr).token.getValue();
            return convertToType(value, type.getValue()); // Converte para o tipo correto
        } else if (expr instanceof ListExpression) {
            return ((ListExpression) expr).getElements(); // Retorna a lista corretamente
        }
        throw new RuntimeException("Erro ao avaliar expressão: tipo desconhecido " + expr.getClass().getSimpleName());
    }



    private Object convertToType(String value, String type) {
        return switch (type) {
            case "int" -> Integer.parseInt(value);
            case "double" -> Double.parseDouble(value);
            case "bool" -> Boolean.parseBoolean(value);
            case "string" -> value;
            case "list" -> throw new RuntimeException("Erro: Listas devem ser avaliadas separadamente.");
            default -> throw new RuntimeException("Tipo desconhecido: " + type);
        };
    }


    private Object getDefaultValue() {
        return switch (type.getValue()) {
            case "int" -> 0;
            case "double" -> 0.0;
            case "string" -> "";
            case "bool" -> false;
            default -> null; // Outros tipos podem ser nulos
        };
    }

    @Override
    public String toString() {
        return "VariableDeclaration{name='" + name + "', type='" + type.getValue() + "', value=" + value + "}";
    }
}
