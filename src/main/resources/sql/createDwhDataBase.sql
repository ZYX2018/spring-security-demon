CREATE TABLE `dwh`.`user` (
                              `user_id` INT NOT NULL,
                              `name` VARCHAR(45) NULL,
                              `sex` VARCHAR(45) NULL,
                              `password` VARCHAR(45) NULL,
                              `create_time` DATE NULL,
                              `update_time` DATE NULL,
                              PRIMARY KEY (`user_id`));
