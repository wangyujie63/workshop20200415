
package shardingsphere.workshop.mysql.proxy.todo;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import shardingsphere.workshop.mysql.proxy.fixture.MySQLAuthenticationHandler;
import shardingsphere.workshop.mysql.proxy.fixture.packet.MySQLErrPacketFactory;
import shardingsphere.workshop.mysql.proxy.fixture.packet.MySQLPacketPayload;
import shardingsphere.workshop.mysql.proxy.fixture.packet.constant.MySQLColumnType;
import shardingsphere.workshop.mysql.proxy.todo.packet.MySQLEofPacket;
import shardingsphere.workshop.mysql.proxy.todo.packet.MySQLColumnDefinition41Packet;
import shardingsphere.workshop.mysql.proxy.todo.packet.MySQLFieldCountPacket;
import shardingsphere.workshop.mysql.proxy.todo.packet.MySQLTextResultSetRowPacket;
import shardingsphere.workshop.parser.engine.ParseEngine;
import shardingsphere.workshop.parser.statement.segment.ColumnNameSegment;
import shardingsphere.workshop.parser.statement.segment.SelectElementsSegment;
import shardingsphere.workshop.parser.statement.segment.WhereClauseSegment;
import shardingsphere.workshop.parser.statement.statement.QueryStatement;

import java.lang.reflect.Array;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

/**
 * Frontend channel inbound handler.
 */
@RequiredArgsConstructor
@Slf4j
public final class FrontendChannelInboundHandler extends ChannelInboundHandlerAdapter {
    
    private final MySQLAuthenticationHandler authHandler = new MySQLAuthenticationHandler();
    
    private boolean authorized;

    private final static String SYMBOL ="*";
    
    @Override
    public void channelActive(final ChannelHandlerContext context) {
        authHandler.handshake(context);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext context, final Object message) {
        if (!authorized) {
            authorized = auth(context, (ByteBuf) message);
            return;
        }
        try (MySQLPacketPayload payload = new MySQLPacketPayload((ByteBuf) message)) {
            executeCommand(context, payload);
        } catch (final Exception ex) {
            log.error("Exception occur: ", ex);
            context.writeAndFlush(MySQLErrPacketFactory.newInstance(1, ex));
        }
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext context) {
        context.fireChannelInactive();
    }
    
    private boolean auth(final ChannelHandlerContext context, final ByteBuf message) {
        try (MySQLPacketPayload payload = new MySQLPacketPayload(message)) {
            return authHandler.auth(context, payload);
        } catch (final Exception ex) {
            log.error("Exception occur: ", ex);
            context.write(MySQLErrPacketFactory.newInstance(1, ex));
        }
        return false;
    }
    
    private void executeCommand(final ChannelHandlerContext context, final MySQLPacketPayload payload) {
        Preconditions.checkState(0x03 == payload.readInt1(), "only support COM_QUERY command type");
        // TODO 1. Read SQL from payload, then system.out it
        String sql = payload.getByteBuf().toString(CharsetUtil.UTF_8);
        System.out.println("sql----->" + sql);
        // TODO 2. Return mock MySQLPacket to client (header: MySQLFieldCountPacket + MySQLColumnDefinition41Packet + MySQLEofPacket, content: MySQLTextResultSetRowPacket
//        context.write(new MySQLFieldCountPacket(1, 3));
//        context.write(new MySQLColumnDefinition41Packet(2, 0, "sharding_db", "t_order", "t_order", "order_id", "order_id", 100, MySQLColumnType.MYSQL_TYPE_STRING,0));
//        context.write(new MySQLColumnDefinition41Packet(3, 0, "sharding_db", "t_order", "t_order", "order_no", "order_no", 100, MySQLColumnType.MYSQL_TYPE_STRING,0));
//        context.write(new MySQLColumnDefinition41Packet(4, 0, "sharding_db", "t_order", "t_order", "order_time", "order_time", 100, MySQLColumnType.MYSQL_TYPE_STRING,0));
//        context.write(new MySQLEofPacket(5));
//        context.write(new MySQLTextResultSetRowPacket(6, ImmutableList.of(100,200, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))));
//        context.write(new MySQLTextResultSetRowPacket(7, ImmutableList.of(200,300, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))));
//        context.write(new MySQLEofPacket(8));
//        context.flush();
        // TODO 3. Parse SQL, return actual data according to SQLStatement
        //先把csv表数据放入内存中
        String tableName = "t_order";
        String columnInfo = "order_id:long,user_id:int,status:string";
        String[] rowsInfo = new String[]{"1000001,10,init", "2000001,20,init", "3000001,30,init"};
        //表中所有的列名，有顺序
        List<String> columnNameList = new LinkedList<>();
        //每列对应的数据类型（key：列名，value：MySQLColumnType）
        Map<String, MySQLColumnType> columnType = new HashMap<>();
        //取出数据类型
        for (String column : columnInfo.split(",")) {
            String[] info = column.split(":");
            columnNameList.add(info[0]);
            columnType.put(info[0], getColumnType(info[1]));
        }
        //用parser解析sql
        QueryStatement queryStatement = (QueryStatement) ParseEngine.parse(sql);
        //只能查t_order表中的数据 否则抛出异常
        if (!tableName.equalsIgnoreCase(queryStatement.getTableName().getIdentifier().getValue())) {
            throw new IllegalStateException("only support query table form t_order ");
        }
        //按照mysql的返包规则组装context
        dealChannelHandlerContext(columnNameList, columnType, rowsInfo, queryStatement, context);
        context.flush();
    }

    /**
     * 按照mysql的返包规则组装context
     * @param columnNameList 表的所有列名
     * @param columnType  表的列的数据类型属性
     * @param rowsInfo 表中所有的行
     * @param queryStatement 解析sql得到的statement
     * @param context
     */
    private void dealChannelHandlerContext(List<String> columnNameList, Map<String, MySQLColumnType> columnType, String[] rowsInfo, QueryStatement queryStatement, final ChannelHandlerContext context) {
        //取出查询的表名
        String tableName = queryStatement.getTableName().getIdentifier().getValue();
        //取出查询的列名
        SelectElementsSegment selectElements = queryStatement.getSelectElements();
        //取出查询条件
        WhereClauseSegment whereClause = queryStatement.getWhereClause();

        //取出sql中查询的列名
        List<String> selectedColumn = getSelectedColumnNames(columnNameList, selectElements);
        int columnCount = selectedColumn.size();
        int sequenceId = 0;
        context.write(new MySQLFieldCountPacket(++sequenceId, columnCount));
        //列名出现的次数，有可能用户输入相同的列,输出的时候区分开
        Map<String, Integer> countMap = new HashMap<>();
        for (int i = 0; i < columnCount; i++) {
            String columnName = selectedColumn.get(i);
            //取出数据类型
            MySQLColumnType myColumnType = columnType.get(columnName);
            if (!countMap.containsKey(columnName)) {
                countMap.put(columnName, 0);
            } else {
                int count = countMap.get(columnName);
                countMap.put(columnName, count + 1);
            }
            if (countMap.get(columnName) > 0) {
                //重复的列 展示时列名加上次数
                columnName = columnName + countMap.get(columnName);
            }
            context.write(new MySQLColumnDefinition41Packet(++sequenceId, 0, "sharding_db", tableName, tableName, columnName, columnName, 100, myColumnType, 0));
        }
        context.write(new MySQLEofPacket(++sequenceId));

        //取出所有值
        for (String rowData : rowsInfo) {
            String[] rowValue = rowData.split(",");
            if (whereClause != null) {
                //根据查询条件whereClause 处理数据rows
                //条件列
                String comparisonColumn = whereClause.getLogicExpression().getColumnName().getIdentifier().getValue();
                //条件值
                String comparisonValue = whereClause.getLogicExpression().getIdentifier().getIdentifier().getValue();
                //运算符
                String comparisonOperator = whereClause.getLogicExpression().getComparisonOperator();
                //条件列的索引
                int index = columnNameList.indexOf(comparisonColumn.toLowerCase());
                if (index == -1) {
                    throw new IllegalStateException("Unknown column  " + comparisonColumn + " near where at line 1");
                }
                //条件列的row值
                String value = rowValue[index];
                if (!checkComparisonValue(comparisonValue, value, comparisonOperator)) {
                    continue;
                }
            }
            //过滤之后组装值
            List<Object> data = new ArrayList<>();
            for (int i = 0; i < columnCount; i++) {
                //按顺序取出列对应的值
                int columnIndex = columnNameList.indexOf(selectedColumn.get(i));
                String value = rowValue[columnIndex];
                data.add(value);
            }
            context.write(new MySQLTextResultSetRowPacket(++sequenceId, data));
        }
        context.write(new MySQLEofPacket(++sequenceId));
    }


    /**
     * 表达式判断是否满足条件
     * 此方法应该将列的数据类型传进来 根据数据类型进行比较大小
     *
     * @param comparisonValue
     * @param rowValue
     * @param comparisonOperator
     * @return
     */
    private boolean checkComparisonValue(String comparisonValue, String rowValue, String comparisonOperator) {
        boolean result;
        switch (comparisonOperator) {
            case "=":
                result = rowValue.equals(comparisonValue);
                break;
            case ">":
                result = Integer.valueOf(rowValue).intValue() > Integer.valueOf(comparisonValue).intValue();
                break;
            case ">=":
                result = Integer.valueOf(rowValue).intValue() >= Integer.valueOf(comparisonValue).intValue();
                break;
            case "<":
                result = Integer.valueOf(rowValue).intValue() < Integer.valueOf(comparisonValue).intValue();
                break;
            case "<=":
                result = Integer.valueOf(rowValue).intValue() <= Integer.valueOf(comparisonValue).intValue();
                break;
            case "!=":
                result = !rowValue.equals(comparisonValue);
                break;
            default:
                result = true;
        }
        return result;
    }

    /**
     * 获取对应字段的数据类型
     * 伪代码
     *
     * @param columnTypeName
     * @return
     */
    private MySQLColumnType getColumnType(String columnTypeName) {
        MySQLColumnType mySQLColumnType = MySQLColumnType.MYSQL_TYPE_STRING;
        if ("int".equalsIgnoreCase(columnTypeName)) {
            mySQLColumnType = MySQLColumnType.valueOfJDBCType(Types.INTEGER);
        }
        if ("long".equalsIgnoreCase(columnTypeName)) {
            mySQLColumnType = MySQLColumnType.valueOfJDBCType(Types.BIGINT);
        }
        if ("string".equalsIgnoreCase(columnTypeName)) {
            mySQLColumnType = MySQLColumnType.valueOfJDBCType(Types.VARCHAR);
        }
        return mySQLColumnType;
    }

    /**
     * 取出sql中查询的列名
     *
     * @param columnNameList
     * @param selectElements
     * @return
     */
    private List<String> getSelectedColumnNames(List<String> columnNameList, SelectElementsSegment selectElements) {
        List<String> selectedColumn = new LinkedList<>();
        //判断查询的列中有没有*
        String ASTERISK_ = selectElements.getASTERISK_();
        if (SYMBOL.equals(ASTERISK_)) {
            selectedColumn.addAll(columnNameList);
        }
        List<ColumnNameSegment> columnNameSegments = selectElements.getColumnName();
        if (CollectionUtils.isNotEmpty(columnNameSegments)) {
            for (ColumnNameSegment columnNameSegment : columnNameSegments) {
                if (columnNameList.indexOf(columnNameSegment.getIdentifier().getValue().toLowerCase()) == -1) {
                    throw new IllegalStateException("Unknown column  " + columnNameSegment.getIdentifier().getValue() + " in field list");
                }
                selectedColumn.add(columnNameSegment.getIdentifier().getValue().toLowerCase());
            }
        }
        return selectedColumn;
    }


}
