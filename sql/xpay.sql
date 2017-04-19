CREATE TABLE IF NOT EXISTS bill_app (
	id BIGSERIAL PRIMARY KEY,
	app_key varchar(64) NOT NULL,
	app_secret varchar(256) NOT NULL,
	create_date TIMESTAMP WITH TIME ZONE NOT NULL default now(), 
	update_date TIMESTAMP WITH TIME ZONE NOT NULL default now(),
	deleted boolean DEFAULT FALSE
);	
ALTER SEQUENCE bill_app_id_seq RESTART 10;
CREATE INDEX idx_app_key ON bill_app(app_key); 

CREATE TABLE IF NOT EXISTS bill_store (
	id BIGSERIAL PRIMARY KEY,
	code varchar(32) NOT NULL,
	name varchar(64) NOT NULL,
	bail NUMERIC NOT NULL default 0,
	non_bail NUMERIC NOT NULL default 0,
	bar NUMERIC NOT NULL default 1000,
	bail_percentage SMALLINT NOT NULL default 10,
	rotation_type varchar(16) NOT NULL default 'RoundRobin',
	rotation_index SMALLINT NOT NULL default 1,
	create_date TIMESTAMP WITH TIME ZONE NOT NULL default now(), 
	update_date TIMESTAMP WITH TIME ZONE NOT NULL default now(),
	deleted boolean DEFAULT FALSE
);
ALTER SEQUENCE bill_store_id_seq RESTART 100;
CREATE INDEX idx_bill_store_code ON bill_store(code); 

CREATE TABLE IF NOT EXISTS bill_store_channel (
	id BIGSERIAL PRIMARY KEY,
	store_id BIGINT NOT NULL,
	ext_store_id varchar(32) NOT NULL,
	priority INT NOT null default 1,
	payment_gateway varchar(16) NOT NULL,
	create_date TIMESTAMP WITH TIME ZONE NOT NULL default now(), 
	update_date TIMESTAMP WITH TIME ZONE NOT NULL default now(),
	deleted boolean DEFAULT FALSE
);
ALTER SEQUENCE bill_store_channel_id_seq RESTART 100;
CREATE INDEX idx_bill_store_channel ON bill_store_channel(store_id); 

CREATE TABLE IF NOT EXISTS bill_order (
	id BIGSERIAL PRIMARY KEY,
	order_no varchar(32) NOT NULL,
	app_id BIGINT NOT NULL,
	store_id BIGINT NOT NULL,
	store_channel BIGINT NOT NULL,
	total_fee varchar(10) NOT NULL,
	order_time varchar(14) NOT NULL,
	pay_channel varchar(16) NOT NULL,
	seller_order_no varchar(64),
	ext_order_no varchar(64),
	attach varchar(256),
	device_id varchar(32),
	ip varchar(32),
	notify_url varchar(256),
	code_url varchar(256),
	prepay_id varchar(64),
	token_id varchar(64),
	status varchar(16),
	detail_id BIGINT,
	create_date TIMESTAMP WITH TIME ZONE NOT NULL default now(), 
	update_date TIMESTAMP WITH TIME ZONE NOT NULL default now(),
	deleted boolean DEFAULT FALSE
);
ALTER SEQUENCE bill_order_id_seq RESTART 1000;
CREATE INDEX idx_order_no ON bill_order(order_no); 
CREATE INDEX idx_ext_order_no ON bill_order(ext_order_no); 

CREATE TABLE IF NOT EXISTS bill_order_detail (
	id BIGSERIAL PRIMARY KEY,
	store_name varchar(64),
	operator varchar(64),
	subject varchar(64),
	description varchar(256),
	items varchar(2048),
	create_date TIMESTAMP WITH TIME ZONE NOT NULL default now(), 
	update_date TIMESTAMP WITH TIME ZONE NOT NULL default now(),
	deleted boolean DEFAULT FALSE
);
ALTER SEQUENCE bill_order_detail_id_seq RESTART 1000;


//Init data
insert into bill_app (id, app_key, app_secret) values (1, 'b471565ef7394b439c00ea47052e', '93039FAF4719BCA16CF51DA9D86D8BCD');
insert into bill_app (id, app_key, app_secret) values (2, '39ba4501-8e43-403e-b31a-1601963151ab', 'wZAU7CE4pXqPiwbxq1jTuoKp4CGSjC4Drbtfc9My0g9PzRGXS1pYzGUmcGDUKwuX20v4hEWonn1Dr66xrbCtBFXJ');
insert into bill_app (id, app_key, app_secret) values (3, '2dbfa7ce-4af4-4b3e-abed-929fbaa4a2f1', 'eGGtcSaUUf2S55Px3RduyNszYD9mDwpsFBM3FvMa2u48UV7GRl3LX9PUorncnk7sGMtbhFzn7fqobd2tr2uBcJRy');

insert into bill_store (id, code, name, bail, non_bail, bar, bail_percentage) values (1, 'T000', 'Bail Store', 0, 0, 0, 100);
insert into bill_store_channel (id, store_id, ext_store_id, payment_gateway) values (1, 1, 'T2017032319251974486873', 'MIAOFU');

insert into bill_store (id, code, name, bail, non_bail, bar, bail_percentage) values (2, 'T001', 'Bail Store 1', 0, 0, 0, 100);
insert into bill_store_channel (id, store_id, ext_store_id, payment_gateway) values (2, 2, '755437000006', 'SWIFTPASS');

insert into bill_store (id, code, name, bail, non_bail, bar, bail_percentage) values (3, 'T002', 'Bail Store 2', 0, 0, 0, 100);
insert into bill_store_channel (id, store_id, ext_store_id, payment_gateway) values (3, 3, '898340149000005', 'CHINAUMS');

insert into bill_store (id, code, name, bail, non_bail, bar, bail_percentage) values (4, 'T003', 'Bail Store 4', 0, 0, 0, 100);
insert into bill_store_channel (id, store_id, ext_store_id, payment_gateway) values (4, 4, '7551000001', 'RUBIPAY');


insert into bill_store (id, code, name) values (51, 'T20070331091523123', '秒付测试商户');
insert into bill_store_channel (id, store_id, ext_store_id, payment_gateway) values (51, 51, 'T2017032319251974486873', 'MIAOFU');

insert into bill_store (id, code, name) values (52, 'T20170412163933306', '威富通测试商户');
insert into bill_store_channel (id, store_id, ext_store_id, payment_gateway) values (52, 52, '755437000006', 'SWIFTPASS');

insert into bill_store (id, code, name) values (53, 'T20170412143221368', '银商测试商户');
insert into bill_store_channel (id, store_id, ext_store_id, payment_gateway) values (53, 53, '898340149000005', 'CHINAUMS');

insert into bill_store (id, code, name) values (54, 'T20170419143221468', 'RubiPay测试商户');
insert into bill_store_channel (id, store_id, ext_store_id, payment_gateway) values (54, 54, '7551000001', 'RUBIPAY');


insert into bill_store (code, name) values ('T20170412153151533', '池乐');
insert into bill_store_channel (store_id, ext_store_id, payment_gateway) values (100, '000010105000126', 'MIAOFU');
insert into bill_store_channel (store_id, ext_store_id, payment_gateway) values (100, '000010105000127', 'MIAOFU');

insert into bill_store (code, name) values ('T20070405151523101', '深圳贝碧嘉');
insert into bill_store_channel (store_id, ext_store_id, payment_gateway) values (101, '102520441241', 'SWIFTPASS');


ALTER table bill_app OWNER TO xpay;
ALTER table bill_store OWNER TO xpay;
ALTER table bill_store_channel OWNER TO xpay;
ALTER table bill_order OWNER TO xpay;
ALTER table bill_order_detail OWNER TO xpay;

