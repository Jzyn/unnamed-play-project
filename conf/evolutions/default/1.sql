# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                            bigint not null,
  filter                        varchar(255),
  constraint pk_category primary key (id)
);
create sequence category_seq;

create table product (
  id                            bigint not null,
  title                         varchar(255),
  category                      bigint,
  useremail                     varchar(255) not null,
  latest_bidder                 varchar(255),
  description                   varchar(1000),
  price                         integer,
  max_price                     integer,
  constraint pk_product primary key (id)
);
create sequence product_seq;

create table product_user (
  product_id                    bigint not null,
  user_email                    varchar(255) not null,
  constraint pk_product_user primary key (product_id,user_email)
);

create table user (
  role                          varchar(255),
  email                         varchar(255) not null,
  name                          varchar(255),
  username                      varchar(255),
  password                      varchar(255),
  address1                      varchar(255),
  address2                      varchar(255),
  city                          varchar(255),
  constraint pk_user primary key (email)
);

alter table product add constraint fk_product_category foreign key (category) references category (id) on delete restrict on update restrict;
create index ix_product_category on product (category);

alter table product add constraint fk_product_useremail foreign key (useremail) references user (email) on delete restrict on update restrict;
create index ix_product_useremail on product (useremail);

alter table product_user add constraint fk_product_user_product foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_product_user_product on product_user (product_id);

alter table product_user add constraint fk_product_user_user foreign key (user_email) references user (email) on delete restrict on update restrict;
create index ix_product_user_user on product_user (user_email);


# --- !Downs

alter table product drop constraint if exists fk_product_category;
drop index if exists ix_product_category;

alter table product drop constraint if exists fk_product_useremail;
drop index if exists ix_product_useremail;

alter table product_user drop constraint if exists fk_product_user_product;
drop index if exists ix_product_user_product;

alter table product_user drop constraint if exists fk_product_user_user;
drop index if exists ix_product_user_user;

drop table if exists category;
drop sequence if exists category_seq;

drop table if exists product;
drop sequence if exists product_seq;

drop table if exists product_user;

drop table if exists user;

