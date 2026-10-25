SELECT 'Zadanie 1.2';

-- Jak wyświetlić wszystkie tabele, widoki i struktury.

-- Tables and views (Table_type: BASE TABLE / VIEW)
SHOW FULL TABLES;
-- Structure of a single table / view
-- DESCRIBE <table>;
-- SHOW CREATE TABLE <table>;
-- Structure of all tables and views at once
SELECT
    table_name,
    column_name,
    column_type,
    is_nullable,
    column_key
FROM information_schema.columns
WHERE table_schema = DATABASE()
ORDER BY table_name, ordinal_position;

SELECT 'Zadanie 2.3';

-- Nie używając JOIN wyświetl tytuły filmów i ich długości dla filmów o 4
-- najmniejszych różnych długościach w całej bazie, wyniki posortuj malejąco po
-- długości.

SELECT
    title,
    length
FROM film
WHERE length IN (
    SELECT length
    FROM (
        SELECT DISTINCT length
        FROM film
        WHERE length IS NOT NULL
        ORDER BY length ASC
        LIMIT 4
    ) AS shortest
)
ORDER BY length DESC;

SELECT 'Zadanie 3.4';

-- Wypisz imiona i nazwiska aktorów, którzy nie wystąpili w żadnym filmie,
-- którego tytuł zaczyna się na literę C.

SELECT
    a.first_name,
    a.last_name
FROM actor a
WHERE a.actor_id NOT IN (
    SELECT fa.actor_id
    FROM film_actor fa
    JOIN film f ON fa.film_id = f.film_id
    WHERE f.title LIKE 'C%'
);

SELECT 'Zadanie 3.5';

-- Wypisz bez powtórzeń tytuły filmów z kategorii Action i ich opisy,
-- uwzględnij tylko te filmy, które wypożyczone były pomiędzy 20 a 30 czerwca
-- 2005 oraz w których opisie nie występuje słowo Drama.

SELECT DISTINCT
    f.title,
    f.description
FROM film f
JOIN film_category fc ON f.film_id = fc.film_id
JOIN category c ON fc.category_id = c.category_id
JOIN inventory i ON f.film_id = i.film_id
JOIN rental r ON i.inventory_id = r.inventory_id
WHERE
    c.name = 'Action'
    AND DATE(r.rental_date) BETWEEN '2005-06-20' AND '2005-06-30'
    AND f.description NOT LIKE '%Drama%';

SELECT 'Zadanie 3.6';

-- Wypisz tytuły filmów z kategorii Action, których data wypożyczenia (z
-- dokładnością do dnia) jest inna niż data dokonania płatności.

SELECT DISTINCT f.title
FROM film f
JOIN film_category fc ON f.film_id = fc.film_id
JOIN category c ON fc.category_id = c.category_id
JOIN inventory i ON f.film_id = i.film_id
JOIN rental r ON i.inventory_id = r.inventory_id
JOIN payment p ON r.rental_id = p.rental_id
WHERE
    c.name = 'Action'
    AND DATE(r.rental_date) <> DATE(p.payment_date);

SELECT 'Zadanie 4.7';

-- Policz ilu klientów wypożyczyło więcej filmów niż klient o emailu
-- MARY.SMITH@sakilacustomer.org.

SELECT COUNT(*) AS customer_count
FROM (
    SELECT r.customer_id
    FROM rental r
    GROUP BY r.customer_id
    HAVING
        COUNT(r.rental_id) > (
            SELECT COUNT(r2.rental_id)
            FROM rental r2
            JOIN customer c ON r2.customer_id = c.customer_id
            WHERE c.email = 'MARY.SMITH@sakilacustomer.org'
        )
) AS more_rentals;

SELECT 'Zadanie 4.8';

-- Wypisz bez powtórzeń imiona i nazwiska klientów, których średnia opłata za
-- wypożyczony film jest niższa niż średnia opłata dokonana przez wszystkich 30
-- lipca 2005.

SELECT DISTINCT
    c.first_name,
    c.last_name
FROM customer c
WHERE c.customer_id IN (
    SELECT p.customer_id
    FROM payment p
    GROUP BY p.customer_id
    HAVING
        AVG(p.amount) < (
            SELECT AVG(p2.amount) FROM payment p2
            WHERE DATE(p2.payment_date) = '2005-07-30'
        )
);

SELECT 'Zadanie 4.9';

-- Wypisz wszystkie pary różnych identyfikatorów aktorów, którzy wystąpili
-- razem w więcej niż dwóch filmach. Każda para powinna występować co najwyżej
-- raz. Jeśli występuje para (X, Y), to nie wypisuj pary (Y, X).

SELECT
    fa1.actor_id AS actor1_id,
    fa2.actor_id AS actor2_id
FROM film_actor fa1
JOIN film_actor fa2 ON fa1.film_id = fa2.film_id
WHERE fa1.actor_id < fa2.actor_id
GROUP BY fa1.actor_id, fa2.actor_id
HAVING COUNT(fa1.film_id) > 2;

SELECT 'Zadanie 4.10';

-- Wypisz imiona i nazwiska różnych aktorów, którzy wystąpili razem w więcej
-- niż 2 filmach oraz liczbę filmów, w których zagrali razem. Każda para
-- powinna występować co najwyżej raz.

SELECT
    a1.first_name AS actor1_first_name,
    a1.last_name AS actor1_last_name,
    a2.first_name AS actor2_first_name,
    a2.last_name AS actor2_last_name,
    COUNT(fa1.film_id) AS film_count
FROM film_actor fa1
JOIN film_actor fa2 ON fa1.film_id = fa2.film_id
JOIN actor a1 ON fa1.actor_id = a1.actor_id
JOIN actor a2 ON fa2.actor_id = a2.actor_id
WHERE fa1.actor_id < fa2.actor_id
GROUP BY fa1.actor_id, fa2.actor_id
HAVING COUNT(fa1.film_id) > 2;

SELECT 'Zadanie 4.11';

-- Wypisz nazwiska aktorów, którzy zagrali w większej liczbie horrorów niż
-- filmów akcji.

SELECT a.last_name
FROM actor a
JOIN film_actor fa ON a.actor_id = fa.actor_id
JOIN film_category fc ON fa.film_id = fc.film_id
JOIN category c ON fc.category_id = c.category_id
GROUP BY a.actor_id, a.last_name
HAVING SUM(c.name = 'Horror') > SUM(c.name = 'Action');

SELECT 'Zadanie 5.12';

-- Dodaj do tabeli language język chiński i zmień wszystkie filmy o ustawionej
-- nazwie kategorii Action na chiński.

INSERT INTO language (name) VALUES ('Chinese');

UPDATE film
SET
    language_id = (
        SELECT language_id FROM language
        WHERE name = 'Chinese'
    )
WHERE film_id IN (
    SELECT fc.film_id
    FROM film_category fc
    JOIN category c ON fc.category_id = c.category_id
    WHERE c.name = 'Action'
);

SELECT
    f.title,
    l.name AS language_name
FROM film f
JOIN language l ON f.language_id = l.language_id
JOIN film_category fc ON f.film_id = fc.film_id
JOIN category c ON fc.category_id = c.category_id
WHERE c.name = 'Action';

SELECT 'Zadanie 5.13';

-- Do tabeli film dodaj kolumnę rental_no i uzupełnij ją liczbą wypożyczeń
-- danego filmu.

ALTER TABLE film ADD COLUMN rental_no integer DEFAULT 0;

UPDATE film f
SET rental_no = (
    SELECT COUNT(r.rental_id)
    FROM rental r
    JOIN inventory i ON r.inventory_id = i.inventory_id
    WHERE i.film_id = f.film_id
);

SELECT
    title,
    rental_no
FROM film
ORDER BY rental_no DESC;
