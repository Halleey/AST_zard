package variables;

import expressions.BinaryExpression;
import expressions.Expression;
import expressions.LiteralExpression;
import expressions.TypedValue;
import lists.ListExpression;
import lists.ZardList;
import tokens.Token;
import variables.exceptions.ExceptionVar;

import java.util.List;


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

        Object evaluatedValue = (value != null) ? evaluateExpressionWithListSupport(value, table) : getDefaultValue();

        table.setVariable(name, new TypedValue(evaluatedValue, type.getValue()));
    }

    private Object evaluateExpressionWithListSupport(Expression value, VariableTable table) {
        if (value instanceof ListExpression listExpr) {
            return createZardList(listExpr, table);
        }
        return evaluateExpression(value, table);
    }

    private ZardList createZardList(ListExpression listExpr, VariableTable table) {
        List<Object> evaluatedElements = listExpr.getElements().stream()
                .map(expr -> expr.evaluate(table).getValue()) // Avalia cada elemento antes de criar a lista
                .toList();
        return new ZardList(evaluatedElements);
    }


    private Object evaluateExpression(Expression expr, VariableTable table) {
        if (expr instanceof LiteralExpression) {
            String value = ((LiteralExpression) expr).token.getValue();
            return convertToType(value, type.getValue()); // Converte para o tipo correto
        } else if (expr instanceof ListExpression) {
            return ((ListExpression) expr).getElements(); // Retorna a lista corretamente
        } else if (expr instanceof BinaryExpression) {
            return expr.evaluate(table).getValue(); // Agora passa a tabela de variáveis corretamente
        } else if (expr instanceof VariableReference) {
            return expr.evaluate(table).getValue(); // Também adiciona suporte para referências a variáveis
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
