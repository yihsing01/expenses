-- V3__Seed_users_and_transactions.sql

-- Seed Users
INSERT INTO users (name, email, password) VALUES
    ('John Doe', 'john@gmail.com', '$2a$12$hNdtwHlD1E4IODpkpQbDUuLO/WHbw53J/77fINoAgml4TGhLTTKfy'),
    ('Jane Smith', 'jane@gmail.com', '$2a$12$9Ee.rPmE3PrNuaaNcFUaRecGDWR00fPygI4f3xvqVOwdu0a8jSr8O');

-- Seed Transactions
INSERT INTO transactions (user_id, category_id, amount, description, transaction_date) VALUES
    (1, 1, 5000.00, 'Salary for August', '2025-08-01'),
    (1, 7, 50.00, 'Grocery shopping', '2025-08-05'),
    (1, 4, 200.00, 'Freelance project', '2025-08-10'),
    (2, 2, 4500.00, 'Salary for August', '2025-08-01'),
    (2, 8, 70.00, 'Dinner out', '2025-08-06'),
    (2, 5, 300.00, 'Car maintenance', '2025-08-08');
