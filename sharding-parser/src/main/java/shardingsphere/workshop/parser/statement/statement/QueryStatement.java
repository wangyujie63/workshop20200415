
package shardingsphere.workshop.parser.statement.statement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shardingsphere.workshop.parser.statement.ASTNode;
import shardingsphere.workshop.parser.statement.segment.SelectElementsSegment;
import shardingsphere.workshop.parser.statement.segment.TableNameSegment;
import shardingsphere.workshop.parser.statement.segment.WhereClauseSegment;

/**
 * Query statement.
 *
 * @author wangyujie63
 */
@RequiredArgsConstructor
@Getter
public final class QueryStatement implements ASTNode {

    /**
     * selected table name
     */
    private final TableNameSegment tableName;

    /**
     *  selected column
     */
    private final SelectElementsSegment selectElements;

    /**
     * where condition
     */
    private final WhereClauseSegment whereClause;
}
