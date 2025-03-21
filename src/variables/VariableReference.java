package variables;

import expressions.Expression;
import expressions.TypedValue;

public class VariableReference extends Expression {
    public final String name;

    public VariableReference(String name) {
        this.name = name;
    }

    @Override
    public TypedValue evaluate(VariableTable table) {
        return table.getVariable(name); // Já retorna um TypedValue armazenado na tabela
    }


    @Override
    public String toString() {
        return "VariableReference{" +
                "name  '" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }
}

