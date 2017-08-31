#!/usr/bin/python
# -*- coding: UTF-8 -*- 

import os
import psycopg2

conn = psycopg2.connect(database="xpay", user="xpay", password="POiu!@zxCV", host="127.0.0.1", port="5432")
cur = conn.cursor()
cur.execute("update bill_store set non_bail=0, bail=0 where id>100 and deleted=false")
conn.commit()
cur.close()
conn.close()
