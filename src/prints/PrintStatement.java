package prints;
import expressions.Expression;
import expressions.TypedValue;
import variables.Statement;
import variables.VariableTable;


public class PrintStatement extends Statement {
    public final Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(VariableTable table) {
        TypedValue value = expression.evaluate(table);
        System.out.println(value.getValue()); // Agora imprime corretamente com concatenação
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "PrintStatement{" +
                "expression " + expression +
                '}';
    }
}
