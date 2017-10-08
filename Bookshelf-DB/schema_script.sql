-- USERS TABLE
create table Users (
	id int not null generated always as identity (start with 1, increment by 1),
	name varchar(63),
	email varchar(255) not null,
	pwd_hash varchar(255) not null,
	location varchar(63),
	is_writer boolean,
	created timestamp,
	last_updated timestamp,
	primary key (id)
);

alter table Users add constraint email_uc unique (email);

create trigger setUsersCreatedTimeStamp
	after insert on Users
	referencing new as newrow
	for each row
	update Users set created = CURRENT_TIMESTAMP, last_updated = CURRENT_TIMESTAMP where id = newrow.id;

create trigger setUserslast_updatedTimeStamp
	after update of id, name, email, pwd_hash, location, is_writer on Users
	referencing old as oldrow
	for each row
	update Users set last_updated = CURRENT_TIMESTAMP where id = oldrow.id;

-- BOOK TABLE
create table Book (
	id int primary key not null generated always as identity (start with 1, increment by 1),
	name varchar(127) not null,
	isbn bigint,
	author varchar(127),
	published date,
	publisher varchar(127),
	printed date,
	created timestamp,
	last_updated timestamp
);

create trigger setBookCreatedTimeStamp
	after insert on Book
	referencing new as newrow
	for each row
	update Book set created = CURRENT_TIMESTAMP, last_updated = CURRENT_TIMESTAMP where id = newrow.id;

create trigger setBooklast_updatedTimeStamp
	after update of id, name, isbn, author, published, publisher, printed on Book
	referencing old as oldrow
	for each row
	update Book set last_updated = CURRENT_TIMESTAMP where id = oldrow.id;

-- READ MANY TO MANY RELATION TABLE
create table ReadM2M (
	id int primary key not null generated always as identity (start with 1, increment by 1),
	user_id int not null,
	book_id int not null,
	next_book_id int,
	category varchar(31),
	created timestamp,
	last_updated timestamp,
	constraint fk_read2users foreign key (user_id) references Users (id),
	constraint fk_read2book foreign key (book_id) references Book (id),
	constraint fk_read2read foreign key (next_book_id) references ReadM2M (id)
);

create trigger setReadM2MCreatedTimeStamp
	after insert on ReadM2M
	referencing new as newrow
	for each row
	update ReadM2M set created = CURRENT_TIMESTAMP, last_updated = CURRENT_TIMESTAMP where id = newrow.id;

create trigger setReadM2Mlast_updatedTimeStamp
	after update of id, user_id, book_id, next_book_id, category on ReadM2M
	referencing old as oldrow
	for each row
	update ReadM2M set last_updated = CURRENT_TIMESTAMP where id = oldrow.id;

-- WRITE MANY TO MANY RELATION TABLE
create table WriteM2M (
	id int primary key not null generated always as identity (start with 1, increment by 1),
	user_id int not null,
	book_id int not null,
	created timestamp,
	last_updated timestamp,
	constraint fk_write2users foreign key (user_id) references Users (id),
	constraint fk_write2book foreign key (book_id) references Book (id)
);

create trigger setWriteM2MCreatedTimeStamp
	after insert on WriteM2M
	referencing new as newrow
	for each row
	update WriteM2M set created = CURRENT_TIMESTAMP, last_updated = CURRENT_TIMESTAMP where id = newrow.id;

create trigger setWriteM2Mlast_updatedTimeStamp
	after update of id, user_id, book_id on WriteM2M
	referencing old as oldrow
	for each row
	update WriteM2M set last_updated = CURRENT_TIMESTAMP where id = oldrow.id;