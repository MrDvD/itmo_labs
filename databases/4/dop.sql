create or replace function log_action()
  returns event_trigger as $$
begin
  copy (select session_user, now(), current_catalog, left(current_query(), 45) || '...')
    to 'myfile.log';
end
$$ language plpgsql;

create event trigger log_ddl on ddl_command_start
  execute procedure log_action();