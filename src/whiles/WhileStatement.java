package whiles;

import expressions.Expression;
import ifs.Block;
import returns.ReturnException;
import variables.Statement;
import variables.VariableTable;

public class WhileStatement extends Statement {
    private final Expression condition;
    private final Block block;

    public WhileStatement(Expression condition, Block block) {
        this.condition = condition;
        this.block = block;
    }

    @Override
    public void execute(VariableTable table) {
        while (condition.evaluate(table).isTruthy()) {
            try {
                block.execute(table);
            } catch (ReturnException e) {
                System.out.println("Retorno encontrado dentro de um bloco menor, encerrando o loop.");
                throw e; // Interrompe apenas o loop e propaga o return
            }
        }
    }

    // Getter para acessar a condição do while
    public Expression getCondition() {
        return condition;
    }

    // Getter para acessar o corpo do while
    public Block getBody() {
        return block;
    }

    @Override
    public String toString() {
        return "WhileStatement:\n" +
                "  Condition: " + condition + "\n" +
                "  " + block;
    }

}
