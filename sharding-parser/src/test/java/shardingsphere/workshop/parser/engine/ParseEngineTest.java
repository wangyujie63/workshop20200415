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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import shardingsphere.workshop.parser.ThreadManager;
import shardingsphere.workshop.parser.WordDownloadUtil;
import shardingsphere.workshop.parser.statement.statement.QueryStatement;
import shardingsphere.workshop.parser.statement.statement.UseStatement;


import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public final class ParseEngineTest {
    
    @Test
    public void testParse() {
        String sql = "use sharding_db";
        UseStatement useStatement = (UseStatement) ParseEngine.parse(sql);
        assertThat(useStatement.getSchemeName().getIdentifier().getValue(), is("sharding_db"));
    }


    //@Test
    public void testQueryParse() {
        String sql = "select *,order_id from t_order";
        QueryStatement queryStatement = (QueryStatement) ParseEngine.parse(sql);
        System.out.println(queryStatement);
        assertThat(queryStatement.getTableName().getIdentifier().getValue(), is("t_order"));
    }


    @Test
    public void test() throws Exception{
        Integer num = Integer.valueOf(241)*Integer.valueOf(85);
        Integer num1 = Integer.valueOf(241)*Integer.valueOf(85)/100;
        System.out.println("==="+num+"==="+num1);
//            //FutureTask执行的任务
//            FutureTask<String> future1 = new FutureTask<String>(
//                    () -> {
//                        System.out.println("执行任务1的线程="+Thread.currentThread().getName());
//                        Thread.sleep(1000);
//                        return "1";
//                    });
//
//        FutureTask<String> future2 = new FutureTask<String>(
//                () -> {
//                    System.out.println("执行任务2的线程="+Thread.currentThread().getName());
//                    Thread.sleep(2000);
//                    return "2";
//                });
//
//        FutureTask<String> future3 = new FutureTask<String>(
//                () -> {
//                    System.out.println("执行任务3的线程="+Thread.currentThread().getName());
//                    Thread.sleep(3000);
//                    return "3";
//                });
//        long startTime= System.currentTimeMillis();
//        ThreadManager.executeFutureTask(future1);
//        ThreadManager.executeFutureTask(future2);
//        ThreadManager.executeFutureTask(future3);
//
//        try {
//
//            String value1 = future1.get();
//            long endTime1= System.currentTimeMillis();
//            System.out.println("执行结果value1耗时"+(endTime1-startTime));
//            String value2 = future2.get();
//            long endTime2= System.currentTimeMillis();
//            System.out.println("执行结果value2耗时"+(endTime2-startTime));
//            String value3 = future3.get();
//            long endTime3= System.currentTimeMillis();
//            System.out.println("执行结果value3耗时"+(endTime3-startTime));
//            Integer total = Integer.valueOf(value1) + Integer.valueOf(value2) + Integer.valueOf(value3);
//            System.out.println("执行最终结果"+total);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

//        List<FutureTask<String>> futureList = new ArrayList<>();
//        int total =0;
//        for(int i=1;i<100;i++) {
//            //FutureTask执行的任务
//            int j=i;
//            FutureTask<String> future = new FutureTask<String>(
//                    () -> {
//                        System.out.println("执行任务的线程="+Thread.currentThread().getName());
//
//                        return String.valueOf(j);
//                    });
//            futureList.add(future);
//            ThreadManager.executeFutureTask(future);
//            total = total +i;
//        }
//        System.out.println("执行最终应该结果====="+total);
//        Integer totalNum = 0;
//        for(FutureTask<String> future:futureList) {
//            String value ="0";
//            try {
//                value = future.get();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            totalNum = totalNum + Integer.valueOf(value);
//            System.out.println("执行结果"+totalNum);
//        }
//        System.out.println("执行最终结果====="+totalNum);

//        Date d = new Date("Fri, 28 Aug 2020 14:32:40 +0000");
//        //Date d1 = new Date("2020-08-28 14:32:40");
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("GTM"));
//
//        System.out.println(sdf.format(d));
//        //System.out.println(sdf.format(d1));
//
//
//
//
//        Calendar now = Calendar.getInstance();
//        System.out.println(now.get(Calendar.HOUR_OF_DAY));
//        System.out.println(now.get(Calendar.MINUTE));
//        System.out.println(now.get(Calendar.SECOND));
//        WordDownloadUtil util = new WordDownloadUtil();
//        Map<String, Object> dataMap = new HashMap<String, Object>();
//        dataMap.put("companyName", "                                        ");
//        dataMap.put("uid", "jimu_user_info-12345");
//        util.createDocNew(dataMap,"D:/","委托授权书.doc","certificate.ftl");
//        System.out.println("模板生成成功");
//        int corePoolSize = Runtime.getRuntime().availableProcessors()*2;
//        System.out.println(corePoolSize);

//        System.out.println(ipList);
//
//        System.out.println(InetAddress.getLocalHost().getHostAddress());
//        System.out.println(15^2);
//        System.out.println(20 << 6);
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
