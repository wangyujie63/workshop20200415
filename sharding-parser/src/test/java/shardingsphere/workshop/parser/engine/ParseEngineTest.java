/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package shardingsphere.workshop.parser.engine;

import org.junit.Test;
import shardingsphere.workshop.parser.statement.statement.QueryStatement;
import shardingsphere.workshop.parser.statement.statement.UseStatement;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public final class ParseEngineTest {
    
    @Test
    public void testParse() {
        String sql = "use sharding_db";
        UseStatement useStatement = (UseStatement) ParseEngine.parse(sql);
        assertThat(useStatement.getSchemeName().getIdentifier().getValue(), is("sharding_db"));
    }


    @Test
    public void testQueryParse() {
        String sql = "select *,order_id from t_order";
        QueryStatement queryStatement = (QueryStatement) ParseEngine.parse(sql);
        System.out.println(queryStatement);
        assertThat(queryStatement.getTableName().getIdentifier().getValue(), is("t_order"));
    }


    @Test
    public void test() {
        System.out.println(15^2);
//        System.out.println(20 << 5);
//        System.out.println(20 >> 2);
//        System.out.println(-20 >>> 2);
//        System.out.println(-20 >> 2);
//        float a = 0.125f;
//        double b = 0.125d;
//        System.out.println((a - b) == 0.0);//true
//        double c = 0.8;
//        double d = 0.7;
//        double e = 0.6;
//        System.out.println((c - d) == (d-e));//false
//        String t =null;
//        switch (t){//NPE
//            case "1":
//                break ;
//
//        }
//        System.out.println(1.0 / 0);//Infinity
//        System.out.println(0.0 / 0.0);//NaN
//        System.out.println(1 / 0);//编译报错  java.lang.ArithmeticException: / by zero

    }
}
