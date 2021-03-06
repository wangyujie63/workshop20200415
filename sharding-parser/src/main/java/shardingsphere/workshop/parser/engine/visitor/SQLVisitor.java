
package shardingsphere.workshop.parser.engine.visitor;

import autogen.MySQLStatementBaseVisitor;
import autogen.MySQLStatementParser.IdentifierContext;
import autogen.MySQLStatementParser.SchemaNameContext;
import autogen.MySQLStatementParser.UseContext;
import autogen.MySQLStatementParser.SelectContext;
import autogen.MySQLStatementParser.WhereClauseContext;
import autogen.MySQLStatementParser.LogicExpressionContext;
import autogen.MySQLStatementParser.SelectElementsContext;
import autogen.MySQLStatementParser.ColumnNameContext;
import autogen.MySQLStatementParser.AssignmentValueContext;
import autogen.MySQLStatementParser.TableNameContext;
import shardingsphere.workshop.parser.statement.ASTNode;
import shardingsphere.workshop.parser.statement.segment.*;
import shardingsphere.workshop.parser.statement.statement.QueryStatement;
import shardingsphere.workshop.parser.statement.statement.UseStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * MySQL visitor.
 */
public final class SQLVisitor extends MySQLStatementBaseVisitor<ASTNode> {

    @Override
    public ASTNode visitUse(final UseContext ctx) {
        SchemeNameSegment schemeName = (SchemeNameSegment) visit(ctx.schemaName());
        return new UseStatement(schemeName);
    }

    @Override
    public ASTNode visitSchemaName(final SchemaNameContext ctx) {
        IdentifierSegment identifier = (IdentifierSegment) visit(ctx.identifier());
        return new SchemeNameSegment(identifier);
    }

    @Override
    public ASTNode visitIdentifier(final IdentifierContext ctx) {
        return new IdentifierSegment(ctx.getText());
    }

    @Override
    public ASTNode visitSelect(final SelectContext ctx) {
        TableNameSegment tableName = (TableNameSegment) visit(ctx.tableName());
        SelectElementsSegment selectElements = (SelectElementsSegment) visit(ctx.selectElements());
        WhereClauseSegment whereClause = ctx.whereClause() == null ? null : (WhereClauseSegment) visit(ctx.whereClause());
        return new QueryStatement(tableName, selectElements, whereClause);
    }

    @Override
    public ASTNode visitTableName(final TableNameContext ctx) {
        IdentifierSegment identifier = (IdentifierSegment) visit(ctx.identifier());
        return new TableNameSegment(identifier);
    }

    @Override
    public ASTNode visitSelectElements(final SelectElementsContext ctx) {
        List<ColumnNameSegment> columnNameList = new ArrayList<>();
        for (int i = 0; i < ctx.columnName().size(); i++) {
            ColumnNameSegment columnName = (ColumnNameSegment) visit(ctx.columnName(i));
            columnNameList.add(columnName);
        }
        String ASTERISK_ = ctx.ASTERISK_() == null ? null : ctx.ASTERISK_().getText();
        return new SelectElementsSegment(columnNameList, ASTERISK_);
    }

    @Override
    public ASTNode visitWhereClause(final WhereClauseContext ctx) {
        LogicExpressionSegment logicExpression = (LogicExpressionSegment) visit(ctx.logicExpression());
        return new WhereClauseSegment(logicExpression);
    }

    @Override
    public ASTNode visitLogicExpression(final LogicExpressionContext ctx) {
        AssignmentValueSegment assignmentValue = (AssignmentValueSegment) visit(ctx.assignmentValue());
        ColumnNameSegment columnName = (ColumnNameSegment) visit(ctx.columnName());
        String comparisonOperator = ctx.comparisonOperator().getText();
        return new LogicExpressionSegment(assignmentValue, columnName, comparisonOperator);
    }

    @Override
    public ASTNode visitAssignmentValue(final AssignmentValueContext ctx) {
        IdentifierSegment identifier = (IdentifierSegment) visit(ctx.identifier());
        return new AssignmentValueSegment(identifier);
    }

    @Override
    public ASTNode visitColumnName(final ColumnNameContext ctx) {
        IdentifierSegment identifier = (IdentifierSegment) visit(ctx.identifier());
        return new ColumnNameSegment(identifier);
    }
}
