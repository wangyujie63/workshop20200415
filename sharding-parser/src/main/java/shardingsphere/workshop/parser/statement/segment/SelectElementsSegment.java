package shardingsphere.workshop.parser.statement.segment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shardingsphere.workshop.parser.statement.ASTNode;

import java.util.List;

/**
 * SelectedElements
 * @author wangyujie63
 *
 */
@RequiredArgsConstructor
@Getter
public class SelectElementsSegment implements ASTNode {

    /**
     * column name
     */
    private final List<ColumnNameSegment> columnName;

    /**
     *  "*"
     */
    private final String ASTERISK_;

}
