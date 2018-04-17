# --- Sample data set

# --- !Ups

insert into category (id,filter) values ( 1,'comic' );
insert into category (id,filter) values ( 2,'Action Figure' );
insert into category (id,filter) values ( 3,'Poster' );

insert into product (id,title,description,price,maxPrice) values ( 1,'Gwenpool #1','First issue of the Gwenpool comics',25,50);
insert into product (id,title,description,price,maxPrice) values ( 2,'Superman','The man of steel in his trademarked pose',60,90);


insert into user(email,name,username,password,role,address1,address2,city) values ( 'user@mail', 'User Eugine', 'Me Me Eugine', 'password', 'user', '1 Meme St.', 'Dankville', 'de wey');
insert into user(email,name,username,password,role,address1,address2,city) values ( 'admin@mail', 'Admin Aiden', 'Me Me Eugine', 'password', 'admin', '1 Meme St.', 'Dankville', 'de wey');

