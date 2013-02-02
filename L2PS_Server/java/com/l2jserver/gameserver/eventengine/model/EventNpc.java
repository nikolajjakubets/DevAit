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
package com.l2jserver.gameserver.eventengine.model;

import javolution.util.FastList;

import com.l2jserver.gameserver.datatables.NpcTable;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.eventengine.container.NpcContainer;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillLaunched;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;

/**
 * @author Rizel
 */
public class EventNpc
{
	
	private L2Npc _owner;
	private int _team;
	
	public int getTeam()
	{
		return _team;
	}
	
	public void setTeam(Integer _team)
	{
		this._team = _team;
	}
	
	public EventNpc(int x, int y, int z, int npcId, int instance)
	{
		_team = 0;
		final L2NpcTemplate template = NpcTable.getInstance().getTemplate(npcId);
		try
		{
			final L2Spawn spawn = new L2Spawn(template);
			spawn.setLocx(x);
			spawn.setLocy(y);
			spawn.setLocz(z);
			spawn.setInstanceId(instance);
			spawn.setAmount(1);
			spawn.setHeading(0);
			spawn.setRespawnDelay(1);
			_owner = spawn.doSpawn();
		}
		catch (Exception e)
		{
			System.out.println("Error on spawn NPC: " + npcId);
		}
	}
	
	public void unspawn()
	{
		_owner.getSpawn().getLastSpawn().deleteMe();
		_owner.getSpawn().stopRespawn();
		SpawnTable.getInstance().deleteSpawn(_owner.getSpawn(), true);
		NpcContainer.getInstance().deleteNpc(this);
	}
	
	public int getId()
	{
		return _owner.getSpawn().getLastSpawn().getObjectId();
	}
	
	public void setTitle(String title)
	{
		_owner.getSpawn().getLastSpawn().setTitle(title);
	}
	
	public L2Npc getNpc()
	{
		return _owner.getSpawn().getLastSpawn();
	}
	
	public void broadcastStatusUpdate()
	{
		_owner.broadcastStatusUpdate();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		EventNpc other = (EventNpc) obj;
		if (_owner == null)
		{
			if (other._owner != null)
			{
				return false;
			}
		}
		else if (!_owner.equals(other._owner))
		{
			return false;
		}
		return true;
	}
	
	public void showBombEffect(FastList<EventPlayer> victims)
	{
		FastList<L2Object> temp = new FastList<>();
		
		for (EventPlayer victim : victims)
		{
			temp.add(victim.getOwner());
		}
		
		_owner.broadcastPacket(new MagicSkillUse(_owner, victims.head().getNext().getValue().getOwner(), 903, 1, 0, 0));
		
		_owner.broadcastPacket(new MagicSkillLaunched(_owner, 903, 1, temp.toArray(new L2Object[temp.size()])));
	}
	
	public void doDie()
	{
		_owner.doDie(_owner);
	}
	
}
