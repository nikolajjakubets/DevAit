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
package com.l2jserver.gameserver.datatables;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.l2jserver.gameserver.engines.DocumentParser;
import com.l2jserver.gameserver.model.actor.L2Npc;

public class NpcPersonalAIData extends DocumentParser
{
	private final Map<String, Map<String, Integer>> _AIData;
	
	protected NpcPersonalAIData()
	{
		_AIData = new HashMap<>();
		load();
	}
	
	@Override
	public void load()
	{
		_AIData.clear();
		parseDatapackFile("data/stats/npc/PersonalAIData.xml");
		_log.info(getClass().getSimpleName() + ": Loaded personal AI data for " + _AIData.size() + " NPC('s).");
	}
	
	@Override
	protected void parseDocument()
	{
		NamedNodeMap attrs;
		for (Node a = getCurrentDocument().getFirstChild(); a != null; a = a.getNextSibling())
		{
			if ("list".equalsIgnoreCase(a.getNodeName()))
			{
				for (Node b = a.getFirstChild(); b != null; b = b.getNextSibling())
				{
					if ("spawn".equalsIgnoreCase(b.getNodeName()))
					{
						attrs = b.getAttributes();
						Map<String, Integer> map = new HashMap<>();
						String name = attrs.getNamedItem("name").getNodeValue();
						for (Node c = b.getFirstChild(); c != null; c = c.getNextSibling())
						{
							// Skip odd nodes
							if (c.getNodeName().equals("#text"))
							{
								continue;
							}
							int val;
							switch (c.getNodeName())
							{
								case "disableRandomAnimation":
								case "disableRandomWalk":
									val = Boolean.parseBoolean(c.getTextContent()) ? 1 : 0;
									break;
								default:
									val = Integer.parseInt(c.getTextContent());
							}
							map.put(c.getNodeName(), val);
						}
						
						if (!map.isEmpty())
						{
							_AIData.put(name, map);
						}
					}
				}
			}
		}
	}
	
	public int getAIValue(String spawnName, String paramName)
	{
		return hasAIValue(spawnName, paramName) ? _AIData.get(spawnName).get(paramName) : -1;
	}
	
	public boolean hasAIValue(String spawnName, String paramName)
	{
		return (spawnName != null) && _AIData.containsKey(spawnName) && _AIData.get(spawnName).containsKey(paramName);
	}
	
	public void initializeNpcParameters(L2Npc npc, String spawnName)
	{
		if (_AIData.containsKey(spawnName))
		{
			Map<String, Integer> map = _AIData.get(spawnName);
			
			try
			{
				for (String key : map.keySet())
				{
					switch (key)
					{
						case "disableRandomAnimation":
							npc.setRandomAnimationEnabled((map.get(key) == 0));
							break;
						case "disableRandomWalk":
							npc.setIsNoRndWalk((map.get(key) == 1));
							break;
					}
				}
			}
			catch (Exception e)
			{
				// Do nothing
			}
		}
	}
	
	public static NpcPersonalAIData getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final NpcPersonalAIData _instance = new NpcPersonalAIData();
	}
}