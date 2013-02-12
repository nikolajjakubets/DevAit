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

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Author: RobikBobik L2PS Team
 */
public final class Q00695_DefendtheHallofSuffering extends Quest
{
	private static final int TEPIOS = 32603;
	private static final int TEPIOSINST = 32530;
	private static final int MOUTHOFEKIMUS = 32537;
	private static final int MARK = 13691;
	
	public Q00695_DefendtheHallofSuffering(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		addStartNpc(TEPIOS);
		addStartNpc(MOUTHOFEKIMUS);
		
		addTalkId(TEPIOS);
		addTalkId(MOUTHOFEKIMUS);
		
		questItemIds = new int[]
		{
			MARK
		};
	}
	
	@Override
	public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		
		if (st == null)
		{
			return htmltext;
		}
		
		if (event.equalsIgnoreCase("32603-02.htm"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		QuestState st = player.getQuestState(getName());
		
		if (st == null)
		{
			return htmltext;
		}
		
		int npcId = npc.getNpcId();
		byte state = st.getState();
		if (state == State.COMPLETED)
		{
			htmltext = "32603-03.htm";
		}
		else if ((state == State.CREATED) && (npcId == TEPIOS))
		{
			boolean checkLvl = ((player.getLevel() >= 75) && (player.getLevel() <= 82));
			if (checkLvl && (st.getQuestItemsCount(MARK) == 1))
			{
				htmltext = "32603-01.htm";
			}
			else if (checkLvl && (st.getQuestItemsCount(MARK) == 0))
			{
				htmltext = "32603-05.htm";
			}
			else
			{
				htmltext = "32603-00.htm";
			}
		}
		else if (state == State.STARTED)
		{
			switch (npcId)
			{
				case MOUTHOFEKIMUS:
					htmltext = "32537-01.htm";
					break;
				case TEPIOSINST:
					htmltext = "32530-01.htm";
					break;
				case TEPIOS:
					htmltext = "32603-04.htm";
					st.exitQuest(true);
					if (st.getQuestItemsCount(MARK) == 0)
					{
						st.giveItems(13691, 1);
					}
					st.giveItems(736, 1);
					break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00695_DefendtheHallofSuffering(695, Q00695_DefendtheHallofSuffering.class.getSimpleName(), "Defend the Hall of Suffering");
	}
}