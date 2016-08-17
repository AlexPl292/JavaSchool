CREATE TABLE IF NOT EXISTS `eCare`.`Tariffs` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `cost` DECIMAL(8,2) NULL,
  `description` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));


CREATE USER 'ecare_user'@'localhost' IDENTIFIED BY 'keinerverstehtdeutsch';
GRANT ALL PRIVILEGES ON * . * TO 'ecare_user'@'localhost';
FLUSH PRIVILEGES;

-- -----------------------------------------------------
-- Table `eCare`.`Options`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `eCare`.`Options` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `cost` DECIMAL(8,2) NULL,
  `connect_cost` DECIMAL(8,2) NULL,
  `description` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `eCare`.`Possible_options_of_tariffs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `eCare`.`Possible_options_of_tariffs` (
  `tariff_id` INT NOT NULL,
  `option_id` INT NOT NULL,
  CONSTRAINT `fk_Possible_options_of_tariffs_1`
  FOREIGN KEY (`tariff_id`)
  REFERENCES `eCare`.`Tariffs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Possible_options_of_tariffs_2`
  FOREIGN KEY (`option_id`)
  REFERENCES `eCare`.`Options` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `eCare`.`Customers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `eCare`.`Customers` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  `date_of_birth` DATE NULL,
  `passport_data` VARCHAR(255) NULL,
  `address` VARCHAR(255) NULL,
  `email` VARCHAR(45) NULL,
  `password` INT NULL,
  `is_blocked` TINYINT(1) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `eCare`.`Contracts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `eCare`.`Contracts` (
  `id` INT NOT NULL,
  `number` VARCHAR(15) NULL,
  `tariff` INT NULL,
  `customer` INT NULL,
  `is_blocked` TINYINT(1) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `number_UNIQUE` (`number` ASC),
  INDEX `fk_Contacts_1_idx` (`tariff` ASC),
  INDEX `fk_Contacts_2_idx` (`customer` ASC),
  CONSTRAINT `fk_Contacts_1`
  FOREIGN KEY (`tariff`)
  REFERENCES `eCare`.`Tariffs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Contacts_2`
  FOREIGN KEY (`customer`)
  REFERENCES `eCare`.`Customers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `eCare`.`Used_options_of_tariff`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `eCare`.`Used_options_of_tariff` (
  `contract_id` INT NOT NULL,
  `option_id` INT NOT NULL,
  INDEX `fk_Used_options_of_tariffs_1_idx` (`option_id` ASC),
  INDEX `fk_Used_options_of_tariffs_2_idx` (`contract_id` ASC),
  CONSTRAINT `fk_Used_options_of_tariffs_1`
  FOREIGN KEY (`option_id`)
  REFERENCES `eCare`.`Options` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Used_options_of_tariffs_2`
  FOREIGN KEY (`contract_id`)
  REFERENCES `eCare`.`Contracts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `eCare`.`Staff`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `eCare`.`Staff` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `eCare`.`Required_option_relationships`
-- -----------------------------------------------------;
CREATE TABLE IF NOT EXISTS `eCare`.`Required_option_relationships` (
  `id_first` INT NOT NULL,
  `id_second` INT NOT NULL,
  INDEX `fk_Required_option_relationships_1_idx` (`id_first` ASC),
  INDEX `fk_Required_option_relationships_2_idx` (`id_second` ASC),
  CONSTRAINT `fk_Required_option_relationships_1`
  FOREIGN KEY (`id_first`)
  REFERENCES `eCare`.`Options` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Required_option_relationships_2`
  FOREIGN KEY (`id_second`)
  REFERENCES `eCare`.`Options` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `eCare`.`Forbidden_option_relationships`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `eCare`.`Forbidden_option_relationships` (
  `id_first` INT NOT NULL,
  `id_second` INT NOT NULL,
  INDEX `fk_Forbidden_option_relationships_1_idx` (`id_first` ASC),
  INDEX `fk_Forbidden_option_relationships_2_idx` (`id_second` ASC),
  CONSTRAINT `fk_Forbidden_option_relationships_1`
  FOREIGN KEY (`id_first`)
  REFERENCES `eCare`.`Options` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Forbidden_option_relationships_2`
  FOREIGN KEY (`id_second`)
  REFERENCES `eCare`.`Options` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
