CREATE TABLE idm_group_ref (
   id             BIGSERIAL NOT NULL,
   group_id       BIGSERIAL NOT NULL UNIQUE,
   PRIMARY KEY (id));

CREATE TABLE user_ref (
   id             BIGSERIAL NOT NULL,
   user_id       BIGSERIAL NOT NULL UNIQUE,
   PRIMARY KEY (id));

CREATE TABLE role (
   id          BIGSERIAL NOT NULL,
   role_type   varchar(255) NOT NULL UNIQUE,
   PRIMARY KEY (id));

CREATE TABLE idm_group_role (
   role_id        int8 NOT NULL,
   idm_group_ref_id   int8 NOT NULL,
   PRIMARY KEY (role_id, idm_group_ref_id));

CREATE TABLE user_idm_group (
   user_ref_id        int8 NOT NULL,
   idm_group_ref_id   int8 NOT NULL,
   PRIMARY KEY (user_ref_id, idm_group_ref_id));

ALTER TABLE user_idm_group ADD CONSTRAINT FKuser_idm_g172082 FOREIGN KEY (user_ref_id) REFERENCES user_ref (id);
ALTER TABLE user_idm_group ADD CONSTRAINT FKuser_idm_g351385 FOREIGN KEY (idm_group_ref_id) REFERENCES idm_group_ref (id);

ALTER TABLE idm_group_role ADD CONSTRAINT FKidm_group_284474 FOREIGN KEY (role_id) REFERENCES role (id);
ALTER TABLE idm_group_role ADD CONSTRAINT FKidm_group_389301 FOREIGN KEY (idm_group_ref_id) REFERENCES idm_group_ref (id);