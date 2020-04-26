package shardingsphere.workshop.parser.statement.segment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shardingsphere.workshop.parser.statement.ASTNode;

/**
 * LogicExpression
 * @author wangyujie63
 *
 */
@RequiredArgsConstructor
@Getter
public class LogicExpressionSegment implements ASTNode {

    /**
     * condition value
     */
    private final AssignmentValueSegment identifier;

    /**
     * condition column
     */
    private final ColumnNameSegment columnName;

    /**
     *  operators
     */
    private final String comparisonOperator;
}
