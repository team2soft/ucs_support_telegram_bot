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
    email_usc varchar(255)       null,
    phone_usc varchar(255)       null,
    employee_id int4       null,
    constraint pk_telegram_users_chat_id primary key (chat_id)
);

comment on table telegram.users is 'Пользователи в telegram';
comment on column telegram.users.chat_id        is 'ID пользователя / чата (chat_id)';
comment on column telegram.users.first_name     is 'Имя пользователя (first_name)';
comment on column telegram.users.last_name      is 'Фамилия пользователя (last_name)';
comment on column telegram.users.username       is 'username пользователя (username)';
comment on column telegram.users.phone_usc      is 'Телефон пользователя в UCS (phone_usc)';
comment on column telegram.users.email_usc      is 'Email пользователя в UCS (email_usc)';
comment on column telegram.users.employee_id    is 'ID сотрудника UCS (employee_id)';
comment on column telegram.users.registered_at  is 'Дата-время регистрации в нашей базе (registered_at)';

CREATE INDEX idx_telegram_users_employee_id ON telegram.users USING btree (employee_id);

ALTER TABLE telegram.users OWNER TO ukrcarservice;
GRANT ALL ON TABLE telegram.users TO ukrcarservice;
GRANT SELECT ON TABLE telegram.users TO qa_ucs;

INSERT INTO telegram.users
(chat_id, first_name, last_name, username, registered_at, email_usc, phone_usc, employee_id)
VALUES(7386948076, 'TestUkrCarService support', 'bot', 'https://t.me/TestUkrCarServiceSupportBot', now(), NULL, NULL, NULL);
INSERT INTO telegram.users
(chat_id, first_name, last_name, username, registered_at, email_usc, phone_usc, employee_id)
VALUES(6854063767, 'UkrCarService support', 'bot', 'https://t.me/UkrCarServiceSupportBot', now(), NULL, NULL, NULL);
/*==============================================================*/
/* Table: telegram.messages                           */
/*==============================================================*/
create table telegram.messages (
   message_id  bigserial   not null,
   create_dttm timestamp not null DEFAULT now(),
   chat_id  int8      null,
   user_id_from  int8      null,
   user_id_to  int8      null,
   feedback_theme_id  int4   null,
   lang  varchar(2)   null,
--    is_from_user boolean not null default false,
   text  text   null,
   constraint pk_telegram_messages_message_id primary key (message_id)
);

comment on table telegram.messages  is 'Сообщения в telegram';
comment on column telegram.messages.message_id is 'ID сообщения (message_id)';
comment on column telegram.messages.create_dttm is 'TIMESTAMP создания записи (заполняется автоматически) (create_dttm)';
comment on column telegram.messages.chat_id is 'ID пользователя / чата (chat_id)';
comment on column telegram.messages.feedback_theme_id is 'ID темы фидбека (feedback_theme_id)';
comment on column telegram.messages.lang is 'Язык пользователя (lang)';
comment on column telegram.messages.user_id_from is 'ID пользователя отправителя (user_id_from)';
comment on column telegram.messages.user_id_to is 'ID пользователя получателя (user_id_to)';
comment on column telegram.messages.text is 'Текст сообщения (text)';

CREATE INDEX idx_messages_chat_id ON telegram.messages USING btree (chat_id);
CREATE INDEX idx_messages_user_id_from ON telegram.messages USING btree (user_id_from);
CREATE INDEX idx_messages_user_id_to ON telegram.messages USING btree (user_id_to);
CREATE INDEX idx_messages_create_dttm ON telegram.messages USING btree (create_dttm);

ALTER TABLE telegram.messages OWNER TO ukrcarservice;
GRANT ALL ON TABLE telegram.messages TO ukrcarservice;
GRANT SELECT ON TABLE telegram.messages TO qa_ucs;

-- ----------------------------------------------------------------------------------
