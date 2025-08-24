-- Insert fixed categories that users cannot modify
INSERT INTO categories (name, type) VALUES
-- Income
('Salary', 'INCOME'),
('Freelance', 'INCOME'),
('Investment', 'INCOME'),
('Business', 'INCOME'),
('Rental Income', 'INCOME'),
('Gifts Received', 'INCOME'),
('Other Income', 'INCOME'),

-- Expense
('Groceries', 'EXPENSE'),
('Food & Dining', 'EXPENSE'),
('Transportation', 'EXPENSE'),
('Car', 'EXPENSE'),
('Housing', 'EXPENSE'),
('Utilities', 'EXPENSE'),
('Clothing', 'EXPENSE'),
('Toiletry', 'EXPENSE'),
('Entertainment', 'EXPENSE'),
('Health', 'EXPENSE'),
('Insurance', 'EXPENSE'),
('Education', 'EXPENSE'),
('Travel', 'EXPENSE'),
('Fitness & Sports', 'EXPENSE'),
('Personal Care', 'EXPENSE'),
('Gifts Given', 'EXPENSE'),
('Charity', 'EXPENSE'),
('Taxes', 'EXPENSE'),
('Other Expenses', 'EXPENSE');
