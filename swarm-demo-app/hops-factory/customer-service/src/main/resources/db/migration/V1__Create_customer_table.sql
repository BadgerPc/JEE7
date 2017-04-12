CREATE TABLE customer
(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  firstname character varying,
  lastname character varying,
  email character varying,
  password character varying,
  CONSTRAINT pk_id PRIMARY KEY (id)
);