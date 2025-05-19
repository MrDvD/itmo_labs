-- clear script
DO $$
DECLARE
    t text;
BEGIN
    FOR t IN
        SELECT table_name
        FROM information_schema.tables
        WHERE table_schema = 's466449'
    LOOP
        EXECUTE 'DROP TABLE IF EXISTS ' || t || ' CASCADE';
    END LOOP;
END $$;

DROP TYPE IF EXISTS ticket_type;
DROP TYPE IF EXISTS event_type;

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

create table USERS (
  id serial primary key,
  name varchar(42) not null,
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
  author integer references USERS(id) on delete cascade
);