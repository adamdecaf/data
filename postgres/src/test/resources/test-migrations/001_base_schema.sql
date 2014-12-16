create or replace function setup_test_table_schema() returns void as
$$

begin
if not exists(select 1 from pg_tables where tablename='test_table') then

create table test_table(
       row_id varchar(36) primary key not null,
       data varchar
);

end if;
end;
$$ language plpgsql;

select setup_test_table_schema();
drop function setup_test_table_schema();
