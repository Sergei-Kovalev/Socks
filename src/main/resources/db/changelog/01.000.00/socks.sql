--liquibase formatted sql
--changeset Siarhei_Kavaleu:db localFilePath:01.000.00/socks.sql
CREATE TABLE socks
(
    id                  UUID                            NOT NULL,
    color               VARCHAR(20)                     NOT NULL,
    cotton_percentage   NUMERIC(3, 2)                   NOT NULL,
    number              INTEGER                         NOT NULL,
    CONSTRAINT pk_socks PRIMARY KEY (id)
);