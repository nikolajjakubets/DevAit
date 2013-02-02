/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.features.data;

import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.util.Util;

/**
 * See Through Silent Move AI.
 * @author Gigiikun
 */
public class _1_SeeThroughSilentMove extends AbstractNpcAI
{
	private static final int[] MOBIDS =
	{
		18001,
		18002,
		22199,
		22215,
		22216,
		22217,
		22327,
		22746,
		22747,
		22748,
		22749,
		22750,
		22751,
		22752,
		22753,
		22754,
		22755,
		22756,
		22757,
		22758,
		22759,
		22760,
		22761,
		22762,
		22763,
		22764,
		22765,
		22794,
		22795,
		22796,
		22797,
		22798,
		22799,
		22800,
		22843,
		22857,
		25725,
		25726,
		25727,
		29009,
		29010,
		29011,
		29012,
		29013
	};
	
	private _1_SeeThroughSilentMove(String name, String descr)
	{
		super(name, descr);
		for (L2Spawn npc : SpawnTable.getInstance().getSpawnTable())
		{
			if (Util.contains(MOBIDS, npc.getNpcid()) && (npc.getLastSpawn() != null) && npc.getLastSpawn().isL2Attackable())
			{
				((L2Attackable) npc.getLastSpawn()).setSeeThroughSilentMove(true);
			}
		}
		registerMobs(MOBIDS, QuestEventType.ON_SPAWN);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		if (npc.isL2Attackable())
		{
			((L2Attackable) npc).setSeeThroughSilentMove(true);
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new _1_SeeThroughSilentMove(_1_SeeThroughSilentMove.class.getSimpleName(), "data");
	}
}
