DROP TABLE IF EXISTS users;

DROP TABLE IF EXISTS films;

DROP TABLE IF EXISTS friends;

DROP TABLE IF EXISTS likes;

DROP TABLE IF EXISTS genres;

DROP TABLE IF EXISTS film_genre;

DROP TABLE IF EXISTS rating;

DROP TABLE IF EXISTS film_rating;

CREATE TABLE IF NOT EXISTS films (
	film_id integer NOT NULL,
	title varchar(64) NOT NULL,
	description varchar(200) NOT NULL,
	duration integer NOT NULL,
	release_date date NOT NULL,
	CONSTRAINT FILM_PK PRIMARY KEY (film_id)
);

CREATE TABLE IF NOT EXISTS users (
	user_id integer NOT NULL,
	email varchar(64) NOT NULL,
	login varchar(64) NOT NULL,
	name varchar(64) NOT NULL,
	birthday_date date NOT NULL,
	CONSTRAINT USER_PK PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS friends (
	user_id integer NOT NULL,
	friend_user_id integer NOT NULL,
	friendship boolean,
	FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (friend_user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
	film_id integer NOT NULL,
	user_id integer,
	FOREIGN KEY (film_id) REFERENCES films(film_id) ON UPDATE CASCADE ON DELETE CASCADE ,
	FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genres (
	genre_id integer NOT NULL,
	name varchar(24) NOT NULL,
	CONSTRAINT GENRE_PK PRIMARY KEY (genre_id)
);

CREATE TABLE IF NOT EXISTS rating (
	rating_id integer NOT NULL,
	name varchar(10) NOT NULL,
	CONSTRAINT RATING_PK PRIMARY KEY (rating_id)
);

CREATE TABLE IF NOT EXISTS film_genre (
	film_id integer NOT NULL,
	genre_id integer,
	FOREIGN KEY (film_id) REFERENCES films(film_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS film_rating (
	film_id integer NOT NULL,
	rating_id integer NOT NULL,
	FOREIGN KEY (film_id) REFERENCES films(film_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (rating_id) REFERENCES rating(rating_id) ON UPDATE NO ACTION ON DELETE NO ACTION
);