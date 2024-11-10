insert into customers(id, name, email, created_date, updated_date) values('1', 'paik', 'melon', localtime(), localtime());
insert into points(id, customer_id, point, created_date, updated_date, version) values('1', '1', 10000, localtime(), localtime(), 0);

insert into customers(id, name, email, created_date, updated_date) values('2', 'sung', 'banana', localtime(), localtime());
insert into points(id, customer_id, point, created_date, updated_date, version) values('2', '2', 10000, localtime(), localtime(), 0);

insert into customers(id, name, email, created_date, updated_date) values('3', 'gyu', 'apple', localtime(), localtime());
insert into points(id, customer_id, point, created_date, updated_date, version) values('3', '3', 10000, localtime(), localtime(), 0);

insert into items(id, item_name, item_price, created_date, updated_date) values('1', '코트', 1, localtime(), localtime());
insert into item_options(id, item_id, item_option_price, item_option_color, item_option_size, created_date, updated_date)  values('1', '1', 0, 'red', '100', localtime(), localtime());
insert into item_inventories(id, item_option_id, quantity, created_date, updated_date, version) values('1', '1', 1000000, localtime(), localtime(), 0);

insert into items(id, item_name, item_price, created_date, updated_date) values('2', '니트', 2, localtime(), localtime());
insert into item_options(id, item_id, item_option_price, item_option_color, item_option_size, created_date, updated_date)  values('2', '2', 0, 'green', '105', localtime(), localtime());
insert into item_inventories(id, item_option_id, quantity, created_date, updated_date, version) values('2', '2', 1000000, localtime(), localtime(), 0);

insert into items(id, item_name, item_price, created_date, updated_date) values('3', '바지', 3, localtime(), localtime());
insert into item_options(id, item_id, item_option_price, item_option_color, item_option_size, created_date, updated_date)  values('3', '3', 0, 'blue', '32', localtime(), localtime());
insert into item_inventories(id, item_option_id, quantity, created_date, updated_date, version) values('3', '3', 1000000, localtime(), localtime(), 0);

insert into items(id, item_name, item_price, created_date, updated_date) values('4', '반바지', 4, localtime(), localtime());
insert into item_options(id, item_id, item_option_price, item_option_color, item_option_size, created_date, updated_date)  values('4', '4', 0, 'yellow', '25', localtime(), localtime());
insert into item_inventories(id, item_option_id, quantity, created_date, updated_date, version) values('4', '4', 1000000, localtime(), localtime(), 0);

insert into items(id, item_name, item_price, created_date, updated_date) values('5', '모자', 5, localtime(), localtime());
insert into item_options(id, item_id, item_option_price, item_option_color, item_option_size, created_date, updated_date)  values('5', '5', 0, 'yellow', '32', localtime(), localtime());
insert into item_inventories(id, item_option_id, quantity, created_date, updated_date, version) values('5', '5', 1000000, localtime(), localtime(), 0);

insert into items(id, item_name, item_price, created_date, updated_date) values('6', '반바지', 6, localtime(), localtime());
insert into item_options(id, item_id, item_option_price, item_option_color, item_option_size, created_date, updated_date)  values('6', '6', 0, 'purple', '25', localtime(), localtime());
insert into item_inventories(id, item_option_id, quantity, created_date, updated_date, version) values('6', '6', 1000000, localtime(), localtime(), 0);

insert into items(id, item_name, item_price, created_date, updated_date) values('7', '양말', 7, localtime(), localtime());
insert into item_options(id, item_id, item_option_price, item_option_color, item_option_size, created_date, updated_date)  values('7', '7', 0, 'green', '270', localtime(), localtime());
insert into item_inventories(id, item_option_id, quantity, created_date, updated_date, version) values('7', '7', 1000000, localtime(), localtime(), 0);

insert into items(id, item_name, item_price, created_date, updated_date) values('8', '반팔', 8, localtime(), localtime());
insert into item_options(id, item_id, item_option_price, item_option_color, item_option_size, created_date, updated_date)  values('8', '8', 0, 'white', 'large', localtime(), localtime());
insert into item_inventories(id, item_option_id, quantity, created_date, updated_date, version) values('8', '8', 1000000, localtime(), localtime(), 0);

insert into items(id, item_name, item_price, created_date, updated_date) values('9', '치마', 9, localtime(), localtime());
insert into item_options(id, item_id, item_option_price, item_option_color, item_option_size, created_date, updated_date)  values('9', '9', 0, 'skyblue', '32', localtime(), localtime());
insert into item_inventories(id, item_option_id, quantity, created_date, updated_date, version) values('9', '9', 1000000, localtime(), localtime(), 0);

insert into items(id, item_name, item_price, created_date, updated_date) values('10', '신발', 10, localtime(), localtime());
insert into item_options(id, item_id, item_option_price, item_option_color, item_option_size, created_date, updated_date)  values('10', '10', 0, 'black', '270', localtime(), localtime());
insert into item_inventories(id, item_option_id, quantity, created_date, updated_date, version) values('10', '10', 1000000, localtime(), localtime(), 0);