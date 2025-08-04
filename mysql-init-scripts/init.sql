USE vuln_app;

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(100) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    admin BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS products (
    name VARCHAR(100) PRIMARY KEY,
    description TEXT,
    restricted BOOLEAN DEFAULT FALSE
);

INSERT INTO users (username, password, admin) VALUES
('admin', 'admin', true),
('blue', 'bluepassword', false);

INSERT INTO products (name, description, restricted) VALUES
('Apple', ' A golden apple is a food item that bestows beneficial effects when consumed.', false),
('Banana', 'The Banana is arguably the most common item in the Mario Kart series.\nThese slippery items cause a vehicle to spin out.', false),
('Orange', 'Oranges are a crafting material and cooking ingredient in Animal Crossing: New Horizons.', false),
('Pineapple', 'Pineapples in Donkey Kong Circus are among the objects that Donkey Kong has to juggle while balancing on a barrel.', false),
('Grapes', 'Grapes are both a crop and a type forage that can be grown from Grape Starter in Fall.', false),
('Strawberry', 'Strawberries are basic collectibles that can be found in every chapter and they appear in several different types.', false),
('Mango', 'Wumpa fruit are Crash Bandicoot\'s favorite food and a common object in the Crash Bandicoot series.', false),
('Kiwi', 'The PlayStation can produce mind-boggling effects.', true);
