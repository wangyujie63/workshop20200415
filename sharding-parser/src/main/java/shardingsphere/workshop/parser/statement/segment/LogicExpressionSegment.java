package shardingsphere.workshop.parser.statement.segment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shardingsphere.workshop.parser.statement.ASTNode;

/**
 * @author wangyujie63
 *
 */
@RequiredArgsConstructor
@Getter
public class LogicExpressionSegment implements ASTNode {

    private final AssignmentValueSegment identifier;

    private final ColumnNameSegment columnName;

    private final String comparisonOperator;
}
