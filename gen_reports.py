#!/usr/bin/python
# -*- coding: UTF-8 -*-

import os
import datetime
import psycopg2

REPORT_PATH = '/data/reports/data/'


def export_csv( cur, store_id, store_code, store_name ):
    FILE_PATH = REPORT_PATH + store_name + '/'
    if not os.path.exists(FILE_PATH):
        os.makedirs(FILE_PATH)

    yesterday = datetime.datetime.now() - datetime.timedelta(days=1)
    ydate = yesterday.strftime("%Y-%m-%d");
    start = ydate + ' 00:00:00'
    end = ydate + ' 23:59:59'

    FILE_NAME= FILE_PATH +'/' + store_code + '_' + ydate + '.csv'

    query = """select order_no, store_id, total_fee, status, update_date From bill_order
                  where store_id = %(store_id)s and update_date >= '%(start)s' and update_date <= '%(end)s' and deleted = false
           """ % dict(store_id=store_id, start=start, end=end)

 #   print query
    outputquery = "COPY ({0}) TO STDOUT WITH CSV HEADER".format(query)
    with open(FILE_NAME, 'w') as f:
        cur.copy_expert(outputquery, f)
    return

def get_all_stores ( cur ):
     query = """select id, code, name from bill_store where id>100 and deleted=false"""
     cur.execute(query)
     rows = cur.fetchall()
     return rows;

conn = psycopg2.connect(database="xpay", user="xpay", password="POiu!@zxCV", host="127.0.0.1", port="5432")
cur = conn.cursor()

rows = get_all_stores( cur )
for row in rows:
   export_csv(cur, str(row[0]), row[1],row[2])

conn.close()
