# --- Sample data set

# --- !Ups

insert into user(email,password,name,role) values ('admin@site.com', 'password', 'Mr. Admin', 'admin');
insert into user(email,password,name,role) values ('Jason@site.com', 'password', 'Jason P', 'user');
insert into user(email,password,name,role) values ('Chris@site.com', 'password', 'Chris McC', 'user');

insert into category (id,filter) values ( 1,'Gaming' );
insert into category (id,filter) values ( 3,'Comic' );
insert into category (id,filter) values ( 4,'Retro' );
insert into category (id,filter) values ( 5,'TV' );
insert into category (id,filter) values ( 6,'Does not apply' );

insert into product (id,title,category,useremail,description,price,max_price) values (1,'Dante Statue',1,'Jason@site.com','Statue of a Nephilim, Dante from DMC',50,80);
insert into product (id,title,category,useremail,description,price,max_price) values (2,'Star Pendant',6,'Jason@site.com','Rare necklace which only 15 were made 100 years ago',250,400);
insert into product (id,title,category,useremail,description,price,max_price) values (3,'Sketch book with full art drawings by animator of Duck tales',5,'Chris@site.com','An original sketch book by lead animator of the duck tales with over 300 drawings on 180 pages ',100,190);
insert into product (id,title,category,useremail,description,price,max_price) values (4,'Collection of Various deadpool comics',3,'Chris@site.com','A wild assortment of comics that follow anti hero Deadpool',50,50);
insert into product (id,title,category,useremail,description,price,max_price) values (5,'Game boy color',4,'Jason@site.com','A great condition game boy color with original packaging (opened) with pokemon Blue version',75,100);

insert into product_user (product_id, user_email) values (3, 'Jason@site.com');






