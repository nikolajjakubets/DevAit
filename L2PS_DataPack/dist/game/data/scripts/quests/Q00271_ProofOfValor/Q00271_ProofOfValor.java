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
package quests.Q00271_ProofOfValor;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Author: RobikBobik L2PS Team
 */
public class Q00271_ProofOfValor extends Quest
{
	private static final int KASHA_WOLF_FANG = 1473;
	private static final int NECKLACE_OF_VALOR = 1507;
	private static final int NECKLACE_OF_COURAGE = 1506;
	private static final int RUKAIN = 30577;
	private static final int KASHA_WOLF = 20475;
	
	public Q00271_ProofOfValor(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		addStartNpc(RUKAIN);
		addTalkId(RUKAIN);
		
		addKillId(KASHA_WOLF);
		
		questItemIds = new int[]
		{
			KASHA_WOLF_FANG
		};
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		if (event.equalsIgnoreCase("30577-03.htm"))
		{
			st.set("cond", "1");
			st.setState(State.STARTED);
			st.playSound("ItemSound.quest_accept");
			
			if ((st.getQuestItemsCount(NECKLACE_OF_COURAGE) >= 1) || (st.getQuestItemsCount(NECKLACE_OF_VALOR) >= 1))
			{
				htmltext = "30577-07.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		int cond = st.getInt("cond");
		
		switch (st.getState())
		{
			case State.CREATED:
				if (player.getRace().ordinal() == 3)
				{
					if ((player.getLevel() >= 4) && (player.getLevel() <= 8))
					{
						htmltext = "30577-02.htm";
					}
					else
					{
						htmltext = "30577-01.htm";
						st.exitQuest(true);
					}
				}
				else
				{
					htmltext = "30577-00.htm";
					st.exitQuest(true);
				}
				break;
			case State.STARTED:
				if (cond == 1)
				{
					htmltext = "30577-04.htm";
				}
				else if (cond == 2)
				{
					htmltext = "30577-05.htm";
					st.takeItems(KASHA_WOLF_FANG, -1);
					
					if (st.getRandom(100) <= 10)
					{
						st.giveItems(NECKLACE_OF_VALOR, 1);
					}
					else
					{
						st.giveItems(NECKLACE_OF_COURAGE, 1);
					}
					
					st.unset("cond");
					st.playSound("ItemSound.quest_finish");
					st.exitQuest(true);
				}
				break;
			case State.COMPLETED:
				htmltext = getAlreadyCompletedMsg(player);
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return null;
		}
		
		if (st.getInt("cond") == 1)
		{
			int count = (int) st.getQuestItemsCount(KASHA_WOLF_FANG);
			int chance = (int) (125 * Config.RATE_QUEST_DROP);
			int numItems = chance / 100;
			chance = chance % 100;
			
			if (st.getRandom(100) <= chance)
			{
				numItems++;
			}
			
			if (numItems > 0)
			{
				if ((count + numItems) >= 50)
				{
					st.set("cond", "2");
					st.playSound("ItemSound.quest_middle");
					numItems = 50 - count;
				}
				else
				{
					st.playSound("ItemSound.quest_itemget");
				}
				
				st.giveItems(KASHA_WOLF_FANG, numItems);
			}
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		new Q00271_ProofOfValor(271, Q00271_ProofOfValor.class.getSimpleName(), "Proof Of Valor");
	}
}