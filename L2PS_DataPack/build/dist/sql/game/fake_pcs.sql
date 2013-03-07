-- ----------------------------
-- Table structure for `fake_pcs`
-- ----------------------------
DROP TABLE IF EXISTS `fake_pcs`;
CREATE TABLE `fake_pcs` (
  `npc_id` int(11) NOT NULL,
  `name` varchar(35) NOT NULL,
  `race` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `sex` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `class` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `pd_under` int(11) NOT NULL DEFAULT '0',
  `pd_head` int(11) NOT NULL DEFAULT '0',
  `pd_rhand` int(11) NOT NULL DEFAULT '0',
  `pd_lhand` int(11) NOT NULL DEFAULT '0',
  `pd_gloves` int(11) NOT NULL DEFAULT '0',
  `pd_chest` int(11) NOT NULL DEFAULT '0',
  `pd_legs` int(11) NOT NULL DEFAULT '0',
  `pd_feet` int(11) NOT NULL DEFAULT '0',
  `pd_cloak` int(11) NOT NULL DEFAULT '0',
  `pd_hair` int(11) NOT NULL DEFAULT '0',
  `pd_hair2` int(11) NOT NULL DEFAULT '0',
  `pd_rbracelet` int(11) NOT NULL DEFAULT '0',
  `pd_lbracelet` int(11) NOT NULL DEFAULT '0',
  `pd_deco1` int(11) NOT NULL DEFAULT '0',
  `pd_deco2` int(11) NOT NULL DEFAULT '0',
  `pd_deco3` int(11) NOT NULL DEFAULT '0',
  `pd_deco4` int(11) NOT NULL DEFAULT '0',
  `pd_deco5` int(11) NOT NULL DEFAULT '0',
  `pd_deco6` int(11) NOT NULL DEFAULT '0',
  `pd_belt` int(11) NOT NULL DEFAULT '0',
  `pd_under_aug` int(11) NOT NULL DEFAULT '0',
  `pd_head_aug` int(11) NOT NULL DEFAULT '0',
  `pd_rhand_aug` int(11) NOT NULL DEFAULT '0',
  `pd_lhand_aug` int(11) NOT NULL DEFAULT '0',
  `pd_gloves_aug` int(11) NOT NULL DEFAULT '0',
  `pd_chest_aug` int(11) NOT NULL DEFAULT '0',
  `pd_legs_aug` int(11) NOT NULL DEFAULT '0',
  `pd_feet_aug` int(11) NOT NULL DEFAULT '0',
  `pd_cloak_aug` int(11) NOT NULL DEFAULT '0',
  `pd_hair_aug` int(11) NOT NULL DEFAULT '0',
  `pd_hair2_aug` int(11) NOT NULL DEFAULT '0',
  `pd_rbracelet_aug` int(11) NOT NULL DEFAULT '0',
  `pd_lbracelet_aug` int(11) NOT NULL DEFAULT '0',
  `pd_deco1_aug` int(11) NOT NULL DEFAULT '0',
  `pd_deco2_aug` int(11) NOT NULL DEFAULT '0',
  `pd_deco3_aug` int(11) NOT NULL DEFAULT '0',
  `pd_deco4_aug` int(11) NOT NULL DEFAULT '0',
  `pd_deco5_aug` int(11) NOT NULL DEFAULT '0',
  `pd_deco6_aug` int(11) NOT NULL DEFAULT '0',
  `pd_belt_aug` int(11) NOT NULL DEFAULT '0',
  `pvp_flag` bit(1) NOT NULL DEFAULT b'0',
  `karma` int(10) unsigned NOT NULL DEFAULT '0',
  `hair_style` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `hair_color` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `face` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `title` varchar(16) NOT NULL DEFAULT '',
  `clan_id` int(11) unsigned NOT NULL DEFAULT '0',
  `clan_crest_id` int(11) unsigned NOT NULL DEFAULT '0',
  `ally_id` int(11) unsigned NOT NULL DEFAULT '0',
  `ally_crest_id` int(11) unsigned NOT NULL DEFAULT '0',
  `sit_while_idle` bit(1) NOT NULL DEFAULT b'0',
  `invisible` bit(1) NOT NULL DEFAULT b'0',
  `mount` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `recom_have` mediumint(4) unsigned NOT NULL DEFAULT '0',
  `mount_npc_id` int(11) unsigned NOT NULL DEFAULT '0',
  `team` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `clan_crest_large_id` int(11) unsigned NOT NULL DEFAULT '0',
  `hero` bit(1) NOT NULL DEFAULT b'0',
  `fishing` bit(1) NOT NULL DEFAULT b'0',
  `fishing_x` int(9) NOT NULL DEFAULT '0',
  `fishing_y` int(9) NOT NULL DEFAULT '0',
  `fishing_z` int(9) NOT NULL DEFAULT '0',
  `name_color` char(6) NOT NULL DEFAULT 'FFFFFF',
  `pledge_class` int(11) unsigned NOT NULL DEFAULT '0',
  `pledge_type` int(11) unsigned NOT NULL DEFAULT '0',
  `title_color` char(6) NOT NULL DEFAULT 'FFFFFF',
  `reputation_score` int(11) unsigned NOT NULL DEFAULT '0',
  `transform_id` int(11) unsigned NOT NULL DEFAULT '0',
  `agathion_id` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`npc_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fake_pcs
-- ----------------------------
INSERT INTO `fake_pcs` VALUES ('800000', 'L2PS', '0', '0', '0', '0', '16306', '6530', '6530', '16315', '16309', '16312', '16318', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '', '0', '0', '0', '0', '', '0', '0', '0', '0', '', '', '0', '0', '0', '0', '0', '', '', '-79430', '247922', '-3769', 'FFFFFF', '0', '0', 'FFFFFF', '0', '0', '0');
