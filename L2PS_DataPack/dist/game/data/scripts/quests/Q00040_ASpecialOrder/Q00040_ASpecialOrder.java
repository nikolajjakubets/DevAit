package quests.Q00040_ASpecialOrder;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.util.Rnd;

/**
 * Author: RobikBobik L2PS Team
 */
public class Q00040_ASpecialOrder extends Quest
{
	static final int HELVETIA = 30081;
	static final int OFULLE = 31572;
	static final int GESTO = 30511;
	static final int OrangeNimbleFish = 6450;
	static final int OrangeUglyFish = 6451;
	static final int OrangeFatFish = 6452;
	static final int FishChest = 12764;
	static final int GoldenCobol = 5079;
	static final int ThornCobol = 5082;
	static final int GreatCobol = 5084;
	static final int SeedJar = 12765;
	static final int WondrousCubic = 10632;
	
	public Q00040_ASpecialOrder(int id, String name, String descr)
	{
		super(id, name, descr);
		
		addStartNpc(HELVETIA);
		addTalkId(HELVETIA);
		addTalkId(OFULLE);
		addTalkId(GESTO);
		
		questItemIds = new int[]
		{
			FishChest,
			SeedJar
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
		
		if (event.equalsIgnoreCase("30081-02.htm"))
		{
			st.set("cond", "1");
			int condition = Rnd.get(1, 2);
			if (condition == 1)
			{
				st.set("cond", "2");
				htmltext = "30081-02a.htm";
			}
			else
			{
				st.set("cond", "5");
				htmltext = "30081-02b.htm";
			}
			st.setState(State.STARTED);
			st.playSound("ItemSound.quest_accept");
		}
		else if (event.equalsIgnoreCase("30511-03.htm"))
		{
			st.set("cond", "6");
			st.playSound("ItemSound.quest_middle");
		}
		else if (event.equalsIgnoreCase("31572-03.htm"))
		{
			st.set("cond", "3");
			st.playSound("ItemSound.quest_middle");
		}
		else if (event.equalsIgnoreCase("30081-05a.htm"))
		{
			st.takeItems(FishChest, 1);
			st.giveItems(WondrousCubic, 1);
			st.playSound("ItemSound.quest_finish");
			st.exitQuest(false);
		}
		else if (event.equalsIgnoreCase("30081-05b.htm"))
		{
			st.takeItems(SeedJar, 1);
			st.giveItems(WondrousCubic, 1);
			st.playSound("ItemSound.quest_finish");
			st.exitQuest(false);
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
		int cond = st.getInt("cond");
		
		if (st.isCompleted())
		{
			htmltext = getAlreadyCompletedMsg(player);
		}
		else if (npcId == HELVETIA)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 40)
				{
					htmltext = "30081-01.htm";
				}
				else
				{
					htmltext = "30081-00.htm";
					st.exitQuest(true);
				}
			}
			else if ((cond == 2) || (cond == 3))
			{
				htmltext = "30081-03a.htm";
			}
			else if (cond == 4)
			{
				htmltext = "30081-04a.htm";
			}
			else if ((cond == 5) || (cond == 6))
			{
				htmltext = "30081-03b.htm";
			}
			else if (cond == 7)
			{
				htmltext = "30081-04b.htm";
			}
		}
		else if (npcId == OFULLE)
		{
			if (cond == 2)
			{
				htmltext = "31572-01.htm";
			}
			else if (cond == 3)
			{
				if ((st.getQuestItemsCount(OrangeNimbleFish) >= 10) && (st.getQuestItemsCount(OrangeUglyFish) >= 10) && (st.getQuestItemsCount(OrangeFatFish) >= 10))
				{
					st.set("cond", "4");
					st.takeItems(OrangeNimbleFish, 10);
					st.takeItems(OrangeUglyFish, 10);
					st.takeItems(OrangeFatFish, 10);
					st.playSound("ItemSound.quest_middle");
					st.giveItems(FishChest, 1);
					htmltext = "31572-05.htm";
				}
				else
				{
					htmltext = "31572-04.htm";
				}
			}
			else if (cond == 4)
			{
				htmltext = "31572-06.htm";
			}
		}
		else if (npcId == GESTO)
		{
			if (cond == 5)
			{
				htmltext = "30511-01.htm";
			}
			else if (cond == 6)
			{
				if ((st.getQuestItemsCount(GoldenCobol) >= 40) && (st.getQuestItemsCount(ThornCobol) >= 40) && (st.getQuestItemsCount(GreatCobol) >= 40))
				{
					st.set("cond", "7");
					st.takeItems(GoldenCobol, 40);
					st.takeItems(ThornCobol, 40);
					st.takeItems(GreatCobol, 40);
					st.playSound("ItemSound.quest_middle");
					st.giveItems(SeedJar, 1);
					htmltext = "30511-05.htm";
				}
				else
				{
					htmltext = "30511-04.htm";
				}
			}
			else if (cond == 7)
			{
				htmltext = "30511-06.htm";
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00040_ASpecialOrder(40, Q00040_ASpecialOrder.class.getSimpleName(), "");
	}
}