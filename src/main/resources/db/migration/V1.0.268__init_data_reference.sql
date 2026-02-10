create table if not exists data_reference_temp
(
    id         integer generated always as identity primary key,
    reference  varchar(255),
    data1      varchar(255),
    data2      varchar(255),
    data3      varchar(255),
    data4      varchar(255),
    data5      varchar(255),
    data6      varchar(255),
    data7      varchar(255),
    data8      varchar(255),
    data9      varchar(255),
    data10     varchar(255),
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text,
    changed_on timestamp,
    changed_by text
);
