SELECT 'Zadanie 18';

SELECT film.title, language.name 
FROM film 
JOIN language ON film.language_id = language.language_id 
WHERE title = 'YOUNG LANGUAGE';

UPDATE film
SET
    language_id = (
        SELECT language_id FROM language
        WHERE name = 'Italian'
    )
WHERE title = 'YOUNG LANGUAGE';

SELECT film.title, language.name 
FROM film 
JOIN language ON film.language_id = language.language_id 
WHERE title = 'YOUNG LANGUAGE';

SELECT 'Zadanie 19';

INSERT INTO language (name) VALUES ('Spanish');

UPDATE film
SET language_id = (
        SELECT language_id FROM language
        WHERE name = 'Spanish'
)
WHERE film_id IN (
  SELECT fa.film_id
  FROM film_actor fa
  JOIN actor a ON fa.actor_id = a.actor_id
  WHERE a.first_name = 'ED' AND a.last_name = 'CHASE'
);

SELECT f.title, l.name language_name, a.first_name, a.last_name
FROM film f
JOIN language l ON f.language_id = l.language_id
JOIN film_actor fa ON f.film_id = fa.film_id 
JOIN actor a ON fa.actor_id = a.actor_id
WHERE a.first_name = 'ED' AND a.last_name = 'CHASE';

SELECT 'Zadanie 20';

ALTER TABLE language ADD COLUMN films_no integer DEFAULT 0;

SELECT f.language_id, COUNT(f.film_id)
FROM film f
GROUP BY f.language_id;

UPDATE language l
SET films_no = (
  SELECT COUNT(f.film_id)
  FROM film f 
  WHERE l.language_id = f.language_id
);

SELECT 'Zadanie 21';

ALTER TABLE film DROP COLUMN release_year;

DESCRIBE film;
