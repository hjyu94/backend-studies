INSERT INTO users (username, password, enabled) values ('user2', 'pass', true);
INSERT INTO users (username, password, enabled) values ('admin2', 'pass', true);

INSERT INTO authorities (username, authority) values ('user2', 'ROLE_USER');
INSERT INTO authorities (username, authority) values ('admin2', 'ROLE_ADMIN');