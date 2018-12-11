INSERT OVERWRITE TABLE  t2f_account_profit_his PARTITION(event_date=${hivevar:natural_date})
SELECT
    t1.init_date AS init_date,
    COALESCE(CASE WHEN t1.fund_account ='' THEN 'NA' ELSE t1.fund_account END,'NA') AS fund_account,        --资产账户
    COALESCE(CASE WHEN t1.client_id ='' THEN 'NA' ELSE t1.client_id END,'NA') AS client_id,                    --客户编号
    COALESCE(CASE WHEN t1.branch_no ='' THEN -1 ELSE t1.branch_no END,-1) AS branch_no,                        --分支机构
    COALESCE(CASE WHEN t1.asset_prop ='' THEN 'NA' ELSE t1.asset_prop END,'NA') AS asset_prop,                    --账户类型
    COALESCE(t5.total_asset,0) AS total_asset,                                --总资产
    COALESCE(t5.end_asset,0)+abs(COALESCE(t4.bank_transfer_out_rmb,0))+abs(COALESCE(t2.market_value_out,0))-COALESCE(t4.bank_transfer_in_rmb,0)-COALESCE(t3.market_value_in,0)-COALESCE(t7.begin_asset,0) AS profit,
    COALESCE(t7.begin_asset,0) AS begin_asset,                                --期初资产
    COALESCE(t5.end_asset,0) AS end_asset,                                    --期末资产
    COALESCE(t4.bank_transfer_out_rmb,0) AS bank_transfer_out_rmb,            --银行转取
    COALESCE(t4.bank_transfer_in_rmb,0) AS bank_transfer_in_rmb,            --银行转存
    COALESCE(t3.market_value_in,0) AS market_value_in,
    COALESCE(t2.market_value_out,0) AS market_value_out,
    CASE WHEN COALESCE(t5.total_asset,0)<>0 AND COALESCE(t7.total_asset,0)<>0 THEN COALESCE(COALESCE(t5.total_asset,0)/(COALESCE(t6.curr_share,0)+(COALESCE(t4.bank_transfer_in_rmb,0)-abs(COALESCE(t4.bank_transfer_out_rmb,0))+COALESCE(t3.market_value_in,0)-abs(COALESCE(t2.market_value_out,0)))/COALESCE(t6.curr_net_value,0)),0)
         WHEN COALESCE(t5.total_asset,0)<>0 AND COALESCE(t7.total_asset,0)=0 THEN 1
        ELSE 0 END AS curr_net_value,        --今日净值
    CASE WHEN COALESCE(t5.total_asset,0)<>0 AND COALESCE(t7.total_asset,0)<>0 THEN COALESCE(COALESCE(t6.curr_share,0)+(COALESCE(t4.bank_transfer_in_rmb,0)-abs(COALESCE(t4.bank_transfer_out_rmb,0))+COALESCE(t3.market_value_in,0)-abs(COALESCE(t2.market_value_out,0)))/COALESCE(t6.curr_net_value,0),0)
         WHEN COALESCE(t5.total_asset,0)<>0 AND COALESCE(t7.total_asset,0)=0 THEN COALESCE(COALESCE(t5.total_asset,0)/1,0)
        ELSE  0  END    AS curr_share,                            --今日份额
    from_unixtime(unix_timestamp(),'yyyyMMdd') AS tech_date
FROM
    (
    SELECT ${hivevar:natural_date} AS init_date,fund_account,client_id,branch_no,asset_prop
    FROM t1b_fundaccount --WHERE asset_prop=0
    GROUP BY fund_account,client_id,branch_no,asset_prop
    ) t1
LEFT JOIN
    (
    SELECT init_date,fund_account,client_id,branch_no,SUM(business_amount*price_avg) AS market_value_out        --转出证券市值
    FROM t2f_deliver_his WHERE entrust_bs=2 AND deliver_type=5 AND event_date=${hivevar:natural_date} GROUP BY init_date,fund_account,client_id,branch_no,entrust_bs
    )t2
    ON t2.init_date=t1.init_date AND t2.fund_account=t1.fund_account AND t2.client_id=t1.client_id --AND t2.branch_no=t1.branch_no
LEFT JOIN
    (
    SELECT init_date,fund_account,client_id,branch_no,SUM(business_amount*price_avg) AS market_value_in        --转入证券市值
    FROM t2f_deliver_his WHERE entrust_bs=1 AND deliver_type=5 AND event_date=${hivevar:natural_date} GROUP BY init_date,fund_account,client_id,branch_no,entrust_bs
    )t3
    ON t3.init_date=t1.init_date AND t3.fund_account=t1.fund_account AND t3.client_id=t1.client_id --AND t3.branch_no=t1.branch_no
LEFT JOIN
    (
    SELECT init_date,fund_account,client_id,branch_no,SUM(bank_transfer_in_rmb) AS bank_transfer_in_rmb,SUM(bank_transfer_out_rmb) AS bank_transfer_out_rmb
    FROM t2f_fund_his WHERE event_date=${hivevar:natural_date} GROUP BY init_date,fund_account,client_id,branch_no
    )t4
    ON t4.init_date=t1.init_date AND t4.fund_account=t1.fund_account AND t4.client_id=t1.client_id --AND t4.branch_no=t1.branch_no
LEFT JOIN
    (
    SELECT ${hivevar:natural_date} AS init_date,t5_4.fund_account,t5_4.branch_no,SUM(t5_4.total_asset) AS total_asset,SUM(t5_4.total_asset) AS end_asset
    FROM
    (
        SELECT t5_1.init_date,t5_1.fund_account,t5_1.branch_no,
            CASE WHEN t5_1.money_type=0 THEN t5_1.total_asset ELSE t5_1.total_asset*t5_2.bill_rate END AS total_asset
        FROM
        (
            SELECT trading_date FROM t1b_transactiondate WHERE natural_date=${hivevar:natural_date}
        )t5_3
        INNER JOIN
            t1a_asset_his t5_1
        ON t5_1.init_date=t5_3.trading_date
        LEFT JOIN
        (
            SELECT
            money_type,
            dest_money_type,
            init_tech_date,
            bill_rate
            FROM
            t1a_exchangerate_his
            group by
            money_type,
            dest_money_type,
            init_tech_date,
            bill_rate
        )t5_2
        ON t5_1.money_type=t5_2.money_type AND t5_2.dest_money_type=0 AND t5_2.init_tech_date=t5_3.trading_date
        WHERE t5_1.event_date=t5_3.trading_date
    )t5_4
    GROUP BY
        t5_4.init_date,
        t5_4.fund_account,
        t5_4.branch_no
    )t5
    ON t5.init_date=t1.init_date AND t5.fund_account=t1.fund_account --AND  t5.branch_no=t1.branch_no
LEFT JOIN
    (
    SELECT ${hivevar:natural_date} AS init_date,fund_account,client_id,branch_no,curr_net_value,curr_share
    FROM t2f_account_profit_his WHERE event_date=from_unixtime(unix_timestamp(CAST(${hivevar:natural_date} AS STRING),'yyyyMMdd')-3600*24,'yyyyMMdd')
    )t6
    ON t6.init_date=t1.init_date AND t6.fund_account=t1.fund_account AND t6.client_id=t1.client_id --AND t6.branch_no=t1.branch_no
LEFT JOIN
    (
    SELECT ${hivevar:natural_date} AS init_date,t7_4.fund_account,t7_4.branch_no,SUM(t7_4.total_asset) AS total_asset,SUM(t7_4.total_asset) AS begin_asset
    FROM
    (
        SELECT t7_1.init_date,t7_1.fund_account,t7_1.branch_no,
            CASE WHEN t7_1.money_type=0 THEN t7_1.total_asset ELSE t7_1.total_asset*t7_2.bill_rate END AS total_asset
        FROM
        (
            SELECT trading_date FROM t1b_transactiondate WHERE natural_date=from_unixtime(unix_timestamp(CAST(${hivevar:natural_date} AS STRING),'yyyyMMdd')-3600*24,'yyyyMMdd')
        )t7_3
        INNER JOIN
            t1a_asset_his t7_1
        ON t7_1.init_date=t7_3.trading_date
        LEFT JOIN
        (
            SELECT
            money_type,
            dest_money_type,
            init_tech_date,
            bill_rate
            FROM
            t1a_exchangerate_his
            group by
            money_type,
            dest_money_type,
            init_tech_date,
            bill_rate
        )t7_2
        ON t7_1.money_type=t7_2.money_type AND t7_2.dest_money_type=0 AND t7_2.init_tech_date=t7_3.trading_date
        WHERE t7_1.event_date=t7_3.trading_date
    )t7_4
    GROUP BY
        t7_4.init_date,
        t7_4.fund_account,
        t7_4.branch_no
    )t7
    ON t7.init_date=t1.init_date AND t7.fund_account=t1.fund_account --AND  t7.branch_no=t1.branch_no;
;




ALTER TABLE t3_app_assets_allocation_inf DROP IF EXISTS PARTITION(event_date=${hivevar:natural_date}); --先删除分区在插入数据
FROM t2f_asset_his t
INSERT INTO TABLE t3_app_assets_allocation_inf PARTITION(event_date=${hivevar:natural_date})
----现金
SELECT
    ${hivevar:natural_date} AS calc_date,
    t.fund_account,
    t.client_id,
    t.init_date AS create_date,
    0 AS asset_type,
    0 AS create_time,
    SUM(t.fund_asset) AS asset_amount,
    COALESCE(round(SUM(t.fund_asset)/SUM(t.net_asset),6),0) AS asset_percentage
WHERE t.event_date=${hivevar:natural_date} and t.asset_prop=0
GROUP BY
    t.fund_account,
    t.client_id,
    t.init_date

----场内
INSERT INTO TABLE t3_app_assets_allocation_inf PARTITION(event_date=${hivevar:natural_date})
SELECT
    ${hivevar:natural_date} AS calc_date,
    t.fund_account,
    t.client_id,
    t.init_date AS create_date,
    1 AS asset_type,
    0 AS create_time,
    SUM(t.secu_market_value) AS asset_amount,
    COALESCE(round(SUM(t.secu_market_value)/SUM(t.net_asset),6),0) AS asset_percentage
WHERE t.event_date=${hivevar:natural_date} and t.asset_prop=0
GROUP BY
    t.fund_account,
    t.client_id,
    t.init_date

----场外
INSERT INTO TABLE t3_app_assets_allocation_inf PARTITION(event_date=${hivevar:natural_date})
SELECT
    ${hivevar:natural_date} AS calc_date,
    t.fund_account,
    t.client_id,
    t.init_date AS create_date,
    3 AS asset_type,
    0 AS create_time,
    SUM(t.opfund_market_value) AS asset_amount,
    COALESCE(round(SUM(t.opfund_market_value)/SUM(t.net_asset),6),0) AS asset_percentage
WHERE t.event_date=${hivevar:natural_date} and t.asset_prop=0
GROUP BY
    t.fund_account,
    t.client_id,
    t.init_date


----证券理财
INSERT INTO TABLE t3_app_assets_allocation_inf PARTITION(event_date=${hivevar:natural_date})
SELECT
    ${hivevar:natural_date} AS calc_date,
    t.fund_account,
    t.client_id,
    t.init_date AS create_date,
    4 AS asset_type,
    0 AS create_time,
    SUM(t.secum_market_value) AS asset_amount,
    COALESCE(round(SUM(t.secum_market_value)/SUM(t.net_asset),6),0) AS asset_percentage
WHERE t.event_date=${hivevar:natural_date} and t.asset_prop=0
GROUP BY
    t.fund_account,
    t.client_id,
    t.init_date;


;
WITH t_deliver AS(
    SELECT
        t1.init_date,
        t1.exchange_type,
        t1.branch_no,
        t1.asset_prop,
        t1.stock_code,
        t2.stock_code as stock_code_2,
        t2.last_price as last_price,
        t1.fund_account,
        t1.client_id,
        t1.stock_type,
        t1.deliver_type,
        t1.entrust_bs,
        t1.business_flag,
        t1.price_avg,
        ABS(SUM(clear_balance_rmb)) AS clear_balance_rmb,
        SUM(business_amount) AS business_amount
    FROM  (select * from t2f_deliver_his WHERE event_date=${hivevar:natural_date})t1
    left join
    (
        select
            stock_code,
            relative_code,
            stock_type,
            exchange_type,
            last_price
        from
            t1b_stkcode
            where stock_type = '3' --and relative_code=113506
        group by
            stock_code,
            relative_code,
            stock_type,
            exchange_type,
            last_price
    )t2
    on
    t1.exchange_type=t2.exchange_type and
    t1.stock_code=t2.relative_code
        GROUP BY
        t1.init_date,
        t1.exchange_type,
        t1.branch_no,
        t1.asset_prop,
        t1.stock_code,
        t2.stock_code,
        t2.last_price,
        t1.fund_account,
        t1.client_id,
        t1.stock_type,
        t1.deliver_type,
        t1.entrust_bs,
        t1.business_flag,
        t1.price_avg
),
t_price AS (
            SELECT
                t10.init_date AS init_date,
                t10.exchange_type,
                t10.stock_code,
                t10.bill_rate*t10.asset_price AS asset_price,                                                 --今日收盘价
                lag((t10.bill_rate*t10.asset_price),1,t10.bill_rate*t10.asset_price) over (partition by t10.exchange_type,t10.stock_code
                    ORDER BY t10.init_date ) AS pre_asset_price                                                     --昨日收盘价
            FROM
            (
                SELECT
                    t6.init_date AS init_date,
                    t6.exchange_type AS exchange_type,
                    t6.stock_code AS stock_code,
                    CASE
                        WHEN t6.money_type=1 THEN t4.bill_rate
                        WHEN t6.money_type=2 AND t6.exchange_type IN ('D','H') THEN t4.bill_rate
                        WHEN t6.exchange_type IN ('G','S') THEN t5.buy_exchange_rate
                        ELSE 1
                        END AS bill_rate,
                    asset_price
                FROM
                    t1a_price_his t6
                LEFT JOIN
                (
                    SELECT
                    money_type,
                    dest_money_type,
                    init_tech_date,
                    bill_rate
                    FROM
                    t1a_exchangerate_his
                    group by
                    money_type,
                    dest_money_type,
                    init_tech_date,
                    bill_rate
                )t4
                    ON t6.money_type=t4.money_type AND t4.dest_money_type=0 AND t4.init_tech_date=t6.init_date
                LEFT JOIN
                (
                    SELECT
                    init_tech_date,
                    exchange_type,
                    buy_exchange_rate
                    FROM
                    t1a_securate_his
                    GROUP BY
                    init_tech_date,
                    exchange_type,
                    buy_exchange_rate
                )t5
                    ON t5.init_tech_date=t6.init_date AND t5.exchange_type=t6.exchange_type
            )t10
)
INSERT OVERWRITE TABLE  t2f_stock_profit_his PARTITION(event_date=${hivevar:natural_date})
SELECT
    t9.init_date,
    COALESCE(CASE WHEN t9.branch_no ='' THEN -1 ELSE t9.branch_no END,-1) AS branch_no,
    COALESCE(CASE WHEN t9.exchange_type ='' THEN 'NA' ELSE t9.exchange_type END,'NA') AS exchange_type,
    COALESCE(CASE WHEN t9.stock_code ='' THEN 'NA' ELSE t9.stock_code END,'NA') AS stock_code,
    COALESCE(CASE WHEN t9.fund_account ='' THEN 'NA' ELSE t9.fund_account END,'NA') AS fund_account,
    COALESCE(CASE WHEN t9.client_id ='' THEN 'NA' ELSE t9.client_id END,'NA') AS client_id,
    COALESCE(CASE WHEN t9.deliver_type ='' THEN 'NA' ELSE t9.deliver_type END,'NA') AS deliver_type,
    0 AS money_type,
    COALESCE(CASE WHEN t9.stock_type ='' THEN 'NA' ELSE t9.stock_type END,'NA') AS stock_type,
    COALESCE(CASE WHEN t9.asset_prop ='' THEN 'NA' ELSE t9.asset_prop END,'NA') AS asset_prop,
    COALESCE(round(t9.total_sell_amt - t9.pre_asset_price * t9.sell_qty + (t9.dividen_amt - t9.dividen_tax),4),0) AS realized_profit,
    COALESCE(round(t9.asset_price * t9.buy_qty - t9.total_buy_amt+(t9.share - t9.buy_qty) * (t9.asset_price - t9.pre_asset_price)+ t9.allotment_profit + t9.dividen_qty * t9.pre_asset_price,4),0) AS unrealized_profit,
    COALESCE(round(t9.total_sell_amt - t9.pre_asset_price * t9.sell_qty + (t9.dividen_amt - t9.dividen_tax),4)
     + round((t9.asset_price * t9.buy_qty - t9.total_buy_amt) + (t9.share - t9.buy_qty) * (t9.asset_price - t9.pre_asset_price)+t9.allotment_profit+t9.dividen_qty*t9.pre_asset_price,4),0) AS profit,
    COALESCE((COALESCE(round(t9.total_sell_amt - t9.pre_asset_price * t9.sell_qty + (t9.dividen_amt - t9.dividen_tax),4)
        + round(t9.asset_price * t9.buy_qty - t9.total_buy_amt + (t9.share - t9.buy_qty) * (t9.asset_price - t9.pre_asset_price)+t9.allotment_profit + t9.dividen_qty * t9.pre_asset_price,4),0)
        / (t9.pre_asset_price * (t9.share - t9.buy_qty+ t9.sell_qty) + t9.total_buy_amt + t9.allotment_cost)),0) AS profit_rate,
    COALESCE(round((t9.pre_asset_price * (t9.share - t9.buy_qty+ t9.sell_qty) + t9.total_buy_amt+ t9.allotment_cost), 4), 0) AS cost,
    from_unixtime(unix_timestamp(),'yyyyMMdd') AS tech_date
from
(    select
        t9_1.init_date,
        t9_1.branch_no,
        t9_1.exchange_type,
        t9_1.stock_code,
        t9_1.fund_account,
        t9_1.client_id,
        min(t9_1.deliver_type) as deliver_type,
        t9_1.stock_type,
        t9_1.asset_prop,
        sum(t9_1.share) as share,
        sum(t9_1.allotment_profit) as allotment_profit,       --配股收益
        sum(t9_1.allotment_cost) as allotment_cost,         --配股日初成本
        sum(total_buy_amt) as total_buy_amt,
        sum(total_sell_amt) as total_sell_amt,
        sum(buy_qty) as buy_qty,
        sum(sell_qty) as sell_qty,
        sum(dividen_amt) as  dividen_amt,
        sum(dividen_tax) as  dividen_tax,
        sum(dividen_qty) as dividen_qty,
        sum(t9_1.asset_price) as asset_price,
        sum(t9_1.pre_asset_price) as pre_asset_price
    from
    (
        SELECT
            t1.init_date,
            t1.branch_no,
            t1.exchange_type,
            t1.stock_code,
            t1.fund_account,
            t1.client_id,
            t1.deliver_type,
            t1.stock_type,
            t1.asset_prop,
            --t1.position_amount AS share_1,
            case when t8.business_flag=4014 then t1.position_amount-t8.business_amount else t1.position_amount end as share,                        --正常持仓减去配股的持仓数量
            case when t8.business_flag=4014 then (t7.asset_price-t8.asset_price)*t8.business_amount else 0 end as allotment_profit,                    --配股盈利
            case when t8.business_flag=4014 then t8.asset_price*t8.business_amount else 0 end as allotment_cost,                                      --配股日初成本(配股价格*配股数量)
            0 AS total_buy_amt,    --今日买入金额
            0 AS total_sell_amt,    --今日卖出金额
            0 AS buy_qty,                --今日买入数量
            0 AS sell_qty,                --今日卖出数量
            0 AS dividen_amt,  --今日分红金额
            0 AS dividen_tax,  --今日红股红利税
            0 AS dividen_qty,       --今日红股数量
            t7.asset_price,
            t7.pre_asset_price
        FROM
            (select * from t2f_stock_his WHERE event_date=${hivevar:natural_date})t1
--        LEFT JOIN
--            t_deliver t2
--            ON
--            t2.business_flag<>4014 and
--            t1.init_date = t2.init_date AND
--    --        t1.branch_no = t2.branch_no AND
--            t1.exchange_type= t2.exchange_type AND
--            t1.stock_code = t2.stock_code AND
--            t1.fund_account = t2.fund_account --AND
    --        t1.client_id = t2.client_id AND
    --        t1.stock_type = t2.stock_type AND
    --        t1.asset_prop = t2.asset_prop
    --        t1.deliver_type=t2.deliver_type
        left join
        (
            select
                   t8_1.init_date,
                   t8_1.exchange_type,
                   t8_1.stock_code,
                   COALESCE(round(sum(t8_1.last_price*t8_1.business_amount)/sum(t8_1.business_amount),6),0) as asset_price,
                   t8_1.fund_account,
                   t8_1.client_id,
                   t8_1.business_flag,
                   0 as clear_balance_rmb,
                   sum(case when t8_2.position_amount=0 then t8_1.business_amount else 0 end) as business_amount
            from
            (
            select * from t_deliver WHERE business_flag=4014) t8_1
            join
            (select * from t2f_stock_his WHERE event_date=${hivevar:natural_date} and stock_type=3)t8_2
            on
               t8_1.init_date = t8_2.init_date AND
                  t8_1.exchange_type= t8_2.exchange_type AND
                  t8_1.stock_code_2 = t8_2.stock_code AND
                  t8_1.fund_account = t8_2.fund_account
            --where t8_1.business_flag=4014 and t8_2.stock_type=3
            group by
                   t8_1.init_date,
                   t8_1.exchange_type,
                   t8_1.stock_code,
                   t8_1.fund_account,
                   t8_1.client_id,
                   t8_1.business_flag
        )t8
            on
            t1.init_date = t8.init_date AND
            t1.exchange_type= t8.exchange_type AND
            t1.stock_code = t8.stock_code AND
            t1.fund_account = t8.fund_account
        LEFT JOIN
            (
                select t7_1.init_date,t7_1.stock_code,t7_1.exchange_type,asset_price,
                       case when t7_2.business_flag=4016 and t7_2.stock_type='l' and pre_asset_price*100=asset_price then pre_asset_price*100
                            else pre_asset_price end as pre_asset_price                 --因为etf基金,昨日价格为1，当天价格为100，所以需要处理昨日价格，变为1*100
                from
                   t_price t7_1
                left join
                   (
                       select ${hivevar:natural_date} as init_date,stock_code,exchange_type,stock_type,business_flag
                       from
                            (
                                SELECT trading_date,natural_date
                                FROM t1b_transactiondate
                                WHERE natural_date=from_unixtime(unix_timestamp(CAST(${hivevar:natural_date} AS STRING),'yyyyMMdd')-3600*24,'yyyyMMdd')
                                group by trading_date,natural_date
                            )t7_3         --取前一天对应的交易日
                       inner join
                            (select * from t1a_deliver_his where business_flag=4016 and stock_type='l')t7_4
                       on t7_4.init_date= t7_3.trading_date    --取 t1a_deliver_his 的前一个交易日的数据
                       --where t7_4.event_month=SUBSTR(t7_3.trading_date,1,6) and t7_4.business_flag=4016 and t7_4.stock_type='l'
                       group by stock_code,exchange_type,stock_type,business_flag
                   )t7_2
                on t7_1.init_date    =  t7_2.init_date    and
                   t7_1.stock_code   =  t7_2.stock_code   and
                   t7_1.exchange_type=  t7_2.exchange_type
            )t7
            ON
            t7.init_date=t1.init_date AND
            t7.exchange_type=t1.exchange_type AND
            t7.stock_code=t1.stock_code
            --WHERE t1.event_date=${hivevar:natural_date} --AND t1.position_amount<>0
      UNION ALL
            SELECT
                t2.init_date,
                t2.branch_no,
                t2.exchange_type,
                t2.stock_code,
                t2.fund_account,
                t2.client_id,
                t2.deliver_type,
                t2.stock_type,
                t2.asset_prop,
                --t1.position_amount AS share_1,
                0 as share,                        --正常持仓减去配股的持仓数量
                0 as allotment_profit,                    --配股盈利
                0 as allotment_cost,                                      --配股日初成本(配股价格*配股数量)
                COALESCE(CASE WHEN (t2.deliver_type IN ('1','2','3') and t2.entrust_bs=1) or t2.business_flag=4104 THEN t2.clear_balance_rmb
                              when t2.deliver_type = '5' and t2.entrust_bs=1 then t2.business_amount*price_avg  ELSE 0 END,0) AS total_buy_amt,    --今日买入金额
                COALESCE(CASE WHEN (t2.deliver_type IN ('1','2','3') and t2.entrust_bs=2) or t2.business_flag=4105 THEN t2.clear_balance_rmb
                              when t2.deliver_type = '5' and t2.entrust_bs=2 then t2.business_amount*price_avg  ELSE 0 END,0) AS total_sell_amt,    --今日卖出金额
                COALESCE(CASE WHEN (t2.deliver_type IN ('1','2','3') and t2.entrust_bs=1) or t2.business_flag=4104 THEN t2.business_amount
                              when t2.deliver_type = '5' and t2.entrust_bs=1 then t2.business_amount  ELSE 0 END,0) AS buy_qty,                --今日买入数量
                COALESCE(CASE WHEN (t2.deliver_type IN ('1','2','3') and t2.entrust_bs=2) or t2.business_flag=4105 THEN t2.business_amount
                              when t2.deliver_type = '5' and t2.entrust_bs=2 then t2.business_amount ELSE 0 END,0) AS sell_qty,                --今日卖出数量
                COALESCE(CASE WHEN t2.business_flag=4018 THEN t2.clear_balance_rmb ELSE 0 END,0) AS dividen_amt,  --今日分红金额
                COALESCE(CASE WHEN t2.business_flag=2434 THEN t2.clear_balance_rmb ELSE 0 END,0) AS dividen_tax,  --今日红股红利税
                COALESCE(CASE WHEN t2.business_flag=4015 THEN t2.business_amount ELSE 0 END,0) AS dividen_qty,       --今日红股数量
                0 as asset_price,
                0 as pre_asset_price
            FROM
                t_deliver t2
            where t2.business_flag<>4014 and (t2.deliver_type IN ('1','2','3','4','5') or t2.business_flag in ('4018','2434','4015'))
    )t9_1
    group by
        t9_1.init_date,
        t9_1.branch_no,
        t9_1.exchange_type,
        t9_1.stock_code,
        t9_1.fund_account,
        t9_1.client_id,
        --t9_1.deliver_type,
        t9_1.stock_type,
        t9_1.asset_prop
        --t9_1.share,
        --t9_1.allotment_profit,
        --t9_1.allotment_cost,
        --t9_1.asset_price,
        --t9_1.pre_asset_price
) t9;
--where fund_account=130067303 and stock_code=511280;
















