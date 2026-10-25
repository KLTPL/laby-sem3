-- Zadanie 2
-- SHOW TABLES;
-- DESCRIBE <tabel>;

-- Zadanie 3
SELECT title FROM film
WHERE length > 120;

-- Zadanie 4
SELECT
    title,
    length
FROM film
WHERE length IN (50, 55)
ORDER BY length ASC, title ASC;

-- Zadanie 5
SELECT title
FROM (
    SELECT
        title,
        length
    FROM film
    WHERE rating = 'PG-13'
    ORDER BY length ASC, title ASC
    LIMIT 4
) AS shortest
ORDER BY title ASC;

-- Zadanie 6
SELECT DISTINCT length
FROM film
ORDER BY length ASC
LIMIT 4;

-- Zadanie 7

SELECT 'Zadanie 7';

SELECT f.title
FROM film AS f
JOIN film_category AS fc ON f.film_id = fc.film_id
JOIN category AS c ON fc.category_id = c.category_id
WHERE f.description LIKE '%Documentary%' AND c.name = 'Family';

-- Zadanie 8

SELECT 'Zadanie 8';

SELECT f.title
FROM film AS f
JOIN film_category AS fc ON f.film_id = fc.film_id
JOIN category AS c ON fc.category_id = c.category_id
WHERE rating <> 'PG-13' AND c.name = 'Children';

-- Zadanie 9

SELECT 'Zadanie 9';

SELECT DISTINCT f.title
FROM film f
JOIN inventory i ON f.film_id = i.film_id
JOIN rental r ON i.inventory_id = r.inventory_id
WHERE
    r.rental_date >= '2005-05-31'
    AND r.rental_date < '2005-07-01'
ORDER BY f.title DESC;

SELECT 'Zadanie 10';

SELECT DISTINCT
    a.first_name,
    a.last_name
FROM film f
JOIN film_actor fa ON f.film_id = fa.film_id
JOIN actor a ON fa.actor_id = a.actor_id
WHERE f.special_features LIKE '%Deleted Scenes%';

SELECT 'Zadanie 11';

SELECT DISTINCT
    c.customer_id,
    c.first_name,
    c.last_name
FROM customer c
JOIN rental r ON c.customer_id = r.customer_id
JOIN payment p ON r.rental_id = p.rental_id
WHERE r.staff_id <> p.staff_id;

SELECT 'Zadanie 12';

SELECT DISTINCT a.last_name
FROM actor a
JOIN film_actor f_a ON a.actor_id = f_a.actor_id
JOIN film f ON f_a.film_id = f.film_id
WHERE f.title LIKE 'C%';
