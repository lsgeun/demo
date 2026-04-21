-- INSERT INTO expense (amount, date) VALUES (1, '2026-01-01');

INSERT INTO expense (amount, date)

WITH
    RECURSIVE rows(amount, date) AS (SELECT 1, CAST('2026-01-01' AS DATE)
                                     UNION ALL
                                     SELECT amount + 1, DATEADD('DAY', 1, date)
                                     FROM rows
                                     WHERE amount < 100)

SELECT *
FROM rows;
