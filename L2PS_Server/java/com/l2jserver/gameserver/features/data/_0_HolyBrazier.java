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
 * this program. If not, see <http://com.l2jserver.ru/>.
 */
package com.l2jserver.gameserver.features.data;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class _0_HolyBrazier extends AbstractNpcAI
{
	
	private static final int HolyBrazier = 32027;
	private static final int GuardianOfTheGrail = 22133;
	
	private L2Npc _guard = null;
	private L2Npc _brazier = null;
	
	public _0_HolyBrazier(int questId, String name, String descr)
	{
		super(name, descr);
		int[] mobs =
		{
			HolyBrazier,
			GuardianOfTheGrail
		};
		registerMobs(mobs);
	}
	
	private void spawnGuard(L2Npc npc)
	{
		if ((_guard == null) && (_brazier != null))
		{
			_guard = addSpawn(GuardianOfTheGrail, _brazier.getX(), _brazier.getY(), _brazier.getZ(), 0, false, 0);
			_guard.setIsNoRndWalk(true);
		}
		return;
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		if (npc.getNpcId() == HolyBrazier)
		{
			_brazier = npc;
			_guard = null;
			npc.setIsNoRndWalk(true);
			spawnGuard(npc);
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		if ((npc.getNpcId() == GuardianOfTheGrail) && !npc.isInCombat() && (npc.getTarget() == null))
		{
			npc.setIsNoRndWalk(true);
		}
		return super.onAggroRangeEnter(npc, player, isPet);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		if (npc.getNpcId() == GuardianOfTheGrail)
		{
			_guard = null;
			spawnGuard(npc);
		}
		else if (npc.getNpcId() == HolyBrazier)
		{
			if (_guard != null)
			{
				_guard.deleteMe();
				_guard = null;
				
			}
			_brazier = null;
		}
		return super.onKill(npc, killer, isPet);
	}
	
	public static void main(String[] args)
	{
		new _0_HolyBrazier(-1, "HolyBrazier", "data");
	}
}