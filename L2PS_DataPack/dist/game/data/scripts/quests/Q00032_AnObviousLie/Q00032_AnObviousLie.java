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
package quests.Q00032_AnObviousLie;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Author: RobikBobik L2PS Team
 */
public class Q00032_AnObviousLie extends Quest
{
	private final static int MAP = 7165;
	private final static int MEDICINAL_HERB = 7166;
	private final static int SPIRIT_ORES = 3031;
	private final static int THREAD = 1868;
	private final static int SUEDE = 1866;
	private final static int RACCOON_EAR = 7680;
	private final static int CAT_EAR = 6843;
	private final static int RABBIT_EAR = 7683;
	private final static int MAXIMILIAN = 30120;
	private final static int GENTLER = 30094;
	private final static int MIKI_THE_CAT = 31706;
	private final static int ALLIGATOR = 20135;
	private final static int CHANCE_FOR_DROP = 30;
	
	private final int[] talkNpc =
	{
		MAXIMILIAN,
		GENTLER,
		MIKI_THE_CAT
	};
	
	private static final int[][] DROPLIST_COND =
	{
		{
			ALLIGATOR,
			MEDICINAL_HERB
		}
	};
	
	public Q00032_AnObviousLie(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(MAXIMILIAN);
		for (int npcId : talkNpc)
		{
			addTalkId(npcId);
		}
		
		for (int[] element : DROPLIST_COND)
		{
			addKillId(element[0]);
		}
		
		this.questItemIds = new int[]
		{
			MEDICINAL_HERB
		};
	}
	
	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmltext = "";
		htmltext = event;
		if (event.equalsIgnoreCase("30120-1.htm"))
		{
			st.set("cond", "1");
			st.setState(State.STARTED);
			st.playSound("ItemSound.quest_accept");
		}
		else if (event.equalsIgnoreCase("30094-1.htm"))
		{
			st.giveItems(MAP, 1);
			st.set("cond", "2");
		}
		else if (event.equalsIgnoreCase("31706-1.htm"))
		{
			st.takeItems(MAP, 1);
			st.set("cond", "3");
		}
		else if (event.equalsIgnoreCase("30094-4.htm"))
		{
			if (st.getQuestItemsCount(MEDICINAL_HERB) > 19)
			{
				st.takeItems(MEDICINAL_HERB, 20);
				st.set("cond", "5");
			}
			else
			{
				htmltext = "You don't have enough materials";
				st.set("cond", "3");
			}
			
		}
		else if (event.equalsIgnoreCase("30094-7.htm"))
		{
			if (st.getQuestItemsCount(SPIRIT_ORES) >= 500)
			{
				st.takeItems(SPIRIT_ORES, 500);
				st.set("cond", "6");
			}
			else
			{
				htmltext = "Youn don't have enough materials";
			}
			
		}
		else if (event.equalsIgnoreCase("31706-4.htm"))
		{
			st.set("cond", "7");
		}
		else if (event.equalsIgnoreCase("30094-10.htm"))
		{
			st.set("cond", "8");
		}
		else if (event.equalsIgnoreCase("30094-13.htm"))
		{
			if ((st.getQuestItemsCount(THREAD) >= 1000) && (st.getQuestItemsCount(SUEDE) >= 500))
			{
				st.takeItems(THREAD, 1000);
				st.takeItems(SUEDE, 500);
			}
			else
			{
				htmltext = "Youn don't have enough materials";
			}
			
		}
		else if (event.equalsIgnoreCase("cat") || event.equalsIgnoreCase("racoon") || event.equalsIgnoreCase("rabbit"))
		{
			if (st.getInt("cond") == 8)
			{
				
				if (event.equalsIgnoreCase("cat"))
				{
					st.giveReward(CAT_EAR, 1);
				}
				else if (event.equalsIgnoreCase("racoon"))
				{
					st.giveReward(RACCOON_EAR, 1);
				}
				else if (event.equalsIgnoreCase("rabbit"))
				{
					st.giveReward(RABBIT_EAR, 1);
				}
				st.exitQuest(false);
				st.unset("cond");
				st.playSound("ItemSound.quest_finish");
				htmltext = "30094-14.htm";
			}
			else
			{
				htmltext = "???";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker)
	{
		String htmltext = getNoQuestMsg(talker);
		QuestState st = talker.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		if (st.getState() == State.COMPLETED)
		{
			htmltext = getAlreadyCompletedMsg(talker);
			return htmltext;
		}
		
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		int id = st.getState();
		if (npcId == MAXIMILIAN)
		{
			if (id == State.CREATED)
			{
				if (talker.getLevel() >= 45)
				{
					htmltext = "30120-0.htm";
				}
				else
				{
					htmltext = "30120-0a.htm";
					st.exitQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "30120-2.htm";
			}
		}
		if (id == State.STARTED)
		{
			if (npcId == GENTLER)
			{
				switch (cond)
				{
					case 1:
						htmltext = "30094-0.htm";
						break;
					case 2:
						htmltext = "30094-2.htm";
						break;
					case 4:
						htmltext = "30094-3.htm";
						break;
					case 5:
						if (st.getQuestItemsCount(SPIRIT_ORES) < 500)
						{
							htmltext = "30094-5.htm";
						}
						else if (st.getQuestItemsCount(SPIRIT_ORES) >= 500)
						{
							htmltext = "30094-6.htm";
						}
						
						break;
					case 6:
						htmltext = "30094-8.htm";
						break;
					case 7:
						htmltext = "30094-9.htm";
						break;
					case 8:
						if ((st.getQuestItemsCount(THREAD) < 1000) || (st.getQuestItemsCount(SUEDE) < 500))
						{
							htmltext = "30094-11.htm";
						}
						else if ((st.getQuestItemsCount(THREAD) >= 1000) || (st.getQuestItemsCount(SUEDE) >= 500))
						{
							htmltext = "30094-12.htm";
						}
						
						break;
				}
			}
			else if (npcId == MIKI_THE_CAT)
			{
				switch (cond)
				{
					case 2:
						htmltext = "31706-0.htm";
						break;
					case 3:
						htmltext = "31706-2.htm";
						break;
					case 6:
						htmltext = "31706-3.htm";
						break;
					case 7:
						htmltext = "31706-5.htm";
						break;
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return null;
		}
		int chance;
		long count;
		
		if (st.getState() != State.STARTED)
		{
			return null;
		}
		
		chance = st.getRandom(100);
		count = st.getQuestItemsCount(MEDICINAL_HERB);
		if ((chance < CHANCE_FOR_DROP) && (st.getInt("cond") == 3))
		{
			if (count < 20)
			{
				st.giveItems(MEDICINAL_HERB, 1);
			}
			if (count == 19)
			{
				st.playSound("ItemSound.quest_middle");
				st.set("cond", "4");
			}
			else
			{
				st.playSound("ItemSound.quest_itemget");
			}
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		new Q00032_AnObviousLie(32, Q00032_AnObviousLie.class.getSimpleName(), "An Obvious Lie");
	}
}