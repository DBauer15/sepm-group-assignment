-- CLEAN DATABASE, except external ingredient data

DELETE FROM diet_plan;
DELETE FROM diet_plan_suggestion;
DELETE FROM recipe;
DELETE FROM recipe_image;
DELETE FROM recipe_ingredient;
DELETE FROM ingredient WHERE user_specific=TRUE;

-- INSERT DEFAULT DATA

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