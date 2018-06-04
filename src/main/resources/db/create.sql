
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

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(53, 'Teriyaki salmon with sesame pak choi', 20,'Heat oven to 200C/180C fan/gas 6 and put the salmon in a shallow baking dish. Mix the sweet chilli, honey, sesame oil, mirin, soy and ginger in a small bowl and pour over the salmon so the steaks are completely covered. Bake for 10 mins while you cook the pak choi.\n\nCut a slice across the base of the pak choi so the leaves separate. Heat the oils in a wok, add the garlic and stir-fry briefly to soften. Add the pak choi and fry until the leaves start to wilt. Pour over the stock, tightly cover the pan and allow to cook for 5 mins \u2013 you are aiming for the stems of the pak choi to be tender but still have a bit of bite.\n\nServe the pak choi in shallow bowls, top with the salmon steaks and spoon over the juices. Scatter with the toasted sesame seeds and serve on its own or with brown rice or noodles.', 5, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(7628, 53, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(5960, 53, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(606, 53, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4649, 53, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2916, 53, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2837, 53, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(739, 53, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2915, 53, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3525, 53, 0.5);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(54, 'Soba noodles with wasabi garlic prawns', 15, 'Mix the wasabi, soy sauce and garlic in a small bowl. Bring a large pan of water to the boil and cook the noodles following pack instructions.\n\nMeanwhile, heat the butter in a frying pan. Once foaming, stir in the prawns and cook for a few mins until pink. Stir in the wasabi mixture with a couple of spoonfuls of the noodle cooking water and heat through. Add the noodles to the pan with the spring onions, toss together and divide between two plates.', 5, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3504, 54, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4649, 54, 1.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2915, 54, 0.2);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6243, 54, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1, 54, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4425, 54, 4.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2975, 54, 1.0);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(55, 'Jerk prawn & coconut rice bowls', 8, 'Heat 1 tbsp flavourless oil in a large frying pan. Add the prawns and the jerk seasoning, and cook for 1-2 mins. Drain the beans, reserving 3 tbsp of the chilli sauce.\n\nAdd the beans to the pan along with the reserved sauce and the coconut rice. Fry for 3-4 mins, then season with salt to taste and spoon into two bowls to serve.', 5, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(250, 55, 1.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4559, 55, 2.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6173, 55, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4425, 55, 2.0);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(56, 'Rice noodles with sundried tomatoes, Parmesan & basil', 15, 'Prepare the noodles according to pack instructions, then drain. Heat the oil, then fry the tomatoes and garlic for 3 mins. Toss the noodles and most of the cheese and basil into the pan, season, then scatter over the remaining cheese and basil.', 2, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6257, 56, 2.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3472, 56, 0.25);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2915, 56, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(33, 56, 0.3);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(259, 56, 1.0);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(57, 'Lemon chicken with spring veg noodles', 20, 'Heat the oil in a non-stick pan, then fry the chicken for 5 mins until almost cooked. Tip onto a plate. Pour 250ml water into the pan with the lemon zest and juice, sugar and ginger. In a bowl, mix the cornflour with a little water until smooth, then whisk into the pan. Bring to the boil, stirring, then add the chicken to the sauce. Reduce the heat. Bubble for a few mins until chicken is cooked and the sauce thickened.\n\nMeanwhile, cook the noodles and veg together in boiling water for 4 mins, then drain. Toss together the chicken, noodles, veg and spring onions; serve scattered with nuts.', 1, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(624, 57, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(1002, 57, 2.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2248, 57, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6135, 57, 0.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2916, 57, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6211, 57, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6237, 57, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2805, 57, 0.25);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2975, 57, 0.25);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(3546, 57, 1.0);

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES(58, 'Lamb cutlets with lentil & feta salad', 20, 'Cook peas in boiling water for 3-4 mins until just tender, then drain. Mix with the lentils, vinegar, sugar and mint, then crumble in the feta and season well.\n\nHeat a griddle pan, brush the cutlets with a little oil and season. Cook in the hot pan for 4 mins on each side until browned and the middle is pink. Divide the salad between four bowls, then top with a couple of cutlets per person.', 1, FALSE);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(2996, 58, 0.75);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(4595, 58, 0.75);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(275, 58, 0.1);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(6135, 58, 1.0);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(19, 58, 0.75);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(604, 58, 0.5);
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES(5103, 58, 4.0);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (59, 'Spinach & watercress soup', 10, 'Put the spinach, watercress, spring onion, vegetable stock, avocado, cooked rice, lemon juice and mixed seeds in a blender with seasoning. Whizz until smooth. Heat until piping hot. Scatter over some toasted seeds if you want added crunch.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3102, 59, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3190, 59, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2984, 59, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 59, 0.4);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2152, 59, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6178, 59, 0.6);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2248, 59, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3530, 59, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3592, 59, 0.125);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (60, 'Celery soup', 55, 'Peel and cut the potatoes into chunks.\n\nHeat the oil in a large saucepan over a medium heat, tip in the celery, garlic and potatoes and coat in the oil. Add a splash of water and a big pinch of salt and cook, stirring regularly for 15 mins, adding a little more water if the veg begins to stick.\n\nPour in the vegetable stock and bring to the boil, then turn the heat down and simmer for 20 mins further, until the potatoes are falling apart and the celery is soft. Use a stick blender to purée the soup, then pour in the milk and blitz again. Season to taste. Serve with crusty bread.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 60, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2861, 60, 0.75);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 60, 0.005);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3023, 60, 0.125);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 60, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (180, 60, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5260, 60, 0.25);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (61, 'Cucumber, pea & lettuce soup', 20, 'Boil 1.4 litres water in a kettle. Heat the oil in a large non-stick frying pan and cook the spring onions for 5 mins, stirring frequently, or until softened. Add the cucumber, lettuce and peas, then pour in the boiled water. Stir in the bouillon, cover and simmer for 10 mins or until the vegetables are soft but still bright green.\n\nBlitz the mixture with a hand blender until smooth. Serve hot or cold, topped with yogurt (if you like), with rye bread alongside.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (663, 61, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2984, 61, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2907, 61, 0.375);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2945, 61, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3002, 61, 0.425);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 61, 0.02);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (104, 61, 0.04);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5280, 61, 0.25);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (62, 'Carrot & ginger soup', 10, 'Peel and chop the carrots and put in a blender with the grated ginger, turmeric, cayenne pepper, wholemeal bread, soured cream and vegetable stock. Blitz until smooth. Heat until piping hot. Swirl through some extra soured cream, or a sprinkling of cayenne, if you like.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2848, 62, 1.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2916, 62, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (258, 62, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (247, 62, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5259, 62, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (55, 62, 0.04);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 62, 0.8);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (63, 'Roasted sweet potato & carrot soup', 50, 'Heat oven to 220C/200C fan/ gas 7 and put the sweet potatoes and carrots into a large roasting tin, drizzled with half a tbsp olive oil and plenty of seasoning. Roast the veg in the oven for 25-30 mins or until caramelised and tender.\n\nMeanwhile, put the remaining 1 tbsp olive oil in a large deep saucepan and fry the onion over a medium-low heat for about 10 mins until softened. Add the garlic and stir for 1 min before adding the stock. Simmer for 5-10 mins until the onions are very soft, then set aside.\n\nOnce the roasted veg is done, leave to cool a little, then transfer to the saucepan and use a hand blender to process until smooth. Stir in the crème fraîche, a little more seasoning and reheat until hot. Serve in bowls topped with a swirl of crème fraîche and a good grinding of black pepper.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3141, 63, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2848, 63, 0.15);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 63, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 63, 0.0625);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 63, 0.0025);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 63, 0.875);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (55, 63, 0.0875);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (64, 'Broccoli & Stilton soup', 45, 'Heat the rapeseed oil in a large saucepan and then add the onions. Cook on a medium heat until soft. Add a splash of water if the onions start to catch.\n\nAdd the celery, leek, potato and a knob of butter. Stir until melted, then cover with a lid. Allow to sweat for 5 minutes. Remove the lid.\n\nPour in the stock and add any chunky bits of broccoli stalk. Cook for 10 – 15 minutes until all the vegetables are soft.\n\nAdd the rest of the broccoli and cook for a further 5 minutes. Carefully transfer to a blender and blitz until smooth. Stir in the stilton, allowing a few lumps to remain. Season with black pepper and serve.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (663, 64, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 64, 0.125);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2861, 64, 0.32);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2942, 64, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3023, 64, 0.06);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (131, 64, 0.04);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 64, 0.875);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2821, 64, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4, 64, 1.2);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (65, 'Creamy tomato soup', 75, 'Put the oil, onions, celery, carrots, potatoes and bay leaves in a big casserole dish, or two saucepans. Fry gently until the onions are softened – about 10-15 mins. Fill the kettle and boil it.\n\nStir in the tomato purée, sugar, vinegar, chopped tomatoes and passata, then crumble in the stock cubes. Add 1 litre boiling water and bring to a simmer. Cover and simmer for 15 mins until the potato is tender, then remove the bay leaves. Purée with a stick blender (or ladle into a blender in batches) until very smooth. Season to taste and add a pinch more sugar if it needs it. The soup can now be cooled and chilled for up to 2 days, or frozen for up to 3 months.\n\nTo serve, reheat the soup, stirring in the milk – try not to let it boil. Serve in small bowls for the children with cheesy sausage rolls then later in bowls for the adults as Hot Bloody Mary soup.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 65, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 65, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2861, 65, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2848, 65, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3023, 65, 0.135);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (220, 65, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3164, 65, 0.03);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5992, 65, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (275, 65, 0.0125);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3238, 65, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7827, 65, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1164, 65, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (180, 65, 0.15);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (66, 'Roasted onion soup with goat''s cheese toasts', 85, 'Heat oven to 200C/180C fan/gas 6. Put the onions in a roasting tin with the oil, salt and pepper. Give it a good stir, then roast for 45 mins, stirring halfway through, until the onions are tinged brown, but not burnt.\n\nTip the onions into a large pan with the stock, mustard and Marmite. Bring to the boil and simmer for 15 mins, then stir in the parsley. Toast 4 of the bread slices then scatter on the cheese. Ladle the soup into bowls, pop a toast into each and serve with the extra slices of bread on the side.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 66, 1.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 66, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 66, 0.875);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (261, 66, 0.75);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7871, 66, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2989, 66, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (140, 66, 0.875);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5259, 66, 3);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (67, 'Chicken noodle soup', 40, 'Pour the stock into a pan and add the chicken, ginger and garlic. Bring to the boil, then reduce the heat, partly cover and simmer for 20 mins, until the chicken is tender. Remove the chicken to a board and shred into bite-size pieces using a couple of forks.\n\nReturn the chicken to the stock with the noodles, corn, mushrooms, half the spring onions and the soy sauce. Simmer for 3-4 mins until the noodles are tender. Ladle into two bowls and scatter over the remaining spring onions, herbs and chilli shreds if using. Serve with extra soy sauce for sprinkling.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 67, 1.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (828, 67, 1.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2916, 67, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 67, 0.01);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6257, 67, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2885, 67, 0.07);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2954, 67, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4649, 67, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (259, 67, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (225, 67, 0.5);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (68, 'Pumpkin soup', 45, 'Heat the olive oil in a large saucepan, then gently cook the onions for 5 mins, until soft but not coloured.\n\nAdd the pumpkin or squash to the pan, then carry on cooking for 8-10 mins, stirring occasionally until it starts to soften and turn golden.\n\nPour the stock into the pan and season with salt and pepper. Bring to the boil, then simmer for 10 mins until the squash is very soft.\n\nPour the double cream into the pan, bring back to the boil, then purée with a hand blender. For an extra-velvety consistency you can pour the soup through a fine sieve. The soup can now be frozen for up to 2 months.\n\nTo make the croutons: cut the bread into small squares. Heat the olive oil in a frying pan, then fry the bread until it starts to become crisp. Add a handful of pumpkin seeds to the pan, then cook for a few mins more until they are toasted. These can be made a day ahead and stored in an airtight container.\n\nReheat the soup if needed, taste for seasoning, then serve scattered with croutons and seeds and drizzled with more olive oil, if you want.', 'DL', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 68, 0.7);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 68, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3217, 68, 1.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1411, 68, 0.4);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (52, 68, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5259, 68, 1.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3517, 68, 0.07);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (69, 'Roast salmon', 35, 'Heat oven to 160C/140C fan/gas 4. To make the crumble, mix the crumbs, onion flakes, herbs, horseradish, 1 tsp salt and melted butter together, then set aside. Line a baking tray with baking parchment, scatter over the sliced onions and drizzle with olive oil. Sprinkle with a little salt and lay the salmon on top, skin-side up. Loosely pack over the crumble mix. Roast in the oven for 20 mins, or until just cooked through.&#10;While the fish is cooking, blanch the kale in boiling water for 2 mins, drain and refresh under cold water. Put the kale, garlic, lemon zest, and oil in a food processor and blend to a coarse salsa. Season to taste.&#10;Once the fish is cooked, remove from the oven and leave to rest for 5 mins. Pull apart into large chunks and serve with onions, salsa and sour cream.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7628, 69, 2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2930, 69, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 69, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 69, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2248, 69, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 69, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (55, 69, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5279, 69, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2977, 69, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2989, 69, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (260, 69, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (270, 69, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (131, 69, 0.1);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (70, 'Scrambled eggs & smoked salmon', 12, 'Beat together the eggs, milk and some seasoning in a jug. Heat the oil in a large non-stick pan over a low heat and pour in the egg mix. Cook, stirring, until it is done around the edges and slightly runny in the middle. Then tip in the tomatoes and warm through for 1 min until the eggs have finished cooking.&#10;Top the toast with the scrambled eggs and chives, and place the smoked salmon on the side with a wedge of lemon to squeeze over.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (112, 70, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (70, 70, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3472, 70, 0.4);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2871, 70, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7628, 70, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5291, 70, 5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2247, 70, 0.5);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (71, 'Spicy tuna & cottage cheese jacket', 70, 'Preheat the oven to 180C/Gas 4/fan oven 160C. Prick the potato several times with a fork and put it straight onto a shelf in the hottest part of the oven. Bake for approximately 1 hour, or until it is soft inside.&#10;Mix tuna with spring onion, cherry tomatoes and coriander. Split jacket potato and fill with the tuna mix and cottage cheese.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3022, 71, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7834, 71, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2879, 71, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3472, 71, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2984, 71, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4394, 71, 8);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (72, 'Two bean, potato & tuna salad', 25, 'Put the potatoes in a pan of boiling water, then boil for 6-8 mins until almost tender. Add both types of beans, then cook for a further 5 mins until everything is cooked. Meanwhile, make the dressing. Whisk together the harissa and vinegar in a small bowl with a little seasoning. Whisk in the oil until the dressing is thickened. Drain potatoes well, toss with half of the dressing, then leave to cool.&#10;Flake the tuna, then fold into the potatoes. Add the remaining dressing. then gently toss. Divide between 4 bowls and serve each portion with a handful of rocket or watercress on top. Serve warm or cold.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3024, 72, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2812, 72, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3098, 72, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4400, 72, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1517, 72, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (276, 72, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 72, 0.1);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (73, 'Tuna melt potato wedges', 35, 'Heat oven to 220c/fan 200c/gas 6. Tip the potato wedges onto a large baking sheet, ensuring they sit in a single layer. Bake for 10 mins. Meanwhile, mix together the mayonnaise, shallot or onion, cheese, tuna and parsley.&#10;Tip the potato wedges into four individual or one large heatproof serving dish and spoon over the tuna mixture. Pop back in the oven for a further 12 mins, until bubbling.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3369, 73, 5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7908, 73, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3236, 73, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (9, 73, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4400, 73, 0.6);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2989, 73, 0.1);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (74, 'Tuna & lemon pasta', 25, 'Cook the pasta in boiling salted water for 8 minutes. Add the beans and cook for a further 3 minutes until both the pasta and beans are just tender. Meanwhile, tip the tuna and its oil into a bowl and flake the fish, keeping the pieces quite large. Stir in the lemon zest, capers, chilli and plenty of salt and pepper.&#10;Drain the pasta and beans, return them to the pan and toss with the tuna mixture. Add a little olive oil if you need to moisten everything. Serve the tuna and lemon pasta on its own or with a tomato and onion salad.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6222, 74, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2805, 74, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4399, 74, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2251, 74, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (269, 74, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (225, 74, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 74, 1);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (75, 'Trout & spelt salad with watercress', 10, 'Steam the broccoli for 3 mins until al dente. Mix together the orange juice, parsley and spelt, then top with the broccoli, trout and watercress.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4391, 75, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3190, 75, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6263, 75, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2989, 75, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2279, 75, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2821, 75, 0.5);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (76, 'Trout risotto', 35, 'In a frying pan, gently fry the onion in the oil until softened. Tip in the risotto rice and cook for 2 mins, stirring continuously. Add a third of the stock, and simmer, gently stirring until the stock has been absorbed. Add half the remaining stock and carry on cooking, stirring a bit more, until that has been absorbed too.&#10;Tip in the final lot of stock and cook until creamy and the rice is just tender. Stir in the trout, sour cream, lemon zest, most of the chives and some seasoning – it won’t need too much salt. Cover and leave for 5 mins to rest. Finally season with a squeeze of lemon juice, divide between two bowls and top with the remaining chives. Great with a green salad on the side.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2975, 76, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 76, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6185, 76, 0.6);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1233, 76, 2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4391, 76, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (55, 76, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2251, 76, 0.4);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2248, 76, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2871, 76, 1);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (77, 'Honey & orange sea bass with lentils', 25, 'Heat oven to 200C/180C fan/gas 6. Place each sea bass fillet, skin-side down, on individual squares of foil. Mix together the orange zest, honey, mustard, 1 tbsp olive oil and some seasoning, and drizzle it over the fillets. Pull the sides of the foil up and twist the edges together to make individual parcels. Place the parcels on a baking tray and bake in the oven for 10 mins until the fish is just cooked and flakes easily when pressed with a knife.&#10;Warm the lentils following pack instructions, then mix with the orange juice, remaining oil, the watercress, herbs and seasoning. Divide the lentils between 2 plates and top each with a sea bass fillet. Drizzle over any roasting juices that are caught in the foil and serve immediately.', 'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4368, 77, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2286, 77, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2279, 77, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5960, 77, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (261, 77, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 77, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4595, 77, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3190, 77, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2989, 77, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (260, 77, 0.2);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (78, 'Salmon & ginger fish cakes', 45, 'Heat oven to 200C/180C fan/gas 6. Toss the chips in a roasting tin with 1 tsp oil. Season and bake for 20-25 mins.&#10;Chop the salmon as finely as you can and place in a bowl with the ginger, lime zest and seasoning. Heat 1 tsp oil in a non-stick pan and soften the spring onions for 2 mins. Stir into the salmon, mix well and shape into 4 patties.&#10;Heat remaining oil in the pan and cook the patties for 3-4 mins each side until golden and cooked through. Cover with a lid and leave to rest for a few mins. Serve 2 patties each with the chips, mayo and lime wedges for squeezing.', 'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3141, 78, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 78, 2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7628, 78, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2916, 78, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2252, 78, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2984, 78, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7908, 78, 1);


--
-- Pasta Recipes
--
INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (79, 'Beef stroganoff', 45, 'Heat the olive oil in a non-stick frying pan then add the sliced onion and cook on a medium heat until completely softened, so around 15 mins, adding a little splash of water if they start to stick at all. Crush in the garlic and cook for a 2-3 mins further, then add the butter. Once the butter is foaming a little, add the mushrooms and cook for around 5 mins until completely softened. Season everything well, then tip onto a plate.&#10;Tip the flour into a bowl with a big pinch of salt and pepper, then toss the steak in the seasoned flour. Add the steak pieces to the pan, splashing in a little oil if the pan looks particularly dry, and fry for 3-4 mins, until well coloured. Tip the onions and mushrooms back into the pan. Whisk the crème fraîche, mustard and beef stock together, then pour into the pan. Cook over a medium heat for around 5 mins. Scatter with parsley, then serve with pappardelle or rice.',
                                                                                   'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 79, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2986, 79, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 79, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (678, 79, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2954, 79, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3640, 79, 5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6211, 79, 0.01);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (49, 79, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (261, 79, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1207, 79, 0.125);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2989, 79, 0.125);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (80, 'Fettucine alfredo', 25, 'In a medium saucepan, stir the clotted cream, butter and cornflour over a low-ish heat and bring to a low simmer. Turn off the heat and keep warm.&#10;Meanwhile, put the cheese in a small bowl and add a good grinding of black pepper, then stir everything together (don’t add any salt at this stage).&#10;Put the pasta in another pan with 2 tsp salt, pour over some boiling water and cook following pack instructions (usually 3-4 mins). When cooked, scoop some of the cooking water into a heatproof jug or mug and drain the pasta, but not too thoroughly.&#10;Add the pasta to the pan with the clotted cream mixture, then sprinkle over the cheese and gently fold everything together over a low heat using a rubber spatula.&#10;When combined, splash in 3 tbsp of the cooking water. At first, the pasta will look wet and sloppy: keep stirring until the water is absorbed and the sauce is glossy. Check the seasoning before transferring to heated bowls. Sprinkle over some chives or parsley, then serve immediately.',
                                                                                   'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (55, 80, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (678, 80, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (156, 80, 4);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6268, 80, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2871, 80, 1);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (81, 'Linguine with avocado, tomato & lime', 30, 'Cook the pasta according to pack instructions – about 10 mins. Meanwhile, put the lime juice and zest in a medium bowl with the avocado, tomatoes, coriander, onion and mix well.&#10;Drain the pasta, toss into the bowl and mix well. Serve straight away while still warm, or cold.',
                                                                                   'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2252, 81, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2152, 81, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3156, 81, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2879, 81, 0.2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2986, 81, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6247, 81, 2.7);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (82, 'Creamy courgette & bacon pasta', 30, 'Heat the olive oil in a large frying pan and sizzle the pancetta or bacon for about 5 mins until starting to crisp. Turn up the heat and add the grated courgette to the pan. Cook for 5 mins or until soft and starting to brown then add the garlic and cook for a minute longer. Season and set aside.&#10;Cook the tagliatelle according to the pack instructions and scoop out a cupful of cooking water. Drain the tagliatelle and tip into the frying pan with the bacon and courgette. Over a low heat toss everything together with the cream and half the Parmesan adding a splash of pasta water too if you need to loosen the sauce. Season to taste and serve twirled into bowls with the remaining Parmesan scattered over.',
                                                                                   'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 82, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7823, 82, 4);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 82, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (33, 82, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (49, 82, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6247, 82, 2.7);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (83, 'Mac ''n'' cheese', 45, 'Heat the grill to its highest setting. In a flameproof casserole dish or metal frying pan, melt the butter and fry the garlic for 2 mins. Add the spinach and cook until it wilts, about 2 mins. Pour in the milk and bring to a gentle bubble. Add the macaroni and stir intermittently for around 20 mins until the pasta is cooked and covered in sauce.&#10;Stir in the cheddar and half the mozzarella. When it starts to melt, sprinkle the remaining mozzarella on top and put it in the oven for around 7-10 mins or until browned on top and sensational underneath.',
                                                                                   'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1, 83, 0.125);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 83, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3102, 83, 0.125);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (81, 83, 0.7);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6229, 83, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (9, 83, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7778, 83, 0.5);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (84, 'Orecchiette with anchovies & purple sprouting broccoli', 25, 'Cook the orecchiette following pack instructions. Meanwhile, heat 3 tbsp of the olive oil and 1 tbsp of the oil from the anchovies in a frying pan. Add the garlic and chilli, and sizzle for 3-4 mins until the garlic is just starting to turn golden. Add the anchovies and lemon juice, and cook for 1-2 mins more until the anchovies melt into the sauce. Put the remaining olive oil, breadcrumbs and lemon zest in another frying pan, stir together and cook until crisp.&#10;When the pasta has 4-5 mins to go, add the broccoli to the pan. When cooked, drain, reserving a cup of the pasta water, then add to the frying pan with the garlic and anchovies. Stir and cook over a low heat for a further 2 mins, adding a splash of pasta water if it looks dry. Season, then serve in pasta bowls with the lemony crumbs sprinkled over the top.',
                                                                                   'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6247, 84, 2.7);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 84, 2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (4279, 84, 2);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 84, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3349, 84, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2251, 84, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5544, 84, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3274, 84, 1);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (85, 'Broccoli & sage pasta', 17, 'Boil the spaghetti for 1 min. Add the broccoli and cook for 4 mins more.&#10;Meanwhile, heat the oil in a frying pan and add the shallots and garlic. Gently cook for 5 mins until golden. Add the chillies and sage to the pan and gently cook for 2 mins. Drain the pasta, mix with the shallot mixture in the pan, then scatter with Parmesan, if you like.',
                                                                                   'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6247, 85, 2.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2821, 85, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 85, 1.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3236, 85, 5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2915, 85, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3230, 85, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (254, 85, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (32, 85, 0.2);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (86, 'Spaghetti with artichokes', 20, 'Cook the spaghetti following pack instructions. Meanwhile, in a small bowl or jug, beat the eggs together with the milk, Parmesan, artichokes and seasoning.&#10;Heat a small pan on a medium heat and add the pine nuts. Toast for a few mins, shaking the pan occasionally, until they are pale golden. Keep your eye on them as they can catch and burn quickly.&#10;Once the pasta is cooked, drain in a colander and tip back into the pan. Put the pan onto a low heat and pour over the egg mixture, tossing together to coat all of the pasta in the sauce – take care not to heat it for too long or it will start to scramble.&#10;Toss through the toasted pine nuts and serve with extra Parmesan at the table, if you like.',
                                                                                   'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6247, 86, 2.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (112, 86, 0.25);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (81, 86, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (33, 86, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2773, 86, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3584, 86, 0.3);


INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (87, 'Pizza pasta salad', 25, 'Cook the pasta following pack instructions. Drain, rinse under cold water to cool, then drain well. Toss with the oil and sundried tomato pesto, season to taste. Place in a jar or plastic box and scatter over the remaining ingredients in layers, ending with the basil.',
                                                                                   'D', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6227, 87, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 87, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3472, 87, 0.1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3156, 87, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (7778, 87, 0.3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2273, 87, 3);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (1736, 87, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (259, 87, 1);

INSERT INTO PUBLIC.RECIPE (ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES (88, 'So-simple spaghetti Bolognese', 30, 'Heat the oil in non-stick frying pan. Tip in the carrot and cook for 5 mins to soften. Scoop out the tomatoes from the can and add to the pan, and cook for 5 mins more. Pour over the tomato juice and basil, then simmer for 15 mins. Whizz together in a blender until smooth. This sauce can be frozen for up to 3 months.&#10;Heat through with the Multi mince. Cook spaghetti according to pack instructions. Reserve some of the cooking water, drain and tip the pasta into the pan along with the sauce. Toss together, thinning with pasta water, if needed, and serve with the extra basil leaves on top and the breadcrumbs.',
                                                                                   'L', false);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (604, 88, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2848, 88, 0.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (3156, 88, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (259, 88, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (6247, 88, 2.5);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (5293, 88, 1);
INSERT INTO PUBLIC.RECIPE_INGREDIENT (INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES (2622, 88, 4);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES
(99921, 'Broccoli baked potatoes', 40.0, STRINGDECODE('Heat oven to 200C/180C fan/gas 6. Microwave the potatoes on High for 12-15 mins until tender. Meanwhile, steam or boil the broccoli for 3 mins, then drain well. When the potatoes have cooled a little, cut them in half lengthways and scoop the insides into a bowl.\n\nPut the potato shells on a baking sheet. Mash the flesh with a fork, then stir in the mustard, egg, most of the cheese and the broccoli. Season if needed, then pile back into the shells. Sprinkle with the reserved cheese and bake for 15 mins until the tops are crisp and golden. Serve with salad and Tomato relish.'), 6, FALSE),
(99922, 'Easy cheesy frittata', 45.0, STRINGDECODE('Ask a grown-up helper to switch the oven on to 180C/ 160C fan/gas 4. Snip or cut up the spring onions and put them in a bowl. Add the peas to the bowl.\n\nGrate the courgette using your rotary grater, one half at a time, then add it to the bowl.\n\nCut the ham into pieces with your scissors, if you do this over the bowl it will fall straight in.\n\nBreak the feta into the bowl by crumbling it with your hands.\n\nCrack the eggs into a bowl and, if any bits of shell fall in, scoop them out with a spoon. Whisk the eggs until the yolks are mixed into the white.\n\nPour the eggs into the other bowl and stir. Brush a round ovenproof dish, about 16cm across, with oil. Tip everything into the dish. Ask a helper to put the dish in the oven for 30 minutes or until the egg is set. Serve with salad and crusty bread.'), 3, FALSE),
(99923, 'Spicy sausage & bean one-pot', 25.0, STRINGDECODE('Heat the oil in a large frying pan. Cook the onion and sausages over a fairly high heat for 8-10 mins, turning the sausages often so they brown all over.\n\nAdd the garlic to the pan with the kidney beans and their sauce. Half-fill one of the cans with water, swirl and then add this to the pan. Stir everything together and bring to the boil. Turn down to simmer and cook for 10 mins, or until the sausages are cooked through. Season and sprinkle with the parsley.'), 6, FALSE),
(99924, 'Easy risotto with bacon & peas', 50.0, STRINGDECODE('Finely chop the onion. Heat 2 tablespoons of olive oil and a knob of butter in a pan, add the onions and fry until lightly browned (about 7 minutes). Add the bacon and fry for a further 5 minutes, until it starts to crisp.\n\nAdd the rice and stock, and bring to the boil. Stir well, then reduce the heat and cook, covered, for 15-20 minutes until the rice is almost tender.\n\nStir in the peas, add a little salt and pepper and cook for a further 3 minutes, until the peas are cooked. Serve sprinkled with freshly grated parmesan and freshly ground black pepper.'), 6, FALSE),
(99925, 'Smoked salmon & pea frittata', 50.0, STRINGDECODE('Thickly slice the potatoes and cook in a pan of boiling salted water until just tender, about 10 minutes. Drain well and leave to cool slightly.\n\nCut the salmon into wide strips. Crack the eggs into a bowl, beat with a fork until lightly foamy, then stir in the smoked salmon, dill, peas and plenty of salt and pepper. Finally, stir in the potatoes.\n\nHeat 3 tablespoons of olive oil in a large non-stick frying pan, carefully pour in the egg mixture and cook over a fairly low heat for 10-15 minutes, until the egg is starting to set just under the surface.\n\nPut a plate that is slightly larger than the top of the pan on top and invert the frittata onto it. Slide it back into the pan and cook for a further 5 minutes to brown the underside. Slide on to a plate and leave to cool for 5 minutes before cutting into wedges. A tomato and chive salad tastes very fresh with this.'), 6, FALSE),
(99926, 'Muddled potato & mozzarella tortilla', 30.0, STRINGDECODE('Start by frying: Heat the oil in a large frying pan. Empty the potatoes into the pan, spread them out to cover the base, then fry for 5 minutes. Pour in the beaten eggs so they completely cover the potatoes, season well and leave the tortilla to cook on a medium heat for about 15-20 minutes, or until the base and edges have set.\n\nGrill to finish: Take the tortilla off the hob and place under a hot grill until the top is firm, then remove from the grill and scatter over the tomatoes and mozzarella. Put the tortilla back under the grill for a further 3-5 minutes, or until the tomatoes are soft and the cheese has melted. To serve, cut into thick wedges.'), 6, FALSE);
INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES
(99927, 'Creamy tomato courgetti', 30.0, STRINGDECODE('Heat oven to 200C/fan180C/gas 6. Cook the leeks in a pan of boiling salted water for 4-5 mins or until just tender. Drain and cool under a cold tap to stop them from cooking any further, then drain again well and pat dry on kitchen paper.\n\nWrap each leek in a slice of ham, then arrange, side-by-side, in a large baking dish. Mix the cheddar in a bowl with the Dijon mustard and cr\u00e8me fra\u00eeche, until well combined. Season to taste. Spread over the leeks, then bake for 15-20 mins until bubbling and golden brown. Serve at once with plenty of crusty bread to mop up the juices.'), 6, FALSE),
(99928, 'Cheesy mushroom omelette', 15.0, STRINGDECODE('Heat the olive oil in a small non-stick frying pan. Tip in the mushrooms and fry over a high heat, stirring occasionally for 2-3 mins until golden. Lift out of the pan into a bowl and mix with the cheese and parsley.\n\nPlace the pan back on the heat and swirl the eggs into it. Cook for 1 min or until set to your liking, swirling with a fork now and again.\n\nSpoon the mushroom mix over one half of the omelette. Using a spatula or palette knife, flip the omelette over to cover the mushrooms. Cook for a few moments more, lift onto a plate and serve with oven chips and salad.'), 6, FALSE),
(99929, 'Sweet potatoes with mushrooms & rosemary', 30.0, STRINGDECODE('Heat oven to 200C/fan 180C/gas 6. Prick the potatoes several times with a fork, then microwave on High for 8-10 mins, turning once, until tender. Meanwhile, heat the oil in a non-stick pan, add the mushrooms and rosemary and cook over a fairly high heat, stirring, until the mushrooms are tender and lightly coloured. Season to taste.\n\nPut the potatoes in the oven and roast for 15 mins until the skins start to crisp. Split open and spoon over the mushrooms. Sprinkle with Parmesan to serve.'), 6, FALSE),
(99930, 'Cheese & chilli melts', 21.0, STRINGDECODE('Put the cheese, tomatoes, chilli and coriander leaves into a bowl with some seasoning, then mix well.\n\nWarm tortillas in the microwave according to pack instructions \u2013 this makes them more bendable. Divide the cheese mix over one half of each tortilla. Fold over the other half to make 8 half-moons, then press down to seal.\n\nBrush the tops with a little oil, then sit, oil-side down, on a hot area of the barbecue. Cook for a couple of mins until crisp and golden, brush the uncooked side with oil, then flip over for another few mins. Slice into wedges, then pile onto a platter while you cook the rest.'), 6, FALSE);

INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES
(121, 99921, 0.25),
(261, 99921, 0.25),
(3366, 99921, 1.0),
(9, 99921, 0.1479363486952715),
(2821, 99921, 0.3170064614898675),
(2986, 99922, 0.5),
(2996, 99922, 0.0625),
(1578, 99922, 0.5),
(19, 99922, 0.1056688204966225),
(119, 99922, 1.0),
(739, 99923, 0.25),
(2986, 99923, 0.25),
(1632, 99923, 2.0),
(2915, 99923, 1.0),
(2989, 99923, 1.0),
(4562, 99923, 0.84535056397298),
(2986, 99924, 0.25),
(2767, 99924, 1.5),
(6187, 99924, 0.3170064614898675),
(3002, 99924, 0.1056688204966225),
(3062, 99925, 0.5283441024831125),
(119, 99925, 2.0),
(260, 99925, 0.03125),
(7628, 99925, 1.0),
(604, 99926, 0.33333333),
(3044, 99926, 0.5621581250420318),
(3156, 99926, 0.422675282),
(26, 99926, 0.1056688204966225),
(119, 99926, 2.0),
(3326, 99927, 2.0),
(1579, 99927, 4.0),
(148, 99927, 0.1056688204966225),
(240, 99927, 0.5),
(604, 99928, 1.0),
(2954, 99928, 1.0),
(9, 99928, 0.1056688204966225),
(2989, 99928, 1.0),
(116, 99928, 2.0),
(3363, 99929, 1.0),
(604, 99929, 0.5),
(2954, 99929, 0.42267528198649),
(271, 99929, 0.5),
(32, 99929, 0.0625),
(9, 99930, 0.13208602562077812),
(3240, 99930, 0.3170064614898675),
(6722, 99930, 0.5),
(2879, 99930, 0.2),
(5702, 99930, 1.0);

INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES
(99821, 'Slow cooker chicken', 300.0, STRINGDECODE('Heat the slow cooker if necessary and add a splash of water to the base. Scrunch up some foil to make a trivet to sit in the base of the bowl to rest the chicken on. Put the chicken into the pot and season the skin. Cover and cook on Low for 5 hours or until the leg or wing feels very loose when you wiggle it. Tip the juices inside the chicken out as you lift it out.\n\nBrown the chicken skin under the grill or carve the chicken before anyone sees it. Spoon the liquid out of the base of the pan to use as gravy, there won''t be much but it will have a good flavour.'), 5, TRUE),
(99822, 'Nutty chicken satay strips', 10.0, STRINGDECODE('Heat oven to 200C, 180C fan, gas 4 and line a baking tray with non-stick paper.\n\nMix the peanut butter with the garlic, curry powder, soy sauce and lime in a bowl. Some nut butters are thicker than others, so if necessary, add a dash of boiling water to get a coating consistency. Add the chicken strips, mix well then arrange on the baking sheet, spaced apart, and bake in the oven for 8-10 mins until cooked, but still juicy.\n\nEat warm with the cucumber sticks and chilli sauce, or leave to cool then keep in the fridge for up to 2 days.'), 5, FALSE),
(99823, 'Bacon & roast onion salad', 5.0, STRINGDECODE('Heat oven to 220C/200C fan/gas 7. Arrange the onion wedges on one side of a baking tray. Drizzle with \u00bd tbsp of the olive oil and season. Put in the oven and roast for 15 mins.\n\nMeanwhile, cook the peas in boiling water for 2 mins, drain and rinse in very cold water. Set aside.\n\nMake the dressing by mixing together another \u00bd tbsp of the oil, the vinegar, mustard and seasoning. Turn the onions, and put the bacon slices and bread next to them on the baking tray. Drizzle the remaining oil over the bread. Return the tray to the oven for 12 mins more, until the bacon and bread are golden.\n\nPut the lettuce and peas in a bowl, add the dressing and toss to combine. Arrange the onion and bread on top. Break up the bacon slightly and scatter over. Drizzle with a little more olive oil, if you like, and eat straight away.'), 5, FALSE),
(99824, 'Mexican chicken burger', 10.0, STRINGDECODE('Put the chicken breast between two pieces of cling film and bash with a rolling pin or pan to about 1cm thick. Mix the chipotle paste with half the lime juice and spread over the chicken.\n\nHeat a griddle pan over a high heat. Once hot, cook the chicken for 3 mins each side until cooked through, adding the cheese for the final 2 mins of cooking. Add the bun, cut-side down, to the griddle pan to toast lightly. Season the chicken.\n\nMeanwhile, mash the avocado with the remaining lime juice. Stir in the cherry tomatoes, jalape\u00f1o and garlic, and season with a little salt. Spread over the base of the bun, then add the chicken followed by the top of the bun.'), 2, FALSE),
(99825, 'Chicken schnitzel Caesar', 30.0, STRINGDECODE('Place a chicken breast between two sheets of cling film. Using a rolling pin or meat tenderiser, bash it gently until about 1cm thick and evenly flattened. Put the flour, eggs and breadcrumbs in three shallow bowls. Season the flour and mix well. Dip one of the chicken breasts into the flour, then the egg, making sure it\u2019s fully coated, then finally into the breadcrumbs. Set aside, then repeat with the other chicken breast.\n\nTo make the Caesar dressing, tip the egg yolk, mustard, vinegar and garlic into a mixing bowl. Whisk together, then slowly drizzle in the oil, whisking constantly, until you have a loose mayonnaise. Fold in the anchovies and grated parmesan, give it a good stir, then set aside.\n\nHeat the oil and butter in a frying pan, add the chicken and fry for 3-4 mins until golden. Turn over and cook for a further 3 mins, then remove from the pan. Tip the lemon juice into the pan, sizzle, then spoon over the schnitzels. Leave to rest for 4 mins.'), 5, FALSE),
(99826, 'Roast beef with red wine & banana shallots', 30.0, STRINGDECODE('Let the meat come to room temperature for 1 hr before you roast it. Heat oven to 220C/200C fan/gas 7. Dry the meat with kitchen paper, then rub the oil all over it. Mix the mustard powder with the sea salt and 1 tsp pepper, then rub this over the meat too. Lay the carrots in a large roasting tin to make a trivet and sit the beef on top, fat-side up.\n\nRoast for 15 mins, then turn the heat down to 180C/160C fan/gas 4 and roast for another 55 mins. This will give you medium beef; for medium-rare, cook for 45 mins. (When it\u2019s done, a probe thermometer inserted into the thickest part of the beef should read 65C).\n\nWith 30 mins cooking time left, baste the beef in the fat that has pooled in the tin, add the shallots and bay leaves, and coat these in the fat too. Season and return to the oven. Meanwhile, boil the wine in a small saucepan to reduce it by two-thirds.\n\nWhen the meat is ready, transfer to a board, cover loosely with foil and leave to rest. Turn the oven up to 200C/180C fan/gas 6 and return the tin to the oven for a further 10-15 mins until the shallots are tender.\n\nScoop out the shallots with a slotted spoon and set aside to keep warm. Remove the excess fat from the tin and discard, leaving 1 tbsp fat and all the dark juices behind. Add the flour and cook on the hob for 2 mins, stirring constantly. Gradually whisk in the reduced wine, followed by the stock and redcurrant jelly, scraping up all the tasty bits from the bottom of the tin. Bubble until thickened and rich, add the juices from the resting meat, then season to taste. Discard the carrots. Serve the beef with the gravy and shallots.'), 2, FALSE);
INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES
(99827, 'Steak with mushroom puff tartlets', 20.0, STRINGDECODE('Heat oven to 200C/fan 180C/gas 6. Roll out the pastry to about the thickness of a \u00a31 coin, then cut out 2 x 12cm circles using a cutter, or by scoring around a saucer. Score a circle 2cm in from the edge, then prick the pastry inside the border. Lift onto a baking tray, then bake for 20-25 mins or until golden and puffed. Press the risen middles down a little, ready for the filling. Can be made a day ahead, then reheated in a hot oven.\n\nHeat the oil in a pan, add the shallots, then fry until softened. Add the mushrooms and thyme, then fry until mushrooms are softened and any liquid almost gone. Add the port or Madeira, then bubble for 2 mins. Add the cream, simmer for 1 min more until the sauce is slightly thickened, then set aside.\n\nRub the steaks with a little oil and seasoning. Heat a griddle pan until hot, then cook the steaks for 2-3 mins on each side (depending on their thickness) for medium rare, a little more if you like your steaks well done. Cover the steaks with foil, then rest them for 10 mins.\n\nReheat the tartlets. Warm the mushroom mixture over a low heat. Set the tartlets on warm plates, then spoon over the mushroom mixture. Sit the fillet steaks on top with a sprig of thyme. Serve with Balsamic spinach and Oven saut\u00e9 potatoes (see below).'), 2, FALSE),
(99828, 'BBQ pulled pork sandwich', 25.0, STRINGDECODE('Heat oven to 160C/140C fan/gas 3. Scatter the onions and bay leaves in the bottom of a large roasting tin. Mix the mustard powder, paprika and 1 tsp ground black pepper with a good pinch of salt. Rub this all over the pork, making sure you rub it into all the crevices. Place the pork, rind-side up, on top of the onions. Pour 200ml water into the bottom of the tin, wrap well with foil and bake for 4 hrs. This can be done up to 2 days ahead \u2013 simply cover the tray in foil and chill until ready to barbecue.\n\n\nLight the barbecue. In a bowl, mix the ketchup, vinegar, Worcestershire sauce and brown sugar. Remove the pork from the tin and pat dry. Place the roasting tin on the hob, pour in the ketchup mixture and bubble vigorously for 10-15 mins until thick and glossy. Remove the bay leaves and pour the sauce into a food processor; blitz until smooth. Smear half the sauce mixture over the meat.\n\nOnce the barbecue flames have died down, put on the pork, skin-side down. Cook for 15 mins until nicely charred, then flip over and cook for another 10 mins. The meat will be very tender, so be careful not to lose any between the bars. Alternatively, heat a combined oven-grill to high, place the pork on a baking tray and cook each side for 10-15 mins until nicely charred.\n\nLift the pork onto a large plate or tray. Remove string and peel off the skin. Using 2 forks, shred the meat into chunky pieces. Add 3-4 tbsp of the barbecue sauce to the meat and toss everything well to coat. Pile into rolls and serve with extra sauce and a little coleslaw.'), 5, FALSE),
(99829, 'Slow-roast pork with apples & peppers', 20.0, STRINGDECODE('Heat oven to 160C/140C fan/gas 3. Sit the pork on a sheet of foil in a roasting tin. Mix the butter, sugar and mustard with 2 tsp salt, rub all over the top and ends of the joint (or joints), then scrunch up the foil to seal tightly in a parcel.\n\nMix the onions and apples in a large, shallow roasting tin. Scatter the peppers over the top and poke in some bay leaves. Mix the vinegar, oil, mustard and sugar with 100ml water and drizzle over everything. Roast the pork for 3\u00bd hrs while you cook the peppers on the shelf below for the first 2 hrs. When you remove the apples and peppers after 2 hrs, set aside and put the Salt-baked potatoes (see ''Goes well with'', right) into the oven underneath the pork instead.\n\nAfter 3 hrs, unwrap the foil from the pork. Scrape any mustard mixture stuck to the foil back onto the pork, sit it and any juices back in the tin and increase oven to 200C/180C fan/gas 6. Roast for 30-45 mins more until the skin is brown and crisping. Remove the pork from the oven, cover and rest for 20 mins while you put the peppers back on the top shelf for 20 mins to finish. Serve the pork thickly sliced alongside the apples and peppers, with any tin juices poured over.'), 5, FALSE);
INSERT INTO PUBLIC.RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED) VALUES
(99830, 'Pork meatballs in red pepper sauce', 15.0, STRINGDECODE('Put the pork mince in a bowl and stir in the apple, garlic, breadcrumbs and some salt and pepper. Shape the mixture into 16 balls, cover and chill for 10 mins.\n\nMeanwhile, make the sauce. Heat the oil in a medium saucepan and add the onion. Cook for 2-3 mins, until softened, then tip in the tomatoes and half a can of water. Stir in the peppers along with some salt and pepper. Partially cover and simmer for 15 mins.\n\nHeat the oil in a large non-stick frying pan and add the meatballs. Cook for 5-6 mins, stirring occasionally until they are browned all over. Set aside and keep warm.\n\nUsing a hand-held blender, whizz the tomato sauce until smooth. Carefully add the meatballs to the sauce and simmer for 5 mins, until cooked through. Meanwhile, cook the spaghetti following pack instructions, drain, then divide between serving plates. Top with the meatballs and sauce.'), 5, FALSE),
(99831, 'Italian pork patties with potato wedges', 15.0, STRINGDECODE('Heat oven to 200C/180C fan/gas 6. Toss potato wedges in a large roasting tin with 1 tbsp oil and lemon juice. Spread out in a single layer. Bake for 35-45 mins, turning halfway, until golden brown and crisp.\n\neanwhile, place the breadcrumbs in a mixing bowl and moisten with 2 tbsp cold water. Add the mince, Parmesan, parsley, garlic and lemon zest. Season, mix well, then shape into 4 large, flat patties.\n\nHeat remaining oil in a pan and cook the patties for 7 mins on each side, or until they have a golden crust and are cooked through (alternatively, cook on the barbecue). Serve with the wedges and a tomato and rocket salad, if you like'), 5, FALSE);

INSERT INTO PUBLIC.RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT) VALUES
(1570, 99821, 1.0),
(7775, 99822, 2.0),
(2915, 99822, 1.0),
(4649, 99822, 2.0),
(2253, 99822, 0.1),
(1004, 99822, 2.0),
(2906, 99822, 3.0),
(2975, 99823, 1.0),
(2996, 99823, 2.0),
(268, 99823, 0.1),
(261, 99823, 1.0),
(2767, 99823, 2.0),
(5281, 99823, 0.5),
(2948, 99823, 2.0),
(1768, 99824, 1.0),
(2253, 99824, 1.0),
(22, 99824, 0.25),
(2153, 99824, 2.0),
(3472, 99824, 1.0),
(2915, 99824, 0.5),
(5279, 99824, 1.0),
(263, 99825, 0.1),
(2915, 99825, 1.0),
(2248, 99825, 1.0),
(1004, 99825, 2.0),
(121, 99825, 1.0),
(2963, 99825, 0.1),
(131, 99825, 0.1),
(2948, 99825, 2.0),
(604, 99825, 3.0),
(132, 99825, 5.0),
(7182, 99826, 2.0),
(627, 99826, 1.0),
(2963, 99826, 1.0),
(262, 99826, 0.1),
(2848, 99826, 1.0),
(2155, 99826, 3.0),
(4227, 99826, 0.25),
(2975, 99828, 2.0),
(2504, 99828, 1.0),
(7827, 99828, 2.0),
(275, 99828, 1.0),
(5991, 99828, 0.25),
(5510, 99827, 5.0),
(604, 99827, 1.0),
(3236, 99827, 5.0),
(2954, 99827, 2.0),
(264, 99827, 1.0),
(2504, 99827, 1.0),
(2504, 99829, 1.0),
(5995, 99829, 3.0),
(2963, 99829, 3.0),
(2975, 99829, 3.0),
(3468, 99829, 3.0),
(263, 99829, 0.1),
(5991, 99829, 0.1),
(1, 99829, 0.25),
(2437, 99830, 1.0),
(2118, 99830, 2.0),
(2915, 99830, 0.5),
(604, 99830, 1.0),
(6253, 99830, 2.0),
(2975, 99830, 1.0),
(3156, 99830, 1.0),
(3444, 99830, 3.0),
(3032, 99831, 1.0),
(604, 99831, 2.0),
(2248, 99831, 0.1),
(2436, 99831, 2.0),
(32, 99831, 1.0),
(2989, 99831, 0.1),
(2915, 99831, 1.0);
