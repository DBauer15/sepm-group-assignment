
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
  PRIMARY KEY (`recipe`, `date`),
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
VALUES (1, 'Build Muscle', 2500, 20.0, 25.0, 50.0, NULL, NULL);

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
VALUES(6174,12, 1); /* Oats */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(3580,12, 0.25); /* Pecan */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(3527,12, 0.125); /* Sunflower seeds */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2408,12, 1.5); /* Medjool dates */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6146,12, 1.5); /* Wheat */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(7880,12, 1); /* Yogurt */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(2186,12, 1); /* Cranberries */

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
VALUES(706,16, 10); /* Sunflower oil*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(152,16, 6); /* Sour cream*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(111,16, 0.5); /* Eggs */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(265, 16, 0.00625); /* Vanilla */

INSERT INTO RECIPE(ID, NAME, DURATION, DESCRIPTION, TAGS, DELETED)
VALUES(17,'Double chocolate loaf cake', 80,'Heat oven to 160C/140C fan/gas 3. Grease and line a 2lb/900g loaf tin with a long strip of baking parchment. To make the loaf cake batter, beat the butter and sugar with an electric whisk until light and fluffy. Beat in the eggs, flour, almonds, baking powder, milk and cocoa until smooth. Stir in the chocolate chips, then scrape into the tin. Bake for 45-50 mins until golden, risen and a skewer poked in the centre comes out clean.&#10;Cool in the tin, then lift out onto a wire rack over some kitchen paper. Melt the extra chocolate chunks separately in pans over barely simmering water, or in bowls in the microwave, then use a spoon to drizzle each in turn over the cake. Leave to set before slicing.','BD','false');
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(131, 17, 0.5); /* Butter */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6135,17, 7); /* Sugar*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(111,17, 0.5); /* Eggs */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6211,17, 0.75); /* Flour*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(3536, 17, 0.25); /* Almonds*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(5539, 17, 0.5); /* Baking Powder*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(70,17, 1); /* Milk */
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(5874,17, 0.125); /* Cocoa*/
INSERT INTO RECIPE_INGREDIENT(INGREDIENT_ID, RECIPE_ID, AMOUNT)
VALUES(6130,17, 1); /* Chocolate*/

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