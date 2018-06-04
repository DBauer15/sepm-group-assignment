
-- -----------------------------------------------------
-- Table `RECIPE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RECIPE` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `duration` DOUBLE NOT NULL,
  `description` CLOB NOT NULL,
  `tags` ENUM('B', 'D', 'L', 'BD', 'BL', 'DL', 'BDL') NOT NULL,
  `deleted` BOOL NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`));



-- -----------------------------------------------------
-- Table `RECIPE_IMAGE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RECIPE_IMAGE` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `RECIPE_id` INT NOT NULL,
  `image` BLOB NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_RECIPE_IMAGE_RECIPE1`
    FOREIGN KEY (`RECIPE_id`)
    REFERENCES `RECIPE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



-- -----------------------------------------------------
-- Table `RECIPE_INGREDIENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RECIPE_INGREDIENT` (
  `INGREDIENT_id` INT NOT NULL,
  `RECIPE_id` INT NOT NULL,
  `amount` DOUBLE NOT NULL,
  PRIMARY KEY (`INGREDIENT_id`, `RECIPE_id`),
  CONSTRAINT `fk_RECIPE_INGREDIENT_INGREDIENT1`
    FOREIGN KEY (`INGREDIENT_id`)
    REFERENCES `INGREDIENT` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RECIPE_INGREDIENT_RECIPE1`
    FOREIGN KEY (`RECIPE_id`)
    REFERENCES `RECIPE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



-- -----------------------------------------------------
-- Table `DIET_PLAN`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DIET_PLAN` (
  `id` INT NOT NULL,
  `NAME` VARCHAR(255) NOT NULL,
  `ENERG_KCAL` DOUBLE NOT NULL,
  `LIPID` DOUBLE NOT NULL,
  `PROTEIN` DOUBLE NOT NULL,
  `CARBOHYDRT` DOUBLE NOT NULL,
  `from_dt` TIMESTAMP,
  `to_dt` TIMESTAMP,
  PRIMARY KEY (`id`));



-- -----------------------------------------------------
-- Table `DIET_PLAN_SUGGESTION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DIET_PLAN_SUGGESTION` (
  `recipe` INT NOT NULL,
  `date` DATE NOT NULL,
  `tag` ENUM('B', 'D', 'L') NOT NULL,
  `DIET_PLAN_id` INT NOT NULL,
  `created_timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`recipe`, `created_timestamp`),
  CONSTRAINT `fk_DIET_PLAN_SUGGESTION_RECIPE1`
    FOREIGN KEY (`recipe`)
    REFERENCES `RECIPE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DIET_PLAN_SUGGESTION_DIET_PLAN1`
    FOREIGN KEY (`DIET_PLAN_id`)
    REFERENCES `DIET_PLAN` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- Table `INGREDIENT`
-- -----------------------------------------------------

-- siehe datenbank fuer daten

CREATE TABLE IF NOT EXISTS `INGREDIENT` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(255) NOT NULL,
  `ENERG_KCAL` DOUBLE NOT NULL,
  `LIPID` DOUBLE NOT NULL,
  `PROTEIN` DOUBLE NOT NULL,
  `CARBOHYDRT` DOUBLE NOT NULL,
  `UNIT_GRAM` DOUBLE,
  `UNIT_NUMBER` DOUBLE,
  `UNIT_NAME` VARCHAR(255) NOT NULL,
  `UNIT_GRAM_NORMALISED` DOUBLE NOT NULL,
  `UNIT_ORIG` VARCHAR(255),
  `USER_SPECIFIC` BOOL NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Data for Table `DIET_PLAN`
-- -----------------------------------------------------

-- pre-defined meal plans

-- Build Muscle
INSERT INTO `DIET_PLAN` (`id`, `NAME`, `ENERG_KCAL`, `LIPID`, `PROTEIN`, `CARBOHYDRT`, `from_dt`, `to_dt`)
VALUES (1, 'Build Muscle', 2500, 25.0, 25.0, 50.0, NULL, NULL);

-- Lose Weight
INSERT INTO `DIET_PLAN` (`id`, `NAME`, `ENERG_KCAL`, `LIPID`, `PROTEIN`, `CARBOHYDRT`, `from_dt`, `to_dt`)
VALUES (2, 'Lose Weight', 1900, 30.0, 40.0, 30.0, NULL, NULL);

-- Carefree
INSERT INTO `DIET_PLAN` (`id`, `NAME`, `ENERG_KCAL`, `LIPID`, `PROTEIN`, `CARBOHYDRT`, `from_dt`, `to_dt`)
VALUES (3, 'Carefree', 3000, 30.0, 10.0, 60.0, NULL, NULL);

-- -----------------------------------------------------
-- Data for Table `RECIPE` and `RECIPE_INGREDIENT`
-- -----------------------------------------------------
INSERT INTO RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES (1, 'Fresh salmon with Thai noodle salad', 20, 'Put a pan of water on to boil. Line a steamer with baking parchment, add the salmon fillets and scatter with a little of the orange zest. When the water is boiling, add the beans to the pan, put the salmon in the steamer on top and cook for 5 mins. Take the salmon off, and if it is cooked, set aside but add the peas and mange tout to the pan and cook for 1 min more, or if not quite cooked leave on top for the extra min. Drain the veg, but return the boiling water to the pan, add the noodles and leave to soak for 5 mins.&#10;Put the curry paste and fish sauce in a salad bowl with the orange juice and a little of the remaining zest and the spring onions. Drain the noodles when they are ready and add to the salad bowl, toss well, then add the chopped orange with the basil or coriander and the cooked vegetables. Tip in the juice from the fish, then toss well and serve in bowls with the salmon on top.', 'DL', false);
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (4355, 1, 1); /* Salmon */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2274, 1, 0.5); /* Orange */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (4549, 1, 0.5); /* Beans */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2996, 1, 0.2); /* Peas */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (7805, 1, 0.25); /* Noodles */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (231, 1, 1); /* Curry Paste */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (1213, 1, 0.5); /* Fish Sauce */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2984, 1, 1); /* Spring Onion */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (219, 1, 1); /* Basil */

INSERT INTO RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES (2, 'Egg & rocket pizzas', 25, 'Heat oven to 200C/180C fan/gas 6. Lay the tortillas on two baking sheets, brush sparingly with the oil then bake for 3 mins. Meanwhile chop the pepper and tomatoes and mix with the tomato purée, seasoning and herbs. Turn the tortillas over and spread with the tomato mixture, leaving the centre free from any large pieces of pepper or tomato.&#10;Break an egg into the centre then return to the oven for 10 mins or until the egg is just set and the tortilla is crispy round the edges. Serve scattered with the rocket and onion.', 'DL', false);
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (5705, 2, 2); /* Wrap */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (1517, 2, 0.25); /* Red Pepper */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3158, 2, 1); /* Tomato */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (260, 2, 0.1); /* Dill */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3209, 2, 1); /* Parsley */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (111, 2, 0.25); /* Egg */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2986, 2, 0.5); /* Onion */

INSERT INTO RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES (3, 'Green club sandwich', 10, 'Toast the bread and spread hummus evenly over one side of each slice. On one slice of bread, lay half the avocado, rocket and tomato. Season with pepper, then cover with another slice.&#10;Pile on the rest of the avocado, rocket and tomato, season again and top with the third slice.', 'BDL', false);
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (5280, 3, 5); /* Toast */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (4673, 3, 0.25); /* Hummus */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2152, 3, 1); /* Avocado */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2874, 3, 10); /* Coleslaw */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3157, 3, 1); /* Cherry Tomatoes */

INSERT INTO RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES (4, 'Steak, roasted pepper & pearl barley salad', 40, 'Put the pearl barley in a large pan of water. Bring to the boil and cook vigorously for 25-30 mins or until tender. Drain thoroughly and transfer to a bowl.&#10;Meanwhile, heat oven to 200C/ 180C fan/gas 6. Put the peppers on a baking tray with the onion wedges, toss in 1 tbsp olive oil and roast for about 20 mins until tender.&#10;While the peppers are roasting, rub the steak with a little bit of oil and season. Cook in a non-stick frying pan for 3-4 mins each side, or to your liking. Set aside to rest for a few mins. Mix the cooked peppers and onions into the barley. Stir though the watercress, lemon juice and some seasoning. Thinly slice the steaks, place on top of the salad and serve with lemon wedges, if you like.', 'DL', false);
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (6145, 4, 0.5); /* Barley */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (1517, 4, 0.5); /* Red Pepper */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3468, 4, 1); /* Yellow Pepper */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2986, 4, 0.5); /* Onion */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (604, 4, 1); /* Olive Oil */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2739, 4, 1); /* Steak */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3190, 4, 1); /* Watercress */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2248, 4, 0.1); /* Lemon Juice */

INSERT INTO RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES (5, 'Crunchy bulgur salad', 25, 'Cook the bulgur following pack instructions, then drain and tip into a large serving bowl to cool. Meanwhile, put the edamame beans in a small bowl, pour over boiling water, leave for 1 min, then drain. Put in a serving bowl with the peppers, radishes, almonds, mint and parsley.&#10;Peel one orange, carefully cut away the segments and add to the bowl. Squeeze the juice of the other into a jam jar with the oil. Season well and shake to emulsify. Pour over the salad, toss well and serve.', 'DL', false);
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (6150, 5, 0.5); /* Bulgur */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2913, 5, 0.25); /* Edamame */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (1517, 5, 1); /* Red Pepper */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3083, 5, 0.25); /* Radish */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3536, 5, 0.25); /* Almonds */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (273, 5, 1); /* Mint */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3209, 5, 1); /* Parsley */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2274, 5, 1); /* Orange */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (604, 5, 3); /* Olive Oil */

INSERT INTO RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES (6, 'Ginger & marmalade roulade', 40, 'Heat the oven to 190C/170C Fan/gas 5 and line a 23 x 33cm Swiss roll tin with baking parchment. Whisk the aquafaba for 5 mins, or until it reaches soft peaks, then add the caster sugar and vanilla. Mix the self-raising flour with the ground ginger, then use a metal spoon to fold the flour into the aquafaba mix. Pour into the lined tin and smooth with a spatula so it’s evenly spread. Bake for 10-12 mins until just firm to the touch.&#10;While the sponge is baking, lay a piece of baking paper onto a work surface and dust with caster sugar. Once the sponge has baked, turn it out on the sugared paper and peel off the baking paper on the bottom. Allow to cool a little.&#10;Whisk the coconut cream with the icing sugar until thickened. Once the cake is cool enough to touch, but not completely cooled, spread it with the marmalade leaving a 2cm border, then scatter with the stem ginger. Spread the cream on top, keeping the border, then use the sheet of baking paper to help you to roll up the sponge, starting from the longest edge. Roll it up as tightly as you can, keeping the filling inside. Roll off the baking paper, then place on a serving dish, dust with icing sugar and serve.', 'BD', false);
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (4181, 6, 1); /* Water */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (5993, 6, 0.1); /* Sugar */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (266, 6, 1); /* Vanilla */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (6213, 6, 0.1); /* Flour */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2916, 6, 0.25); /* Ginger */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3563, 6, 0.25); /* Coconut Cream */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (5965, 6, 0.25); /* Marmalade */

INSERT INTO RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES (7, 'Minted melon, tomato & prosciutto salad', 10, 'To make the dressing, whisk all the ingredients together in a bowl and set aside.&#10;Toss the tomatoes and melon together in a bowl with a little dressing, some sea salt and black pepper. Loosely lay the prosciutto over a platter and pile the tomatoes and melon on top. Drizzle with extra dressing, scatter with mint and serve straight away with crusty bread.', 'BDL', false);
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3157, 7, 1); /* Cherry Tomatoes */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2266, 7, 1); /* Melon */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (1581, 7, 1); /* Ham */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (273, 7, 1); /* Mint */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (5705, 7, 2); /* Bread */

INSERT INTO RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES (8, 'Pasta with lemon & chive pesto', 15, 'Place the garlic, herbs, pine nuts, cheese and lemon zest in a small bowl, season well, then stir in the olive oil and lemon juice. Set aside.&#10;Cook the pasta in a pan of salted boiling water following pack instructions, then drain well. Tip into a serving bowl and toss through the pesto. Serve with extra grated Parmesan, if you like.', 'DL', false);
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2915, 8, 0.1); /* Garlic */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3209, 8, 1); /* Parsley */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2871, 8, 1); /* Chives */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3584, 8, 0.2); /* Pine Nuts */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (33, 8, 1); /* Parmesan */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2248, 8, 0.1); /* Lemon Juice */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (604, 8, 3); /* Olive Oil */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (6237, 8, 6); /* Noodles */

INSERT INTO RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES (9, 'Prawn pasta', 25, 'Cook the pasta in a large pan of boiling, salted water according to pack instructions. Meanwhile, heat a small knob of the butter in a frying pan. When it starts to sizzle, add the prawns and fry for 1 min until they start to change colour. Add the garlic and sizzle for 1 min more, splash in the wine, then bring to the boil. Swirl in the rest of the butter, season with salt and pepper and a squeeze of lemon juice, then stir in the chopped parsley.&#10;When the pasta is just cooked, drain and toss through the prawns. Divide the pasta between 2 bowls, pour over any excess sauce and serve straightaway.', 'DL', false);
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (6251, 9, 4); /* Spaghetti */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (131, 9, 0.1); /* Butter */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (4423, 9, 6); /* Shrimp */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2915, 9, 0.1); /* Garlic */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (4046, 9, 3); /* Wine */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (3209, 9, 1); /* Parsley */

INSERT INTO RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES (10, 'Passion fruit cream', 35, 'Whisk the yolk and sugar until light and fluffy and doubled in size, about 3 mins. Beat in the Cointreau, lemon and the flesh of 2 passion fruits. Softly whip the cream until it just holds some shape (it stiffens quickly towards the end so whip slowly as it gets thicker). Chill 2 dessert glasses.&#10;To assemble, gently fold the egg mix into the double cream, having first given the eggs another brisk whisk. Spoon the cream into little glasses and top them with the seeds from the remaining passion fruit and a drizzle of Cointreau.', 'BDL', false);
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (113, 10, 0.25); /* Egg */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (5993, 10, 0.1); /* Sugar */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2248, 10, 0.1); /* Lemon Juice */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (2300, 10, 1.5); /* Passion Fruit */
INSERT INTO RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES (52, 10, 1); /* Cream */

INSERT INTO RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES(11,'Poached eggs with broccoli, tomatoes & wholemeal flatbread', 11,'Boil the kettle. Heat oven to 120C/100C fan/gas 1/2 and put an ovenproof plate inside to warm up. Fill a wide-based saucepan one-third full of water from the kettle and bring to the boil. Add the broccoli and cook for 2 mins. Add the tomatoes, return to the boil and cook for 30 secs. Lift out with tongs or a slotted spoon and place on the warm plate in the oven while you poach the eggs.&#10;Return the water to a gentle simmer. Break the eggs into the pan, one at a time, and cook for 2 1/2 - 3 mins or until the whites are set and the yolks are runny.&#10;Divide the flatbreads between the two plates and top with the broccoli and tomatoes. Use a slotted spoon to drain the eggs, then place on top. Sprinkle with the seeds and drizzle with the oil. Season with a little black pepper and the chilli flakes, and serve immediately.','B','false');
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2821,11, 0.5); /* Broccoli */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(3158,11, 0.5); /* Tomatoes */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(111,11, 0.5); /* Eggs */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(5258,11, 4); /* Bread */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(3519,11, 0.25); /* Seeds */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(631,11, 0.5); /* Oil */

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES(12,'Homemade muesli with oats, dates & berries', 7,'Tip the oats into a frying pan and heat gently, stirring frequently until they are just starting to toast. Add the pecans and seeds to warm briefly, then tip into a large bowl and toss so they cool quickly.&#10;Add the dates and puffed wheat, mix well until thoroughly combined, then serve topped with the yogurt and fruit, and a sprinkling of cinnamon, if you like.','B','false');
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6174,12, 0.2); /* Oats */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(3580,12, 0.125); /* Pecan */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(3527,12, 0.125); /* Sunflower seeds */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2408,12, 1); /* Medjool dates */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6146,12, 0.3); /* Wheat */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(7880,12, 0.5); /* Yogurt */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2186,12, 0.5); /* Cranberries */

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES(13,'Pink barley porridge with vanilla yogurt',25,'Tip the barley and oats into a bowl, pour over 1 litre boiling water and stir well. Cover and leave to soak overnight.&#10;The next morning, tip the mixture into a pan and stir in the plums. Simmer for 15 mins, stirring frequently and adding a little water if necessary to get a consistency you like.&#10;Stir the vanilla into the yogurt and serve on top of the porridge with the seeds sprinkled over.','B','false');
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6145, 13, 0.5); /* Barley */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6174, 13, 0.5); /* Oats */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2343, 13, 1); /* Plum */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(265, 13, 0.00625); /* Vanilla */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(173, 13, 1); /* Yogurt */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(3527,13, 0.125); /* Sunflower seeds */

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES(14,'Orange & raspberry granola', 40,'Put 200g oats and 500ml water in a food processor and blitz for 1 min. Line a sieve with clean muslin and pour in the oat mixture. Leave to drip through for 5 mins, then twist the ends of the muslin and squeeze well to capture as much of the oat milk as possible – it should be the consistency of single cream. Best chilled at least 1 hr before serving. Can be kept in a sealed or covered jug in the fridge for up to 3 days.&#10;Heat oven to 200C/180C fan/gas 6 and line a baking tray with baking parchment. Put the orange juice in a medium saucepan and bring to the boil. Boil rapidly for 5 mins or until the liquid has reduced by half, stirring occasionally. Mix the remaining 200g oats with the orange zest and cinnamon. Remove the pan from the heat and stir the oat mixture into the juice. Spread over the lined tray in a thin layer and bake for 10-15 mins or until lightly browned and crisp, turning the oats every few mins. Leave to cool on the tray.&#10;Once cool, mix the oats with the raspberries, flaked almonds and seeds. Can be kept in a sealed jar for up to one week. To serve, spoon the granola into bowls, pour over the oat milk and top with the orange segments and mint leaves, if you like.','B','false' );
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6174, 14, 1); /* Oats */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2277, 14, 0.5); /* Oranges */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(226, 14, 0.5); /* Cinnamon */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2364, 14, 0.5); /* Raspberries*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(3536, 14, 0.25); /* Almonds*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(3527,14, 0.125); /* Sunflower seeds */

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES(15,'Avocado & strawberry smoothie', 5, 'Put all the ingredients in a blender and whizz until smooth. If the consistency is too thick, add a little water.','B','false');
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2154,15, 0.5); /* Avocado */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2374,15, 1); /* Strawberry */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(7880,15, 1); /* Yogurt */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(70,15, 1); /* Milk */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2248,15, 0.1); /* Lemon Juice */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(5960,15, 0.1); /* Honey*/

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES(16,'Easy chocolate cupcakes',45, 'Heat oven to 180C/fan 160C/gas 4 and line a 10-hole muffin tin with paper cases. Whizz the chocolate into small pieces in a food processor. In the largest mixing bowl you have, tip in the flour, sugar, cocoa, oil, 100ml soured cream, eggs, vanilla and 100ml water. Whisk everything together with electric beaters until smooth, then quickly stir in 100g of the whizzed-up chocolate bits. Divide between the 10 cases, then bake for 20 mins until a skewer inserted comes out clean (make sure you don’t poke it into a chocolate chip bit). Cool on a wire rack.&#10;To make the icing, put the remaining chocolate bits, soured cream and 3 tbsp sugar in a small saucepan. Heat gently, stirring, until the chocolate is melted and you have a smooth icing. Chill in the fridge until firm enough to swirl on top of the muffins, then tuck in.','BD','false');
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6130,16, 2.5); /* Dark Chocolate*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6211,16, 1); /* Flour*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6135,16, 7); /* Sugar*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(5874,16, 0.5); /* Cocoa*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(706,16, 3); /* Sunflower oil*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(152,16, 6); /* Sour cream*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(111,16, 0.5); /* Eggs */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(265, 16, 0.00625); /* Vanilla */

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES(17,'Double chocolate loaf cake', 80,'Heat oven to 160C/140C fan/gas 3. Grease and line a 2lb/900g loaf tin with a long strip of baking parchment. To make the loaf cake batter, beat the butter and sugar with an electric whisk until light and fluffy. Beat in the eggs, flour, almonds, baking powder, milk and cocoa until smooth. Stir in the chocolate chips, then scrape into the tin. Bake for 45-50 mins until golden, risen and a skewer poked in the centre comes out clean.&#10;Cool in the tin, then lift out onto a wire rack over some kitchen paper. Melt the extra chocolate chunks separately in pans over barely simmering water, or in bowls in the microwave, then use a spoon to drizzle each in turn over the cake. Leave to set before slicing.','BD','false');
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(131, 17, 0.25); /* Butter */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6135,17, 5); /* Sugar*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(111,17, 0.5); /* Eggs */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6211,17, 0.75); /* Flour*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(3536, 17, 0.25); /* Almonds*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(5539, 17, 0.5); /* Baking Powder*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(70,17, 0.75); /* Milk */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(5874,17, 0.125); /* Cocoa*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6130,17, 0.75); /* Chocolate*/

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES(18,'The classic pancake',30,'Sift the flour with a pinch of salt into a medium-size bowl and make a well in the middle. Mix the milk and 100ml of water together. Break the eggs into the well and start whisking slowly. Add the milk and water in a steady stream, whisking constantly and gradually incorporating the flour as you do so.&#10;Whisk until the batter is smooth and all the flour has been incorporated. Set the batter aside to rest for 30 mins, then whisk the melted butter into the batter.&#10;Heat the pan over a medium heat. Very lightly grease the pan with melted butter. Using a ladle, pour roughly 2 tbsp of batter into the pan and swirl it around so the bottom of the pan is evenly coated. You want to use just enough batter to make a delicate, lacy pancake. Cook the pancake for about 45 secs on one side until golden and then using a palette knife or fish slice, flip the pancake over and cook the other side for about 30 secs until it freckles.&#10;Slide the pancake out of the pan and either serve immediately or stack on a plate with baking parchment in between. Continue until all the batter is used up.','B','false');
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6211,18, 0.5); /* Flour*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(70,18, 1); /* Milk */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(111,18, 0.5); /* Eggs */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(131, 18, 0.1); /* Butter */

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES(19,'Feta & peach couscous', 20,'De-stone and quarter the peaches. Put in a roasting tin with the seeds, chunks of feta and drizzle over 3 tbsp olive oil. Bake for 12-15 mins at 200C/180C fan/gas 6. Cook the couscous following pack instructions. Toss the couscous with the roasted peach mixture and season to taste before serving.','L','false');
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2301, 19, 2); /* Peaches */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(3527,19, 0.125); /* Sunflower seeds */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6164,19, 0.75); /* Couscous */

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES(20,'Turkey burgers', 45,'Heat 1 tbsp oil in a pan and gently fry the onion for 5 mins until soft. Add the garlic and cook for 1 min. Add the oats and fry for 2 mins more. Tip into a bowl and set aside to cool.&#10;Add the rest of the ingredients to the cooled mixture and mix well with your hands. Season to taste and shape into 8 patties.&#10;Heat oven to 200C/fan 180C/gas 6. Heat the remaining olive oil in a large, non-stick frying pan and sear the burgers on each side until well coloured (3-4 mins). Transfer to a baking sheet and cook in the oven for 10-15 mins. Serve in rolls with Tangy tomato chutney and cucumber slices.&#10;For the chutney heat 1 tbsp of olive oil in a pan and add 1 finely chopped onion. Cook for 5 mins until softened. Stir in 1 crushed garlic clove and cook for a further min. Add 1 tbsp sundried tomato paste, a 400g can good-quality chopped tomatoes and a pinch of sugar. Gently cook for 20-25 mins until rich and thick. Season to taste, then leave to cool before serving.','DL','false');
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(604,20, 1); /* Olive oil */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2975,20, 0.5); /* Onions */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2915,20, 0.125); /* Garlic */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6174,20, 0.25); /* Oats */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2136,20, 0.5); /* Apricots */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2848,20, 0.25); /* Carrots */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(111,20, 0.25); /* Eggs */

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(21, 'Salmon with beetroot, feta & lime salsa', 10,'Chop the beetroot and feta into small cubes and mix with the juice and zest of one lime and some seasoning.\n\nSeason the salmon. Heat 2 tbsp of oil in a nonstick frying pan over a high heat. When hot add the salmon, skin-side down, and cook for 3 mins. Flip over, turn the heat down and cook for a further 4-5 mins. Serve with the beetroot salsa and the remaining lime, cut into wedges.', 2, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2814, 21, 0.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(19, 21, 0.25);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2252, 21, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(7628, 21, 1.0);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(22, 'Basic omelette recipe', 5,'Season the beaten eggs well with salt and pepper. Heat the oil and butter in a non-stick frying pan over a medium-low heat until the butter has melted and is foaming. \n\nPour the eggs into the pan, tilt the pan ever so slightly from one side to another to allow the eggs to swirl and cover the surface of the pan completely. Let the mixture cook for about 20 seconds then scrape a line through the middle with a spatula. \n\nTilt the pan again to allow it to fill back up with the runny egg. Repeat once or twice more until the egg has just set. \n\nAt this point you can fill the omelette with whatever you like \u2013 some grated cheese, sliced ham, fresh herbs, saut\u00e9ed mushrooms or smoked salmon all work well. Scatter the filling over the top of the omelette and fold gently in half with the spatula. Slide onto a plate to serve.', 6, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(111, 22, 0.75);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(624, 22, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1, 22, 0.15);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(23, 'Nutty chicken satay strips', 20, 'Heat oven to 200C, 180C fan, gas 4 and line a baking tray with non-stick paper.\n\nMix the peanut butter with the garlic, curry powder, soy sauce and lime in a bowl. Some nut butters are thicker than others, so if necessary, add a dash of boiling water to get a coating consistency. Add the chicken strips, mix well then arrange on the baking sheet, spaced apart, and bake in the oven for 8-10 mins until cooked, but still juicy.\n\nEat warm with the cucumber sticks and chilli sauce, or leave to cool then keep in the fridge for up to 2 days.', 5, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4669, 23, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2915, 23, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4649, 23, 0.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2253, 23, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1002, 23, 2.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2906, 23, 0.25);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1196, 23, 1.0);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(24, 'Cottage cheese fritters', 20, 'In a bowl, combine all ingredients except the flour and oil. Mix well, then stir in the flour.\n\nHeat the oil in a large frying pan over a medium heat. Working in batches of two or three (the mixture should make six fritters), spoon large blobs into the pan and flatten with the back of the spoon to make thick discs. Turn when crisp and golden on the bottom. When both sides are cooked, remove from the pan and keep warm in a low oven while you fry the next batch.\n\nServe hot, sprinkled with parmesan and herbs.', 4, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(15, 24, 1.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3157, 24, 0.25);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2247, 24, 0.3);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(33, 24, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3102, 24, 0.25);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(604, 24, 1.0);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(25, 'Teriyaki salmon with sesame pak choi', 20,'Heat oven to 200C/180C fan/gas 6 and put the salmon in a shallow baking dish. Mix the sweet chilli, honey, sesame oil, mirin, soy and ginger in a small bowl and pour over the salmon so the steaks are completely covered. Bake for 10 mins while you cook the pak choi.\n\nCut a slice across the base of the pak choi so the leaves separate. Heat the oils in a wok, add the garlic and stir-fry briefly to soften. Add the pak choi and fry until the leaves start to wilt. Pour over the stock, tightly cover the pan and allow to cook for 5 mins \u2013 you are aiming for the stems of the pak choi to be tender but still have a bit of bite.\n\nServe the pak choi in shallow bowls, top with the salmon steaks and spoon over the juices. Scatter with the toasted sesame seeds and serve on its own or with brown rice or noodles.', 5, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(7628, 25, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(5960, 25, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(606, 25, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4649, 25, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2916, 25, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2837, 25, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(739, 25, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2915, 25, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3525, 25, 0.5);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(26, 'Soba noodles with wasabi garlic prawns', 15, 'Mix the wasabi, soy sauce and garlic in a small bowl. Bring a large pan of water to the boil and cook the noodles following pack instructions.\n\nMeanwhile, heat the butter in a frying pan. Once foaming, stir in the prawns and cook for a few mins until pink. Stir in the wasabi mixture with a couple of spoonfuls of the noodle cooking water and heat through. Add the noodles to the pan with the spring onions, toss together and divide between two plates.', 5, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3504, 26, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4649, 26, 1.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2915, 26, 0.2);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6243, 26, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1, 26, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4425, 26, 4.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2975, 26, 1.0);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(27, 'Jerk prawn & coconut rice bowls', 8, 'Heat 1 tbsp flavourless oil in a large frying pan. Add the prawns and the jerk seasoning, and cook for 1-2 mins. Drain the beans, reserving 3 tbsp of the chilli sauce.\n\nAdd the beans to the pan along with the reserved sauce and the coconut rice. Fry for 3-4 mins, then season with salt to taste and spoon into two bowls to serve.', 5, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(250, 27, 1.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4559, 27, 2.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6173, 27, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4425, 27, 2.0);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(28, 'Rice noodles with sundried tomatoes, Parmesan & basil', 15, 'Prepare the noodles according to pack instructions, then drain. Heat the oil, then fry the tomatoes and garlic for 3 mins. Toss the noodles and most of the cheese and basil into the pan, season, then scatter over the remaining cheese and basil.', 2, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6257, 28, 2.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3472, 28, 0.25);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2915, 28, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(33, 28, 0.3);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(259, 28, 1.0);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(29, 'Lemon chicken with spring veg noodles', 20, 'Heat the oil in a non-stick pan, then fry the chicken for 5 mins until almost cooked. Tip onto a plate. Pour 250ml water into the pan with the lemon zest and juice, sugar and ginger. In a bowl, mix the cornflour with a little water until smooth, then whisk into the pan. Bring to the boil, stirring, then add the chicken to the sauce. Reduce the heat. Bubble for a few mins until chicken is cooked and the sauce thickened.\n\nMeanwhile, cook the noodles and veg together in boiling water for 4 mins, then drain. Toss together the chicken, noodles, veg and spring onions; serve scattered with nuts.', 1, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(624, 29, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1002, 29, 2.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2248, 29, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6135, 29, 0.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2916, 29, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6211, 29, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6237, 29, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2805, 29, 0.25);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2975, 29, 0.25);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3546, 29, 1.0);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(30, 'Lamb cutlets with lentil & feta salad', 20, 'Cook peas in boiling water for 3-4 mins until just tender, then drain. Mix with the lentils, vinegar, sugar and mint, then crumble in the feta and season well.\n\nHeat a griddle pan, brush the cutlets with a little oil and season. Cook in the hot pan for 4 mins on each side until browned and the middle is pink. Divide the salad between four bowls, then top with a couple of cutlets per person.', 1, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2996, 30, 0.75);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4595, 30, 0.75);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(275, 30, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6135, 30, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(19, 30, 0.75);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(604, 30, 0.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(5103, 30, 4.0);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (31, 'Spinach & watercress soup', 10, 'Put the spinach, watercress, spring onion, vegetable stock, avocado, cooked rice, lemon juice and mixed seeds in a blender with seasoning. Whizz until smooth. Heat until piping hot. Scatter over some toasted seeds if you want added crunch.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3102, 31, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3190, 31, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2984, 31, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 31, 0.4);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2152, 31, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6178, 31, 0.6);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2248, 31, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3530, 31, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3592, 31, 0.125);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (32, 'Celery soup', 55, 'Peel and cut the potatoes into chunks.\n\nHeat the oil in a large saucepan over a medium heat, tip in the celery, garlic and potatoes and coat in the oil. Add a splash of water and a big pinch of salt and cook, stirring regularly for 15 mins, adding a little more water if the veg begins to stick.\n\nPour in the vegetable stock and bring to the boil, then turn the heat down and simmer for 20 mins further, until the potatoes are falling apart and the celery is soft. Use a stick blender to purée the soup, then pour in the milk and blitz again. Season to taste. Serve with crusty bread.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 32, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2861, 32, 0.75);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 32, 0.005);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3023, 32, 0.125);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 32, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (180, 32, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5260, 32, 0.25);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (33, 'Cucumber, pea & lettuce soup', 20, 'Boil 1.4 litres water in a kettle. Heat the oil in a large non-stick frying pan and cook the spring onions for 5 mins, stirring frequently, or until softened. Add the cucumber, lettuce and peas, then pour in the boiled water. Stir in the bouillon, cover and simmer for 10 mins or until the vegetables are soft but still bright green.\n\nBlitz the mixture with a hand blender until smooth. Serve hot or cold, topped with yogurt (if you like), with rye bread alongside.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (663, 33, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2984, 33, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2907, 33, 0.375);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2945, 33, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3002, 33, 0.425);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 33, 0.02);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (104, 33, 0.04);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5280, 33, 0.25);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (34, 'Carrot & ginger soup', 10, 'Peel and chop the carrots and put in a blender with the grated ginger, turmeric, cayenne pepper, wholemeal bread, soured cream and vegetable stock. Blitz until smooth. Heat until piping hot. Swirl through some extra soured cream, or a sprinkling of cayenne, if you like.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2848, 34, 1.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2916, 34, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (258, 34, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (247, 34, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5259, 34, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (55, 34, 0.04);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 34, 0.8);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (35, 'Roasted sweet potato & carrot soup', 50, 'Heat oven to 220C/200C fan/ gas 7 and put the sweet potatoes and carrots into a large roasting tin, drizzled with half a tbsp olive oil and plenty of seasoning. Roast the veg in the oven for 25-30 mins or until caramelised and tender.\n\nMeanwhile, put the remaining 1 tbsp olive oil in a large deep saucepan and fry the onion over a medium-low heat for about 10 mins until softened. Add the garlic and stir for 1 min before adding the stock. Simmer for 5-10 mins until the onions are very soft, then set aside.\n\nOnce the roasted veg is done, leave to cool a little, then transfer to the saucepan and use a hand blender to process until smooth. Stir in the crème fraîche, a little more seasoning and reheat until hot. Serve in bowls topped with a swirl of crème fraîche and a good grinding of black pepper.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3141, 35, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2848, 35, 0.15);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 35, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 35, 0.0625);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 35, 0.0025);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 35, 0.875);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (55, 35, 0.0875);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (36, 'Broccoli & Stilton soup', 45, 'Heat the rapeseed oil in a large saucepan and then add the onions. Cook on a medium heat until soft. Add a splash of water if the onions start to catch.\n\nAdd the celery, leek, potato and a knob of butter. Stir until melted, then cover with a lid. Allow to sweat for 5 minutes. Remove the lid.\n\nPour in the stock and add any chunky bits of broccoli stalk. Cook for 10 – 15 minutes until all the vegetables are soft.\n\nAdd the rest of the broccoli and cook for a further 5 minutes. Carefully transfer to a blender and blitz until smooth. Stir in the stilton, allowing a few lumps to remain. Season with black pepper and serve.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (663, 36, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 36, 0.125);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2861, 36, 0.32);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2942, 36, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3023, 36, 0.06);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (131, 36, 0.04);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 36, 0.875);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2821, 36, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4, 36, 1.2);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (37, 'Creamy tomato soup', 75, 'Put the oil, onions, celery, carrots, potatoes and bay leaves in a big casserole dish, or two saucepans. Fry gently until the onions are softened – about 10-15 mins. Fill the kettle and boil it.\n\nStir in the tomato purée, sugar, vinegar, chopped tomatoes and passata, then crumble in the stock cubes. Add 1 litre boiling water and bring to a simmer. Cover and simmer for 15 mins until the potato is tender, then remove the bay leaves. Purée with a stick blender (or ladle into a blender in batches) until very smooth. Season to taste and add a pinch more sugar if it needs it. The soup can now be cooled and chilled for up to 2 days, or frozen for up to 3 months.\n\nTo serve, reheat the soup, stirring in the milk – try not to let it boil. Serve in small bowls for the children with cheesy sausage rolls then later in bowls for the adults as Hot Bloody Mary soup.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 37, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 37, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2861, 37, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2848, 37, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3023, 37, 0.135);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (220, 37, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3164, 37, 0.03);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5992, 37, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (275, 37, 0.0125);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3238, 37, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7827, 37, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1164, 37, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (180, 37, 0.15);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (38, 'Roasted onion soup with goat''s cheese toasts', 85, 'Heat oven to 200C/180C fan/gas 6. Put the onions in a roasting tin with the oil, salt and pepper. Give it a good stir, then roast for 45 mins, stirring halfway through, until the onions are tinged brown, but not burnt.\n\nTip the onions into a large pan with the stock, mustard and Marmite. Bring to the boil and simmer for 15 mins, then stir in the parsley. Toast 4 of the bread slices then scatter on the cheese. Ladle the soup into bowls, pop a toast into each and serve with the extra slices of bread on the side.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 38, 1.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 38, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 38, 0.875);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (261, 38, 0.75);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7871, 38, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2989, 38, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (140, 38, 0.875);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5259, 38, 3);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (39, 'Chicken noodle soup', 40, 'Pour the stock into a pan and add the chicken, ginger and garlic. Bring to the boil, then reduce the heat, partly cover and simmer for 20 mins, until the chicken is tender. Remove the chicken to a board and shred into bite-size pieces using a couple of forks.\n\nReturn the chicken to the stock with the noodles, corn, mushrooms, half the spring onions and the soy sauce. Simmer for 3-4 mins until the noodles are tender. Ladle into two bowls and scatter over the remaining spring onions, herbs and chilli shreds if using. Serve with extra soy sauce for sprinkling.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 39, 1.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (828, 39, 1.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2916, 39, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 39, 0.01);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6257, 39, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2885, 39, 0.07);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2954, 39, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4649, 39, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (259, 39, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (225, 39, 0.5);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (40, 'Pumpkin soup', 45, 'Heat the olive oil in a large saucepan, then gently cook the onions for 5 mins, until soft but not coloured.\n\nAdd the pumpkin or squash to the pan, then carry on cooking for 8-10 mins, stirring occasionally until it starts to soften and turn golden.\n\nPour the stock into the pan and season with salt and pepper. Bring to the boil, then simmer for 10 mins until the squash is very soft.\n\nPour the double cream into the pan, bring back to the boil, then purée with a hand blender. For an extra-velvety consistency you can pour the soup through a fine sieve. The soup can now be frozen for up to 2 months.\n\nTo make the croutons: cut the bread into small squares. Heat the olive oil in a frying pan, then fry the bread until it starts to become crisp. Add a handful of pumpkin seeds to the pan, then cook for a few mins more until they are toasted. These can be made a day ahead and stored in an airtight container.\n\nReheat the soup if needed, taste for seasoning, then serve scattered with croutons and seeds and drizzled with more olive oil, if you want.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 40, 0.7);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 40, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3217, 40, 1.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 40, 0.4);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (52, 40, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5259, 40, 1.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3517, 40, 0.07);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (41, 'Roast salmon', 35, 'Heat oven to 160C/140C fan/gas 4. To make the crumble, mix the crumbs, onion flakes, herbs, horseradish, 1 tsp salt and melted butter together, then set aside. Line a baking tray with baking parchment, scatter over the sliced onions and drizzle with olive oil. Sprinkle with a little salt and lay the salmon on top, skin-side up. Loosely pack over the crumble mix. Roast in the oven for 20 mins, or until just cooked through.&#10;While the fish is cooking, blanch the kale in boiling water for 2 mins, drain and refresh under cold water. Put the kale, garlic, lemon zest, and oil in a food processor and blend to a coarse salsa. Season to taste.&#10;Once the fish is cooked, remove from the oven and leave to rest for 5 mins. Pull apart into large chunks and serve with onions, salsa and sour cream.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7628, 41, 2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2930, 41, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 41, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 41, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2248, 41, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 41, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (55, 41, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5279, 41, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2977, 41, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2989, 41, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (260, 41, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (270, 41, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (131, 41, 0.1);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (42, 'Scrambled eggs & smoked salmon', 12, 'Beat together the eggs, milk and some seasoning in a jug. Heat the oil in a large non-stick pan over a low heat and pour in the egg mix. Cook, stirring, until it is done around the edges and slightly runny in the middle. Then tip in the tomatoes and warm through for 1 min until the eggs have finished cooking.&#10;Top the toast with the scrambled eggs and chives, and place the smoked salmon on the side with a wedge of lemon to squeeze over.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (112, 42, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (70, 42, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3472, 42, 0.4);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2871, 42, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7628, 42, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5291, 42, 5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2247, 42, 0.5);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (43, 'Spicy tuna & cottage cheese jacket', 70, 'Preheat the oven to 180C/Gas 4/fan oven 160C. Prick the potato several times with a fork and put it straight onto a shelf in the hottest part of the oven. Bake for approximately 1 hour, or until it is soft inside.&#10;Mix tuna with spring onion, cherry tomatoes and coriander. Split jacket potato and fill with the tuna mix and cottage cheese.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3022, 43, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7834, 43, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2879, 43, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3472, 43, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2984, 43, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4394, 43, 8);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (44, 'Two bean, potato & tuna salad', 25, 'Put the potatoes in a pan of boiling water, then boil for 6-8 mins until almost tender. Add both types of beans, then cook for a further 5 mins until everything is cooked. Meanwhile, make the dressing. Whisk together the harissa and vinegar in a small bowl with a little seasoning. Whisk in the oil until the dressing is thickened. Drain potatoes well, toss with half of the dressing, then leave to cool.&#10;Flake the tuna, then fold into the potatoes. Add the remaining dressing. then gently toss. Divide between 4 bowls and serve each portion with a handful of rocket or watercress on top. Serve warm or cold.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3024, 44, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2812, 44, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3098, 44, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4400, 44, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1517, 44, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (276, 44, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 44, 0.1);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (45, 'Tuna melt potato wedges', 35, 'Heat oven to 220c/fan 200c/gas 6. Tip the potato wedges onto a large baking sheet, ensuring they sit in a single layer. Bake for 10 mins. Meanwhile, mix together the mayonnaise, shallot or onion, cheese, tuna and parsley.&#10;Tip the potato wedges into four individual or one large heatproof serving dish and spoon over the tuna mixture. Pop back in the oven for a further 12 mins, until bubbling.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3369, 45, 5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7908, 45, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3236, 45, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (9, 45, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4400, 45, 0.6);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2989, 45, 0.1);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (46, 'Tuna & lemon pasta', 25, 'Cook the pasta in boiling salted water for 8 minutes. Add the beans and cook for a further 3 minutes until both the pasta and beans are just tender. Meanwhile, tip the tuna and its oil into a bowl and flake the fish, keeping the pieces quite large. Stir in the lemon zest, capers, chilli and plenty of salt and pepper.&#10;Drain the pasta and beans, return them to the pan and toss with the tuna mixture. Add a little olive oil if you need to moisten everything. Serve the tuna and lemon pasta on its own or with a tomato and onion salad.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6222, 46, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2805, 46, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4399, 46, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2251, 46, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (269, 46, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (225, 46, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 46, 1);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (47, 'Trout & spelt salad with watercress', 10, 'Steam the broccoli for 3 mins until al dente. Mix together the orange juice, parsley and spelt, then top with the broccoli, trout and watercress.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4391, 47, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3190, 47, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6263, 47, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2989, 47, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2279, 47, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2821, 47, 0.5);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (48, 'Trout risotto', 35, 'In a frying pan, gently fry the onion in the oil until softened. Tip in the risotto rice and cook for 2 mins, stirring continuously. Add a third of the stock, and simmer, gently stirring until the stock has been absorbed. Add half the remaining stock and carry on cooking, stirring a bit more, until that has been absorbed too.&#10;Tip in the final lot of stock and cook until creamy and the rice is just tender. Stir in the trout, sour cream, lemon zest, most of the chives and some seasoning – it won’t need too much salt. Cover and leave for 5 mins to rest. Finally season with a squeeze of lemon juice, divide between two bowls and top with the remaining chives. Great with a green salad on the side.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 48, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 48, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6185, 48, 0.6);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1233, 48, 2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4391, 48, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (55, 48, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2251, 48, 0.4);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2248, 48, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2871, 48, 1);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (49, 'Honey & orange sea bass with lentils', 25, 'Heat oven to 200C/180C fan/gas 6. Place each sea bass fillet, skin-side down, on individual squares of foil. Mix together the orange zest, honey, mustard, 1 tbsp olive oil and some seasoning, and drizzle it over the fillets. Pull the sides of the foil up and twist the edges together to make individual parcels. Place the parcels on a baking tray and bake in the oven for 10 mins until the fish is just cooked and flakes easily when pressed with a knife.&#10;Warm the lentils following pack instructions, then mix with the orange juice, remaining oil, the watercress, herbs and seasoning. Divide the lentils between 2 plates and top each with a sea bass fillet. Drizzle over any roasting juices that are caught in the foil and serve immediately.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4368, 49, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2286, 49, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2279, 49, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5960, 49, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (261, 49, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 49, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4595, 49, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3190, 49, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2989, 49, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (260, 49, 0.2);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (50, 'Salmon & ginger fish cakes', 45, 'Heat oven to 200C/180C fan/gas 6. Toss the chips in a roasting tin with 1 tsp oil. Season and bake for 20-25 mins.&#10;Chop the salmon as finely as you can and place in a bowl with the ginger, lime zest and seasoning. Heat 1 tsp oil in a non-stick pan and soften the spring onions for 2 mins. Stir into the salmon, mix well and shape into 4 patties.&#10;Heat remaining oil in the pan and cook the patties for 3-4 mins each side until golden and cooked through. Cover with a lid and leave to rest for a few mins. Serve 2 patties each with the chips, mayo and lime wedges for squeezing.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3141, 50, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 50, 2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7628, 50, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2916, 50, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2252, 50, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2984, 50, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7908, 50, 1);


--
-- Pasta Recipes
--
INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (51, 'Beef stroganoff', 45, 'Heat the olive oil in a non-stick frying pan then add the sliced onion and cook on a medium heat until completely softened, so around 15 mins, adding a little splash of water if they start to stick at all. Crush in the garlic and cook for a 2-3 mins further, then add the butter. Once the butter is foaming a little, add the mushrooms and cook for around 5 mins until completely softened. Season everything well, then tip onto a plate.&#10;Tip the flour into a bowl with a big pinch of salt and pepper, then toss the steak in the seasoned flour. Add the steak pieces to the pan, splashing in a little oil if the pan looks particularly dry, and fry for 3-4 mins, until well coloured. Tip the onions and mushrooms back into the pan. Whisk the crème fraîche, mustard and beef stock together, then pour into the pan. Cook over a medium heat for around 5 mins. Scatter with parsley, then serve with pappardelle or rice.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 51, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2986, 51, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 51, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (678, 51, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2954, 51, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3640, 51, 5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6211, 51, 0.01);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (49, 51, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (261, 51, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1207, 51, 0.125);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2989, 51, 0.125);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (52, 'Fettucine alfredo', 25, 'In a medium saucepan, stir the clotted cream, butter and cornflour over a low-ish heat and bring to a low simmer. Turn off the heat and keep warm.&#10;Meanwhile, put the cheese in a small bowl and add a good grinding of black pepper, then stir everything together (don’t add any salt at this stage).&#10;Put the pasta in another pan with 2 tsp salt, pour over some boiling water and cook following pack instructions (usually 3-4 mins). When cooked, scoop some of the cooking water into a heatproof jug or mug and drain the pasta, but not too thoroughly.&#10;Add the pasta to the pan with the clotted cream mixture, then sprinkle over the cheese and gently fold everything together over a low heat using a rubber spatula.&#10;When combined, splash in 3 tbsp of the cooking water. At first, the pasta will look wet and sloppy: keep stirring until the water is absorbed and the sauce is glossy. Check the seasoning before transferring to heated bowls. Sprinkle over some chives or parsley, then serve immediately.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (55, 52, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (678, 52, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (156, 52, 4);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6268, 52, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2871, 52, 1);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (53, 'Linguine with avocado, tomato & lime', 30, 'Cook the pasta according to pack instructions – about 10 mins. Meanwhile, put the lime juice and zest in a medium bowl with the avocado, tomatoes, coriander, onion and mix well.&#10;Drain the pasta, toss into the bowl and mix well. Serve straight away while still warm, or cold.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2252, 53, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2152, 53, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3156, 53, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2879, 53, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2986, 53, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6247, 53, 2.7);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (54, 'Creamy courgette & bacon pasta', 30, 'Heat the olive oil in a large frying pan and sizzle the pancetta or bacon for about 5 mins until starting to crisp. Turn up the heat and add the grated courgette to the pan. Cook for 5 mins or until soft and starting to brown then add the garlic and cook for a minute longer. Season and set aside.&#10;Cook the tagliatelle according to the pack instructions and scoop out a cupful of cooking water. Drain the tagliatelle and tip into the frying pan with the bacon and courgette. Over a low heat toss everything together with the cream and half the Parmesan adding a splash of pasta water too if you need to loosen the sauce. Season to taste and serve twirled into bowls with the remaining Parmesan scattered over.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 54, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7823, 54, 4);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 54, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (33, 54, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (49, 54, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6247, 54, 2.7);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (55, 'Mac ''n'' cheese', 45, 'Heat the grill to its highest setting. In a flameproof casserole dish or metal frying pan, melt the butter and fry the garlic for 2 mins. Add the spinach and cook until it wilts, about 2 mins. Pour in the milk and bring to a gentle bubble. Add the macaroni and stir intermittently for around 20 mins until the pasta is cooked and covered in sauce.&#10;Stir in the cheddar and half the mozzarella. When it starts to melt, sprinkle the remaining mozzarella on top and put it in the oven for around 7-10 mins or until browned on top and sensational underneath.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1, 55, 0.125);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 55, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3102, 55, 0.125);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (81, 55, 0.7);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6229, 55, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (9, 55, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7778, 55, 0.5);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (56, 'Orecchiette with anchovies & purple sprouting broccoli', 25, 'Cook the orecchiette following pack instructions. Meanwhile, heat 3 tbsp of the olive oil and 1 tbsp of the oil from the anchovies in a frying pan. Add the garlic and chilli, and sizzle for 3-4 mins until the garlic is just starting to turn golden. Add the anchovies and lemon juice, and cook for 1-2 mins more until the anchovies melt into the sauce. Put the remaining olive oil, breadcrumbs and lemon zest in another frying pan, stir together and cook until crisp.&#10;When the pasta has 4-5 mins to go, add the broccoli to the pan. When cooked, drain, reserving a cup of the pasta water, then add to the frying pan with the garlic and anchovies. Stir and cook over a low heat for a further 2 mins, adding a splash of pasta water if it looks dry. Season, then serve in pasta bowls with the lemony crumbs sprinkled over the top.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6247, 56, 2.7);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 56, 2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4279, 56, 2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 56, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3349, 56, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2251, 56, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5544, 56, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3274, 56, 1);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (57, 'Broccoli & sage pasta', 17, 'Boil the spaghetti for 1 min. Add the broccoli and cook for 4 mins more.&#10;Meanwhile, heat the oil in a frying pan and add the shallots and garlic. Gently cook for 5 mins until golden. Add the chillies and sage to the pan and gently cook for 2 mins. Drain the pasta, mix with the shallot mixture in the pan, then scatter with Parmesan, if you like.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6247, 57, 2.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2821, 57, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 57, 1.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3236, 57, 5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 57, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3230, 57, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (254, 57, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (32, 57, 0.2);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (58, 'Spaghetti with artichokes', 20, 'Cook the spaghetti following pack instructions. Meanwhile, in a small bowl or jug, beat the eggs together with the milk, Parmesan, artichokes and seasoning.&#10;Heat a small pan on a medium heat and add the pine nuts. Toast for a few mins, shaking the pan occasionally, until they are pale golden. Keep your eye on them as they can catch and burn quickly.&#10;Once the pasta is cooked, drain in a colander and tip back into the pan. Put the pan onto a low heat and pour over the egg mixture, tossing together to coat all of the pasta in the sauce – take care not to heat it for too long or it will start to scramble.&#10;Toss through the toasted pine nuts and serve with extra Parmesan at the table, if you like.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6247, 58, 2.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (112, 58, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (81, 58, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (33, 58, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2773, 58, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3584, 58, 0.3);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (59, 'Pizza pasta salad', 25, 'Cook the pasta following pack instructions. Drain, rinse under cold water to cool, then drain well. Toss with the oil and sundried tomato pesto, season to taste. Place in a jar or plastic box and scatter over the remaining ingredients in layers, ending with the basil.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6227, 59, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 59, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3472, 59, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3156, 59, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7778, 59, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2273, 59, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1736, 59, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (259, 59, 1);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (60, 'So-simple spaghetti Bolognese', 30, 'Heat the oil in non-stick frying pan. Tip in the carrot and cook for 5 mins to soften. Scoop out the tomatoes from the can and add to the pan, and cook for 5 mins more. Pour over the tomato juice and basil, then simmer for 15 mins. Whizz together in a blender until smooth. This sauce can be frozen for up to 3 months.&#10;Heat through with the Multi mince. Cook spaghetti according to pack instructions. Reserve some of the cooking water, drain and tip the pasta into the pan along with the sauce. Toss together, thinning with pasta water, if needed, and serve with the extra basil leaves on top and the breadcrumbs.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 60, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2848, 60, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3156, 60, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (259, 60, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6247, 60, 2.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5293, 60, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2622, 60, 4);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES
(61, 'Broccoli baked potatoes', 40.0, STRINGDECODE('Heat oven to 200C/180C fan/gas 6. Microwave the potatoes on High for 12-15 mins until tender. Meanwhile, steam or boil the broccoli for 3 mins, then drain well. When the potatoes have cooled a little, cut them in half lengthways and scoop the insides into a bowl.\n\nPut the potato shells on a baking sheet. Mash the flesh with a fork, then stir in the mustard, egg, most of the cheese and the broccoli. Season if needed, then pile back into the shells. Sprinkle with the reserved cheese and bake for 15 mins until the tops are crisp and golden. Serve with salad and Tomato relish.'), 6, FALSE),
(62, 'Easy cheesy frittata', 45.0, STRINGDECODE('Ask a grown-up helper to switch the oven on to 180C/ 160C fan/gas 4. Snip or cut up the spring onions and put them in a bowl. Add the peas to the bowl.\n\nGrate the courgette using your rotary grater, one half at a time, then add it to the bowl.\n\nCut the ham into pieces with your scissors, if you do this over the bowl it will fall straight in.\n\nBreak the feta into the bowl by crumbling it with your hands.\n\nCrack the eggs into a bowl and, if any bits of shell fall in, scoop them out with a spoon. Whisk the eggs until the yolks are mixed into the white.\n\nPour the eggs into the other bowl and stir. Brush a round ovenproof dish, about 16cm across, with oil. Tip everything into the dish. Ask a helper to put the dish in the oven for 30 minutes or until the egg is set. Serve with salad and crusty bread.'), 3, FALSE),
(63, 'Spicy sausage & bean one-pot', 25.0, STRINGDECODE('Heat the oil in a large frying pan. Cook the onion and sausages over a fairly high heat for 8-10 mins, turning the sausages often so they brown all over.\n\nAdd the garlic to the pan with the kidney beans and their sauce. Half-fill one of the cans with water, swirl and then add this to the pan. Stir everything together and bring to the boil. Turn down to simmer and cook for 10 mins, or until the sausages are cooked through. Season and sprinkle with the parsley.'), 6, FALSE),
(64, 'Easy risotto with bacon & peas', 50.0, STRINGDECODE('Finely chop the onion. Heat 2 tablespoons of olive oil and a knob of butter in a pan, add the onions and fry until lightly browned (about 7 minutes). Add the bacon and fry for a further 5 minutes, until it starts to crisp.\n\nAdd the rice and stock, and bring to the boil. Stir well, then reduce the heat and cook, covered, for 15-20 minutes until the rice is almost tender.\n\nStir in the peas, add a little salt and pepper and cook for a further 3 minutes, until the peas are cooked. Serve sprinkled with freshly grated parmesan and freshly ground black pepper.'), 6, FALSE),
(65, 'Smoked salmon & pea frittata', 50.0, STRINGDECODE('Thickly slice the potatoes and cook in a pan of boiling salted water until just tender, about 10 minutes. Drain well and leave to cool slightly.\n\nCut the salmon into wide strips. Crack the eggs into a bowl, beat with a fork until lightly foamy, then stir in the smoked salmon, dill, peas and plenty of salt and pepper. Finally, stir in the potatoes.\n\nHeat 3 tablespoons of olive oil in a large non-stick frying pan, carefully pour in the egg mixture and cook over a fairly low heat for 10-15 minutes, until the egg is starting to set just under the surface.\n\nPut a plate that is slightly larger than the top of the pan on top and invert the frittata onto it. Slide it back into the pan and cook for a further 5 minutes to brown the underside. Slide on to a plate and leave to cool for 5 minutes before cutting into wedges. A tomato and chive salad tastes very fresh with this.'), 6, FALSE),
(66, 'Muddled potato & mozzarella tortilla', 30.0, STRINGDECODE('Start by frying: Heat the oil in a large frying pan. Empty the potatoes into the pan, spread them out to cover the base, then fry for 5 minutes. Pour in the beaten eggs so they completely cover the potatoes, season well and leave the tortilla to cook on a medium heat for about 15-20 minutes, or until the base and edges have set.\n\nGrill to finish: Take the tortilla off the hob and place under a hot grill until the top is firm, then remove from the grill and scatter over the tomatoes and mozzarella. Put the tortilla back under the grill for a further 3-5 minutes, or until the tomatoes are soft and the cheese has melted. To serve, cut into thick wedges.'), 6, FALSE);
INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES
(67, 'Creamy tomato courgetti', 30.0, STRINGDECODE('Heat oven to 200C/fan180C/gas 6. Cook the leeks in a pan of boiling salted water for 4-5 mins or until just tender. Drain and cool under a cold tap to stop them from cooking any further, then drain again well and pat dry on kitchen paper.\n\nWrap each leek in a slice of ham, then arrange, side-by-side, in a large baking dish. Mix the cheddar in a bowl with the Dijon mustard and cr\u00e8me fra\u00eeche, until well combined. Season to taste. Spread over the leeks, then bake for 15-20 mins until bubbling and golden brown. Serve at once with plenty of crusty bread to mop up the juices.'), 6, FALSE),
(68, 'Cheesy mushroom omelette', 15.0, STRINGDECODE('Heat the olive oil in a small non-stick frying pan. Tip in the mushrooms and fry over a high heat, stirring occasionally for 2-3 mins until golden. Lift out of the pan into a bowl and mix with the cheese and parsley.\n\nPlace the pan back on the heat and swirl the eggs into it. Cook for 1 min or until set to your liking, swirling with a fork now and again.\n\nSpoon the mushroom mix over one half of the omelette. Using a spatula or palette knife, flip the omelette over to cover the mushrooms. Cook for a few moments more, lift onto a plate and serve with oven chips and salad.'), 6, FALSE),
(69, 'Sweet potatoes with mushrooms & rosemary', 30.0, STRINGDECODE('Heat oven to 200C/fan 180C/gas 6. Prick the potatoes several times with a fork, then microwave on High for 8-10 mins, turning once, until tender. Meanwhile, heat the oil in a non-stick pan, add the mushrooms and rosemary and cook over a fairly high heat, stirring, until the mushrooms are tender and lightly coloured. Season to taste.\n\nPut the potatoes in the oven and roast for 15 mins until the skins start to crisp. Split open and spoon over the mushrooms. Sprinkle with Parmesan to serve.'), 6, FALSE),
(70, 'Cheese & chilli melts', 21.0, STRINGDECODE('Put the cheese, tomatoes, chilli and coriander leaves into a bowl with some seasoning, then mix well.\n\nWarm tortillas in the microwave according to pack instructions \u2013 this makes them more bendable. Divide the cheese mix over one half of each tortilla. Fold over the other half to make 8 half-moons, then press down to seal.\n\nBrush the tops with a little oil, then sit, oil-side down, on a hot area of the barbecue. Cook for a couple of mins until crisp and golden, brush the uncooked side with oil, then flip over for another few mins. Slice into wedges, then pile onto a platter while you cook the rest.'), 6, FALSE);

INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES
(121, 61, 0.25),
(261, 61, 0.25),
(3366, 61, 1.0),
(9, 61, 0.1479363486952715),
(2821, 61, 0.3170064614898675),
(2986, 62, 0.5),
(2996, 62, 0.0625),
(1578, 62, 0.5),
(19, 62, 0.1056688204966225),
(119, 62, 1.0),
(739, 63, 0.25),
(2986, 63, 0.25),
(1632, 63, 2.0),
(2915, 63, 1.0),
(2989, 63, 1.0),
(4562, 63, 0.84535056397298),
(2986, 64, 0.25),
(2767, 64, 1.5),
(6187, 64, 0.3170064614898675),
(3002, 64, 0.1056688204966225),
(3062, 65, 0.5283441024831125),
(119, 65, 2.0),
(260, 65, 0.03125),
(7628, 65, 1.0),
(604, 66, 0.33333333),
(3044, 66, 0.5621581250420318),
(3156, 66, 0.422675282),
(26, 66, 0.1056688204966225),
(119, 66, 2.0),
(3326, 67, 2.0),
(1579, 67, 4.0),
(148, 67, 0.1056688204966225),
(240, 67, 0.5),
(604, 68, 1.0),
(2954, 68, 1.0),
(9, 68, 0.1056688204966225),
(2989, 68, 1.0),
(116, 68, 2.0),
(3363, 69, 1.0),
(604, 69, 0.5),
(2954, 69, 0.42267528198649),
(271, 69, 0.5),
(32, 69, 0.0625),
(9, 70, 0.13208602562077812),
(3240, 70, 0.3170064614898675),
(6722, 70, 0.5),
(2879, 70, 0.2),
(5702, 70, 1.0);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES
(71, 'Slow cooker chicken', 300.0, STRINGDECODE('Heat the slow cooker if necessary and add a splash of water to the base. Scrunch up some foil to make a trivet to sit in the base of the bowl to rest the chicken on. Put the chicken into the pot and season the skin. Cover and cook on Low for 5 hours or until the leg or wing feels very loose when you wiggle it. Tip the juices inside the chicken out as you lift it out.\n\nBrown the chicken skin under the grill or carve the chicken before anyone sees it. Spoon the liquid out of the base of the pan to use as gravy, there won''t be much but it will have a good flavour.'), 5, TRUE),
(72, 'Nutty chicken satay strips', 10.0, STRINGDECODE('Heat oven to 200C, 180C fan, gas 4 and line a baking tray with non-stick paper.\n\nMix the peanut butter with the garlic, curry powder, soy sauce and lime in a bowl. Some nut butters are thicker than others, so if necessary, add a dash of boiling water to get a coating consistency. Add the chicken strips, mix well then arrange on the baking sheet, spaced apart, and bake in the oven for 8-10 mins until cooked, but still juicy.\n\nEat warm with the cucumber sticks and chilli sauce, or leave to cool then keep in the fridge for up to 2 days.'), 5, FALSE),
(73, 'Bacon & roast onion salad', 5.0, STRINGDECODE('Heat oven to 220C/200C fan/gas 7. Arrange the onion wedges on one side of a baking tray. Drizzle with \u00bd tbsp of the olive oil and season. Put in the oven and roast for 15 mins.\n\nMeanwhile, cook the peas in boiling water for 2 mins, drain and rinse in very cold water. Set aside.\n\nMake the dressing by mixing together another \u00bd tbsp of the oil, the vinegar, mustard and seasoning. Turn the onions, and put the bacon slices and bread next to them on the baking tray. Drizzle the remaining oil over the bread. Return the tray to the oven for 12 mins more, until the bacon and bread are golden.\n\nPut the lettuce and peas in a bowl, add the dressing and toss to combine. Arrange the onion and bread on top. Break up the bacon slightly and scatter over. Drizzle with a little more olive oil, if you like, and eat straight away.'), 5, FALSE),
(74, 'Mexican chicken burger', 10.0, STRINGDECODE('Put the chicken breast between two pieces of cling film and bash with a rolling pin or pan to about 1cm thick. Mix the chipotle paste with half the lime juice and spread over the chicken.\n\nHeat a griddle pan over a high heat. Once hot, cook the chicken for 3 mins each side until cooked through, adding the cheese for the final 2 mins of cooking. Add the bun, cut-side down, to the griddle pan to toast lightly. Season the chicken.\n\nMeanwhile, mash the avocado with the remaining lime juice. Stir in the cherry tomatoes, jalape\u00f1o and garlic, and season with a little salt. Spread over the base of the bun, then add the chicken followed by the top of the bun.'), 2, FALSE),
(75, 'Chicken schnitzel Caesar', 30.0, STRINGDECODE('Place a chicken breast between two sheets of cling film. Using a rolling pin or meat tenderiser, bash it gently until about 1cm thick and evenly flattened. Put the flour, eggs and breadcrumbs in three shallow bowls. Season the flour and mix well. Dip one of the chicken breasts into the flour, then the egg, making sure it\u2019s fully coated, then finally into the breadcrumbs. Set aside, then repeat with the other chicken breast.\n\nTo make the Caesar dressing, tip the egg yolk, mustard, vinegar and garlic into a mixing bowl. Whisk together, then slowly drizzle in the oil, whisking constantly, until you have a loose mayonnaise. Fold in the anchovies and grated parmesan, give it a good stir, then set aside.\n\nHeat the oil and butter in a frying pan, add the chicken and fry for 3-4 mins until golden. Turn over and cook for a further 3 mins, then remove from the pan. Tip the lemon juice into the pan, sizzle, then spoon over the schnitzels. Leave to rest for 4 mins.'), 5, FALSE),
(76, 'Roast beef with red wine & banana shallots', 30.0, STRINGDECODE('Let the meat come to room temperature for 1 hr before you roast it. Heat oven to 220C/200C fan/gas 7. Dry the meat with kitchen paper, then rub the oil all over it. Mix the mustard powder with the sea salt and 1 tsp pepper, then rub this over the meat too. Lay the carrots in a large roasting tin to make a trivet and sit the beef on top, fat-side up.\n\nRoast for 15 mins, then turn the heat down to 180C/160C fan/gas 4 and roast for another 55 mins. This will give you medium beef; for medium-rare, cook for 45 mins. (When it\u2019s done, a probe thermometer inserted into the thickest part of the beef should read 65C).\n\nWith 30 mins cooking time left, baste the beef in the fat that has pooled in the tin, add the shallots and bay leaves, and coat these in the fat too. Season and return to the oven. Meanwhile, boil the wine in a small saucepan to reduce it by two-thirds.\n\nWhen the meat is ready, transfer to a board, cover loosely with foil and leave to rest. Turn the oven up to 200C/180C fan/gas 6 and return the tin to the oven for a further 10-15 mins until the shallots are tender.\n\nScoop out the shallots with a slotted spoon and set aside to keep warm. Remove the excess fat from the tin and discard, leaving 1 tbsp fat and all the dark juices behind. Add the flour and cook on the hob for 2 mins, stirring constantly. Gradually whisk in the reduced wine, followed by the stock and redcurrant jelly, scraping up all the tasty bits from the bottom of the tin. Bubble until thickened and rich, add the juices from the resting meat, then season to taste. Discard the carrots. Serve the beef with the gravy and shallots.'), 2, FALSE);
INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES
(77, 'Steak with mushroom puff tartlets', 20.0, STRINGDECODE('Heat oven to 200C/fan 180C/gas 6. Roll out the pastry to about the thickness of a \u00a31 coin, then cut out 2 x 12cm circles using a cutter, or by scoring around a saucer. Score a circle 2cm in from the edge, then prick the pastry inside the border. Lift onto a baking tray, then bake for 20-25 mins or until golden and puffed. Press the risen middles down a little, ready for the filling. Can be made a day ahead, then reheated in a hot oven.\n\nHeat the oil in a pan, add the shallots, then fry until softened. Add the mushrooms and thyme, then fry until mushrooms are softened and any liquid almost gone. Add the port or Madeira, then bubble for 2 mins. Add the cream, simmer for 1 min more until the sauce is slightly thickened, then set aside.\n\nRub the steaks with a little oil and seasoning. Heat a griddle pan until hot, then cook the steaks for 2-3 mins on each side (depending on their thickness) for medium rare, a little more if you like your steaks well done. Cover the steaks with foil, then rest them for 10 mins.\n\nReheat the tartlets. Warm the mushroom mixture over a low heat. Set the tartlets on warm plates, then spoon over the mushroom mixture. Sit the fillet steaks on top with a sprig of thyme. Serve with Balsamic spinach and Oven saut\u00e9 potatoes (see below).'), 2, FALSE),
(78, 'BBQ pulled pork sandwich', 25.0, STRINGDECODE('Heat oven to 160C/140C fan/gas 3. Scatter the onions and bay leaves in the bottom of a large roasting tin. Mix the mustard powder, paprika and 1 tsp ground black pepper with a good pinch of salt. Rub this all over the pork, making sure you rub it into all the crevices. Place the pork, rind-side up, on top of the onions. Pour 200ml water into the bottom of the tin, wrap well with foil and bake for 4 hrs. This can be done up to 2 days ahead \u2013 simply cover the tray in foil and chill until ready to barbecue.\n\n\nLight the barbecue. In a bowl, mix the ketchup, vinegar, Worcestershire sauce and brown sugar. Remove the pork from the tin and pat dry. Place the roasting tin on the hob, pour in the ketchup mixture and bubble vigorously for 10-15 mins until thick and glossy. Remove the bay leaves and pour the sauce into a food processor; blitz until smooth. Smear half the sauce mixture over the meat.\n\nOnce the barbecue flames have died down, put on the pork, skin-side down. Cook for 15 mins until nicely charred, then flip over and cook for another 10 mins. The meat will be very tender, so be careful not to lose any between the bars. Alternatively, heat a combined oven-grill to high, place the pork on a baking tray and cook each side for 10-15 mins until nicely charred.\n\nLift the pork onto a large plate or tray. Remove string and peel off the skin. Using 2 forks, shred the meat into chunky pieces. Add 3-4 tbsp of the barbecue sauce to the meat and toss everything well to coat. Pile into rolls and serve with extra sauce and a little coleslaw.'), 5, FALSE),
(79, 'Slow-roast pork with apples & peppers', 20.0, STRINGDECODE('Heat oven to 160C/140C fan/gas 3. Sit the pork on a sheet of foil in a roasting tin. Mix the butter, sugar and mustard with 2 tsp salt, rub all over the top and ends of the joint (or joints), then scrunch up the foil to seal tightly in a parcel.\n\nMix the onions and apples in a large, shallow roasting tin. Scatter the peppers over the top and poke in some bay leaves. Mix the vinegar, oil, mustard and sugar with 100ml water and drizzle over everything. Roast the pork for 3\u00bd hrs while you cook the peppers on the shelf below for the first 2 hrs. When you remove the apples and peppers after 2 hrs, set aside and put the Salt-baked potatoes (see ''Goes well with'', right) into the oven underneath the pork instead.\n\nAfter 3 hrs, unwrap the foil from the pork. Scrape any mustard mixture stuck to the foil back onto the pork, sit it and any juices back in the tin and increase oven to 200C/180C fan/gas 6. Roast for 30-45 mins more until the skin is brown and crisping. Remove the pork from the oven, cover and rest for 20 mins while you put the peppers back on the top shelf for 20 mins to finish. Serve the pork thickly sliced alongside the apples and peppers, with any tin juices poured over.'), 5, FALSE);
INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES
(80, 'Pork meatballs in red pepper sauce', 15.0, STRINGDECODE('Put the pork mince in a bowl and stir in the apple, garlic, breadcrumbs and some salt and pepper. Shape the mixture into 16 balls, cover and chill for 10 mins.\n\nMeanwhile, make the sauce. Heat the oil in a medium saucepan and add the onion. Cook for 2-3 mins, until softened, then tip in the tomatoes and half a can of water. Stir in the peppers along with some salt and pepper. Partially cover and simmer for 15 mins.\n\nHeat the oil in a large non-stick frying pan and add the meatballs. Cook for 5-6 mins, stirring occasionally until they are browned all over. Set aside and keep warm.\n\nUsing a hand-held blender, whizz the tomato sauce until smooth. Carefully add the meatballs to the sauce and simmer for 5 mins, until cooked through. Meanwhile, cook the spaghetti following pack instructions, drain, then divide between serving plates. Top with the meatballs and sauce.'), 5, FALSE),
(81, 'Italian pork patties with potato wedges', 15.0, STRINGDECODE('Heat oven to 200C/180C fan/gas 6. Toss potato wedges in a large roasting tin with 1 tbsp oil and lemon juice. Spread out in a single layer. Bake for 35-45 mins, turning halfway, until golden brown and crisp.\n\neanwhile, place the breadcrumbs in a mixing bowl and moisten with 2 tbsp cold water. Add the mince, Parmesan, parsley, garlic and lemon zest. Season, mix well, then shape into 4 large, flat patties.\n\nHeat remaining oil in a pan and cook the patties for 7 mins on each side, or until they have a golden crust and are cooked through (alternatively, cook on the barbecue). Serve with the wedges and a tomato and rocket salad, if you like'), 5, FALSE);

INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES
(1570, 71, 1.0),
(7775, 72, 2.0),
(2915, 72, 1.0),
(4649, 72, 2.0),
(2253, 72, 0.1),
(1004, 72, 2.0),
(2906, 72, 3.0),
(2975, 73, 1.0),
(2996, 73, 2.0),
(268, 73, 0.1),
(261, 73, 1.0),
(2767, 73, 2.0),
(5281, 73, 0.5),
(2948, 73, 2.0),
(1768, 74, 1.0),
(2253, 74, 1.0),
(22, 74, 0.25),
(2153, 74, 2.0),
(3472, 74, 1.0),
(2915, 74, 0.5),
(5279, 74, 1.0),
(263, 75, 0.1),
(2915, 75, 1.0),
(2248, 75, 1.0),
(1004, 75, 2.0),
(121, 75, 1.0),
(2963, 75, 0.1),
(131, 75, 0.1),
(2948, 75, 2.0),
(604, 75, 3.0),
(132, 75, 5.0),
(7182, 76, 2.0),
(627, 76, 1.0),
(2963, 76, 1.0),
(262, 76, 0.1),
(2848, 76, 1.0),
(2155, 76, 3.0),
(4227, 76, 0.25),
(2975, 78, 2.0),
(2504, 78, 1.0),
(7827, 78, 2.0),
(275, 78, 1.0),
(5991, 78, 0.25),
(5510, 77, 5.0),
(604, 77, 1.0),
(3236, 77, 5.0),
(2954, 77, 2.0),
(264, 77, 1.0),
(2504, 77, 1.0),
(2504, 79, 1.0),
(5995, 79, 3.0),
(2963, 79, 3.0),
(2975, 79, 3.0),
(3468, 79, 3.0),
(263, 79, 0.1),
(5991, 79, 0.1),
(1, 79, 0.25),
(2437, 80, 1.0),
(2118, 80, 2.0),
(2915, 80, 0.5),
(604, 80, 1.0),
(6253, 80, 2.0),
(2975, 80, 1.0),
(3156, 80, 1.0),
(3444, 80, 3.0),
(3032, 81, 1.0),
(604, 81, 2.0),
(2248, 81, 0.1),
(2436, 81, 2.0),
(32, 81, 1.0),
(2989, 81, 0.1),
(2915, 81, 1.0);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(82, 'Parmesan spring chicken', 10, 'Heat grill to medium and line the grill pan with foil. Beat the egg white on a plate with a little salt and pepper. Tip the Parmesan onto another plate. Dip the chicken first in egg white, then the cheese. Grill the coated chicken for 10-12 mins, turning once until browned and crisp.\n\nMeanwhile, boil the potatoes for 10 mins, adding the peas for the final 3 mins, then drain. Toss the vegetables with the spinach leaves, vinegar, oil and seasoning to taste. Divide between four warm plates, then serve with the chicken.', 2, FALSE);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(112, 82, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(132, 82, 1.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1003, 82, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3233, 82, 2.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2996, 82, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3102, 82, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(275, 82, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(604, 82, 0.5);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(83, 'Flamb\u00e9ed chicken with asparagus', 50, 'Dust the chicken with the flour. Heat the oil and butter in a large, wide pan with a lid, add the chicken, then fry on all sides until nicely browned. Add the shallots, then fry for about 2 mins until they start to soften, but not colour. Pour in the brandy, carefully ignite, then stand well back until the flames have died down. Stir in the stock and bring to the boil. Reduce heat, cover, then cook for 15 mins until the chicken is just tender.\n\nAdd the asparagus to the sauce. Cover, then cook for 5 mins more until tender. Stir in the cr\u00e8me fra\u00eeche and tarragon and warm through. Season to taste.', 2, FALSE);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1003, 83, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6197, 83, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(604, 83, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1, 83, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3236, 83, 1.0);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(84, 'Spring salmon with minty veg', 20, 'Boil the potatoes in a large pan for 4 mins. Tip in the peas and beans, bring back up to a boil, then carry on cooking for another 3 mins until the potatoes and beans are tender. Whizz the olive oil, lemon zest and juice and mint in a blender to make a dressing(or finely chop the mint and whisk into the oil and lemon).\n\nPut the salmon in a microwave-proof dish, season, then pour the dressing over. Cover with cling film, pierce, then microwave on High for 4-5 mins until cooked through. Drain the veg, then mix with the hot dressing and cooking juices from the fish. Serve the fish on top of the vegetables.', 2, FALSE);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3022, 84, 1.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2996, 84, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(604, 84, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2248, 84, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(7628, 84, 1.0);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(85, 'Greek roast lamb', 100,'Heat oven to 240C/fan 220C/gas 9. Pound the garlic, half the oregano, lemon zest and a pinch of salt in a pestle and mortar, then add the lemon juice and a drizzle of olive oil. Stab the lamb all over with a sharp knife, then push as much of the herb paste as you can into the holes.\n\nTip the potatoes into a large roasting tin, then toss in the remaining olive oil and any remaining herb paste. Nestle the lamb amongst the potatoes, roast for 20 mins, then reduce the temperature to 180C/fan 160C/gas 4. Roast for 1 hr 15 mins for medium-rare, adding another 15 mins if you prefer your lamb medium. Baste the lamb once or twice with the juices and toss the potatoes. When the lamb is done to your liking, remove from the tin and let it rest. Throw the rest of the oregano in with the potatoes, scoop from the tin and keep warm.\n\nPlace the roasting tin over a medium flame, add the canned tomatoes and olives to the pan juices, then simmer for a few mins. Serve the lamb with the potatoes and sauce and a simple salad.', 5, FALSE);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2915, 85, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(243, 85, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2248, 85, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(604, 85, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3022, 85, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3472, 85, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2271, 85, 2.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(5103, 85, 3.0);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(86, 'Rhubarb spice cake', 80, 'Heat oven to 180C/fan 160C/gas 4 and put the kettle on. Butter and line a deep 20cm square cake tin. Sift the flour and spices into a bowl. Beat together the butter and sugar until light and fluffy in the food processor, then beat in the golden syrup. Dissolve the bicarbonate of soda in 200ml boiling water, then gradually pour through the spout of the processor. Pulse in the flour, then add the eggs, mixing briefly. Remove the bowl from the processor, then gently stir in the rhubarb.\n\nPour the mixture into the tin and bake for 50-60 mins, until the cake feels firm to the touch and springs back when pressed. Cool in the tin for 5 mins, then turn out and cool on a wire rack. Dust with icing sugar.', 3, FALSE);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1, 86, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6197, 86, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2916, 86, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6135, 86, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6002, 86, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(121, 86, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2367, 86, 1.0);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(87, 'Spring fish pie', 40, 'Heat oven to 220C/fan 200C /gas 7. Tip the spinach into a colander sitting in the sink and tip the potatoes into a saucepan. Bring a kettle full of water to the boil and pour enough over the potatoes to cover and slowly pour the rest over the spinach to wilt it. Bring the potatoes to the boil and cook for 8-10 mins until tender, then drain and roughly mash.\n\nLeave the spinach to cool, then squeeze out excess water with your hands. Scatter the spinach over the bottom of 2 individual or 1 small ovenproof dish leaving two gaps for the eggs. Crack the eggs into the gaps, then season with salt and pepper. Distribute the fish over the spinach and eggs. Spread over the cr\u00e8me fra\u00eeche and drizzle with the lemon juice. Loosely spoon over the potatoes, drizzle over the olive oil, then bake for 20-25 mins until the top is crispy and golden and the sauce is bubbling at the sides. Leave to stand for a few mins, then serve straight from the dish.', 1, FALSE);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3102, 87, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(111, 87, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4360, 87, 2.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2248, 87, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(604, 87, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3022, 87, 1.0);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(88, 'Hot cross bread & lemon pudding', 60, 'Butter a 1-litre baking dish that will quite snugly fit the buns. Cut each bun into 3 slices, and sandwich back together with a generous spreading of curd. Arrange buns in the dish.\n\nWhisk egg, cream, milk and remaining curd, then sieve into a jug with the vanilla and 3 tbsp of the sugar. Pour over the buns and stand at room temperature for 30 mins for the custard to soak in.\n\nHeat oven to 160C/140C fan/ gas 3. Scatter the remaining sugar and lemon zest over the pudding. Bake for 30-40 mins until the top is golden and the custard gently set. Stand for 5 mins, then serve with cream or vanilla ice cream, if you like.', 0, FALSE);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1, 88, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(5268, 88, 2.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2251, 88, 4.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(111, 88, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(53, 88, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(69, 88, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(265, 88, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6135, 88, 0.5);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(89, 'Lamb & garlic bread salad', 55, 'Heat oven to 200C/180C fan/gas 6. Mix half the mint with 1 tbsp oil, the lemon zest and a large pinch of salt and black pepper, then rub the mixture all over the lamb cutlets.\n\nBake the garlic bread for 15 mins, then allow to cool a little. Tear into chunks and return to the oven for 5 mins to dry out.\n\nMeanwhile, put a griddle pan or large frying pan over a high heat and, once very hot, cook the lamb for 2-3 mins each side or until nicely seared outside but still pink in the middle. (You may have to do this in two batches.) Set aside to rest while you make the salad.\n\nPut the tomatoes and cucumber in the same hot pan and cook for 2 mins or until a little charred \u2013 you will need to do this in batches. Tip into a large bowl and add the\ntoasted bread, remaining mint and the parsley.\n\nMix the lemon juice with the remaining olive oil and the honey, then season. Pour the dressing over the salad and toss gently so the tomatoes don\u2019t break up too much. Serve the warm salad alongside the lamb cutlets.', 2, FALSE);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(604, 89, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2248, 89, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(5103, 89, 3.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3472, 89, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2906, 89, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2989, 89, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(5960, 89, 0.1);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(90, 'Strawberry labneh', 20, 'Mix the yogurt with a pinch of salt. Line a sieve with muslin and sit over a deep bowl. Spoon in the yogurt and put in the fridge to strain for 4 hrs.\n\nMeanwhile, hull and quarter the strawberries, mix them with the sugar and rosewater and leave to macerate.\n\nAfter 4 hrs, turn the labneh out into a clean bowl. Gently fold through the honey. Take \u00bc of the strawberries and pur\u00e9e them in a blender, then fold into the labneh, so you have a rippled yogurt. Serve in glass bowls with the rest of the strawberries on top, scatter with pistachios, and serve with the pistachio & coriander seed biscuits on the side.', 0, FALSE);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(215, 90, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2374, 90, 2.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6135, 90, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(5960, 90, 0.2);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(91, 'Summer beans on toast with prosciutto', 20, 'Heat a medium-sized pan of water until boiling, and heat a griddle pan over a high heat. Add the broad beans and green beans to the boiling water. Cook for 2 mins or until just tender, then drain and remove the broad bean skins, if you like. Mix the vegetables with the pesto.\n\nMeanwhile, drizzle the cut side of the ciabatta with a little oil, then rub with the squashed garlic. Place, oiled-side down, in the griddle pan. Toast until charred griddle lines appear \u2013 about 2 mins.\n\nPlace a piece of toasted ciabatta on each plate, spread with the cream cheese, then top with the pesto veg, a slice of prosciutto and a handful of rocket leaves. Drizzle with some more olive oil and serve.', 6, FALSE);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2820, 91, 2.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2805, 91, 1.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(5258, 91, 3.0);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(604, 91, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2915, 91, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(17, 91, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1581, 91, 1.0);
