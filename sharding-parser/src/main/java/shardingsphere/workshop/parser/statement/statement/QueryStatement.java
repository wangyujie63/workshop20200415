
package shardingsphere.workshop.parser.statement.statement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shardingsphere.workshop.parser.statement.ASTNode;
import shardingsphere.workshop.parser.statement.segment.SchemeNameSegment;
import shardingsphere.workshop.parser.statement.segment.SelectElementsSegment;
import shardingsphere.workshop.parser.statement.segment.WhereClauseSegment;

import javax.swing.text.Segment;

/**
 * Query statement.
 *
 * @author wangyujie
 */
@RequiredArgsConstructor
@Getter
public final class QueryStatement implements ASTNode {
    
    private final SchemeNameSegment schemeName;

    private final SelectElementsSegment selectElements;

    private final WhereClauseSegment whereClause;
}
