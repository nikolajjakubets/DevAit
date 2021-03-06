/*
 * Copyright (C) 2004-2013 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.itemhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.L2Playable;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;

/**
 * Updated: RobikBobik L2PS Team
 */
public class ManaPotion extends ItemSkills
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!Config.ENABLE_MANA_POTIONS_SUPPORT)
		{
			playable.sendPacket(SystemMessageId.NOTHING_HAPPENED);
			return false;
		}
		if ((!Config.ENABLE_MANA_POTIONS_IN_PVP) && (playable.getPvpFlag() > 0))
		{
			CreatureSay msg3 = new CreatureSay(2, Say2.BATTLEFIELD, "[RESTRICTION]", "You cant use mana potions in PvP.");
			playable.sendPacket(msg3);
			return false;
		}
		return super.useItem(playable, item, forceUse);
	}
}