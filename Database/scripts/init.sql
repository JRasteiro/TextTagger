DROP TABLE IF EXISTS wordtagger_usage;
CREATE TABLE wordtagger_usage(id SERIAL PRIMARY KEY, client varchar(50), rawtext TEXT);