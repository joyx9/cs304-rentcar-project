drop table returns;
drop table rentals;
drop table reservations;
drop table customer;
drop table vehicle;
drop table vehicletype;
drop sequence reserveConfNo;
drop sequence rentID;

create table vehicletype
	(vtname varchar(40) not null,
	features varchar(40),
	wrate int not null,
	drate int not null,
	hrate int not null,
	wirate int not null,
	dirate int not null,
	hirate int not null,
	krate int not null,
	primary key (vtname));

create table vehicle 
	(vlicense varchar(40) not null,
	make varchar(40) not null,
	model varchar(40) not null,
	year int not null,
	color varchar(40) not null,
	odometer int not null,
	vtname varchar(40) not null,
	status varchar(40) not null,
	location varchar(40) not null,
	city varchar(40) not null,
	primary key (vlicense),
	foreign key (vtname) references vehicletype ON DELETE CASCADE);

create table customer
	(name varchar(40) not null,
	address varchar(40) not null,
	dlicense int not null,
	primary key (dlicense));

	
create table reservations
	(confNo int not null,
	vtname varchar(40) not null,
	dlicense int not null,
	reserveFromDate varchar(40) not null,
	reserveToDate varchar(40) not null,
	primary key (confNo),
	foreign key (dlicense) references customer ON DELETE CASCADE,
	foreign key (vtname) references vehicletype ON DELETE CASCADE);

create table rentals
	(rid int not null,
	dlicense int not null,
	vlicense varchar(40) not null,
	rentFromDate varchar(40) not null,
	rentToDate varchar(40) not null,
	odometer int not null,
	cardName varchar(40) not null,
	cardNo int not null,
	ExpDate varchar(40) not null,
	confNo int,
	primary key (rid),
	foreign key (vlicense) references vehicle ON DELETE CASCADE,
	foreign key (confNo) references reservations ON DELETE CASCADE,
	foreign key (dlicense) references customer ON DELETE CASCADE);


create table returns
	(rid int not null,
	dateReturned varchar(40) not null,
	timeReturned varchar(40) not null,
	odometer int not null,
	fulltank char(1),
	value varchar(40) not null,
	foreign key (rid) references rentals ON DELETE CASCADE,
	check (fulltank = 'y' or fulltank ='n'));

create sequence reserveConfNo 
start with 1
increment by 1;

create sequence rentID
start with 1
increment by 1;

-- Insert customer, item and purchase data

insert into customer 
values('John', '5234 cool st.', 5523634);

insert into vehicletype
values('economy', 'gps', 10, 5, 2, 100, 50, 20, 10);
insert into vehicletype
values('compact', 'small', 10, 5, 2, 100, 50, 20, 10);
insert into vehicletype
values('standard', 'radio', 10, 5, 2, 100, 50, 20, 10);
insert into vehicletype
values('SUV', 'hot wheels', 10, 5, 2, 100, 50, 20, 10);
insert into vehicletype
values('truck', 'big', 10, 5, 2, 100, 50, 20, 10);

insert into vehicle
values('AAA123', 'Honda', 'Accord', 2000, 'black', 10, 'economy', 'available', '654 Main St', 'Vancouver');
insert into vehicle
values('AAA124', 'Honda', 'Pilot', 2003, 'white', 10, 'economy', 'available', '654 SuperRent St', 'Winnepeg');
insert into vehicle
values('AAA125', 'Mazda', 'Accord', 2010, 'blue', 10, 'economy', 'available', '654 Broadway Rd', 'Burnaby');
insert into vehicle
values('AAA126', 'Lexus', 'GX', 2000, 'black', 10, 'compact', 'available', '654 Main St', 'Vancouver');
insert into vehicle
values('AAA127', 'Hyundai', 'Accord', 1995, 'black', 10, 'compact', 'available', '654 Main St', 'Vancouver');
insert into vehicle
values('AAA128', 'Subaru', 'Mini', 2010, 'red', 10, 'compact', 'available', '654 Main St', 'Burnaby');
insert into vehicle
values('AAA129', 'Honda', 'Accord', 2000, 'silver', 10, 'standard', 'available', '654 Main St', 'Montreal');
insert into vehicle
values('NFG325', 'Toyota', 'Corolla', 2009, 'silver', 10, 'standard', 'available', '654 Main St', 'Toronto');
insert into vehicle
values('FHF324', 'Ford', 'Mustang', 2009, 'black', 10, 'standard', 'available', '654 Main St', 'Winnepeg');
insert into vehicle
values('FDG523', 'Toyota', 'RAV4', 2010, 'white', 10, 'SUV', 'available', '654 Main St', 'Calgary');
insert into vehicle
values('GFR325', 'Honda', 'Passport', 2015, 'red', 10, 'SUV', 'available', '654 Main St', 'Toronto');
insert into vehicle
values('LSD352', 'Honda', 'Fit', 2014, 'blue', 10, 'SUV', 'available', '654 Main St', 'Calgary');
insert into vehicle
values('LFO350', 'Honda', 'Pilot', 2015, 'black', 10, 'truck', 'available', '654 Main St', 'Vancouver');
insert into vehicle
values('LGT333', 'Ford', 'Explorer', 2012, 'silver', 10, 'truck', 'available', '654 Main St', 'Burnaby');
insert into vehicle
values('SDF168', 'Subaru', 'Forester', 2009, 'black', 10, 'truck', 'available', '654 Main St', 'Vancouver');
insert into vehicle
values('KGL954', 'Ford', 'Escape', 2012, 'blue', 10, 'economy', 'available', '654 Main St', 'Winnepeg');
insert into vehicle
values('HGF453', 'Honda', 'Accord', 2014, 'red', 10, 'compact', 'available', '654 Main St', 'Montreal');
insert into vehicle
values('KHG056', 'Subaru', 'Outback', 2015, 'black', 10, 'standard', 'available', '654 Main St', 'Vancouver');
insert into vehicle
values('FHE532', 'Mazda', 'Point', 2009, 'white', 10, 'SUV', 'available', '654 Main St', 'Toronto');
insert into vehicle
values('GFT325', 'Toyota', 'Tomato', 2015, 'white', 10, 'truck', 'available', '654 Main St', 'Burnaby');
