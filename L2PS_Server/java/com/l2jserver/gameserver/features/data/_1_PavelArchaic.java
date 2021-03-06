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

import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Pavel Archaic AI.
 * @author Gnacik
 */
public class _1_PavelArchaic extends AbstractNpcAI
{
	private static final int[] MOBS1 =
	{
		22801,
		22804
	};
	
	private static final int[] MOBS2 =
	{
		18917
	};
	
	private _1_PavelArchaic(String name, String descr)
	{
		super(name, descr);
		registerMobs(MOBS1, QuestEventType.ON_KILL);
		registerMobs(MOBS2, QuestEventType.ON_ATTACK);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (!npc.isDead())
		{
			npc.doDie(attacker);
			
			if (getRandom(100) < 40)
			{
				L2Attackable golem1 = (L2Attackable) addSpawn(22801, npc.getLocation(), false, 0);
				attackPlayer(golem1, attacker);
				
				L2Attackable golem2 = (L2Attackable) addSpawn(22804, npc.getLocation(), false, 0);
				attackPlayer(golem2, attacker);
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		L2Attackable _golem = (L2Attackable) addSpawn(npc.getNpcId() + 1, npc.getLocation(), false, 0);
		attackPlayer(_golem, killer);
		return super.onKill(npc, killer, isPet);
	}
	
	public static void main(String[] args)
	{
		new _1_PavelArchaic(_1_PavelArchaic.class.getSimpleName(), "data");
	}
}
