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
package npc.Teleports.GatekeeperSpirit;

import npc.AbstractNpcAI;

import com.l2jserver.gameserver.SevenSigns;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;

/**
 * Gatekeeper Spirit AI.
 * @author Charus, lion
 */
public class GatekeeperSpirit extends AbstractNpcAI
{
	private final static int GATEKEEPER_SPIRIT_ENTER = 31111;
	private final static int GATEKEEPER_SPIRIT_EXIT = 31112;
	private final static int LILITH = 25283;
	private final static int ANAKIM = 25286;
	
	private GatekeeperSpirit(String name, String descr)
	{
		super(name, descr);
		
		addStartNpc(GATEKEEPER_SPIRIT_ENTER);
		addFirstTalkId(GATEKEEPER_SPIRIT_ENTER);
		addTalkId(GATEKEEPER_SPIRIT_ENTER);
		addEventId(LILITH, Quest.QuestEventType.ON_KILL);
		addEventId(ANAKIM, Quest.QuestEventType.ON_KILL);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		int playerCabal = SevenSigns.getInstance().getPlayerCabal(player.getObjectId());
		int sealAvariceOwner = SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_AVARICE);
		int compWinner = SevenSigns.getInstance().getCabalHighestScore();
		
		if ((playerCabal == sealAvariceOwner) && (playerCabal == compWinner))
		{
			switch (sealAvariceOwner)
			{
				case SevenSigns.CABAL_DAWN:
					htmltext = "dawn.htm";
					break;
				case SevenSigns.CABAL_DUSK:
					htmltext = "dusk.htm";
					break;
				case SevenSigns.CABAL_NULL:
					npc.showChatWindow(player);
					break;
			}
		}
		else
		{
			npc.showChatWindow(player);
		}
		
		return htmltext;
	}
	
	@Override
	/**
	 * TODO: Should be spawned 10 seconds after boss dead
	 */
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == LILITH)
		{
			// exit_necropolis_boss_lilith
			addSpawn(GATEKEEPER_SPIRIT_EXIT, 184410, -10111, -5488, 0, false, 900000);
		}
		else if (npcId == ANAKIM)
		{
			// exit_necropolis_boss_anakim
			addSpawn(GATEKEEPER_SPIRIT_EXIT, 184410, -13102, -5488, 0, false, 900000);
		}
		
		return super.onKill(npc, killer, isPet);
	}
	
	public static void main(String[] args)
	{
		new GatekeeperSpirit(GatekeeperSpirit.class.getSimpleName(), "npc/Teleports/");
	}
}