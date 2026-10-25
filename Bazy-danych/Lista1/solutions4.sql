SELECT 'Zadanie 13';

SELECT
    c.name,
    COUNT(f_c.film_id) AS film_count
FROM category c
JOIN film_category f_c ON c.category_id = f_c.category_id
GROUP BY c.category_id
ORDER BY film_count DESC;

SELECT 'Zadanie 14';

SELECT
    c.customer_id,
    c.first_name,
    c.last_name
FROM customer c
JOIN rental r ON c.customer_id = r.customer_id
JOIN payment p ON r.rental_id = p.rental_id
GROUP BY c.customer_id
HAVING
    AVG(p.amount) < (
        SELECT AVG(p2.amount) FROM payment p2
        WHERE DATE(p2.payment_date) = '2005-07-30'
    );

SELECT 'Zadanie 15';

SELECT
    f_a.actor_id AS actor1_id,
    f_a2.actor_id AS actor2_id
FROM film_actor f_a
JOIN film f ON f_a.film_id = f.film_id
JOIN film_actor f_a2 ON f.film_id = f_a2.film_id
WHERE f_a.actor_id < f_a2.actor_id
GROUP BY f_a.actor_id, f_a2.actor_id
HAVING COUNT(f.film_id) > 1;

SELECT 'Zadanie 16';

SELECT
    a.actor_id,
    a.first_name,
    a.last_name,
    COUNT(c.category_id)
FROM actor a
LEFT JOIN film_actor fa ON a.actor_id = fa.actor_id
LEFT JOIN film f ON fa.film_id = f.film_id
LEFT JOIN film_category fc ON f.film_id = fc.film_id
LEFT JOIN category c ON fc.category_id = c.category_id AND c.name = 'Horror'
GROUP BY
    a.actor_id,
    a.first_name,
    a.last_name;

SELECT 'Zadanie 17';

SELECT
    a.actor_id,
    a.first_name,
    a.last_name
FROM actor a
JOIN film_actor fa ON a.actor_id = fa.actor_id
JOIN film_category fc ON fa.film_id = fc.film_id
JOIN category c ON fc.category_id = c.category_id
GROUP BY a.actor_id, a.first_name, a.last_name
HAVING SUM(c.name = 'Horror') > SUM(c.name = 'Action');
