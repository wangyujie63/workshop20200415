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
public class WhereClauseSegment implements ASTNode {

    private final LogicExpressionSegment logicExpression;


}
