package lists;
import expressions.Expression;
import expressions.TypedValue;
import variables.VariableTable;

import java.util.List;

public class ListExpression extends Expression {
    private final List<Expression> elements;

    public ListExpression(List<Expression> elements) {
        this.elements = elements;
    }

    @Override
    public TypedValue evaluate(VariableTable table) {
        ZardList list = new ZardList();
        for (Expression expr : elements) {
            list.add(expr.evaluate(table).getValue()); // Avalia os elementos antes de adicionar
        }
        return new TypedValue(list, "list");
    }
    public List<Expression> getElements() {
        return elements;
    }

}
