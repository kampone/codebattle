package com.codenjoy.dojo.sample.client;

import java.util.List;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.sample.client.Board;
import com.codenjoy.dojo.sample.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

/**
 * User: your name Р­С‚Рѕ С‚РІРѕР№ Р°Р»РіРѕСЂРёС‚Рј AI РґР»СЏ РёРіСЂС‹.
 * Р РµР°Р»РёР·СѓР№ РµРіРѕ РЅР° СЃРІРѕРµ СѓСЃРјРѕС‚СЂРµРЅРёРµ. РћР±СЂР°С‚Рё
 * РІРЅРёРјР°РЅРёРµ РЅР° {@see YourSolverTest} - С‚Р°Рј РїСЂРёРіРѕС‚РѕРІР»РµРЅ
 * С‚РµСЃС‚РѕРІС‹Р№ С„СЂРµР№РјРІРѕСЂРє РґР»СЏ С‚РµР±СЏ.
 */
public class YourSolver implements Solver<Board> {

	private static final String USER_NAME = "kosmonavtmikki@gmail.com";

	static int i = 0;

	private Dice dice;
	private Board board;

	public YourSolver(Dice dice) {
		this.dice = dice;
	}

	@Override
	public String get(Board board) {
		this.board = board;
		Point me = board.getMe();
		List<Point> goldList = board.get(Elements.GOLD);
		Point nearestGold = findTheNearestGold(me, goldList);
		
		return chooseDirection(me, nearestGold);
	}

	private String chooseDirection(Point me, Point nearestGold) {
		int x = me.getX() - nearestGold.getX();
		int y = me.getY() - nearestGold.getY();
		
		System.out.println("ME: x("+ me.getX() + ") y("+ me.getY() + ")");
		System.out.println("GO: x("+ nearestGold.getX() + ") y("+ nearestGold.getY() + ")");

		
		System.out.println(x);
		System.out.println(y);
		if (Math.abs(x) >= Math.abs(y)) {
			if (x < 0) {
				return Direction.RIGHT.toString();
			}
			if (x >= 0) {
				return Direction.LEFT.toString();
			}
		}

		if (Math.abs(x) < Math.abs(y)) {
			if (y < 0) {
				return Direction.DOWN.toString();
			}
			if (y >= 0) {
				return Direction.UP.toString();
			}
		}
		return null;
	}

	private Point findTheNearestGold(Point me, List<Point> goldList) {
		System.out.println(goldList);
		double minDistance = Double.MAX_VALUE;
		int minIndex = 0;
		for (Point point : goldList) {
			double distance = findDistance(me, point);
			if (distance < minDistance) {
				minDistance = distance;
				minIndex = goldList.indexOf(point);
			}
		}
		return goldList.get(minIndex);
	}

	private double findDistance(Point me, Point point) {
		double x = Math.pow(me.getX() - point.getX(), 2);
		double y = Math.pow(me.getY() - point.getY(), 2);
		return Math.sqrt(x + y);
	}

	public static void main(String[] args) {
		start(USER_NAME, WebSocketRunner.Host.REMOTE);
	}

	public static void start(String name, WebSocketRunner.Host server) {
		try {
			WebSocketRunner.run(server, name, new YourSolver(new RandomDice()), new Board());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
