#!/usr/bin/python
# -*- coding: UTF-8 -*- 

import os
import datetime
import psycopg2
import csv
import codecs, sys

REPORT_PATH = '/data/reports/data/'
reload(sys)
sys.setdefaultencoding('utf8')

def export_csv( cur, store_id, store_code, store_name ):
    FILE_PATH = REPORT_PATH + store_name + '/' 
    if not os.path.exists(FILE_PATH):
        os.makedirs(FILE_PATH)

    yesterday = datetime.datetime.now() - datetime.timedelta(days=1)
    ydate = yesterday.strftime("%Y-%m-%d");
    start = ydate + ' 00:00:00'
    end = ydate + ' 23:59:59'
    order_no = "Order_No"
    seller_order_no = "Client_Order_No"
    ext_order_no = "Channel_Order_No"
    target_order_no = "Target_Order_No"
    total_fee = "Total_Amount"
    status = "Order_Status"
    order_time = "Order_Time"
    update_date = "Pay_Time"
    ext_store_id = "Channel_No"    
    bill_type = "Bill_Type"    

    FILE_NAME= FILE_PATH +'/' + store_code + '_' + ydate + '.csv'
    
    query = """select a.order_no as %(order_no)s, '\t' || a.seller_order_no as %(seller_order_no)s, '\t' || a.ext_order_no as %(ext_order_no)s, '\t' || a.target_order_no as %(target_order_no)s, a.total_fee as %(total_fee)s, a.status as %(status)s, '\t' || a.order_time as %(order_time)s, a.update_date as %(update_date)s, '\t' || b.ext_store_id as %(ext_store_id)s, b.bill_type as %(bill_type)s  From bill_order a, bill_store_channel b
                  where a.store_channel=b.id and a.store_id = %(store_id)s and a.update_date >= '%(start)s' and a.update_date <= '%(end)s' and a.deleted = false
           """ % dict(order_no=order_no,seller_order_no=seller_order_no,ext_order_no=ext_order_no,target_order_no=target_order_no,total_fee=total_fee,status=status,order_time=order_time,update_date=update_date,ext_store_id=ext_store_id,bill_type=bill_type,store_id=store_id, start=start, end=end)
  
 #   print query
    outputquery = "COPY ({0}) TO STDOUT CSV HEADER".format(query)
    with codecs.open(FILE_NAME, 'wb', 'utf-8') as f:
        cur.copy_expert(outputquery, f) 
    return 

def get_all_stores ( cur ):
     query = """select id, code, name from bill_store where id>100 and deleted=false"""
     cur.execute(query)
     rows = cur.fetchall()
     return rows;

conn = psycopg2.connect(database="xpay", user="xpay", password="POiu!@zxCV", host="127.0.0.1", port="5432")
conn.set_client_encoding('utf-8')
cur = conn.cursor()

rows = get_all_stores( cur )
for row in rows: 
   export_csv(cur, str(row[0]), row[1],row[2])

cur.close()
conn.close()
