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
package npc.Dorian;

import npc.AbstractNpcAI;

import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.features.data.Q00024_InhabitantsOfTheForestOfTheDead;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.clientpackets.Say2;

/**
 * Dorian (Raid Fighter) - Quest AI
 * @author malyelfik
 */
public class Dorian extends AbstractNpcAI
{
	// NPC
	private static final int DORIAN = 25332;
	// Items
	private static final int SILVER_CROSS = 7153;
	private static final int BROKEN_SILVER_CROSS = 7154;
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equals("checkArea"))
		{
			if (npc.isDecayed())
			{
				cancelQuestTimers("checkArea");
			}
			else
			{
				for (L2PcInstance pl : npc.getKnownList().getKnownPlayersInRadius(300))
				{
					final QuestState qs = pl.getQuestState(Q00024_InhabitantsOfTheForestOfTheDead.class.getSimpleName());
					if ((qs != null) && qs.isCond(3))
					{
						qs.takeItems(SILVER_CROSS, -1);
						qs.giveItems(BROKEN_SILVER_CROSS, 1);
						qs.setCond(4, true);
						broadcastNpcSay(npc, Say2.NPC_ALL, NpcStringId.THAT_SIGN);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		startQuestTimer("checkArea", 3000, npc, null, true);
		return null;
	}
	
	public Dorian(String name, String descr)
	{
		super(name, descr);
		addSpawnId(DORIAN);
		
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawnTable())
		{
			if ((spawn != null) && (spawn.getNpcid() == DORIAN))
			{
				startQuestTimer("checkArea", 3000, spawn.getLastSpawn(), null, true);
			}
		}
	}
	
	public static void main(String[] args)
	{
		new Dorian(Dorian.class.getSimpleName(), "npc");
	}
}