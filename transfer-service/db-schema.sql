

create table accounts(
    account_id varchar(12) primary key,
    account_holder_name varchar(100) not null,
    balance decimal(15,2) not null
)

insert into accounts(account_id, account_holder_name, balance) values
('A001', 'John Doe', 1500.00),
('A002', 'Jane Smith', 2500.50),
('A003', 'Alice Johnson', 3000.75);