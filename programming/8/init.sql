-- clear script
do $$
declare
  t text;
begin
  for t in
    select table_name
    from information_schema.tables
    where table_schema = 'public'
  loop
    execute 'drop table if exists ' || t || ' cascade';
  end loop;
end $$;

drop type if exists ticket_type;
drop type if exists event_type;

-- create script
create type ticket_type as enum(
  'VIP',
  'USUAL',
  'BUDGETARY',
  'CHEAP'
);

create type event_type as enum(
  'CONCERT',
  'BASEBALL',
  'OPERA'
);

create table COLLECTIONS (
  id serial primary key,
  name varchar(42) not null,
  type text,
  creation_date timestamp not null default now()
);

create table USERS (
  name varchar(42) primary key,
  passwd_hash text not null
);

create table EVENTS (
  id bigserial primary key,
  name text not null,
  description varchar(1190) not null,
  type event_type not null
);

create table TICKETS (
  id bigserial primary key,
  name text not null,
  x real not null,
  y real not null,
  creation_date timestamp not null default now(),
  price integer,
  type ticket_type not null,
  event bigint references EVENTS(id) on delete cascade,
  author varchar(42) references USERS(name) on delete cascade
);

insert into COLLECTIONS(name, type) values ('A new collection', 'Tickets');