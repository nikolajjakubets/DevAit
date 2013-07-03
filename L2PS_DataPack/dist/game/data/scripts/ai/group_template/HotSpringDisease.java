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
 * this program.
 */
package ai.group_template;

import ai.npc.AbstractNpcAI;

import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.util.Util;
import com.l2jserver.util.Rnd;

/**
 * Author: RobikBobik Thanks to Lord W. for help
 */
public class HotSpringDisease extends AbstractNpcAI
{
	static final int[] disease1mobs =
	{
		21314,
		21316,
		21317,
		21319,
		21321,
		21322
	};
	static final int[] disease2mobs =
	{
		21317,
		21322
	};
	static final int[] disease3mobs =
	{
		21316,
		21319
	};
	static final int[] disease4mobs =
	{
		21314,
		21321
	};
	
	// Chance to get infected by disease
	private static final int DISEASE_CHANCE = 5;
	
	public HotSpringDisease(String name, String descr)
	{
		super(name, descr);
		
		registerMobs(disease1mobs);
		registerMobs(disease2mobs);
		registerMobs(disease3mobs);
		registerMobs(disease4mobs);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon)
	{
		if (Util.contains(disease1mobs, npc.getNpcId()))
		{
			if (Rnd.get(100) < DISEASE_CHANCE)
			{
				npc.setTarget(attacker);
				npc.doCast(SkillTable.getInstance().getInfo(4554, Rnd.get(10) + 1));
			}
		}
		if (Util.contains(disease2mobs, npc.getNpcId()))
		{
			if (Rnd.get(100) < DISEASE_CHANCE)
			{
				npc.setTarget(attacker);
				npc.doCast(SkillTable.getInstance().getInfo(4553, Rnd.get(10) + 1));
			}
		}
		if (Util.contains(disease3mobs, npc.getNpcId()))
		{
			if (Rnd.get(100) < DISEASE_CHANCE)
			{
				npc.setTarget(attacker);
				npc.doCast(SkillTable.getInstance().getInfo(4552, Rnd.get(10) + 1));
			}
		}
		if (Util.contains(disease4mobs, npc.getNpcId()))
		{
			if (Rnd.get(100) < DISEASE_CHANCE)
			{
				npc.setTarget(attacker);
				npc.doCast(SkillTable.getInstance().getInfo(4551, Rnd.get(10) + 1));
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	public static void main(String[] args)
	{
		new HotSpringDisease(HotSpringDisease.class.getSimpleName(), "ai/group_template");
	}
}