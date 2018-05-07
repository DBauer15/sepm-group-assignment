
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
  PRIMARY KEY (`id`))



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
    ON UPDATE NO ACTION)



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
    ON UPDATE NO ACTION)



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
  PRIMARY KEY (`id`))



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
    ON UPDATE NO ACTION)

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
  PRIMARY KEY (`id`))
