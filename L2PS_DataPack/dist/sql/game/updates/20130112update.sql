ALTER TABLE `spawnlist` ADD `respawn_random` mediumint(5) NOT NULL DEFAULT '0' AFTER `respawn_delay`;
ALTER TABLE `custom_spawnlist` ADD `respawn_random` mediumint(5) NOT NULL DEFAULT '0' AFTER `respawn_delay`;