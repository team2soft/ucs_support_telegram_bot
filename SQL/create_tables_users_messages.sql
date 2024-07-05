--***************************************************************
/*
drop table telegram.users cascade;
drop table telegram.messages cascade;
*/
--***************************************************************
/*==============================================================*/
/* Table: telegram.users                             */
/*==============================================================*/
create table telegram.users (
    chat_id          int8       not null,
    first_name varchar(255)     null,
    last_name varchar(255)      null,
    username varchar(255)       null,
    registered_at TIMESTAMP     null,
    constraint pk_telegram_users_chat_id primary key (chat_id)
);

comment on table telegram.users is 'Пользователи в telegram';
comment on column telegram.users.chat_id        is 'ID пользователя / чата (chat_id)';
comment on column telegram.users.first_name     is 'Имя пользователя (first_name)';
comment on column telegram.users.last_name      is 'Фамилия пользователя (last_name)';
comment on column telegram.users.username       is 'username пользователя (username)';
comment on column telegram.users.registered_at  is 'Дата-время регистрации в нашей базе (registered_at)';


ALTER TABLE telegram.users OWNER TO ukrcarservice;
GRANT ALL ON TABLE telegram.users TO ukrcarservice;
GRANT SELECT ON TABLE telegram.users TO qa_ucs;
/*==============================================================*/
/* Table: telegram.messages                           */
/*==============================================================*/
create table telegram.messages (
   message_id  bigserial   not null,
   chat_id  int8      null,
   feedback_theme_id  int4   null,
   lang  varchar(2)   null,
   is_from_user boolean null default false,
   message  text   null,
   constraint pk_telegram_messages_chat_id primary key (chat_id)
);

comment on table telegram.messages  is 'Сообщения в telegram';
comment on column telegram.messages.message_id is 'ID сообщения (message_id)';
comment on column telegram.messages.chat_id is 'ID пользователя / чата (chat_id)';
comment on column telegram.messages.feedback_theme_id is 'ID темы фидбека (feedback_theme_id)';
comment on column telegram.messages.lang is 'Язык пользователя (lang)';
comment on column telegram.messages.is_from_user is 'Признак входящего от пользователя (is_from_user)';
comment on column telegram.messages.message is 'Текст сообщения (message)';

ALTER TABLE telegram.messages OWNER TO ukrcarservice;
GRANT ALL ON TABLE telegram.messages TO ukrcarservice;
GRANT SELECT ON TABLE telegram.messages TO qa_ucs;

-- ----------------------------------------------------------------------------------
