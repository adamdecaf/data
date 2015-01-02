create or replace function http_storage_schema() returns void as
$$

begin

if not exists(select 1 from pg_tables where tablename='http_requests') then
   create table http_requests(
          http_request_id varchar(36) primary key not null,
          http_exchange_id varchar(36) not null,
          uri varchar(36) not null,
          time timestamp(0) not null
   );
end if;

if not exists(select 1 from pg_tables where tablename='http_responses') then
   create table http_responses(
          http_response_id varchar(36) primary key not null,
          http_exchange_id varchar(36) not null,
          uri varchar(36) not null,
          status_code int not null,
          s3_object_key varchar(256) not null,
          time timestamp(0) not null
   );
end if;

end;
$$ language plpgsql;

select http_storage_schema();
drop function http_storage_schema();
