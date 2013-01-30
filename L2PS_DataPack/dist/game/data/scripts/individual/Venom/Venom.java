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
package individual.Venom;

import java.util.ArrayList;
import java.util.List;

import npc.AbstractNpcAI;

import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.instancemanager.GlobalVariablesManager;
import com.l2jserver.gameserver.instancemanager.MapRegionManager;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.skills.L2Skill;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.scripting.scriptengine.events.SiegeEvent;

/**
 * Venom AI on Rune Castle.
 * @author nonom, MELERIX
 */
public final class Venom extends AbstractNpcAI
{
	private static final int CASTLE = 8; // Rune
	
	private static final int VENOM = 29054;
	private static final int TELEPORT_CUBE = 29055;
	private static final int DUNGEON_KEEPER = 35506;
	
	private static final byte ALIVE = 0;
	private static final byte DEAD = 1;
	
	private static final int HOURS_BEFORE = 24;
	
	private static final Location[] TARGET_TELEPORTS =
	{
		new Location(12860, -49158, 976),
		new Location(14878, -51339, 1024),
		new Location(15674, -49970, 864),
		new Location(15696, -48326, 864),
		new Location(14873, -46956, 1024),
		new Location(12157, -49135, -1088),
		new Location(12875, -46392, -288),
		new Location(14087, -46706, -288),
		new Location(14086, -51593, -288),
		new Location(12864, -51898, -288),
		new Location(15538, -49153, -1056),
		new Location(17001, -49149, -1064)
	};
	
	private static final Location TRHONE = new Location(11025, -49152, -537);
	private static final Location DUNGEON = new Location(11882, -49216, -3008);
	private static final Location TELEPORT = new Location(12589, -49044, -3008);
	private static final Location CUBE = new Location(12047, -49211, -3009);
	
	private static final SkillHolder VENOM_STRIKE = new SkillHolder(4993, 1);
	private static final SkillHolder SONIC_STORM = new SkillHolder(4994, 1);
	private static final SkillHolder VENOM_TELEPORT = new SkillHolder(4995, 1);
	private static final SkillHolder RANGE_TELEPORT = new SkillHolder(4996, 1);
	
	private L2Npc _venom;
	private L2Npc _massymore;
	
	private int _venomX;
	private int _venomY;
	private int _venomZ;
	
	private boolean _aggroMode = false;
	private boolean _prisonIsOpen = false;
	
	// @formatter:off
	private static final int[] TARGET_TELEPORTS_OFFSET =
	{
		650, 100, 100, 100, 100, 650, 200, 200, 200, 200, 200, 650
	};
	// @formatter:on
	
	private static List<L2PcInstance> _targets = new ArrayList<>();
	
	private Venom(String name, String descr)
	{
		super(name, descr);
		
		addStartNpc(DUNGEON_KEEPER, TELEPORT_CUBE);
		addFirstTalkId(DUNGEON_KEEPER, TELEPORT_CUBE);
		addTalkId(DUNGEON_KEEPER, TELEPORT_CUBE);
		addSpawnId(VENOM);
		addSpellFinishedId(VENOM);
		addAttackId(VENOM);
		addKillId(VENOM);
		addAggroRangeEnterId(VENOM);
		addSiegeNotify();
		
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawnTable())
		{
			
			switch (spawn.getNpcid())
			{
				case DUNGEON_KEEPER:
					_massymore = spawn.getLastSpawn();
					break;
				case VENOM:
					_venom = spawn.getLastSpawn();
					_venomX = _venom.getX();
					_venomY = _venom.getY();
					_venomZ = _venom.getZ();
					_venom.disableSkill(VENOM_TELEPORT.getSkill(), 0);
					_venom.disableSkill(RANGE_TELEPORT.getSkill(), 0);
					_venom.doRevive();
					((L2Attackable) _venom).setCanReturnToSpawnPoint(false);
					if (checkStatus() == DEAD)
					{
						_venom.deleteMe();
					}
					break;
			}
		}
		
		final long currentTime = System.currentTimeMillis();
		final long startSiegeDate = CastleManager.getInstance().getCastleById(CASTLE).getSiegeDate().getTimeInMillis();
		final long openingDungeonDate = startSiegeDate - (HOURS_BEFORE * 360000);
		
		if ((currentTime > openingDungeonDate) && (currentTime < startSiegeDate))
		{
			_prisonIsOpen = true;
		}
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker)
	{
		switch (npc.getNpcId())
		{
			case TELEPORT_CUBE:
			{
				talker.teleToLocation(MapRegionManager.TeleportWhereType.Town);
				break;
			}
			case DUNGEON_KEEPER:
			{
				if (_prisonIsOpen)
				{
					talker.teleToLocation(TELEPORT, 0);
				}
				else
				{
					return "35506-02.html";
				}
				break;
			}
		}
		return super.onTalk(npc, talker);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		switch (event)
		{
			case "tower_check":
				if (CastleManager.getInstance().getCastleById(CASTLE).getSiege().getControlTowerCount() <= 1)
				{
					changeLocation(MoveTo.THRONE);
					broadcastNpcSay(_massymore, Say2.SHOUT, NpcStringId.OH_NO_THE_DEFENSES_HAVE_FAILED_IT_IS_TOO_DANGEROUS_TO_REMAIN_INSIDE_THE_CASTLE_FLEE_EVERY_MAN_FOR_HIMSELF);
					cancelQuestTimer("tower_check", npc, null);
					startQuestTimer("raid_check", 10000, npc, null, true);
				}
				break;
			case "raid_check":
				if (!npc.isInsideZone(ZoneId.SIEGE) && !npc.isTeleporting())
				{
					npc.teleToLocation(new Location(_venomX, _venomY, _venomZ), false);
				}
				break;
			case "cube_despawn":
				if (npc != null)
				{
					npc.deleteMe();
				}
				break;
		}
		return event;
	}
	
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		if (isPet)
		{
			return super.onAggroRangeEnter(npc, player, isPet);
		}
		
		if (_aggroMode && (_targets.size() < 10) && (getRandom(3) < 1) && !player.isDead())
		{
			_targets.add(player);
		}
		return super.onAggroRangeEnter(npc, player, isPet);
	}
	
	@Override
	public boolean onSiegeEvent(SiegeEvent event)
	{
		if (event.getSiege().getCastle().getCastleId() == CASTLE)
		{
			if (event.getSiege().getCastle().getIsTimeRegistrationOver() && !event.getSiege().getAttackerClans().isEmpty())
			{
				_prisonIsOpen = true;
				changeLocation(MoveTo.PRISON);
			}
			
			switch (event.getStage())
			{
				case START:
					_aggroMode = true;
					_prisonIsOpen = false;
					if ((_venom != null) && !_venom.isDead())
					{
						_venom.setCurrentHp(_venom.getMaxHp());
						_venom.setCurrentMp(_venom.getMaxMp());
						_venom.enableSkill(VENOM_TELEPORT.getSkill());
						_venom.enableSkill(RANGE_TELEPORT.getSkill());
						startQuestTimer("tower_check", 30000, _venom, null, true);
					}
					break;
				case END:
					_aggroMode = false;
					if ((_venom != null) && !_venom.isDead())
					{
						changeLocation(MoveTo.PRISON);
						_venom.disableSkill(VENOM_TELEPORT.getSkill(), 0);
						_venom.disableSkill(RANGE_TELEPORT.getSkill(), 0);
					}
					updateStatus(ALIVE);
					cancelQuestTimer("tower_check", _venom, null);
					cancelQuestTimer("raid_check", _venom, null);
					break;
			}
		}
		return true;
	}
	
	@Override
	public String onSpellFinished(L2Npc npc, L2PcInstance player, L2Skill skill)
	{
		switch (skill.getId())
		{
			case 4222:
				npc.teleToLocation(new Location(_venomX, _venomY, _venomZ), false);
				break;
			case 4995:
				teleportTarget(player);
				((L2Attackable) npc).stopHating(player);
				break;
			case 4996:
				teleportTarget(player);
				((L2Attackable) npc).stopHating(player);
				if ((_targets != null) && (_targets.size() > 0))
				{
					for (L2PcInstance target : _targets)
					{
						final long x = player.getX() - target.getX();
						final long y = player.getY() - target.getY();
						final long z = player.getZ() - target.getZ();
						final long range = 250;
						if (((x * x) + (y * y) + (z * z)) <= (range * range))
						{
							teleportTarget(target);
							((L2Attackable) npc).stopHating(target);
						}
					}
					_targets.clear();
				}
				break;
		}
		return super.onSpellFinished(npc, player, skill);
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		if (!npc.isTeleporting())
		{
			if (checkStatus() == DEAD)
			{
				npc.deleteMe();
			}
			else
			{
				npc.doRevive();
				broadcastNpcSay(npc, Say2.SHOUT, NpcStringId.WHO_DARES_TO_COVET_THE_THRONE_OF_OUR_CASTLE_LEAVE_IMMEDIATELY_OR_YOU_WILL_PAY_THE_PRICE_OF_YOUR_AUDACITY_WITH_YOUR_VERY_OWN_BLOOD);
			}
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		final double distance = Math.sqrt(npc.getPlanDistanceSq(attacker.getX(), attacker.getY()));
		if (_aggroMode && (getRandom(100) < 25))
		{
			npc.setTarget(attacker);
			npc.doCast(VENOM_TELEPORT.getSkill());
		}
		else if (_aggroMode && (npc.getCurrentHp() < (npc.getMaxHp() / 3)) && (getRandom(100) < 25) && !npc.isCastingNow())
		{
			npc.setTarget(attacker);
			npc.doCast(RANGE_TELEPORT.getSkill());
		}
		else if ((distance > 300) && (getRandom(100) < 10) && !npc.isCastingNow())
		{
			npc.setTarget(attacker);
			npc.doCast(VENOM_STRIKE.getSkill());
		}
		else if ((getRandom(100) < 10) && !npc.isCastingNow())
		{
			npc.setTarget(attacker);
			npc.doCast(SONIC_STORM.getSkill());
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		updateStatus(DEAD);
		broadcastNpcSay(npc, Say2.SHOUT, NpcStringId.ITS_NOT_OVER_YET_IT_WONT_BE_OVER_LIKE_THIS_NEVER);
		if (!CastleManager.getInstance().getCastleById(CASTLE).getSiege().getIsInProgress())
		{
			L2Npc cube = addSpawn(TELEPORT_CUBE, CUBE, false, 0);
			startQuestTimer("cube_despawn", 120000, cube, null);
		}
		cancelQuestTimer("raid_check", npc, null);
		return super.onKill(npc, killer, isPet);
	}
	
	/**
	 * Alters the Venom location
	 * @param loc enum
	 */
	private void changeLocation(MoveTo loc)
	{
		switch (loc)
		{
			case THRONE:
				_venom.teleToLocation(TRHONE, false);
				break;
			case PRISON:
				if ((_venom == null) || _venom.isDead() || _venom.isDecayed())
				{
					_venom = addSpawn(VENOM, DUNGEON, false, 0);
				}
				else
				{
					_venom.teleToLocation(DUNGEON, false);
				}
				cancelQuestTimer("raid_check", _venom, null);
				cancelQuestTimer("tower_check", _venom, null);
				break;
		}
		_venomX = _venom.getX();
		_venomY = _venom.getY();
		_venomZ = _venom.getZ();
	}
	
	private void teleportTarget(L2PcInstance player)
	{
		if ((player != null) && !player.isDead())
		{
			final int rnd = getRandom(11);
			player.teleToLocation(TARGET_TELEPORTS[rnd], TARGET_TELEPORTS_OFFSET[rnd]);
			player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		}
	}
	
	/**
	 * Checks if Venom is Alive or Dead
	 * @return status
	 */
	private int checkStatus()
	{
		int checkStatus = ALIVE;
		if (GlobalVariablesManager.getInstance().isVariableStored("VenomStatus"))
		{
			checkStatus = Integer.parseInt(GlobalVariablesManager.getInstance().getStoredVariable("VenomStatus"));
		}
		else
		{
			GlobalVariablesManager.getInstance().storeVariable("VenomStatus", "0");
		}
		return checkStatus;
	}
	
	/**
	 * Update the Venom status
	 * @param status the new status. 0 = ALIVE, 1 = DEAD.
	 */
	private void updateStatus(int status)
	{
		GlobalVariablesManager.getInstance().storeVariable("VenomStatus", Integer.toString(status));
	}
	
	private enum MoveTo
	{
		THRONE,
		PRISON
	}
	
	public static void main(String[] args)
	{
		new Venom(Venom.class.getSimpleName(), "individual");
	}
}
